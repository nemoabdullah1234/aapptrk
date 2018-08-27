package com.nicbit.traquer.stryker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.nicbit.traquer.stryker.Models.Item;
import com.nicbit.traquer.stryker.Models.inventory.SensorData;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.enums.ItemStatus;

import java.util.List;

import io.akwa.akcore.BeaconData;

public class CaseHistoryItemAdapter extends ArrayAdapter<Item> {

    private List<Item> shipmentItems;
    Context context;
    private ItemClickListener listener;
    private boolean isFirstTime = false;

    public CaseHistoryItemAdapter(Context context, List<Item> shipmentItems, ItemClickListener listener) {
        super(context, 0, shipmentItems);
        this.shipmentItems = shipmentItems;
        this.context = context;
        this.listener = listener;
    }

    public void setIsFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    public List<Item> getItemList() {
        return shipmentItems;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup parent) {
        final ViewHolder holder;
        if (contentView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = layoutInflater.inflate(R.layout.case_details_history_list_item, parent, false);
            holder.txtShipmentNo = (TextView) contentView.findViewById(R.id.txtShipmentNo);
            holder.txtUsedUnusedStatus = (TextView) contentView.findViewById(R.id.usedUnusedStatus);
            holder.checkbox = (Switch) contentView.findViewById(R.id.checkbox);
            holder.txtShipmentTitle = (TextView) contentView.findViewById(R.id.txtShipmentTitle);
            holder.btnComment = (ImageView) contentView.findViewById(R.id.commentExpandBtn);
            holder.outer = (RelativeLayout) contentView.findViewById(R.id.rl_outer);
            holder.mStatus = (ImageView) contentView.findViewById(R.id.iv_status);


            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();

        }
        final Item item = shipmentItems.get(position);
        if (item != null) {
            holder.txtShipmentNo.setText(item.getL1());
            holder.txtShipmentTitle.setText(item.getL2());
            holder.checkbox.setChecked(ItemStatus.getStateByStatus(item.getL5()));
            holder.txtUsedUnusedStatus.setText(ItemStatus.getNameByStatus(item.getL5()));

            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    shipmentItems.get(position).setL5(ItemStatus.getStatusByState(isChecked));
                    holder.txtUsedUnusedStatus.setText(ItemStatus.getNameByStatus(item.getL5()));
//                        notifyDataSetChanged();

                }
            });
            holder.btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemCommentClicked(item);
                }
            });
            holder.outer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(item);
                }
            });

//            if(item.getFound()!=null){
            if (item.getFound()) {
                holder.mStatus.setImageResource(R.drawable.green_tick);
            } else {
                if (!isFirstTime) {
                    holder.mStatus.setImageResource(R.drawable.grey_tick);
                } else {
                    holder.mStatus.setImageResource(R.drawable.item_not_found);
                }
            }
//            }

        }


        return contentView;
    }

    public void onBeaconFound(BeaconData iBeacon) {

        int size = shipmentItems.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                Item product = shipmentItems.get(i);
                SensorData sensor = product.getSensor();
                if (sensor != null && !product.getFound() && sensor.getMinor() == iBeacon.getMinor()
                        && sensor.getMajor() == iBeacon.getMajor()) {
                    product.setFound(true);
                    Log.i("test", product.getSkuId() + "");
//                    shipmentItems.remove(i);
//                    shipmentItems.add(0, product);
                }
            }
            notifyDataSetChanged();
        }

    }

    @Override
    public int getCount() {
        return shipmentItems.size();
    }

    @Override
    public Item getItem(int position) {
        return shipmentItems.get(position);
    }

    static class ViewHolder {
        TextView txtShipmentNo, txtShipmentTitle, txtUsedUnusedStatus;
        ImageView btnComment;
        Switch checkbox;
        RelativeLayout outer;
        ImageView mStatus;
    }


    public interface ItemClickListener {
        void onItemClicked(Item item);

        void onItemCommentClicked(Item item);
    }


}