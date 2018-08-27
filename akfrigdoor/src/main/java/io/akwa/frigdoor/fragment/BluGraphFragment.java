package io.akwa.frigdoor.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.akwa.frigdoor.MyMarkerView;
import io.akwa.frigdoor.R;

/**
 * Created by niteshgoel on 3/30/16.
 */
public class BluGraphFragment extends BluBaseFragment {
    private TextView txtAttrCompany, txtReportedIssueCount, txtAttrName1, txtLocationName;
    private LinearLayout lllocation;
    private LineChart mChart;
    private LinearLayout llReportIssue;
    private boolean isViewCreated=false;

    public BluGraphFragment() {
    }

    @Override
    protected void updateViews() {
       initGraph();
        if (bluTagData.bluTagInfo.temp != null && bluTagData.bluTagInfo.temp.length > 0)
            setTempData(bluTagData.bluTagInfo.temp);
        renderData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blu_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        isViewCreated=true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (getActivity() instanceof BluTagDetailActivity){
//            bluTagData = ((BluTagDetailActivity) getActivity()).getTagData();
//        }else{
//            bluTagData = ((BluScanHistoryDetailActivity) getActivity()).getTagData();
//        }
//        if (bluTagData != null) {
//
//        }
    }

    private void renderData() {
        setTopView();
//        updateCount();
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

        mChart = (LineChart) view.findViewById(R.id.chart);

    }

    private void initGraph() {
        mChart.setDrawGridBackground(false);
        // no description text
//        mChart.setDescription("");
        mChart.getDescription().setEnabled(false);
//        mChart.setNoDataTextDescription("Please scan the tag.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        float maxTemp = bluTagData.bluTagInfo.getMaxTemp();
        float minTemp = bluTagData.bluTagInfo.getMinTemp();

        float maxRecordedTemp = bluTagData.bluTagInfo.getMaxRecordedTemp();
        float minRecordedTemp = bluTagData.bluTagInfo.getMinRecordedTemp();

        float defaultMin=5;


        float maxX = Math.max(maxRecordedTemp, maxTemp);
        if(maxX>0){
            maxX+=defaultMin;
        }else{
            maxX-=defaultMin;
        }
        float minX = Math.min(minRecordedTemp, minTemp);
        if(minX>0){
            minX-=defaultMin;
        }else{
            minX+=defaultMin;
        }
        LimitLine ll1 = new LimitLine(maxTemp, "Max");
        ll1.setLineWidth(2f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(minTemp, "Min");
        ll2.setLineWidth(2f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setValueFormatter(new YAxisValueFormatter());
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMaxValue(maxX);
        leftAxis.setAxisMinValue(minX);
        //leftAxis.setYOffset(20f);
        // leftAxis.enableGridDashedLine(10f, 10f, 0f);
        //  leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);

        // set the marker to the chart
        mChart.setMarker(mv);
//        mChart.setDescription("");

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data

        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
//        mChart.invalidate();

        // get the legend (only possible after setting data)
//         Legend legend = mChart.getLegend();

        // modify the legend ...
//         legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
//         legend.setForm(Legend.LegendForm.LINE);
//        legend.setla



//        ArrayList<String> xData = new ArrayList<String>();
//        XAxis xAxis = mChart.getXAxis();
//        String startDate = bluTagData.bluTagInfo.getStartTime();
//        xData.add(0, startDate);
//        xAxis.setValues(xData);
        //xAxis.resetLabelsToSkip();


    }


    private void setTempData(Float[] temp) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy\nHH:mm");
        final ArrayList<String> arrayList = new ArrayList<>();
//        ArrayList<Integer> temp = new ArrayList<>();
//        for (int ii=0;ii<100;ii++){
//            temp.add(ii);
//        }
        long milisec=1000 * bluTagData.bluTagInfo.getEpochTempRecordStartTime();
        Date date=new Date(milisec);
        String format = simpleDateFormat.format(date);
        arrayList.add(format);

        for (int i=0;i<temp.length;i++){
            milisec=milisec + bluTagData.bluTagInfo.getMeasurementCycle()*1000;
            date=new Date(milisec);
            format = simpleDateFormat.format(date);
            arrayList.add(format);
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i = 0; i < temp.length; i++) {
            yVals.add(new Entry(i,temp[i]));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Temperature");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
//        set1.enableDashedLine(10f, 5f, 0f);
//        set1.enable
//        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.BLUE);
        set1.setCircleColor(Color.BLUE);
        set1.setLineWidth(1f);
        set1.setCircleRadius(2f);
        set1.setDrawCircleHole(true);
        set1.setDrawFilled(false);
        set1.setDrawValues(false);
//        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        final String[] quarters = new String[] { "Q1", "Q2", "Q3", "Q4" };

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return arrayList.get((int)value);
            }

            // we don't draw numbers, so no decimal digits needed
//            @Override
//            public int getDecimalDigits() {  return 0; }


        };

        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        xAxis.setLabelRotationAngle(-90);
//        xAxis.setAvoidFirstLastClipping(true);
//        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
//        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(set1);

        // set data
        if (!mChart.isEmpty())
            mChart.clearValues();
        mChart.setData(data);
        mChart.invalidate();
    }


    private class YAxisValueFormatter implements IAxisValueFormatter{
        protected DecimalFormat mFormat;

        public YAxisValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }

        /**
         * Allow a custom decimalformat
         *
         * @param format
         */
        public YAxisValueFormatter(DecimalFormat format) {
            this.mFormat = format;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value) + getString(R.string.temperature_unit);
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser & isViewCreated) {
//            updateCount();
        } else {

        }
    }
}
