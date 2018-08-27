package com.nicbit.traquer.stryker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nicbit.traquer.stryker.Models.Item;
import com.nicbit.traquer.stryker.R;

import java.util.List;

public class ShipmentItemAdapter extends ArrayAdapter<Item> {

    private List<Item> shipmentItems;
    Context context;
    private ItemClickListener listener;
    private boolean isDashBoard;


    public ShipmentItemAdapter(Context context, List<Item> shipmentItems, ItemClickListener listener, boolean isDashBoard) {
        super(context, 0, shipmentItems);
        this.shipmentItems = shipmentItems;
        this.context = context;
        this.listener = listener;
        this.isDashBoard = isDashBoard;
    }


    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;
        if (contentView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = layoutInflater.inflate(R.layout.shipment_list_item, parent, false);
            holder.txtShipmentNo = (TextView) contentView.findViewById(R.id.txtShipmentNo);
            holder.ivMissing = (ImageView) contentView.findViewById(R.id.iv_missing);
            holder.txtShipmentStatus = (TextView) contentView.findViewById(R.id.txtShipmentStatus);
            holder.txtShipmentTitle = (TextView) contentView.findViewById(R.id.txtShipmentTitle);
            holder.btnComment = (Button) contentView.findViewById(R.id.commentExpandBtn);
            holder.commentLayout = (LinearLayout) contentView.findViewById(R.id.commentLayout);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();

        }
        final Item item = shipmentItems.get(position);
        holder.txtShipmentNo.setText(item.getL1());
        holder.txtShipmentTitle.setText(item.getL2());
        if (isDashBoard) {
            holder.txtShipmentStatus.setVisibility(View.GONE);
            holder.btnComment.setVisibility(View.GONE);
        } else {
            holder.txtShipmentStatus.setVisibility(View.GONE);

        }

        if (item.getIsMissing() == 1) {
            holder.ivMissing.setVisibility(View.VISIBLE);
        } else {
            holder.ivMissing.setVisibility(View.GONE);
        }


        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCommentClicked(item.getSkuId());
            }
        });
        holder.commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(item.getSkuId());
            }
        });

        return contentView;
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
        TextView txtShipmentNo, txtShipmentStatus, txtShipmentTitle;
        ImageView ivMissing;
        Button btnComment;
        LinearLayout commentLayout;
    }


    public interface ItemClickListener {
        void onItemClicked(String itemId);

        void onCommentClicked(String itemId);

    }

}