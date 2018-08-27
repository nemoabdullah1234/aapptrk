package com.nicbit.traquer.stryker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicbit.traquer.common.EventsLog;
import com.nicbit.traquer.stryker.Models.ReaderGetCasesResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.enums.CaseStatus;

import java.util.ArrayList;
import java.util.List;

public class SwipeRecyclerViewAdapter extends RecyclerView.Adapter<SwipeRecyclerViewAdapter.CaseViewHolder> {


    CaseAdapterViewClickListener caseAdapterViewClickListener;
    private ArrayList<ReaderGetCasesResponse> caseList;
    boolean isHome = false;

    public SwipeRecyclerViewAdapter(CaseAdapterViewClickListener caseAdapterViewClickListener, ArrayList<ReaderGetCasesResponse> objects, boolean isHome) {
        this.caseAdapterViewClickListener = caseAdapterViewClickListener;
        this.caseList = objects;
        this.isHome = isHome;
    }


    @Override
    public CaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.case_item_new_layout, parent, false);
        return new CaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CaseViewHolder viewHolder, final int position) {
        final ReaderGetCasesResponse item = caseList.get(position);

        viewHolder.nameTextView.setText(item.getH1());
        viewHolder.nameTextView.setSelected(true);
        viewHolder.dateTextView.setText(item.getH2());
        viewHolder.titleTextView.setText(item.getL1() + " | " + item.getH3());
        viewHolder.addressTextView.setText(item.getL3());
        viewHolder.addressTextView.setSelected(true);
        viewHolder.statusTextView.setText(item.getL4());
        viewHolder.caseIdTextView.setText(item.getCaseId());

        CaseStatus.setCaseImage(item.getCaseStatus(), viewHolder.statusImageView);

        if (item.getIsReported() == 0) {
            viewHolder.isReportedImageView.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.isReportedImageView.setVisibility(View.VISIBLE);
        }

        if (isHome) {
            viewHolder.isCompletedImageView.setVisibility(View.GONE);
            viewHolder.isFavouritedImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.isFavouritedImageView.setVisibility(View.GONE);
            if (item.getIsCompleted() == 0)
                viewHolder.isCompletedImageView.setVisibility(View.GONE);
            else
                viewHolder.isCompletedImageView.setVisibility(View.GONE);

        }

        viewHolder.isCompletedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caseAdapterViewClickListener.onReportCaseClicked(item);
            }
        });

        viewHolder.isFavouritedImageView.setTag("unfavorited");

        viewHolder.isFavouritedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.isFavouritedImageView.getTag().equals("unfavorited")) {
                    viewHolder.isFavouritedImageView.setBackgroundResource(R.drawable.icon_favorites_on);
                    viewHolder.isFavouritedImageView.setTag("favorited");
                    EventsLog.customEvent("REP_HOME", "FAVORITE", "NO");
                } else {
                    viewHolder.isFavouritedImageView.setBackgroundResource(R.drawable.icon_favorites_off);
                    viewHolder.isFavouritedImageView.setTag("unfavorited");
                    EventsLog.customEvent("REP_HOME", "FAVORITE", "YES");
                }
                caseAdapterViewClickListener.onWatchCaseClicked(item);
            }
        });


        if (item.getIsWatched() == 0) {
            viewHolder.isFavouritedImageView.setBackgroundResource(R.drawable.icon_favorites_off);
        } else {
            viewHolder.isFavouritedImageView.setBackgroundResource(R.drawable.icon_favorites_on);
        }

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caseAdapterViewClickListener.onItemClick(item);
            }
        });
    }


    @Override
    public int getItemCount() {
        return caseList.size();
    }


    public void addAll(boolean isClear, List<ReaderGetCasesResponse> list) {
        if (isClear) {
            caseList.clear();
        }
        caseList.addAll(list);
        notifyDataSetChanged();
    }

    public interface CaseAdapterViewClickListener {
        void onReportCaseClicked(ReaderGetCasesResponse readerCase);

        void onItemClick(ReaderGetCasesResponse readerCase);

        void onWatchCaseClicked(ReaderGetCasesResponse readerCase);
    }

    public static class CaseViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView, titleTextView,
                addressTextView, caseIdTextView;
        ImageView statusImageView, isReportedImageView, isFavouritedImageView, isCompletedImageView;
        RelativeLayout linearLayout;
        TextView statusTextView, nameTextView;

        public CaseViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.txtTitle);
            addressTextView = (TextView) itemView.findViewById(R.id.txtAddress);
            nameTextView = (TextView) itemView.findViewById(R.id.txtName);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTxt);
            statusTextView = (TextView) itemView.findViewById(R.id.txtStatus);
            statusImageView = (ImageView) itemView.findViewById(R.id.imgStatus);
            isReportedImageView = (ImageView) itemView.findViewById(R.id.isReportedImageView);
            linearLayout = (RelativeLayout) itemView.findViewById(R.id.outerLayout);
            caseIdTextView = (TextView) itemView.findViewById(R.id.tv_case_id);
            isFavouritedImageView = (ImageView) itemView.findViewById(R.id.isFavouritedImageView);
            isCompletedImageView = (ImageView) itemView.findViewById(R.id.surgeryReportImageView);
        }
    }
}
