package com.nicbit.traquer.stryker.inventory.frigdoor;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.stryker.Models.newModels.TempApiResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by niteshgoel on 10/26/17.
 */

public class TemperatureInfoActivity extends BaseActivity implements TemperatureInfoContract.View {

    @BindView(R.id.title)
    TextView title;


    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.temp_pager)
    ViewPager viewPager;

    private TemperatureInfoPresenter mActionsListener;
    private String skuId;
    private TempPagerAdapter adapter;
    private FragmentManager fragmentManager;
    private ApiTempModel tempData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frigdor);
        ButterKnife.bind(this);
        setupActionBar();
        mActionsListener = new TemperatureInfoPresenter(this);
        skuId = getIntent().getExtras().getString(StringUtils.IntentKey.SKU_ID);
        fragmentManager = getSupportFragmentManager();
        viewPager.setOffscreenPageLimit(2);
        tabLayout.addTab(tabLayout.newTab().setText("Overview"));
        tabLayout.addTab(tabLayout.newTab().setText("Graph"));
//        tabLayout.addTab(tabLayout.newTab().setText("Info"));
//        tabLayout.addTab(tabLayout.newTab().setText("History"));
        getTempInfo(skuId);
    }

    private void showFragment() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupActionBar() {
        title.setText("Temp Details");
    }

    @OnClick(R.id.btnBack)
    public void onBackButtonClick() {
        finish();
    }

    public ApiTempModel getTempData() {
        return tempData;
    }

    void getTempInfo(String skuId) {
        DialogClass.showDialog(this, this.getString(R.string.please_wait));
        mActionsListener.getTempInfo(skuId);
    }

    @Override
    public void onTempInfoResponse(TempApiResponse response, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            if (response.getCode() == StringUtils.SUCCESS_CODE) {

                ApiTempModel tempModel = response.getTempOneRecord();

                if (tempModel != null) {
                    this.tempData = tempModel;
                    setPager();
                    showFragment();
//                    setData(tempModel);
                }
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            getTempInfo(skuId);
                        }
                    }
                }, this);
            } else {
                ErrorMessageHandler.handleErrorMessage(response.getCode(), this);
            }
        } else {
            DialogClass.alerDialog(this, getResources().getString(R.string.check_internet_connection));
        }
    }

    public void setPager() {
        adapter = new TempPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }
}
