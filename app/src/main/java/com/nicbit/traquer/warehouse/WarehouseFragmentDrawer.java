package com.nicbit.traquer.warehouse;

import android.content.Intent;

import com.nicbit.traquer.stryker.Models.NavDrawerItem;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.editProfile.StrykerEditProfileActivity;
import com.nicbit.traquer.stryker.home.BaseFragmentDrawer;

import java.util.ArrayList;

public class WarehouseFragmentDrawer extends BaseFragmentDrawer {
    @Override
    public ArrayList<NavDrawerItem> setDrawerItems() {

        ArrayList<NavDrawerItem> items = new ArrayList<>();
        items.add(new NavDrawerItem("FAQ"
                , R.drawable.menu_help_off));
        items.add(new NavDrawerItem("About"
                , R.drawable.menu_about_off));
        items.add(new NavDrawerItem(getString(R.string.send_diagnostic_text)
                , R.drawable.send_diagnostic_icon));
        items.add(new NavDrawerItem("Logout"
                , R.drawable.menu_logout_off));

        return items;
    }

    @Override
    protected Intent getUpdateProfileIntent() {
        return new Intent(activity, StrykerEditProfileActivity.class);

    }
}
