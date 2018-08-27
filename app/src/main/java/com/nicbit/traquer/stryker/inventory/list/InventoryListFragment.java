package com.nicbit.traquer.stryker.inventory.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nicbit.traquer.common.EventsLog;
import com.nicbit.traquer.common.newInventory.location.SelectCityActivity;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.inventory.LocationData;
import com.nicbit.traquer.stryker.Models.inventory.Product;
import com.nicbit.traquer.stryker.Models.inventory.ScanData;
import com.nicbit.traquer.stryker.Models.inventory.SearchNearLocationsResponse;
import com.nicbit.traquer.stryker.Models.inventory.UpdateInventoryScanRequest;
import com.nicbit.traquer.stryker.Models.inventory.Zone;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.inventory.ScanFragment;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.util.AppLog;
import com.nicbit.traquer.stryker.util.CurrentLocationUtil;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.stryker.util.StringUtils;
import com.nicbit.traquer.stryker.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.akwa.akcore.BeaconData;
import io.akwa.akproximity.kontakt.KontaktBeaconScannerManual;

import static android.app.Activity.RESULT_OK;

public class InventoryListFragment extends ScanFragment implements InventoryListContract.View, KontaktBeaconScannerManual.KontaktBeaconListener, ZoneExpandableAdapter.InventoryListItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.listView)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_view)
    LinearLayout mEmptyView;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_overall)
    TextView tvOverAll;
    @BindView(R.id.tv_all_found)
    TextView tvAllFound;
    @BindView(R.id.tv_all_missing)
    TextView tvAllMissing;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private InventoryListPresenter mActionsListener;
    ZoneExpandableAdapter mAdapter;
    private Context context;
    ArrayList<Object> locationList;
    int defaultLocationId = 0;
    KontaktBeaconScannerManual kontaktBeaconScannerManual;
    Set<BeaconData> kontakBeacons;
    String locationJson;
    boolean isPresentOnLocation = false;
    boolean isScanning = false;
    private long scanStartTime;
    private long scanEndTime;
    private boolean isScanClicked = false;
    private boolean isChooseAddressShown = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new InventoryListPresenter(this);
        LocationData inventoryLocation = getInventoryLocation();
        if (inventoryLocation != null) {
//            searchNearLocations(inventoryLocation.getLocationId());
        } else {
            searchNearLocations(defaultLocationId);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        checkBluetoothOnOff();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ZoneExpandableAdapter(context, new ArrayList<ZoneGroup>(), this);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ?
                        0 : mRecyclerView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled((topRowVerticalPosition >= 0));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setAdapter(ArrayList<ZoneGroup> zones) {
        mAdapter = new ZoneExpandableAdapter(context, zones, this);
        mAdapter.setIsFirstTime(false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        updateTotal(false);
    }

    private void updateTotal(boolean isFinish) {
        List<ZoneGroup> groups = (List<ZoneGroup>) mAdapter.getGroups();
        ArrayList<ScanData> scanDataList = new ArrayList<>();
        int all, found;
        all = found = 0;
        for (ZoneGroup zoneGroup : groups) {
            List<Product> products = zoneGroup.getItems();
            all = all + products.size();
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                if (product.getIsFound() == 1) {
                    found++;
                    AppLog.i("found" + product.getName());
                }
                if (isFinish) {
                    ScanData scanData = new ScanData();
                    if (product.getIsFound() == 1) {
                        scanData.setFound(1);
                    } else {
                        scanData.setFound(0);
                    }
                    scanData.setProductId(product.getProductId());
                    scanData.setSensorId(product.getSensor().getId());
                    scanData.setSkuId(product.getSkuId());
                    scanData.setZoneId(zoneGroup.getZoneId());
                    scanDataList.add(scanData);
                }
            }
        }
        if (isFinish) {
            tvOverAll.setText("All : " + all);
            tvAllFound.setText("Nearby : " + found);
            int miss = all - found;
            tvAllMissing.setText("Missing : " + miss);

            updateInventoryToServer(scanDataList);

        } else {
            tvOverAll.setText("All : " + all);
            tvAllFound.setText("Nearby : 0");
            tvAllMissing.setText("Missing : 0");
        }

    }

    @OnClick(R.id.rl_address)
    public void chooseAddress() {
        if (isScanning) {
            stopScanAndIcon();
        }
        EventsLog.customEvent("SCAN", "ADDRESS", "CLICK");
        Intent intent = new Intent(context, SelectCityActivity.class);
        intent.putExtra("locationJson", locationJson);
        int id = 0;
        Object tag = tvLocation.getTag();
        if (tag != null) {
            id = (int) tag;
        }
        intent.putExtra("locationId", id);
        startActivityForResult(intent, StringUtils.REQUEST_LOCATION);
    }

    private void stopScanAndIcon() {
        kontaktBeaconScannerManual.stopRanginBeacon();
        isScanning = false;
        stopAnimation();
        mAdapter.isScanningFinish = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StringUtils.REQUEST_LOCATION && resultCode == RESULT_OK) {
            onLocationReceive(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onLocationReceive(Intent data) {
        LocationData locationData = data.getParcelableExtra("LocationData");
        tvLocation.setText(locationData.getAddress());
//        searchNearLocations(locationData.getLocationId());
//        defaultLocationId = locationData.getLocationId();
        setInventoryLocation(locationData);
    }

    @OnClick(R.id.iv_sync)
    public void onScanClick() {
        if (isScanning) {
            isScanClicked = false;
            scanEndTime = System.currentTimeMillis() / 1000L;
            EventsLog.customEvent("SCAN", "END", "" + scanEndTime);
            stopScanAndIcon();
            mAdapter.notifyDataSetChanged();
            updateTotal(true);
        } else {
            isScanClicked = true;
            checkBluetoothOnOff();
        }
    }

    public void startScan() {
        scanStartTime = System.currentTimeMillis() / 1000L;
        EventsLog.customEvent("SCAN", "START", "" + scanStartTime);
        mAdapter.isScanningFinish = false;
        mAdapter.notifyDataSetChanged();
        kontakBeacons = new HashSet<>();
        kontaktBeaconScannerManual = new KontaktBeaconScannerManual(context, this);
        kontaktBeaconScannerManual.startScanning();
        isScanning = true;
        startAnimation();
    }

    void searchNearLocations(Integer locationId) {
        DialogClass.showDialog(context, this.getString(R.string.please_wait));
        mActionsListener.searchNearLocations(CurrentLocationUtil.latitude, CurrentLocationUtil.longitude, locationId);
    }

    @Override
    public void onSearchNearLocationsResponse(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(context);
        stopRefresh();
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                SearchNearLocationsResponse searchNearLocationsResponse = response.getData().getReaderqqqq();
                if (searchNearLocationsResponse != null) {
                    setData(searchNearLocationsResponse);
                }
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            searchNearLocations(defaultLocationId);
                        }
                    }
                }, getActivity());
            } else {
                ErrorMessageHandler.handleErrorMessage(response.getCode(), getActivity());
            }
        } else {
            DialogClass.alerDialog(context, getResources().getString(R.string.check_internet_connection));
        }
    }

    @Override
    public void onInventoryScanUpdated(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(context);
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_LONG).show();
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                        }
                    }
                }, getActivity());
            } else {
                ErrorMessageHandler.handleErrorMessage(response.getCode(), getActivity());
            }
        } else {
            DialogClass.alerDialog(context, getResources().getString(R.string.check_internet_connection));
        }
    }

    private void updateInventoryToServer(ArrayList<ScanData> scanDataList) {
        DialogClass.showDialog(context, this.getString(R.string.please_wait));
        int locationId = (int) tvLocation.getTag();

        UpdateInventoryScanRequest updateInventoryScanRequest = new UpdateInventoryScanRequest();
        updateInventoryScanRequest.setItems(scanDataList);
        updateInventoryScanRequest.setCurrentLatitude(CurrentLocationUtil.latitude);
        updateInventoryScanRequest.setCurrentLongitude(CurrentLocationUtil.longitude);
        updateInventoryScanRequest.setIsPresentOnLocation(isPresentOnLocation ? 1 : 0);
        updateInventoryScanRequest.setLocationId(locationId);
        updateInventoryScanRequest.setScanStartTime(scanStartTime);
        updateInventoryScanRequest.setScanEndTime(scanEndTime);
        mActionsListener.updateInventoryScan(updateInventoryScanRequest);
    }


    private void setData(SearchNearLocationsResponse searchNearLocationsResponse) {

        Gson gson = new Gson();
        locationJson = gson.toJson(searchNearLocationsResponse);
        ArrayList<Zone> zones = searchNearLocationsResponse.getZones();
        ArrayList<Product> products = new ArrayList<>();
        if (zones.size() > 0) {
            products = searchNearLocationsResponse.getZones().get(0).getProducts();
        }
        ArrayList<LocationData> current = searchNearLocationsResponse.getLocation().getCurrent();

        if (current.size() == 0) {
            tvLocation.setText("No Location Found");
        } else {
            LocationData currentLocationData = current.get(0);
            tvLocation.setText(currentLocationData.getAddress());
            tvLocation.setTag(currentLocationData.getLocationId());
            final ArrayList<LocationData> near = searchNearLocationsResponse.getLocation().getNear();

            for (LocationData locationData : near) {
                if (locationData.getLocationId() == currentLocationData.getLocationId()) {
                    isPresentOnLocation = true;
                    break;
                } else {
                    isPresentOnLocation = false;
                }
            }

            if (near.size() > 0 && !isPresentOnLocation && !PrefUtils.isLocationDialogChoosed()) {
                String msg = "We'hv detected your location as " + near.get(0).getAddress() + ". Do you want to switch?";
                PrefUtils.setLocationDialogChoosed(true);
                DialogClass.showAlertDialog(context, msg, "", "SWITCH LOCATION", "No,THANKS", new DialogClass.DialogClickListener() {
                            @Override
                            public void onPositiveClick() {
//                                searchNearLocations(near.get(0).getLocationId());
                            }

                            @Override
                            public void onNegativeClick() {
                            }
                        }
                );
            }
        }

        LocationData inventoryLocation = getInventoryLocation();
        if (inventoryLocation == null && products.size() == 0 && !isChooseAddressShown) {
            isChooseAddressShown = true;
            chooseAddress();
        }

        ArrayList<ZoneGroup> zoneGroups = new ArrayList<>();
        for (Zone zone : zones) {
            zoneGroups.add(new ZoneGroup(zone.getZone(), zone.getProducts(), zone.getZoneId()));
        }
        setAdapter(zoneGroups);
    }

    public void setInventoryLocation(LocationData locationData) {
        Gson gson = new Gson();
        String locationJson = gson.toJson(locationData);
        PrefUtils.setInventoryLocation(locationJson);
    }

    public LocationData getInventoryLocation() {
        Gson gson = new Gson();
        String inventoryLocation = PrefUtils.getInventoryLocation();
        if (inventoryLocation.equals("")) {
            return null;
        } else {
            return gson.fromJson(inventoryLocation, LocationData.class);
        }
    }

    @Override
    public void onBeaconDetected(BeaconData beaconData) {
        kontakBeacons.add(beaconData);
        int minor = beaconData.getMinor();
        Log.i("beacon", minor + "");
        mAdapter.setIsFirstTime(true);
        mAdapter.onBeaconFound(beaconData);
    }

    @Override
    public void onItemClick(Product product) {
//        if (isScanning) {
//            stopScanAndIcon();
//        }
//        Intent intent = new Intent(context, InventoryDetailActivity.class);
//        intent.putExtra(StringUtils.SKU_ID, product.getSkuId());
//        intent.putExtra(StringUtils.INTENT_SOURCE, "Inventory");
//        intent.putExtra(StringUtils.IntentKey.IS_EDIT, true);
//        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDestroy() {
        if (kontaktBeaconScannerManual != null) {
            kontaktBeaconScannerManual.stopRanginBeacon();
        }
        super.onDestroy();
    }

    @Override
    public void onBluetoothChange(boolean isOn) {
        if (isOn) {
            changeIconState(true);
            if (isScanClicked) {
                Toast.makeText(getActivity(), "Scan for minimum five minutes", Toast.LENGTH_LONG).show();
                startScan();
            }
        } else {
            changeIconState(false);
        }
    }

    @Override
    public void onRefresh() {
        if (isScanning) {
            stopScanAndIcon();
        }
        searchNearLocations(defaultLocationId);
    }

    private void stopRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setEmptyView(mEmptyView);
    }
}
