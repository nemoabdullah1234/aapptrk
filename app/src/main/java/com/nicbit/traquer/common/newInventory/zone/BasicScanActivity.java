package com.nicbit.traquer.common.newInventory.zone;

import android.bluetooth.BluetoothAdapter;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.stryker.R;

import butterknife.BindView;

/**
 * Created by rohitkumar on 7/14/17.
 */

public abstract class BasicScanActivity extends BaseActivity {

    @BindView(R.id.iv_sync)
    ImageView ivScan;
    @BindView(R.id.fl_bg)
    FrameLayout scanBg;

    public void stopAnimation() {
        ivScan.clearAnimation();
    }

    public void startAnimation() {
        ivScan.startAnimation(AnimationUtils.loadAnimation(this, R.anim.plus));
    }

    public void changeIconState(boolean turnedOn) {
        if (turnedOn) {
            scanBg.setBackgroundResource(R.drawable.blue_circle);
            ivScan.setBackgroundResource(R.drawable.connection);
        } else {
            scanBg.setBackgroundResource(R.drawable.red_circle);
            ivScan.setBackgroundResource(R.drawable.no_connection);
        }
    }

    public void checkBluetoothOnOff() {
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            onBluetoothChange(false);
            // Device does not support Bluetooth
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                onBluetoothChange(true);
            } else {
                onBluetoothChange(false);
            }

        }
    }

    public abstract void onBluetoothChange(boolean b);
}
