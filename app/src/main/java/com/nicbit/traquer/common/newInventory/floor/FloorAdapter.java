package com.nicbit.traquer.common.newInventory.floor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicbit.traquer.common.newInventory.response.FloorApiResponse;
import com.nicbit.traquer.stryker.R;

import java.util.List;

/**
 * Created by rohitkumar on 7/14/17.
 */

public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.ViewHolder> implements View.OnClickListener {

    private List<FloorApiResponse.Floor> floors;
    private Context context;
    private static int VIEW_TYPE_CITY = 2;
    private FloorAdapter.RecycleViewItemClickListener listener;
    private int selectCityId = 0;

    public FloorAdapter(Context context, List<FloorApiResponse.Floor> floors, FloorAdapter.RecycleViewItemClickListener listener) {
        this.floors = floors;
        this.context = context;
        this.listener = listener;

    }

    @Override
    public FloorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView;

        itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.floor_list_item, parent, false);

        return (new FloorAdapter.ViewHolder(itemLayoutView));
    }

    @Override
    public void onBindViewHolder(FloorAdapter.ViewHolder holder, int position) {

        final FloorApiResponse.Floor floor = floors.get(position);
        holder.cityNamePcTextView.setText(floor.getName());
        holder.item.setTag(floor);
        holder.item.setOnClickListener(this);
        holder.cityNamePcTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(floor);
            }
        });


    }

    @Override
    public void onClick(View view) {
        FloorApiResponse.Floor floor = (FloorApiResponse.Floor) view.getTag();
        listener.onItemClick(floor);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item;
        TextView cityNamePcTextView, headerNamePcTextView;
        ImageButton tickButton;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.rlSelectCity);
            cityNamePcTextView = (TextView) itemView.findViewById(R.id.floorNameTextView);
            tickButton = (ImageButton) itemView.findViewById(R.id.tickButton);
        }
    }

    @Override
    public int getItemCount() {
        if (floors != null)
            return floors.size();
        return 0;
    }

    public interface RecycleViewItemClickListener {
        void onItemClick(FloorApiResponse.Floor floor);
    }
}