package com.nicbit.traquer.stryker.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nicbit.traquer.stryker.Models.ReaderSearchCasesResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.caseDetail.CaseDetailActivity;
import com.nicbit.traquer.stryker.enums.CaseStatus;
import com.nicbit.traquer.stryker.inventory.details.InventoryDetailActivity;
import com.nicbit.traquer.stryker.search.SearchType;
import com.nicbit.traquer.stryker.util.StringUtils;

import java.util.List;

public class NewSearchAdapter extends RecyclerView.Adapter<NewSearchAdapter.ViewHolder> {


    private List<ReaderSearchCasesResponse> mSearchList;
    private Context mContext;


    public NewSearchAdapter(Context context, List<ReaderSearchCasesResponse> mSearchListData) {
        this.mContext = context;
        this.mSearchList = mSearchListData;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (viewType == SearchType.CASE.getType()) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.case_item_new_layout, viewGroup, false);

            return new NewSearchAdapter.FirstViewHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_search_layout, viewGroup, false);
            return new NewSearchAdapter.SecondViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(NewSearchAdapter.ViewHolder holder, int position) {
        final ReaderSearchCasesResponse data = mSearchList.get(position);

        if (data.getType() == SearchType.CASE.getType()) {
            FirstViewHolder viewHolder = (FirstViewHolder) holder;
            viewHolder.nameTextView.setText(data.getH1());
            viewHolder.nameTextView.setSelected(true);
            viewHolder.dateTextView.setText(data.getH2());
            viewHolder.titleTextView.setText(data.getL1() + " | " + data.getH3());
            viewHolder.addressTextView.setText(data.getL3());
            viewHolder.addressTextView.setSelected(true);
            viewHolder.statusTextView.setText(data.getL4());
            viewHolder.caseIdTextView.setText(data.getCaseId());
            if (data.getIsReported() == 0) {
                viewHolder.isReportedImageView.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.isReportedImageView.setVisibility(View.VISIBLE);
            }
            viewHolder.detailLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, CaseDetailActivity.class);
                    i.putExtra(StringUtils.CASE_NUMBER, data.getParams().getCaseId());
                    i.putExtra(StringUtils.ID, data.getParams().getId());
                    i.putExtra(StringUtils.IS_DASHBOARD, true);
                    mContext.startActivity(i);
                }
            });
            CaseStatus.setCaseImage(data.getCaseStatus(), viewHolder.statusImageView);
        } else {
            final SecondViewHolder viewHolder = (SecondViewHolder) holder;
            viewHolder.txtTitle.setText(data.getH1());
            viewHolder.txtSubTitle.setText(data.getH2());
            viewHolder.txtAddress.setText(data.getL3().isEmpty() ? "Not available" : data.getL3());
            viewHolder.outerLayout.setTag(data.getParams().getSkuId());
            if (data.getIsCaseAssociated() == 1) {
                viewHolder.caseAssociated.setVisibility(View.GONE);
                viewHolder.caseAssociatedDetail.setVisibility(View.VISIBLE);
                viewHolder.txtName.setText(data.getL1());
                viewHolder.txtPhone.setText(data.getL2());
                viewHolder.txtName.setSelected(true);
            } else {
                viewHolder.caseAssociated.setVisibility(View.GONE);
                viewHolder.caseAssociatedDetail.setVisibility(View.GONE);
            }

            viewHolder.outerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, InventoryDetailActivity.class);
                    String skuId = (String) v.getTag();
                    intent.putExtra(StringUtils.IntentKey.SKU_ID, skuId);
                    intent.putExtra(StringUtils.IntentKey.IS_EDIT, false);
                    intent.putExtra(StringUtils.INTENT_SOURCE, "Inventory");
                    mContext.startActivity(intent);
                }
            });

            viewHolder.txtPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + viewHolder.txtPhone.getText().toString()));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mSearchList != null)
            return mSearchList.size();
        return 0;
    }


    @Override
    public int getItemViewType(int position) {
        return (mSearchList.get(position).getType());

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class FirstViewHolder extends ViewHolder {

        TextView dateTextView, titleTextView,
                addressTextView, caseIdTextView, statusTextView, nameTextView;
        ImageView statusImageView, isReportedImageView;
        RelativeLayout detailLayout;

        public FirstViewHolder(View v) {
            super(v);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTxt);
            statusTextView = (TextView) itemView.findViewById(R.id.txtStatus);
            statusImageView = (ImageView) itemView.findViewById(R.id.imgStatus);
            isReportedImageView = (ImageView) itemView.findViewById(R.id.isReportedImageView);
            titleTextView = (TextView) itemView.findViewById(R.id.txtTitle);
            addressTextView = (TextView) itemView.findViewById(R.id.txtAddress);
            nameTextView = (TextView) itemView.findViewById(R.id.txtName);
            caseIdTextView = (TextView) itemView.findViewById(R.id.tv_case_id);
            detailLayout = (RelativeLayout) itemView.findViewById(R.id.outerLayout);

        }
    }

    public class SecondViewHolder extends ViewHolder {
        TextView txtTitle, txtSubTitle, txtAddress, txtName, txtPhone;
        ImageView caseAssociated;
        RelativeLayout caseAssociatedDetail, outerLayout;

        public SecondViewHolder(View v) {
            super(v);
            txtTitle = (TextView) itemView.findViewById(R.id.tv_title);
            txtSubTitle = (TextView) itemView.findViewById(R.id.tv_sub_title);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            txtName = (TextView) itemView.findViewById(R.id.tv_name);
            txtPhone = (TextView) itemView.findViewById(R.id.tv_phone);
            caseAssociated = (ImageView) itemView.findViewById(R.id.caseAssociated);
            caseAssociatedDetail = (RelativeLayout) itemView.findViewById(R.id.caseAssociatedDetail);
            outerLayout = (RelativeLayout) itemView.findViewById(R.id.rl_outer);

        }
    }
}
