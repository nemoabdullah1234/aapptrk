package com.nicbit.traquer.stryker.caseDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.common.comments.ItemCommentActivity;
import com.nicbit.traquer.common.issueDetail.IssueDetailActivity;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.CaseDetails;
import com.nicbit.traquer.stryker.Models.Item;
import com.nicbit.traquer.stryker.Models.ReaderGetCaseDetailsResponse;
import com.nicbit.traquer.stryker.Models.Shipment;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.adapter.CasePagerAdapter;
import com.nicbit.traquer.stryker.adapter.MapPagerAdapter;
import com.nicbit.traquer.stryker.adapter.ShipmentAdapter;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.login.StrykerLoginActivity;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.search.SearchActivity;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.PrefUtils;
import com.nicbit.traquer.stryker.util.StringUtils;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaseDetailActivity extends BaseActivity implements ShipmentAdapter.ShipmentAdapterViewClickListener, SwipeRefreshLayout.OnRefreshListener, CaseDetailContract.View {


    @BindView(R.id.btnBack)
    public ImageView btnBack;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.subTitle)
    TextView subTitle;


    @BindView(R.id.listPager)
    ViewPager listPager;

    @BindView(R.id.mapPager)
    ViewPager mapPager;


    @BindView(R.id.txtShipmentStatus)
    TextView txtShipmentStatus;

    @BindView(R.id.txtShipmentLocation)
    TextView txtShipmentLocation;

    @BindView(R.id.btnReport)
    Button btnReport;

    @BindView(R.id.btnMap)
    Button btnMap;
    @BindView(R.id.rltPager)
    RelativeLayout rltPager;

    @BindView(R.id.padeIndicatorView)
    CirclePageIndicator pageIndicator;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.rightButton)
    public ImageView search;

    private String mCaseNumber;
    private boolean isDashBoard;
    private Integer isAssigned = 0;

    private CaseDetailContract.UserActionsListener mActionsListener;
    private ShipmentAdapter mAdapter;
    private List<Shipment> mListHeaderData;
    private HashMap<Shipment, List<Item>> mListChildData;
    private ReaderGetCaseDetailsResponse mReaderGetCaseDetailsResponse;
    CasePagerAdapter listPagerAdapter;
    MapPagerAdapter mapPagerAdapter;
    List<Shipment> shipments;
    private Shipment currentShipment;
    String mapUrl;
    int currentPosition;
    boolean isCompleted;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_detail);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            mCaseNumber = getIntent().getExtras().getString(StringUtils.CASE_NUMBER);
            id = getIntent().getExtras().getString(StringUtils.ID);

            isDashBoard = getIntent().getExtras().getBoolean(StringUtils.IS_DASHBOARD);
            isCompleted = getIntent().getExtras().getBoolean(StringUtils.IS_COMPLETED);
        }
        mActionsListener = new CaseDetailPresenter(this);
        title.setText(mCaseNumber);

        getCaseDetail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.rightButton)
    public void search() {
        Intent intent = new Intent(CaseDetailActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    private void initPager(final List<Shipment> shipments) {

        listPagerAdapter = new CasePagerAdapter(getSupportFragmentManager(), shipments, mReaderGetCaseDetailsResponse.getCaseDetails(), mCaseNumber, isDashBoard, id);
        mapPagerAdapter = new MapPagerAdapter(getSupportFragmentManager(), shipments, mReaderGetCaseDetailsResponse.getCaseDetails());
        listPager.setAdapter(listPagerAdapter);
        listPager.setVisibility(View.VISIBLE);
        mapPager.setVisibility(View.GONE);
        pageIndicator.setViewPager(listPager);
        pageIndicator.setPageColor(0x88cecbc6);
        pageIndicator.setFillColor(0xFF424449);
        pageIndicator.setStrokeColor(0xFFcecbc6);
        currentShipment = shipments.get(0);

        listPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                setHeadrData(position);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mapPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                setHeadrData(position);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @OnClick(R.id.btnBack)
    public void onBackClick() {
        finish();
    }

    @OnClick(R.id.btnReport)
    public void onReportClick() {
        if (isAssigned == 1) {
            Intent i = new Intent(this, IssueDetailActivity.class);
            i.putExtra(StringUtils.IS_COMPLETED, isCompleted);
            i.putExtra(StringUtils.CASE_NUMBER, mCaseNumber);
            i.putExtra(StringUtils.ISSUE_ID, "" + currentShipment.getIssueId());
            i.putExtra(StringUtils.CASE_ID, "" + id);
            i.putExtra(StringUtils.SHIPMENT_ID, "" + currentShipment.getShipmentId());
            i.putExtra(StringUtils.SHIPPING_NUMBER, currentShipment.getShipmentNo());
            i.putExtra(StringUtils.SHIPPING_TEXT, currentShipment.getL1());
            startActivity(i);
        } else {
            Toast.makeText(this, "Order is not assigned to you.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onShipmentClicked(int position) {
        if (isAssigned == 1) {
            Intent i = new Intent(this, IssueDetailActivity.class);
            Shipment shipment = mListHeaderData.get(position);
            i.putExtra(StringUtils.CASE_NUMBER, mCaseNumber);
            i.putExtra(StringUtils.ISSUE_ID, "" + shipment.getIssueId());
            i.putExtra(StringUtils.SHIPPING_NUMBER, shipment.getShipmentNo());
            i.putExtra(StringUtils.CASE_ID, "" + id);
            i.putExtra(StringUtils.SHIPMENT_ID, "" + currentShipment.getShipmentId());
            i.putExtra(StringUtils.SHIPPING_TEXT, currentShipment.getL1());
            startActivity(i);
        } else {
            Toast.makeText(this, "Order is not assigned to you.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onShipmentItemClicked(String skuId) {
        if (isAssigned == 1) {
            Intent i = new Intent(this, ItemCommentActivity.class);
            i.putExtra(StringUtils.CASE_NUMBER, mCaseNumber);
            i.putExtra(StringUtils.SKU_ID, skuId);
            startActivity(i);
        } else {
            Toast.makeText(this, "Order is not assigned to you.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetCaseDetailDone(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                mReaderGetCaseDetailsResponse = response.getData().getReaderGetCaseDetailsResponse();

                if (mReaderGetCaseDetailsResponse != null) {
                    CaseDetails mCaseDetails = mReaderGetCaseDetailsResponse.getCaseDetails();
                    isAssigned = mCaseDetails.getIsAssigned();
                    title.setText(mCaseDetails.getCaseId());
                    shipments = mReaderGetCaseDetailsResponse.getShipments();
                    if (shipments != null && shipments.size() > 0)
                        initPager(shipments);
                }
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {

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

    private void getCaseDetail() {
        DialogClass.showDialog(this, getString(R.string.please_wait));
        mActionsListener.getCaseDetail(PrefUtils.getSessionToken(), id);
    }

    @OnClick(R.id.btnMap)
    public void loadMap() {
        if (listPager.getVisibility() == View.VISIBLE) {
            btnMap.setText("List");
            btnMap.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.list_icon), null, null);
            mapPager.setVisibility(View.VISIBLE);
            listPager.setVisibility(View.GONE);
            mapPager.setAdapter(mapPagerAdapter);
            pageIndicator.setViewPager(mapPager);
            mapPager.setCurrentItem(currentPosition);


        } else {

            btnMap.setText("Map");
            btnMap.setCompoundDrawablesWithIntrinsicBounds(null, ContextCompat.getDrawable(this, R.drawable.map_marker_icon), null, null);
            mapPager.setVisibility(View.GONE);
            listPager.setVisibility(View.VISIBLE);
            listPager.setAdapter(listPagerAdapter);
            pageIndicator.setViewPager(mapPager);
            listPager.setCurrentItem(currentPosition);


        }

    }


    public void setHeadrData(int position) {


        currentShipment = shipments.get(position);
        mapUrl = currentShipment.getMap().getUrl();
        subTitle.setText(currentShipment.getL1());

        //  txtShipmentStatus.setText(ItemStatus.getStatusName(currentShipment.getShipStatus()));
        txtShipmentStatus.setText(currentShipment.getL2());
        txtShipmentLocation.setText(currentShipment.getL3());
        txtShipmentLocation.setSelected(true);
        currentPosition = position;
        btnMap.setAlpha(0.5f);
        btnReport.setAlpha(0.5f);
        switch (currentShipment.getShipStatus()) {
            case 0:
                btnMap.setAlpha(0.5f);
                btnReport.setAlpha(0.5f);
                btnMap.setClickable(false);
                btnReport.setClickable(false);
                break;
            case 10:
            case 20:
            case 30:
            case 40:
            case 45:
            case 50:
            case 25:
            case 60:
                btnMap.setAlpha(1);
                btnReport.setAlpha(1);
                btnMap.setClickable(true);
                btnReport.setClickable(true);
                break;
            default:
                btnMap.setAlpha(1);
                btnReport.setAlpha(1);
                btnMap.setClickable(true);
                btnReport.setClickable(true);
                break;

        }
      /*  if (currentShipment.getShipStatus() == 0) {
            btnMap.setAlpha(0.5f);
            btnReport.setAlpha(0.5f);
            btnMap.setClickable(false);
            btnReport.setClickable(false);
        } else if (currentShipment.getShipStatus() == 10) {
            btnMap.setAlpha(1);
            btnReport.setAlpha(1);
            btnMap.setClickable(true);
            btnReport.setClickable(true);
        } else if (currentShipment.getShipStatus() == 40) {
            btnMap.setAlpha(1);
            btnReport.setAlpha(1);
            btnMap.setClickable(true);
            btnReport.setClickable(true);
        } else if (currentShipment.getShipStatus() == 60) {
            btnMap.setAlpha(1);
            btnReport.setAlpha(1);
            btnMap.setClickable(true);
            btnReport.setClickable(true);
        }
        else if (currentShipment.getShipStatus() == 20) {
            btnMap.setAlpha(1);
            btnReport.setAlpha(1);
            btnMap.setClickable(true);
            btnReport.setClickable(true);
        }*/
    }

    @Override
    public Intent getLoginIntent() {
        return new Intent(this, StrykerLoginActivity.class);
    }

}
