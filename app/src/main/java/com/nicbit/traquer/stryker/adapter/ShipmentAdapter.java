package com.nicbit.traquer.stryker.adapter;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nicbit.traquer.stryker.Models.Item;
import com.nicbit.traquer.stryker.Models.Shipment;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.enums.ItemStatus;
import com.nicbit.traquer.stryker.util.TypefaceTextView;

import java.util.HashMap;
import java.util.List;


public class ShipmentAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<Shipment> listDataHeader;
    private HashMap<Shipment, List<Item>> listDataChild;
    private boolean isFromDashboard;
    private ShipmentAdapterViewClickListener listener;

    public ShipmentAdapter(ShipmentAdapterViewClickListener listener, Context context, List<Shipment> listDataHeader,
                           HashMap<Shipment, List<Item>> listChildData, boolean isFromDashboard) {
        this.listener = listener;
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.isFromDashboard = isFromDashboard;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = layoutInflater.inflate(R.layout.layout_shipment_child_item, null);
            holder = new ViewHolder();
            holder.label5TextView = (TypefaceTextView) vi.findViewById(R.id.txtLabel5);
            holder.label6TextView = (TypefaceTextView) vi.findViewById(R.id.txtLabel6);
            holder.label7TextView = (TypefaceTextView) vi.findViewById(R.id.txtLabel7);
            holder.line = vi.findViewById(R.id.line);
            holder.childRelativeLayout = (RelativeLayout) vi.findViewById(R.id.rlChild);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        Item childItem = (Item) getChild(groupPosition, childPosition);

        int childCount = getChildrenCount(groupPosition);
        if (childCount > 0) {
            holder.label5TextView.setText(childItem.getL1());
            holder.label6TextView.setText(childItem.getL2());
            if (isFromDashboard) {
                holder.label7TextView.setText("");
            } else {
                holder.label7TextView.setText(ItemStatus.getNameByStatus(childItem.getL5()));
            }
            holder.label6TextView.setSelected(true);
            holder.childRelativeLayout.setTag(childItem.getItemId());
            holder.childRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String data = (String) v.getTag();
                    listener.onShipmentItemClicked(data);
                }
            });

            if (childCount > 1 && childPosition < childCount - 1) {
                holder.line.setVisibility(View.VISIBLE);
            } else {
                holder.line.setVisibility(View.GONE);
                if ((childPosition + 1) == childCount) {

                }
            }
        }


        return vi;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = layoutInflater.inflate(R.layout.layout_shipment_header_item, null);
            holder = new ViewHolder();
            holder.label1TextView = (TypefaceTextView) vi.findViewById(R.id.txtLabel1);
            holder.label2TextView = (TypefaceTextView) vi.findViewById(R.id.txtLabel2);
            holder.label3TextView = (TypefaceTextView) vi.findViewById(R.id.txtLabel3);
            holder.label4TextView = (TypefaceTextView) vi.findViewById(R.id.txtLabel4);
            holder.reportIssueBtn = (TypefaceTextView) vi.findViewById(R.id.reportIssueBtn);
            holder.errorImageView = (ImageView) vi.findViewById(R.id.errorImageView);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        Shipment headerItem = (Shipment) getGroup(groupPosition);

        holder.label1TextView.setText(headerItem.getL1());
        holder.label2TextView.setText(headerItem.getL2());
        holder.label2TextView.setTextColor(Color.parseColor(headerItem.getColor()));
        holder.label3TextView.setText(headerItem.getL3());
        holder.label3TextView.setSelected(true);
        holder.label4TextView.setText(headerItem.getL4());
        holder.reportIssueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int data = (int) v.getTag();
                listener.onShipmentClicked(data);
            }
        });
        holder.reportIssueBtn.setTag(groupPosition);

        if (headerItem.getIsReported() == 0) {
            holder.errorImageView.setVisibility(View.GONE);
        } else {
            holder.errorImageView.setVisibility(View.VISIBLE);
        }

        return vi;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }


    public static class ViewHolder {
        TypefaceTextView label1TextView, label2TextView,
                label3TextView, label4TextView, label5TextView, label6TextView, label7TextView, reportIssueBtn;
        View line;
        RelativeLayout childRelativeLayout;
        ImageView errorImageView;

    }

    public interface ShipmentAdapterViewClickListener {
        void onShipmentClicked(int position);

        void onShipmentItemClicked(String skuId);
    }
}