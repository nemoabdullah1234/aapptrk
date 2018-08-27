package com.nicbit.traquer.stryker.inventory.frigdoor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nicbit.traquer.stryker.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TempBreachAdapter extends ArrayAdapter<ApiBreachInfoModel> {

    private ArrayList<ApiBreachInfoModel> breachInfoList;
    Context context;

    public TempBreachAdapter(Context context, int textViewResourceId,
                             ArrayList<ApiBreachInfoModel> breachInfos) {
        super(context, textViewResourceId, breachInfos);
        this.breachInfoList = breachInfos;
        this.context = context;
    }


    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;
        if (contentView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = layoutInflater.inflate(R.layout.layout_temp_breach_item, parent, false);
            holder.dateCustomTextView = (TextView) contentView.findViewById(R.id.datetxt);
            holder.timeCustomTextView = (TextView) contentView.findViewById(R.id.timeTxt);
            holder.temperatureCustomTextView = (TextView) contentView.findViewById(R.id.temperatureTxt);

            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();


        }
        if (position < getCount()) {
            ApiBreachInfoModel breachInfo = breachInfoList.get(position);
            if (breachInfo != null) {
                holder.dateCustomTextView.setText(formatDate(new Date(breachInfo.getStart())));
                holder.timeCustomTextView.setText(eu.blulog.blulib.Utils.secondsToInterval(breachInfo.duration));
                holder.temperatureCustomTextView.setText(breachInfo.minMaxTemp + context.getString(R.string.temperature_unit));
            }

        }


        return contentView;
    }

    public static String formatDate(Date date) {
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy H:mm a");
        String str = ft.format(date).replace("am", "AM").replace("pm", "PM");
        return str;
    }

    @Override
    public int getCount() {
        return breachInfoList.size();
    }

    @Override
    public ApiBreachInfoModel getItem(int position) {
        return breachInfoList.get(position);
    }

    static class ViewHolder {
        TextView dateCustomTextView, timeCustomTextView, temperatureCustomTextView;

    }


}