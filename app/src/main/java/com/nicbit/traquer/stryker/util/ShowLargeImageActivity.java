package com.nicbit.traquer.stryker.util;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.nicbit.traquer.common.BaseActivity;
import com.nicbit.traquer.stryker.R;
import com.squareup.picasso.Picasso;

public class ShowLargeImageActivity extends BaseActivity {

    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_large_image);
        imageUrl = getIntent().getStringExtra(StringUtils.IntentKey.IMAGE_URL);

        Button btnDone = (Button) findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        if (imageUrl != null) {
            Picasso.with(this).load(imageUrl).error(R.drawable.update_profile).into(imageView);
        }
    }
}
