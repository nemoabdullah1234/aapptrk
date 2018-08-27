package com.nicbit.traquer.common.newInventory.location;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.stryker.Models.inventory.LocationData;
import com.nicbit.traquer.stryker.Models.inventory.Locations;
import com.nicbit.traquer.stryker.Models.inventory.SearchNearLocationsResponse;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.inventory.list.InventoryListActivity;
import com.nicbit.traquer.stryker.util.SimpleDividerItemDecoration;
import com.nicbit.traquer.stryker.view.EmptyRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCityActivity extends BaseActivity implements CityAdapter.RecycleViewItemClickListener, BaseActivity.CheckRecordAudio {


    @BindView(R.id.selectCityRecyclerView)
    EmptyRecyclerView selectCityRecyclerView;

    @BindView(R.id.cancelBtn)
    public ImageView mCancelBtn;

    @BindView(R.id.voiceBtn)
    ImageView mVoiceButton;

    @BindView(R.id.tv_empty_view)
    LinearLayout mEmptyView;

    @BindView(R.id.searchTextView)
    EditText etSearch;


    private CityAdapter cityAdapter;

    private ArrayList<Object> locationList;
    protected static final int RESULT_SPEECH = 101;
    String locationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcselect_city);
        ButterKnife.bind(this);
        String locationJson = getIntent().getStringExtra("locationJson");
        locationId = getIntent().getStringExtra("locationId");
        setLocationData(locationJson);
        setCheckRecordAudio(this);
        initViews(locationId);
        allClickListeners();
        mEmptyView.requestFocus();
    }

    @OnClick(R.id.cancelBtn)
    void onCancelPressed() {
        if (locationId.isEmpty() && InventoryListActivity.inventoryListActivity != null) {
            InventoryListActivity.inventoryListActivity.finish();
        }
        this.finish();
    }

    @OnClick(R.id.voiceBtn)
    void onVoiceClicked() {
        checkRecordAudio();
    }

    private void setLocationData(String locationJson) {
        Gson gson = new Gson();
        SearchNearLocationsResponse searchNearLocationsResponse = gson.fromJson(locationJson, SearchNearLocationsResponse.class);

        locationList = new ArrayList<>();
        Locations locations = searchNearLocationsResponse.getLocation();
        if (locations.getNear().size() == 0) {
            locationList.addAll(locations.getOther());
        } else {
            locationList.add("Near By");
            locationList.addAll(locations.getNear());
            locationList.add("Other");
            locationList.addAll(locations.getOther());
        }
    }

    private void initViews(String locationId) {
        selectCityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectCityRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        cityAdapter = new CityAdapter(this, locationList, locationList, this, locationId);
        selectCityRecyclerView.setAdapter(cityAdapter);
        selectCityRecyclerView.setEmptyView(mEmptyView);
    }


    @Override
    public void onItemClick(LocationData locationData) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Intent intent = new Intent();
        intent.putExtra("LocationData", locationData);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getParent().finish();
                onBackPressed();
                break;
            case R.id.menu_search:
                Intent intent = new Intent(this, getSearchActivity());
                startActivity(intent);
                break;
        }

        return true;
    }


    private void allClickListeners() {

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                cs = cs.toString().trim();
                SelectCityActivity.this.cityAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);

    }

    @Override
    public void onRecordAudioGranted(boolean isGranted) {
        if (isGranted)
            openVoiceRecorder();
    }

    public void openVoiceRecorder() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            startActivityForResult(intent, RESULT_SPEECH);
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(this,
                    "Ops! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String q = text.get(0);
                    if (!q.equals("")) {
                        etSearch.setText(q);
                    }
                }
                break;
            }
        }
    }
}
