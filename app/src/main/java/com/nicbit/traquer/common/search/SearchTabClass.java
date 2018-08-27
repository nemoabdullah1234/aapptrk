package com.nicbit.traquer.common.search;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicbit.traquer.stryker.R;

public class SearchTabClass {
    public Activity activity;

    public SearchTabClass(Activity activity, String[] tabValues) {
        this.activity = activity;
        this.tabValues = tabValues;
    }


    int[] tabIconsUnselected = {R.drawable.icon_cases_off, R.drawable.icon_items_off};

    String[] tabValues;

    int[] tabIconsSelected = {R.drawable.icon_cases_on, R.drawable.icon_items_on};


    public int getTabIconUnselected(int id) {
        return tabIconsUnselected[id];
    }

    public int getTabIconSelected(int id) {
        return tabIconsSelected[id];
    }

    public String getTabValue(int id) {
        return tabValues[id];

    }


    public View getTabView(int id) {
        View view = LayoutInflater.from(activity).inflate(R.layout.custom_tab, null);
        TextView newTab = (TextView) view.findViewById(R.id.text);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        newTab.setText(getTabValue(id));
        newTab.setTextColor(ContextCompat.getColor(activity, R.color.traquer_white_fifty_opacity));
        icon.setImageResource(getTabIconUnselected(id));
        return view;
    }

    public void getUnSelectedTabView(int id, TabLayout.Tab tab) {
        TextView newTab = (TextView) tab.getCustomView().findViewById(R.id.text);
        ImageView icon = (ImageView) tab.getCustomView().findViewById(R.id.icon);
        newTab.setTextColor(ContextCompat.getColor(activity, R.color.traquer_white_fifty_opacity));
        icon.setImageResource(getTabIconUnselected(id));
    }

    public void getSelectedTabView(int id, TabLayout.Tab tab) {
        TextView newTab = (TextView) tab.getCustomView().findViewById(R.id.text);
        newTab.setTextColor(ContextCompat.getColor(activity, R.color.traquer_white));
        ImageView icon = (ImageView) tab.getCustomView().findViewById(R.id.icon);
        icon.setImageResource(getTabIconSelected(id));
    }
}
