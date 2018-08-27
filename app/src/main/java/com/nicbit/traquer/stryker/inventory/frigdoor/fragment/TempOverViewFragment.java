package com.nicbit.traquer.stryker.inventory.frigdoor.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.inventory.frigdoor.TempBreachAdapter;

import static com.nicbit.traquer.stryker.BaseApplication.context;


public class TempOverViewFragment extends TempBaseFragment {

    private TextView tvAttr1, tvAttr2;
    private ListView breachesListView;
    private TempBreachAdapter adapter;
    private boolean isViewCreated = false;


    public TempOverViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temp_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        isViewCreated = true;
    }

    @Override
    protected void updateViews() {
        renderData();
    }


    private void renderData() {

        if (tempData != null) {
//            tvAttr2.setText(tempData.getMinRecordedTemp()+"");
            tvAttr1.setText(tempData.getMinTemp() + "/" + tempData.getMaxTemp() + context.getString(R.string.temperature_unit));
            tvAttr2.setText(tempData.getMinRecordedTemp() + "/" + tempData.getMaxRecordedTemp() + context.getString(R.string.temperature_unit));
        } else {
            tvAttr1.setText("NA");
            tvAttr2.setText("NA");

        }
        if (tempData.getBreachInfos() != null && tempData.getBreachInfos().size() > 0) {
            adapter = new TempBreachAdapter(baseActivity, R.layout.country_list_items, tempData.getBreachInfos());
            breachesListView.setAdapter(adapter);
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser & isViewCreated) {
//            updateCount();
//        }
//    }

    private void initView(View view) {

//        txtAttrName1 = (TextView) view.findViewById(R.id.txtAttrName1);
        tvAttr1 = (TextView) view.findViewById(R.id.txtAttrValue1);
        tvAttr2 = (TextView) view.findViewById(R.id.txtAttrValue2);
        breachesListView = (ListView) view.findViewById(R.id.breachesListView);
        breachesListView.setEmptyView(view.findViewById(android.R.id.empty));
    }


}