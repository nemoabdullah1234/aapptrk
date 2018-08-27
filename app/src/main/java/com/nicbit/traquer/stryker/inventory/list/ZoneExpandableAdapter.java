package com.nicbit.traquer.stryker.inventory.list;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicbit.traquer.stryker.Models.inventory.Product;
import com.nicbit.traquer.stryker.Models.inventory.SensorData;
import com.nicbit.traquer.stryker.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import io.akwa.akcore.BeaconData;

public class ZoneExpandableAdapter extends ExpandableRecyclerViewAdapter<ZoneExpandableAdapter.ZoneViewHolder, ZoneExpandableAdapter.ProductViewHolder> {

    private boolean isFirstTime = false;
    private final Context context;
    private InventoryListItemClickListener listener;
    public boolean isScanningFinish = false;


    public ZoneExpandableAdapter(Context context, List<? extends ExpandableGroup> zones, InventoryListItemClickListener listener) {
        super(zones);
        this.context = context;
        this.listener = listener;
    }

    public void setIsFirstTime(boolean isFirstTime) {
        this.isFirstTime = isFirstTime;
    }

    @Override
    public ZoneViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_zone, parent, false);
        return new ZoneViewHolder(view);
    }

    @Override
    public ProductViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_list_item, parent, false);
        return new ProductViewHolder(view);
    }


    @Override
    public void onBindChildViewHolder(ProductViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Product item = ((ZoneGroup) group).getItems().get(childIndex);
        holder.mItemName.setText(item.getName());
        holder.mItemId.setText(item.getCode());
        if (item.getIsFound() == 1) {
            holder.mStatus.setImageResource(R.drawable.green_tick);
        } else if (isScanningFinish) {
            holder.mStatus.setImageResource(R.drawable.item_not_found);
        } else {
            holder.mStatus.setImageResource(R.drawable.grey_tick);
        }

        holder.mOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public boolean isGroupExpanded(int flatPos) {
        return super.isGroupExpanded(flatPos);
    }


    @Override
    public void onBindGroupViewHolder(ZoneViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.zoneName.setText(group.getTitle());
        List<Product> products = group.getItems();
        int i = 0;
        for (Product product : products) {
            if (product.getIsFound() == 1) {
                i++;
            }
        }
        if (products.size() == 0) {
            holder.arrow.setVisibility(View.GONE);
        } else {
            holder.arrow.setVisibility(View.VISIBLE);
        }
        holder.all.setText("All : " + products.size());
        holder.found.setText("Nearby : " + i);
        int miss = products.size() - i;
        holder.missing.setText("Missing : " + miss);
        if (isScanningFinish) {
            if (miss == 0) {
                holder.colorCode.setBackgroundColor(ContextCompat.getColor(context, R.color.zone_all_found));
            } else {
                holder.colorCode.setBackgroundColor(ContextCompat.getColor(context, R.color.zone_all_not_found));
            }
        } else {
            holder.colorCode.setBackgroundColor(ContextCompat.getColor(context, R.color.traquer_white));
        }
    }


    public void clearData() {
        notifyDataSetChanged();
    }

    public void onBeaconFound(BeaconData iBeacon) {

        List<ZoneGroup> groups = (List<ZoneGroup>) getGroups();

        int size = groups.size();
        if (size > 0) {
            for (ZoneGroup zoneGroup : groups) {
                List<Product> products = zoneGroup.getItems();
                if (products.size() > 0) {
                    for (int i = 0; i < products.size(); i++) {
                        Product product = products.get(i);
                        SensorData sensor = product.getSensor();
                        if (sensor != null && product.getIsFound() == 0 && sensor.getMinor() == iBeacon.getMinor()
                                && sensor.getMajor() == iBeacon.getMajor()) {
                            product.setIsFound(1);
                            Log.i("test", product.getSkuId() + "");
                        }
                    }
                    notifyDataSetChanged();
                }
            }

        }

    }

    public class ZoneViewHolder extends GroupViewHolder {

        private TextView zoneName, found, all, missing, colorCode;
        private ImageView arrow;

        public ZoneViewHolder(View itemView) {
            super(itemView);
            zoneName = (TextView) itemView.findViewById(R.id.tv_name);
            found = (TextView) itemView.findViewById(R.id.tv_found);
            all = (TextView) itemView.findViewById(R.id.tv_all);
            missing = (TextView) itemView.findViewById(R.id.tv_missing);
            colorCode = (TextView) itemView.findViewById(R.id.tv_color_code);
            missing = (TextView) itemView.findViewById(R.id.tv_missing);
            arrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            arrow.setBackgroundResource(R.drawable.arrow_open);
        }

        private void animateCollapse() {
            arrow.setBackgroundResource(R.drawable.arrow_close);
        }
    }

    public class ProductViewHolder extends ChildViewHolder {

        TextView mItemId, mItemName;
        RelativeLayout mOuter;
        ImageView mStatus;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mItemId = (TextView) itemView.findViewById(R.id.tv_item_id);
            mItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            mOuter = (RelativeLayout) itemView.findViewById(R.id.rl_item);
            mStatus = (ImageView) itemView.findViewById(R.id.iv_status);
        }
    }

    public interface InventoryListItemClickListener {
        void onItemClick(Product Product);
    }
}
