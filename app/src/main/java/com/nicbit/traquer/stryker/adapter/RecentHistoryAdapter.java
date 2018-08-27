package com.nicbit.traquer.stryker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nicbit.traquer.stryker.R;

import java.util.List;

public class RecentHistoryAdapter extends RecyclerView.Adapter<RecentHistoryAdapter.ViewHolder> {


    List<String> recentHistoryList;
    private Context mContext;
    ItemClickListener itemClickListener;

    public RecentHistoryAdapter(Context context, List<String> recentHistoryList, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.recentHistoryList = recentHistoryList;
        this.itemClickListener = itemClickListener;
    }


    @Override
    public int getItemCount() {
        if (recentHistoryList != null)
            return recentHistoryList.size();
        return 0;
    }

    @Override
    public RecentHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_history_item_layout, parent, false);

        return (new ViewHolder(itemLayoutView));
    }

    @Override
    public void onBindViewHolder(final RecentHistoryAdapter.ViewHolder viewHolder, int position) {
        if (recentHistoryList != null) {
            viewHolder.searchTv.setText(recentHistoryList.get(position));
        }

        viewHolder.searchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClicked(viewHolder.searchTv.getText().toString());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView searchTv;


        public ViewHolder(View itemView) {
            super(itemView);
            searchTv = (TextView) itemView.findViewById(R.id.search_text_view);

        }
    }

    public interface ItemClickListener {
        void onItemClicked(String data);
    }
}

