package com.nicbit.traquer.stryker.inventory.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.common.newInventory.floor.FloorListFragment;
import com.nicbit.traquer.stryker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryListActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FloorListFragment mInventoryFragment;
    public static InventoryListActivity inventoryListActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_list_activity);
        ButterKnife.bind(this);
        inventoryListActivity = this;
        setupActionBar();
        addFragment();
        createReceiver();
    }

    @Override
    public void onBluetoothChange(boolean isOn) {
//        mInventoryFragment.onBluetoothChange(isOn);
    }


    private void addFragment() {
        mInventoryFragment = new FloorListFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, mInventoryFragment, "inventory");
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        getSupportActionBar().setTitle("");
        title.setText("Inventory");
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
                Intent intent = new Intent(this, getSearchActivity());
                startActivity(intent);
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }


}
