package com.nicbit.traquer.stryker.returns.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.stryker.Models.ApiResponseModel;
import com.nicbit.traquer.stryker.Models.ProductDetails;
import com.nicbit.traquer.stryker.Models.UpdateInventoryRequestModel;
import com.nicbit.traquer.stryker.Models.UpdateItemModel;
import com.nicbit.traquer.stryker.Models.inventory.Item;
import com.nicbit.traquer.stryker.Models.inventory.ReaderGetItemInventoryResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.adapter.InventoryDetailItemAdapter;
import com.nicbit.traquer.stryker.exception.ErrorMessageHandler;
import com.nicbit.traquer.stryker.exception.NicbitException;
import com.nicbit.traquer.stryker.listener.SessionTokenListener;
import com.nicbit.traquer.stryker.network.SessionToken;
import com.nicbit.traquer.stryker.search.SearchActivity;
import com.nicbit.traquer.stryker.util.DialogClass;
import com.nicbit.traquer.stryker.util.KeyboardControler;
import com.nicbit.traquer.stryker.util.ShowLargeImageActivity;
import com.nicbit.traquer.stryker.util.SimpleDividerItemDecoration;
import com.nicbit.traquer.stryker.util.StringUtils;
import com.nicbit.traquer.stryker.view.EmptyRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemQuantityActivity extends BaseActivity implements ItemQuantityContract.View, InventoryDetailItemAdapter.InventoryListItemClickListener {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.listView)
    EmptyRecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_view)
    LinearLayout mEmptyView;
    @BindView(R.id.tv_location)
    TextView tvLocation;


    @BindView(R.id.txtitemCode)
    TextView textItemCode;
    @BindView(R.id.txtSurgeryName)
    TextView txtSurgeryName;
    @BindView(R.id.txtSurgeryCategory)
    TextView txtSurgeryCategory;

    @BindView(R.id.btnWrite)
    ImageView btnWrite;
    @BindView(R.id.rightButton)
    ImageView searchButton;

    @BindView(R.id.imgProduct)
    ImageView imgProduct;

    private ItemQuantityPresenter mActionsListener;
    private int skuId;
    private InventoryDetailItemAdapter mAdapter;
    ArrayList<Item> items;
    boolean isEdit;
    ProductDetails productDetails;
    private String mCaseNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_detail_activity);
        ButterKnife.bind(this);
        setupActionBar();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mAdapter = new InventoryDetailItemAdapter(this, new ArrayList<Item>(), this, true);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        skuId = getIntent().getExtras().getInt(StringUtils.IntentKey.SKU_ID);
        isEdit = getIntent().getExtras().getBoolean(StringUtils.IntentKey.IS_EDIT);
        mCaseNumber = getIntent().getExtras().getString(StringUtils.CASE_NUMBER);
        mActionsListener = new ItemQuantityPresenter(this);
        getCaseItemInventory();
        if (isEdit)
            btnWrite.setVisibility(View.VISIBLE);
        else
            btnWrite.setVisibility(View.GONE);

        searchButton.setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.imgProduct)
    public void onClickProductImage() {
        if (productDetails != null && productDetails.getImages() != null && productDetails.getImages().size() > 0) {

            Intent intent = new Intent(ItemQuantityActivity.this, ShowLargeImageActivity.class);
            intent.putExtra(StringUtils.IntentKey.IMAGE_URL, productDetails.getImages().get(0).getFull());
            startActivity(intent);
        } else {
            Toast.makeText(this, "No Image Find", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.rightButton)
    public void search() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnBack)
    public void onBackButtonClick() {
        finish();
    }

    void getCaseItemInventory() {
        DialogClass.showDialog(this, this.getString(R.string.please_wait));
        mActionsListener.getCaseItemQuantity(skuId, mCaseNumber);
    }


    private void setupActionBar() {
        title.setText("Inventory Details");
    }

    @OnClick(R.id.btnWrite)
    public void onWriteButtonClick() {
        if (mAdapter.isEditable()) {
            KeyboardControler.hideKeyboard(this);
            mAdapter.setEditMode(false);
            btnWrite.setImageResource(R.drawable.inventory_btn_edit);
            updateToServer();
        } else {
            btnWrite.setImageResource(R.drawable.inventory_btn_edit_save);
            mAdapter.setEditMode(true);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCaseItemQuantityReceive(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                ReaderGetItemInventoryResponse itemInventoryResponse = response.getData().getReaderGetCaseItemQuantityResponse();
                if (itemInventoryResponse != null) {
                    setData(itemInventoryResponse);
                }
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            getCaseItemInventory();
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

    @Override
    public void onCaseItemQuantityUpdated(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                getCaseItemInventory();
                Toast.makeText(this, "Quantity updated successfully.", Toast.LENGTH_SHORT).show();
            } else if (response.getCode() == 209) {
                SessionToken.get(new SessionTokenListener() {
                    @Override
                    public void onSessionTokenUpdate(boolean isUpdate) {
                        if (isUpdate) {
                            updateToServer();
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

    private void setData(ReaderGetItemInventoryResponse itemInventoryResponse) {

        items = itemInventoryResponse.getItems();
        mAdapter.setdata(items);
        productDetails = itemInventoryResponse.getProductDetails();
        if (productDetails != null) {
            if (productDetails.getImages() != null && productDetails.getImages().size() > 0) {
                Picasso.with(this)
                        .load(productDetails.getImages().get(0).getThumb())
                        .placeholder(R.drawable.no_item)
                        .error(R.drawable.no_item)
                        .into(imgProduct);

            }

            txtSurgeryName.setText(productDetails.getName());
            txtSurgeryCategory.setText(productDetails.getCategory());
            tvLocation.setText(productDetails.getCurrentLocation());
            textItemCode.setText(productDetails.getCode());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }


    @Override
    public void onItemClick(Item item) {

    }

    public void updateToServer() {

        List<UpdateItemModel> itemList = new ArrayList<>();
        for (Item item : items) {
            if (item.isChange()) {
                UpdateItemModel updateItemModel = new UpdateItemModel();
                updateItemModel.setProductId(item.getProductId());
                updateItemModel.setUsedQuantity(item.getUpdatedQuantity());
                updateItemModel.setSkuId(item.getSkuId());
                updateItemModel.setCaseNo(mCaseNumber);
                itemList.add(updateItemModel);

            }

        }

        if (itemList.size() > 0) {
            UpdateInventoryRequestModel updateInventoryRequestModel = new UpdateInventoryRequestModel();
            updateInventoryRequestModel.setItems(itemList);
            DialogClass.showDialog(this, this.getString(R.string.please_wait));
            mActionsListener.updateCaseItemQuantity(updateInventoryRequestModel);
        } else
            Toast.makeText(this, "No item found.", Toast.LENGTH_SHORT).show();

    }


}
