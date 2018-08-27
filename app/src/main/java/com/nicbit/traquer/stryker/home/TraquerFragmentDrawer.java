package com.nicbit.traquer.stryker.home;

import android.content.Intent;

import com.nicbit.traquer.stryker.Models.NavDrawerItem;
import com.nicbit.traquer.stryker.R;
import com.nicbit.traquer.stryker.editProfile.StrykerEditProfileActivity;
import com.nicbit.traquer.stryker.util.PrefUtils;

import java.util.ArrayList;

public class TraquerFragmentDrawer extends BaseFragmentDrawer {
    @Override
    public ArrayList<NavDrawerItem> setDrawerItems() {

        ArrayList<NavDrawerItem> items = new ArrayList<>();
        items.add(new NavDrawerItem("Order History"
                , R.drawable.menu_casehistory));
        items.add(new NavDrawerItem("Inventory"
                , R.drawable.menu_inventory));
        items.add(new NavDrawerItem("Notifications"
                , R.drawable.menu_notifications_off));
        items.add(new NavDrawerItem("Settings"
                , R.drawable.menu_settings_off));
        items.add(new NavDrawerItem("FAQ"
                , R.drawable.menu_help_off));
        items.add(new NavDrawerItem("About"
                , R.drawable.menu_about_off));
        items.add(new NavDrawerItem(getString(R.string.send_diagnostic_text)
                , R.drawable.send_diagnostic_icon));
        if (PrefUtils.getLogStatus()) {
            items.add(new NavDrawerItem("Disable Log"
                    , R.drawable.send_diagnostic_icon));
        } else {
            items.add(new NavDrawerItem("Enable Log"
                    , R.drawable.send_diagnostic_icon));
        }
        items.add(new NavDrawerItem("Logout"
                , R.drawable.menu_logout_off));
        items.add(new NavDrawerItem("Temp Scan"
                , R.drawable.menu_logout_off));
        items.add(new NavDrawerItem("NFC Scan"
                , R.drawable.menu_logout_off));

        return items;
    }

    @Override
    protected Intent getUpdateProfileIntent() {
        return new Intent(activity, StrykerEditProfileActivity.class);

    }
}
