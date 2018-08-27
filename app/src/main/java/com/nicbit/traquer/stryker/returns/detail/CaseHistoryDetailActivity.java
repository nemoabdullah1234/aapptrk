package com.nicbit.traquer.stryker.returns.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaseHistoryDetailActivity extends BaseActivity {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.subTitle)
    TextView subTitle;

    String caseNumber;
    @BindView(R.id.btnBack)
    public ImageView btnBack;


    private CaseHistoryDetailFragment mCaseHistoryDetailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_history_detail);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            caseNumber = getIntent().getExtras().getString(StringUtils.CASE_NUMBER);
            addFragment(caseNumber);
        }
        setActionToolbar();
        createReceiver();
    }

    public void setActionToolbar() {
        subTitle.setText(caseNumber);
        title.setVisibility(View.GONE);
    }


    @OnClick(R.id.btnBack)
    public void onBackClick() {
        finish();
    }

    private void addFragment(String caseNumber) {
        mCaseHistoryDetailFragment = new CaseHistoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(StringUtils.CASE_NUMBER, caseNumber);
        mCaseHistoryDetailFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, mCaseHistoryDetailFragment, "");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.menu_search:
                Intent intent = new Intent(this, getSearchActivity());
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onBluetoothChange(boolean isOn) {
        mCaseHistoryDetailFragment.onBluetoothChange(isOn);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
