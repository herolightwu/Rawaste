package com.odelan.yang.rawaste.Activity.Ethereum;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.odelan.yang.rawaste.Activity.BaseActivity;
import com.odelan.yang.rawaste.Model.EtherscanAPI;
import com.odelan.yang.rawaste.Model.Price;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Interface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletActivity extends BaseActivity {

    @BindView(R.id.layout_ethereum) LinearLayout layout_ethereum;
    @BindView(R.id.txt_balance) TextView txt_balance;
    @BindView(R.id.txt_usd) TextView txt_usd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        ButterKnife.bind(this);
        setActivity();
    }

    void setActivity() {
        layout_ethereum.setVisibility(View.GONE);
        AppData.user.balance = 0;
        layout_ethereum.setVisibility(View.VISIBLE);
        EtherscanAPI.getTokenBalances(AppData.user.address, new Interface.OnResult<Double>() {
            @Override
            public void onSuccess(Double result) {

                AppData.user.balance = result;
                txt_balance.setText(String.format("%.4f", result));
                EtherscanAPI.getEtherPrice(new Interface.OnResult<Price>() {
                    @Override
                    public void onSuccess(Price price) {
                        Double eth_usd = Double.valueOf(price.ethusd);
                        txt_usd.setText(String.format("%.2f USD", eth_usd * AppData.user.balance));
                    }
                    @Override
                    public void onFailure(String error) {
                        showToast(error);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }

    @OnClick(R.id.btn_send) void onClickSend() {
        Intent intent = new Intent(this, SendActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_receive) void onClickReceive() {
        Intent intent = new Intent(this, ReceiveActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
}
