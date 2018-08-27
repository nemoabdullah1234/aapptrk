package com.nicbit.traquer.common.comments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.common.comments.ItemCommentFragment;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.search.SearchActivity;
import com.nicbit.traquer.stryker.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemCommentActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.subTitle)
    TextView mSubTitle;
    private ItemCommentFragment mFragment;
    private String l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_issue_detail);
        ButterKnife.bind(this);
        setupActionBar();
        if (getIntent().getExtras() != null) {
            String caseNo = getIntent().getExtras().getString(StringUtils.CASE_NUMBER);
            String skuId = getIntent().getExtras().getString(StringUtils.SKU_ID);
            l2 = getIntent().getExtras().getString(StringUtils.IntentKey.L2);
            boolean isCompleted = getIntent().getExtras().getBoolean(StringUtils.IS_COMPLETED);
            addFragment(caseNo, skuId, isCompleted);
            mSubTitle.setText(l2);
        }
    }

    @OnClick(R.id.btnBack)
    public void onBackClick() {
        finish();
    }

    @OnClick(R.id.rightButton)
    public void search() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void setupActionBar() {
        mTitle.setText("Notes");
    }

    private void addFragment(String caseNo, String skuId, boolean isCompleted) {
        Bundle bundle = new Bundle();
        bundle.putString(StringUtils.CASE_NUMBER, caseNo);
        bundle.putString(StringUtils.SKU_ID, skuId);
        bundle.putString(StringUtils.IntentKey.L2, l2);
        bundle.putBoolean(StringUtils.IS_COMPLETED, isCompleted);
        mFragment = new ItemCommentFragment();
        mFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, mFragment, "");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_search:
                Intent intent = new Intent(ItemCommentActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFragment.onActivityResult(requestCode, resultCode, data);
    }
}
