package com.nicbit.traquer.stryker.forgotPassword;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nicbit.traquer.stryker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class DefaultResetPasswordActivity extends BaseResetPasswordActivity {

    @BindView(R.id.appNameTxt)
    public ImageView mAppName;
    @BindView(R.id.edtNewPassword)
    public EditText mNewPAssword;
    @BindView(R.id.edtConfirmPassword)
    public EditText medtConfirmPassword;
    @BindView(R.id.btn_save_password)
    public TextView mSavePassword;
    String token, email;

    public abstract void setAppName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        setAppName();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            token = bundle.getString("PasswordToken");
            email = bundle.getString("Email");

        }

    }


    @OnClick(R.id.btn_save_password)
    public void resetPassword() {
        if (!mNewPAssword.getText().toString().equals("") && !medtConfirmPassword.getText().toString().equals("")) {
            if (mNewPAssword.getText().toString().equals(medtConfirmPassword.getText().toString())) {
                resetPassword(email, token, mNewPAssword.getText().toString());
            } else {
                mNewPAssword.setText("");
                medtConfirmPassword.setText("");
                Toast.makeText(this, getResources().getString(R.string.password_didnt_match), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.enter_inputs), Toast.LENGTH_LONG).show();
        }
    }
}
