package com.nicbit.traquer.stryker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicbit.traquer.stryker.Models.inventory.Item;
import com.nicbit.traquer.stryker.R;

import java.util.List;

public class InventoryDetailItemAdapter extends RecyclerView.Adapter<InventoryDetailItemAdapter.ViewHolder> {


    private final Context mContext;
    private List<Item> mDataList;
    private InventoryListItemClickListener listener;
    private boolean isFromSurgerySheet;
    public boolean isEditMode;


    public InventoryDetailItemAdapter(Context context, List<Item> dataList, InventoryListItemClickListener listener, boolean isFromSurgerySheet) {
        mDataList = dataList;
        mContext = context;
        this.listener = listener;
        this.isFromSurgerySheet = isFromSurgerySheet;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;

    }


    @Override
    public InventoryDetailItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_detail_list_item, parent, false);
        return (new ViewHolder(itemLayoutView));
    }

    @Override
    public void onBindViewHolder(final InventoryDetailItemAdapter.ViewHolder holder, final int position) {
        final Item item = mDataList.get(position);
        holder.mItemName.setText(item.getName());
        holder.mItemId.setText(item.getCode());
        holder.editText.setTag(position);


        if (isEditMode) {
            holder.editText.setBackgroundResource(R.drawable.inventory_edit_text_background);
            holder.editText.setFocusableInTouchMode(true);

        } else {
            holder.editText.setBackgroundColor(Color.TRANSPARENT);
            holder.editText.setFocusable(false);
        }

        if (isFromSurgerySheet) {
            holder.mQuantity.setVisibility(View.VISIBLE);
            holder.mQuantity.setText("Qty : " + item.getQuantity());
            holder.editText.setHint("Used");
            holder.editText.setHint("Used");
            holder.editText.setText("" + item.getUsedQuantity());
        } else {
            holder.editText.setText("" + item.getQuantity());
            holder.mQuantity.setVisibility(View.GONE);
            holder.editText.setHint("Qty");
        }

        holder.mOuter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);

            }
        });
        holder.editText.addTextChangedListener(new TextWatcher() {
            // the user's changes are saved here
            public void onTextChanged(CharSequence c, int start, int before, int count) {
                try {
                    int tag = (int) holder.editText.getTag();
                    Item item = mDataList.get(tag);
                    if (!c.toString().isEmpty()) {
                        int updatedQty = Integer.parseInt(c.toString());
                        if (isFromSurgerySheet) {
                            if (item.getQuantity() >= updatedQty) {
                                item.setUpdatedQuantity(updatedQty);
                                item.setChange(true);
                            } else {
                                Toast.makeText(mContext, "used quantity should not be greater then total quantity.", Toast.LENGTH_SHORT).show();
                                holder.editText.setText("");
                            }
                        } else {
                            item.setUpdatedQuantity(updatedQty);
                            item.setChange(true);
                        }
                    } else {
                        item.setChange(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                // this space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // this one too
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<Item> getmDataList() {
        return mDataList;
    }

    public void setdata(List<Item> productList) {
        mDataList.clear();
        mDataList.addAll(productList);
        notifyDataSetChanged();
    }


    public void clearData() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mItemId, mItemName, mQuantity;
        RelativeLayout mOuter;
        EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemId = (TextView) itemView.findViewById(R.id.tv_item_id);
            mItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            editText = (EditText) itemView.findViewById(R.id.edtCount);
            mOuter = (RelativeLayout) itemView.findViewById(R.id.rl_item);
            mQuantity = (TextView) itemView.findViewById(R.id.tv_qty);
        }
    }

    public interface InventoryListItemClickListener {
        void onItemClick(Item item);
    }

    public boolean isEditable() {
        return isEditMode;
    }

}
