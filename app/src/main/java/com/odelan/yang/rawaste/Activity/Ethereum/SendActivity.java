package com.odelan.yang.rawaste.Activity.Ethereum;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.rawaste.Activity.BaseActivity;
import com.odelan.yang.rawaste.Model.EtherscanAPI;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;

import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendActivity extends BaseActivity {
    private BigInteger gasLimit = new BigInteger("21000");
    private BigInteger gasPrice = new BigInteger("60000000000");

    @BindView(R.id.edit_address) EditText edit_address;
    @BindView(R.id.edit_amount) EditText edit_amount;

    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        ButterKnife.bind(this);

        hud = KProgressHUD.create(SendActivity.this).setLabel("Please wait");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                edit_address.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    void sendEthereum(String address, Double amount) {
        hud.show();
        Credentials keys = Credentials.create(new ECKeyPair(new BigInteger(AppData.user.privateKey), new BigInteger(AppData.user.publicKey)));
        EtherscanAPI.getNonceForAddress(AppData.user.address, new Interface.OnResult<BigInteger>() {
            @Override
            public void onSuccess(BigInteger nonce) {
                // make raw transaction data
                RawTransaction tx = RawTransaction.createTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        address,
                        new BigDecimal(amount).multiply(Constants.ONE_ETHER).toBigInteger(),
                        ""
                );
                byte[] signed = TransactionEncoder.signMessage(tx, (byte) 1, keys);
                // send transaction
                sendTransaction(signed);
            }

            @Override
            public void onFailure(String error) {
                hud.dismiss();
                showToast(error);
            }
        });
    }

    void sendTransaction(byte[] signed) {
        EtherscanAPI.forwardTransaction("0x" + Hex.toHexString(signed), new Interface.OnResult<Boolean>() {
            @Override
            public void onSuccess(Boolean value) {
                hud.dismiss();
                showToast("Success");
            }

            @Override
            public void onFailure(String error) {
                hud.dismiss();
                showToast(error);
            }
        });
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_send) void onClickSend() {
        String toAddress = edit_address.getText().toString();
        if (toAddress.isEmpty()) {
            showToast("Please input address");
            return;
        }
        if (!toAddress.startsWith("0x")) toAddress = "0x" + toAddress;

        String amount = edit_amount.getText().toString();
        if (amount.isEmpty()) {
            showToast("Please input amount");
            return;
        }
        sendEthereum(toAddress, Double.valueOf(amount));
    }

    @OnClick(R.id.btn_qrcode) void onClickQRCode() {
        new IntentIntegrator(this).initiateScan();
    }
}
