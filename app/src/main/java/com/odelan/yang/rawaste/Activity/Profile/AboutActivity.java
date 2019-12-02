package com.odelan.yang.rawaste.Activity.Profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.odelan.yang.rawaste.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
}
