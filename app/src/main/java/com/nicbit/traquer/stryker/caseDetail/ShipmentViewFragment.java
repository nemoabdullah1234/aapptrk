package com.nicbit.traquer.stryker.caseDetail;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nicbit.traquer.common.EventsLog;
import com.nicbit.traquer.common.comments.ItemCommentActivity;
import com.nicbit.traquer.stryker.Models.CaseDetails;
import com.nicbit.traquer.stryker.Models.Item;
import com.nicbit.traquer.stryker.Models.Shipment;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.adapter.ShipmentItemAdapter;
import com.nicbit.traquer.stryker.inventory.details.InventoryDetailActivity;
import com.nicbit.traquer.stryker.util.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShipmentViewFragment extends Fragment implements ShipmentItemAdapter.ItemClickListener {
    private static final String SHIPMENT_DETAILS = "shipmentdtails";
    private static final String CASE_DETAILS = "caseDetails";
    Shipment shipment;
    String mCaseNumber;
    String caseId;
    TextView txtDoctorName, txtLocation, txtPhone, txtFax, txtSurgeryDetails, txtDeliveryStart, txtEstimatedDelivery;
    CaseDetails caseDetails;
    @BindView(R.id.listView)
    ListView listView;
    private TextView txtEstimatedDeliveryDate, txtDeliveryStartDate;
    boolean isDashboard;
    ImageView imgStatus;
    Button btnMap;
    Button btnReport;

    public ShipmentViewFragment() {
        // Required empty public constructor
    }

    public static ShipmentViewFragment newInstance(Shipment shipment, CaseDetails caseDetails, String mCaseNumber, boolean isDashBoard, String caseId) {
        ShipmentViewFragment fragment = new ShipmentViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(SHIPMENT_DETAILS, shipment);
        args.putParcelable(CASE_DETAILS, caseDetails);
        args.putString(StringUtils.CASE_NUMBER, mCaseNumber);
        args.putString(StringUtils.CASE_ID, caseId);
        args.putBoolean(StringUtils.IS_DASHBOARD, isDashBoard);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shipment = getArguments().getParcelable(SHIPMENT_DETAILS);
            caseDetails = getArguments().getParcelable(CASE_DETAILS);
            mCaseNumber = getArguments().getString(StringUtils.CASE_NUMBER);
            caseId = getArguments().getString(StringUtils.CASE_ID);
            isDashboard = getArguments().getBoolean(StringUtils.IS_DASHBOARD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shipment_view, container, false);
        ButterKnife.bind(this, view);
        View headerView = inflater.inflate(R.layout.shipment_details_header_view, listView, false);
        txtSurgeryDetails = (TextView) headerView.findViewById(R.id.txtSurgeryName);
        txtPhone = (TextView) headerView.findViewById(R.id.txtPhone);
        txtFax = (TextView) headerView.findViewById(R.id.txtFax);
        txtDoctorName = (TextView) headerView.findViewById(R.id.txtDocterName);
        txtEstimatedDelivery = (TextView) headerView.findViewById(R.id.txtDeliverEnd);
        txtEstimatedDeliveryDate = (TextView) headerView.findViewById(R.id.txtDeliverEndDate);
        txtDeliveryStart = (TextView) headerView.findViewById(R.id.txtDeliverStart);
        txtDeliveryStartDate = (TextView) headerView.findViewById(R.id.txtDeliverStartDate);
        txtLocation = (TextView) headerView.findViewById(R.id.txtLocation);
        imgStatus = (ImageView) headerView.findViewById(R.id.imgStatus);


        listView.addHeaderView(headerView);
        if (shipment != null && shipment.getItems().size() > 0) {
            setAdapter(shipment.getItems());
            setShipmentDetails(shipment);
        }
        setDoctorDetails(caseDetails);


        txtPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EventsLog.customEvent("CASE_DETAIL", "PHONE", "CLICK");
                String number = (String) txtPhone.getTag();
                if (number != null && !number.equals("NA") && !number.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);
                }
            }
        });
        return view;
    }


    public void setAdapter(List<Item> itemList) {
        ShipmentItemAdapter shipmentItemAdapter = new ShipmentItemAdapter(getActivity(), itemList, this, isDashboard);
        listView.setAdapter(shipmentItemAdapter);

    }

    public void setDoctorDetails(CaseDetails caseDetails) {
        if (caseDetails != null) {
            txtDoctorName.setText(caseDetails.getL1());
            txtSurgeryDetails.setText(caseDetails.getL2() + " | " + caseDetails.getL7());
            txtLocation.setText(caseDetails.getL3());
            if (caseDetails.getL4().isEmpty()) {
                txtPhone.setVisibility(View.GONE);
            } else {
                txtPhone.setVisibility(View.VISIBLE);
                txtPhone.setText(caseDetails.getL4());

            }

            if (caseDetails.getL5().isEmpty()) {
                txtFax.setVisibility(View.GONE);
            } else {
                txtFax.setVisibility(View.GONE);
                // txtFax.setText("Fax: " + caseDetails.getL5());

            }
            txtPhone.setTag(caseDetails.getL4());
        }
    }

    public void setShipmentDetails(Shipment shipment) {

        switch (shipment.getShipStatus()) {
            case 0:
                //Fail Case
                txtDeliveryStart.setText("Order Received");
                txtDeliveryStartDate.setText(shipment.getL5());
                txtEstimatedDeliveryDate.setText(shipment.getL4());
                txtEstimatedDelivery.setText("Surgery Date");
                imgStatus.setVisibility(View.GONE);
                break;
            case 10:
                //New
                txtDeliveryStart.setText("Order Received");
                txtDeliveryStartDate.setText(shipment.getL5());
                txtEstimatedDeliveryDate.setText(shipment.getL4());
                txtEstimatedDelivery.setText("ETD");
                imgStatus.setImageResource(R.drawable.new_icon);
                imgStatus.setVisibility(View.VISIBLE);
                break;
            case 20:
                //Scheduled
                txtDeliveryStart.setText("Order Scheduled");
                txtDeliveryStartDate.setText(shipment.getL5());
                txtEstimatedDeliveryDate.setText(shipment.getL4());
                txtEstimatedDelivery.setText("ETD");
                imgStatus.setImageResource(R.drawable.scheduled_icon);
                imgStatus.setVisibility(View.VISIBLE);
                break;

            case 25:
                //Partial shipped
                txtDeliveryStart.setText("Order Shipped");
                txtDeliveryStartDate.setText(shipment.getL5());
                txtEstimatedDeliveryDate.setText(shipment.getL4());
                txtEstimatedDelivery.setText("ETD");
                imgStatus.setImageResource(R.drawable.partial_shipped);
                imgStatus.setVisibility(View.VISIBLE);
                break;

            case 30:
                //Soft-Shipped
                txtDeliveryStart.setText("Order Soft Shipped");
                txtDeliveryStartDate.setText(shipment.getL5());
                txtEstimatedDeliveryDate.setText(shipment.getL4());
                txtEstimatedDelivery.setText("ETD");
                imgStatus.setImageResource(R.drawable.intransit_icon);
                imgStatus.setVisibility(View.VISIBLE);
                break;

            case 40:
                //In-transit
                txtDeliveryStart.setText("Order Shipped");
                txtDeliveryStartDate.setText(shipment.getL5());
                txtEstimatedDeliveryDate.setText(shipment.getL4());
                txtEstimatedDelivery.setText("ETD");
                imgStatus.setImageResource(R.drawable.intransit_icon);
                imgStatus.setVisibility(View.VISIBLE);
                break;

            case 45:
                //Partial delivered
                txtDeliveryStart.setText("Order Shipped");
                txtDeliveryStartDate.setText(shipment.getL5());
                txtEstimatedDeliveryDate.setText(shipment.getL4());
                txtEstimatedDelivery.setText("ETD");
                imgStatus.setImageResource(R.drawable.partial_delivered);
                imgStatus.setVisibility(View.VISIBLE);
                break;

            case 50:
                //Soft-Delivered
                txtDeliveryStart.setText("Order Shipped");
                txtDeliveryStartDate.setText(shipment.getL5());
                txtEstimatedDeliveryDate.setText(shipment.getL4());
                txtEstimatedDelivery.setText("Delivered");
                imgStatus.setImageResource(R.drawable.delievered_icon);
                imgStatus.setVisibility(View.VISIBLE);
                break;

            case 60:
                //Delivered
                txtDeliveryStart.setText("Order Shipped");
                txtDeliveryStartDate.setText(shipment.getL5());
                txtEstimatedDeliveryDate.setText(shipment.getL4());
                txtEstimatedDelivery.setText("Delivered");
                imgStatus.setImageResource(R.drawable.delievered_icon);
                imgStatus.setVisibility(View.VISIBLE);
                break;


            case 70:
                //Cancelled
                break;
        }

    }


    @Override
    public void onItemClicked(String skuID) {
        Intent i = new Intent(getActivity(), InventoryDetailActivity.class);
        i.putExtra(StringUtils.IntentKey.SKU_ID, skuID);
        i.putExtra(StringUtils.CASE_NUMBER, mCaseNumber);
        i.putExtra(StringUtils.CASE_ID, caseId);
        i.putExtra(StringUtils.SHIPMENT_ID, skuID);
        i.putExtra(StringUtils.SHIPPING_TEXT, shipment.getL1());
        i.putExtra(StringUtils.INTENT_SOURCE, "Shipment");
        i.putExtra(StringUtils.IntentKey.IS_EDIT, false);
        getActivity().startActivity(i);
    }

    @Override
    public void onCommentClicked(String itemId) {
        Intent i = new Intent(getActivity(), ItemCommentActivity.class);
        i.putExtra(StringUtils.IS_COMPLETED, true);
        i.putExtra(StringUtils.CASE_NUMBER, mCaseNumber);
        i.putExtra(StringUtils.SKU_ID, itemId);
        getActivity().startActivity(i);
    }
}
