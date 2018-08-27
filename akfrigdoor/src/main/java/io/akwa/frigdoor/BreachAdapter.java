package io.akwa.frigdoor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class BreachAdapter extends ArrayAdapter<BreachInfo> {

    private ArrayList<BreachInfo> breachInfoList;
    Context context;

    public BreachAdapter(Context context, int textViewResourceId,
                         ArrayList<BreachInfo> breachInfos) {
        super(context, textViewResourceId, breachInfos);
        this.breachInfoList =breachInfos;
        this.context = context;
    }


    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;
        if (contentView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = layoutInflater.inflate(R.layout.layout_breach_item, parent, false);
            holder.dateCustomTextView = (TextView) contentView.findViewById(R.id.datetxt);
            holder.timeCustomTextView = (TextView) contentView.findViewById(R.id.timeTxt);
            holder.temperatureCustomTextView = (TextView) contentView.findViewById(R.id.temperatureTxt);

            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();


        }
        if (position < getCount()) {
            BreachInfo breachInfo = breachInfoList.get(position);
            if (breachInfo != null) {
                holder.dateCustomTextView.setText(formatDate(breachInfo.start));
                holder.timeCustomTextView.setText(eu.blulog.blulib.Utils.secondsToInterval(breachInfo.duration));
                holder.temperatureCustomTextView.setText(breachInfo.minMaxTemp + context.getString(R.string.temperature_unit));
            }

        }


        return contentView;
    }

    public static String formatDate(Date date){
        SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy H:mm a");
        String str =ft.format(date).replace("am", "AM").replace("pm","PM");
        return str;
    }

    @Override
    public int getCount() {
        return breachInfoList.size();
    }

    @Override
    public BreachInfo getItem(int position) {
        return breachInfoList.get(position);
    }

    static class ViewHolder {
        TextView dateCustomTextView,timeCustomTextView,temperatureCustomTextView ;

    }



}