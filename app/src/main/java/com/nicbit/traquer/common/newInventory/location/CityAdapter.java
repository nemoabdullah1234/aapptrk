package com.nicbit.traquer.common.newInventory.location;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicbit.traquer.stryker.Models.inventory.LocationData;
import com.nicbit.traquer.stryker.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> implements View.OnClickListener, Filterable {

    private List cityList;
    private Context context;
    private static int VIEW_TYPE_CITY = 2;
    private RecycleViewItemClickListener listener;
    private String selectCityId;
    private List filteredList;

    public CityAdapter(Context context, ArrayList cityList, ArrayList filteredList, RecycleViewItemClickListener listener, String locationId) {
        this.cityList = cityList;
        this.filteredList = filteredList;
        this.context = context;
        this.listener = listener;
        this.selectCityId = locationId;

    }


    @Override
    public void onClick(View view) {
        LocationData LocationData = (LocationData) view.getTag();
        listener.onItemClick(LocationData);
    }


    @Override
    public int getItemViewType(int position) {
        Object obj = filteredList.get(position);
        if (obj != null) {
            int VIEW_TYPE_HEADER = 1;
            if (obj instanceof String)
                return VIEW_TYPE_HEADER;
            else return VIEW_TYPE_CITY;
        } else
            return VIEW_TYPE_CITY;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView;
        if (viewType == VIEW_TYPE_CITY) {
            itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.select_city_item, parent, false);
        } else {
            itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_city, parent, false);
        }
        return (new ViewHolder(itemLayoutView));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Object obj = filteredList.get(position);

        if (obj != null) {
            if (obj instanceof String) {
                String header = (String) obj;
                holder.headerNamePcTextView.setText(header);
            } else {
                LocationData LocationData = (LocationData) obj;
                holder.cityNamePcTextView.setText(LocationData.getAddress());
                if (LocationData.getLocationId().equals(selectCityId)) {
                    holder.tickButton.setVisibility(View.VISIBLE);
                } else {
                    holder.tickButton.setVisibility(View.GONE);
                }
                holder.item.setTag(LocationData);
                holder.item.setOnClickListener(this);
            }

        }

    }

    @Override
    public Filter getFilter() {
        return new UserFilter(this, cityList);

    }


    private static class UserFilter extends Filter {

        private final CityAdapter adapter;

        private final List originalList;

        final List filteredList;

        private UserFilter(CityAdapter adapter, List originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                for (final Object obj : originalList) {
                    if (obj instanceof LocationData) {
                        LocationData LocationData = (LocationData) obj;
                        if (LocationData.getAddress().toLowerCase().contains(constraint.toString().toLowerCase()))
                            filteredList.add(LocationData);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.cityList.clear();
            adapter.cityList = originalList;
            adapter.filteredList.clear();
            adapter.filteredList.addAll((ArrayList) results.values);

            adapter.notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item;
        TextView cityNamePcTextView, headerNamePcTextView;
        ImageButton tickButton;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (RelativeLayout) itemView.findViewById(R.id.rlSelectCity);
            cityNamePcTextView = (TextView) itemView.findViewById(R.id.cityNameTextView);
            tickButton = (ImageButton) itemView.findViewById(R.id.tickButton);
            headerNamePcTextView = (TextView) itemView.findViewById(R.id.headerNameTextView);
        }
    }

    @Override
    public int getItemCount() {
        if (filteredList != null)
            return filteredList.size();
        return 0;
    }

    public interface RecycleViewItemClickListener {
        void onItemClick(LocationData LocationData);
    }
}