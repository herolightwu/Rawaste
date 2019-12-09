package com.odelan.yang.rawaste.Activity.Payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.rawaste.Activity.Auth.LoginActivity;
import com.odelan.yang.rawaste.Activity.Ethereum.ReceiveActivity;
import com.odelan.yang.rawaste.Activity.Ethereum.SendActivity;
import com.odelan.yang.rawaste.Model.Bonus;
import com.odelan.yang.rawaste.Model.Cart;
import com.odelan.yang.rawaste.Model.EtherscanAPI;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.Model.Price;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.Model.User;
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

public class PaymentActivity extends AppCompatActivity {

    private BigInteger gasLimit = new BigInteger("21000");
    private BigInteger gasPrice = new BigInteger("60000000000");

    @BindView(R.id.img_ethereum) ImageView img_ethereum;
    @BindView(R.id.img_bonuschain) ImageView img_bonuschain;
    @BindView(R.id.layout_ethereum) LinearLayout layout_ethereum;
    @BindView(R.id.txt_balance) TextView txt_balance;
    @BindView(R.id.txt_usd) TextView txt_usd;

    Order order;
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        hud = KProgressHUD.create(PaymentActivity.this).setLabel("Please wait");

        Intent intent = getIntent();
        order = intent.getParcelableExtra(Constants.INTENT_ORDER);

        removeAllCheck();
        setActivity();
    }

    void setActivity() {
        layout_ethereum.setVisibility(View.GONE);
        AppData.user.balance = 0;
        if (order.type == 0) {
        } else if (order.type == 1) {
            layout_ethereum.setVisibility(View.VISIBLE);
            setCheck(img_ethereum, true);
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

        } else if (order.type == 2) {
            setCheck(img_bonuschain, true);
        }
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

    @OnClick(R.id.btn_pay) void onClickPay() {
        if (AppData.user.balance < order.total) {
            showToast("Insufficient Balance");
            return;
        }

        hud.show();
        User.checkUserWithID(order.SID, new Interface.OnResult<User>() {
            @Override
            public void onSuccess(User result) {
                sendEthereum(result.address);
            }
            @Override
            public void onFailure(String error) {
                hud.dismiss();
                showToast(error);
            }
        });
    }

    void sendEthereum(String address) {
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
                        new BigDecimal(order.total).multiply(Constants.ONE_ETHER).toBigInteger(),
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

                /** Remove product from cart */
                Cart.deleteCart(AppData.user.ID, order.PID);

                /** Create new order */
                order.timestamp = System.currentTimeMillis() / 1000;
                order.createID();
                Order.createOrder(order);
                Intent intent = new Intent(PaymentActivity.this, BillActivity.class);
                intent.putExtra(Constants.INTENT_ORDER, order);
                startActivity(intent);
                finish();

                /** Add bonuschain to buyer */
                Bonus bonus = new Bonus();
                bonus.UID = AppData.user.ID;
                bonus.timestamp = order.timestamp;
                bonus.OID = order.ID;
                bonus.amount = 50;
                Bonus.createBonus(bonus);
                AppData.user.bonuschain += 50;
                User.updateUser(AppData.user);
            }

            @Override
            public void onFailure(String error) {
                hud.dismiss();
                showToast(error);
            }
        });
    }

    void removeAllCheck() {
        setCheck(img_ethereum, false);
        setCheck(img_bonuschain, false);
    }

    void setCheck(ImageView view, Boolean status) {
        if (status == true) {
            view.setImageResource(R.mipmap.check_checked);
        } else {
            view.setImageResource(R.mipmap.check_empty);
        }
    }
    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
