package io.akwa.frigdoor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import io.akwa.frigdoor.BreachAdapter;
import io.akwa.frigdoor.FrigdorHomeActivity;
import io.akwa.frigdoor.R;


public class BluTagOverViewFragment extends BluBaseFragment {

    private TextView tvAttr1, tvAttr2;
    private TextView txtAttrCompany, txtReportedIssueCount, txtAttrName1, txtLocationName;
    private LinearLayout lllocation;
    private ListView breachesListView;
    private BreachAdapter adapter;
    private LinearLayout llReportIssue;
    private boolean isViewCreated=false;


    public BluTagOverViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blu_tag_overview_new, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        isViewCreated=true;
    }

    @Override
    protected void updateViews() {
        renderData();
        updateCount();
    }


    private void renderData() {

        setTopView();
        if (bluTagData != null && bluTagData.bluTagInfo != null) {
            tvAttr2.setText(bluTagData.bluTagInfo.getMinRecordedTemperature());
            tvAttr1.setText(bluTagData.bluTagInfo.getMinTemperature()+"/"+bluTagData.bluTagInfo.getMaxTemperature());
            tvAttr2.setText(bluTagData.bluTagInfo.getMinRecordedTemperature()+"/"+bluTagData.bluTagInfo.getMaxRecordedTemperature());
        } else {
            tvAttr1.setText("NA");
            tvAttr2.setText("NA");

        }
        if (bluTagData.bluTagInfo.getBreachInfos() != null && bluTagData.bluTagInfo.getBreachInfos().size() > 0) {
            adapter = new BreachAdapter(baseActivity, R.layout.country_list_items, bluTagData.bluTagInfo.getBreachInfos());
            breachesListView.setAdapter(adapter);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser & isViewCreated) {
            updateCount();
        }
    }

    private void updateCount() {
        if (FrigdorHomeActivity.reportCount == 0) {
            txtReportedIssueCount.setVisibility(View.GONE);
        } else {
            txtReportedIssueCount.setVisibility(View.VISIBLE);
            txtReportedIssueCount.setText(FrigdorHomeActivity.reportCount + "");

        }
    }

    private void initView(View view) {

        txtAttrCompany = (TextView) view.findViewById(R.id.txtAttrCompany);
        txtReportedIssueCount = (TextView) view.findViewById(R.id.txtReportedIssueCount);
        txtAttrName1 = (TextView) view.findViewById(R.id.txtAttrName1);
        txtLocationName = (TextView) view.findViewById(R.id.txtLocationName);
        lllocation = (LinearLayout) view.findViewById(R.id.lllocation);
        llReportIssue = (LinearLayout) view.findViewById(R.id.llReportIssue);

        llReportIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReportIssueClick();
            }
        });

        tvAttr1 = (TextView) view.findViewById(R.id.txtAttrValue1);
        tvAttr2 = (TextView) view.findViewById(R.id.txtAttrValue2);
        breachesListView = (ListView) view.findViewById(R.id.breachesListView);
        breachesListView.setEmptyView(view.findViewById(android.R.id.empty));
    }

    private void setTopView() {
        lllocation.setVisibility(View.GONE);
        if (bluTagData.productName != null && !TextUtils.isEmpty(bluTagData.productName))
            txtAttrCompany.setText(bluTagData.productName);
        else
            txtAttrCompany.setText("Product name not found");

        if (bluTagData.serialNo != null && !TextUtils.isEmpty(bluTagData.serialNo))
            txtAttrName1.setText("Serial No. " + bluTagData.serialNo);
        else
            txtAttrName1.setText("Serial no. not found");

        txtReportedIssueCount.setText("");
        txtLocationName.setText("");
    }

}