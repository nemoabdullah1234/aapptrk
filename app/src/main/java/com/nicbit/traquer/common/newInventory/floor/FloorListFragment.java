package com.nicbit.traquer.common.newInventory.floor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nicbit.traquer.common.EventsLog;
import com.nicbit.traquer.common.newInventory.location.LocationContract;
import com.nicbit.traquer.common.newInventory.location.LocationPresenter;
import com.nicbit.traquer.common.newInventory.location.SelectCityActivity;
import com.nicbit.traquer.common.newInventory.response.FloorApiResponse;
import com.nicbit.traquer.common.newInventory.response.LocationApiResponse;
import com.nicbit.traquer.common.newInventory.zone.ZoneActivity;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.inventory.LocationData;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.util.CurrentLocationUtil;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.stryker.util.StringUtils;
import com.nicbit.traquer.stryker.view.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.akwa.akcore.BeaconData;
import io.akwa.akproximity.kontakt.KontaktBeaconScannerManual;

import static android.app.Activity.RESULT_OK;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class FloorListFragment extends Fragment implements FloorView.View, SwipeRefreshLayout.OnRefreshListener, FloorAdapter.RecycleViewItemClickListener, LocationContract.View {

    @BindView(R.id.listView)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_view)
    LinearLayout mEmptyView;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;


    private FloorPresenter mActionsListener;
    private LocationPresenter locationPresenter;

    FloorAdapter mAdapter;
    private Context context;
    ArrayList<Object> locationList;
    String defaultLocationId;
    KontaktBeaconScannerManual kontaktBeaconScannerManual;
    Set<BeaconData> kontakBeacons;
    String locationJson;
    private boolean isChooseAddressShown = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new FloorPresenter(this);
        locationPresenter = new LocationPresenter(this);
        LocationData inventoryLocation = getInventoryLocation();
        if (inventoryLocation != null) {
            defaultLocationId = inventoryLocation.getLocationId();
            getFloors(inventoryLocation.getLocationId());
            getLocation(inventoryLocation.getLocationId());

        } else {
            getLocation(defaultLocationId);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.floor_list_layout, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new FloorAdapter(context, new ArrayList<FloorApiResponse.Floor>(), this);
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

    private void setAdapter(List<FloorApiResponse.Floor> floors) {
        mAdapter = new FloorAdapter(context, floors, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
    }


    public void onLocationReceive(Intent data) {
        LocationData locationData = data.getParcelableExtra("LocationData");
        tvLocation.setText(locationData.getAddress());
        tvLocation.setTag(locationData.getLocationId());
        defaultLocationId = locationData.getLocationId();
        getFloors(defaultLocationId);
        setInventoryLocation(locationData);
    }

    @OnClick(R.id.rl_address)
    public void chooseAddress() {
//        if (isScanning) {
//            stopScanAndIcon();
//        }
        EventsLog.customEvent("SCAN", "ADDRESS", "CLICK");
        Intent intent = new Intent(context, SelectCityActivity.class);
        intent.putExtra("locationJson", locationJson);
        String id = "";
        Object tag = tvLocation.getTag();
        if (tag != null) {
            id = (String) tag;
        }
        intent.putExtra("locationId", id);
        startActivityForResult(intent, StringUtils.REQUEST_LOCATION);
    }

    private void setData(LocationApiResponse locationApiResponse) {

        Gson gson = new Gson();
        locationJson = gson.toJson(locationApiResponse);
        ArrayList<LocationData> current = locationApiResponse.getLocation().getCurrent();
        if (current.size() == 0 || defaultLocationId == null) {
            tvLocation.setText("No Location Found");
        } else {
            LocationData currentLocationData = current.get(0);
            if (defaultLocationId != null) {
                tvLocation.setText(currentLocationData.getAddress());
                tvLocation.setTag(currentLocationData.getLocationId());
            }
//            final ArrayList<LocationData> near = locationApiResponse.getLocation().getNear();
//            for (LocationData locationData : near) {
//                if (locationData.getLocationId() == currentLocationData.getLocationId()) {
//                    isPresentOnLocation = true;
//                    break;
//                } else {
//                    isPresentOnLocation = false;
//                }
//            }
//
//            if (near.size() > 0 && !isPresentOnLocation && !PrefUtils.isLocationDialogChoosed()) {
//                String msg = "We'hv detected your location as " + near.get(0).getAddress() + ". Do you want to switch?";
//                PrefUtils.setLocationDialogChoosed(true);
//                DialogClass.showAlertDialog(context, msg, "", "SWITCH LOCATION", "No,THANKS", new DialogClass.DialogClickListener() {
//                            @Override
//                            public void onPositiveClick() {
////                                searchNearLocations(near.get(0).getLocationId());
//                            }
//
//                            @Override
//                            public void onNegativeClick() {
//                            }
//                        }
//                );
//            }
        }

        LocationData inventoryLocation = getInventoryLocation();
        if (inventoryLocation == null && !isChooseAddressShown) {
            isChooseAddressShown = true;
            chooseAddress();
        }
    }


    void getFloors(String locationId) {
        DialogClass.showDialog(context, this.getString(R.string.please_wait));
        mActionsListener.getFloorList(locationId);
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
    public void onRefresh() {
        getFloors(defaultLocationId);
    }

    private void stopRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setEmptyView(mEmptyView);
    }

    @Override
    public void onFloorList(FloorApiResponse response, NicbitException e) {
        DialogClass.dismissDialog(context);
        stopRefresh();
        if (e == null) {
            if (response.getCode() == 200) {
                setAdapter(response.getData().getFloors());
            } else if (response.getCode() == 209) {

            } else {
                ErrorMessageHandler.handleErrorMessage(response.getCode(), getActivity());
            }
        } else {
            DialogClass.alerDialog(context, getResources().getString(R.string.check_internet_connection));
        }
    }

    @Override
    public void onItemClick(FloorApiResponse.Floor floor) {
        Intent intent = new Intent(getActivity(), ZoneActivity.class);
        intent.putExtra("floorId", floor.getId());
        intent.putExtra("floorName", floor.getName());
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StringUtils.REQUEST_LOCATION && resultCode == RESULT_OK) {
            onLocationReceive(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLocationList(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(context);
        stopRefresh();
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                LocationApiResponse locationApiResponse = response.getData().getReaderSearchNearLocationsResponse();
                if (locationApiResponse != null) {
                    setData(locationApiResponse);
                }
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            getLocation(defaultLocationId);
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

    public void getLocation(String locationId) {
        DialogClass.showDialog(context, this.getString(R.string.please_wait));
        locationPresenter.getLocationList(CurrentLocationUtil.latitude, CurrentLocationUtil.longitude, locationId);
    }
}
