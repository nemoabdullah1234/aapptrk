package com.nicbit.traquer.stryker.inventory.details;

import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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
import com.nicbit.traquer.stryker.inventory.frigdoor.TemperatureInfoActivity;
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

public class InventoryDetailActivity extends BaseActivity implements InventoryDetailContract.View, InventoryDetailItemAdapter.InventoryListItemClickListener {

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

    @BindView(R.id.iv_beacon)
    ImageView beaconButton;

    @BindView(R.id.iv_temp)
    ImageView tempButton;

    @BindView(R.id.iv_nfc)
    ImageView nfcButton;

    @BindView(R.id.imgProduct)
    ImageView imgProduct;

    private InventoryDetailPresenter mActionsListener;
    private String skuId;
    private InventoryDetailItemAdapter mAdapter;
    ArrayList<Item> items;
    boolean isEdit;
    ProductDetails productDetails;
    private boolean isClickable;
    private String source;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_detail_activity);
        ButterKnife.bind(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        mAdapter = new InventoryDetailItemAdapter(this, new ArrayList<Item>(), this, false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);
        skuId = getIntent().getExtras().getString(StringUtils.IntentKey.SKU_ID);
        uid = getIntent().getExtras().getString(StringUtils.IntentKey.UID);
        isEdit = getIntent().getExtras().getBoolean(StringUtils.IntentKey.IS_EDIT);
        source = getIntent().getExtras().getString(StringUtils.INTENT_SOURCE);
        setupActionBar();
        mActionsListener = new InventoryDetailPresenter(this);
        if (TextUtils.isEmpty(skuId)) {
            getItemInventoryByUid(uid);
        } else {
            getItemInventory(skuId);
        }
        if (isEdit)
            btnWrite.setVisibility(View.VISIBLE);
        else
            btnWrite.setVisibility(View.GONE);

        searchButton.setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.imgProduct)
    public void onClickProductImage() {
        if (productDetails != null && productDetails.getImages() != null && productDetails.getImages().size() > 0) {

            Intent intent = new Intent(this, ShowLargeImageActivity.class);
            intent.putExtra(StringUtils.IntentKey.IMAGE_URL, productDetails.getImages().get(0).getFull());
            startActivity(intent);
        } else {
            Toast.makeText(this, "No Image Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void opemTemp() {
        Intent intent = new Intent(this, TemperatureInfoActivity.class);
        intent.putExtra(StringUtils.IntentKey.SKU_ID, skuId);
        startActivity(intent);
    }

    @OnClick(R.id.rightButton)
    public void search() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_temp)
    public void onTempTagClicked() {
        opemTemp();
    }

    void getItemInventory(String skuId) {
        DialogClass.showDialog(this, this.getString(R.string.please_wait));
        mActionsListener.getItemInventory(skuId);
    }

    void getItemInventoryByUid(String uid) {
        DialogClass.showDialog(this, this.getString(R.string.please_wait));
        mActionsListener.getItemInventoryByUid(uid);
    }

    @OnClick(R.id.btnBack)
    public void onBackButtonClick() {
        finish();
    }

    private void setupActionBar() {

        if (source.equals("Shipment")) {
            title.setText(getIntent().getExtras().getString(StringUtils.CASE_NUMBER));
            tvLocation.setText(getIntent().getExtras().getString(StringUtils.SHIPPING_TEXT));
        } else if (source.equals("Inventory")) {
            title.setText("Inventory Details");
        }
    }

    @OnClick(R.id.btnWrite)
    public void onWriteButtonClick() {
        if (isClickable) {
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
    }

    @Override
    public void onItemInventoryResponse(ApiResponseModel response, NicbitException e) {
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
                            getItemInventory(skuId);
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
    public void onItemUpdated(ApiResponseModel response, NicbitException e) {
        DialogClass.dismissDialog(this);
        if (e == null) {
            if (response.getStatus() == StringUtils.SUCCESS_STATUS) {
                getItemInventory(skuId);
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
            if (source.equals("Inventory")) {
                tvLocation.setText(productDetails.getCurrentLocation());
            }

            textItemCode.setText(productDetails.getCode());
            txtSurgeryCategory.setText(productDetails.getCategory());
            if (productDetails.getHaveChild() == 0) {
                setLocked(btnWrite);
                isClickable = false;
            } else {
                isClickable = true;
                setUnlocked(btnWrite);
            }

            List<ProductDetails.AttachedThing> things = productDetails.getThings();

            if (things != null && things.size() > 0) {

                for (ProductDetails.AttachedThing thing : things) {
                    if (thing.getType().equalsIgnoreCase("beacon")) {
                        beaconButton.setVisibility(View.VISIBLE);
                    }
                    if (thing.getType().equalsIgnoreCase("nfcTag")) {
                        nfcButton.setVisibility(View.VISIBLE);
                    }
                    if (thing.getType().equalsIgnoreCase("tempTag")) {
                        tempButton.setVisibility(View.VISIBLE);
                    }

                }

            }

        }
    }


    public static void setLocked(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
    }

    public static void setUnlocked(ImageView v) {
        v.setColorFilter(null);
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
                updateItemModel.setQuantity(item.getUpdatedQuantity());
                updateItemModel.setSkuId(item.getSkuId());
                itemList.add(updateItemModel);

            }

        }

        if (itemList.size() > 0) {
            UpdateInventoryRequestModel updateInventoryRequestModel = new UpdateInventoryRequestModel();
            updateInventoryRequestModel.setItems(itemList);
            DialogClass.showDialog(this, this.getString(R.string.please_wait));
            mActionsListener.updateItemInventory(updateInventoryRequestModel);
        } else
            Toast.makeText(this, "Nothing to update", Toast.LENGTH_SHORT).show();

    }


}
