package com.nicbit.traquer.common.newInventory.zone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicbit.traquer.common.EventsLog;
import com.nicbit.traquer.common.newInventory.response.ZoneApiResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.inventory.details.InventoryDetailActivity;
import com.nicbit.traquer.stryker.util.AppLog;
import com.nicbit.traquer.stryker.util.DialogClass;
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


public class ZoneActivity extends BasicScanActivity implements ZoneView.View, KontaktBeaconScannerManual.KontaktBeaconListener, ZoneAdapter.InventoryListItemClickListener, SwipeRefreshLayout.OnRefreshListener {


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
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_title)
    TextView mTitle;


    private ZonePresenter mActionsListener;
    ZoneAdapter mAdapter;
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
    String floorID;
    String floorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        ButterKnife.bind(this);
        context = this;
        mActionsListener = new ZonePresenter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new ZoneAdapter(context, new ArrayList<ZoneGroup>(), this);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        floorID = getIntent().getStringExtra("floorId");
        floorName = getIntent().getStringExtra("floorName");
        tvLocation.setText(floorName);
        getZones(floorID);
        setupActionBar();

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


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_search:
                Intent intent = new Intent(this, getSearchActivity());
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setAdapter(ArrayList<ZoneGroup> zones) {
        mAdapter = new ZoneAdapter(context, zones, this);
        mAdapter.setIsFirstTime(false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        updateTotal(false);
    }

    private void setupActionBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        getSupportActionBar().setTitle("");
        mTitle.setText("Zones");
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
                    scanData.setProductId(product.getId());
                    if (product.getThings().size() > 0) {
                        scanData.setSensorId(product.getThings().get(0).getName());
                        scanData.setSkuId(product.getThings().get(0).getId());
                    }
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


    private void stopScanAndIcon() {
        kontaktBeaconScannerManual.stopRanginBeacon();
        isScanning = false;
        stopAnimation();
        mAdapter.isScanningFinish = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkBluetoothOnOff();
    }

    void getZones(String floorID) {
        DialogClass.showDialog(context, this.getString(R.string.please_wait));
        mActionsListener.getZoneList(floorID);
    }

    @Override
    public void onZoneList(ZoneApiResponse response, NicbitException e) {
        DialogClass.dismissDialog(context);
        stopRefresh();
        if (e == null) {
            if (response.getCode() == 200) {
                setData(response);

            } else if (response.getCode() == 209) {

            } else {
                ErrorMessageHandler.handleErrorMessage(response.getCode(), this);
            }
        } else {
            DialogClass.alerDialog(context, getResources().getString(R.string.check_internet_connection));
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
        if (isScanning) {
            stopScanAndIcon();
        }
        Intent intent = new Intent(context, InventoryDetailActivity.class);
        intent.putExtra(StringUtils.SKU_ID, product.getId());
        intent.putExtra(StringUtils.INTENT_SOURCE, "Inventory");
        intent.putExtra(StringUtils.IntentKey.IS_EDIT, false);
        startActivity(intent);
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
                Toast.makeText(this, "Scan for minimum five minutes", Toast.LENGTH_LONG).show();
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
        getZones(floorID);
    }

    private void stopRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setEmptyView(mEmptyView);
    }

    private void setData(ZoneApiResponse searchNearLocationsResponse) {

        List<ZoneApiResponse.Zones> zones = searchNearLocationsResponse.getData().getZones();
        List<Product> products = new ArrayList<>();
        if (zones.size() > 0) {
            products = searchNearLocationsResponse.getData().getZones().get(0).getProductList();
        }
        ArrayList<ZoneGroup> zoneGroups = new ArrayList<>();
        for (ZoneApiResponse.Zones zone : zones) {
            zoneGroups.add(new ZoneGroup(zone.getName(), zone.getProductList(), zone.getId()));
        }
        setAdapter(zoneGroups);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    private void updateInventoryToServer(ArrayList<ScanData> scanDataList) {
      /*  DialogClass.showDialog(context, this.getString(R.string.please_wait));
        int locationId = (int) tvLocation.getTag();

        UpdateInventoryScanRequest updateInventoryScanRequest = new UpdateInventoryScanRequest();
       // updateInventoryScanRequest.setItems(scanDataList);
        updateInventoryScanRequest.setCurrentLatitude(CurrentLocationUtil.latitude);
        updateInventoryScanRequest.setCurrentLongitude(CurrentLocationUtil.longitude);
        updateInventoryScanRequest.setIsPresentOnLocation(isPresentOnLocation ? 1 : 0);
        updateInventoryScanRequest.setLocationId(locationId);
        updateInventoryScanRequest.setScanStartTime(scanStartTime);
        updateInventoryScanRequest.setScanEndTime(scanEndTime);*/
        // mActionsListener.updateInventoryScan(updateInventoryScanRequest);
    }

}
