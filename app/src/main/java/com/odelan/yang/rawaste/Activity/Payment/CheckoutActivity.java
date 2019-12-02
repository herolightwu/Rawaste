package com.odelan.yang.rawaste.Activity.Payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.odelan.yang.rawaste.Adapter.CartAdapter;
import com.odelan.yang.rawaste.Model.Cart;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckoutActivity extends AppCompatActivity implements CartAdapter.EventListener {

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    CartAdapter adapter;
    List<Product> carts = new ArrayList<>();
    List<Integer> counts = new ArrayList<>();

    @BindView(R.id.txt_subtotal_amount) TextView txt_subtotal_amount;
    @BindView(R.id.txt_shipping_amount) TextView txt_shipping_amount;
    @BindView(R.id.txt_packing_amount) TextView txt_packing_amount;
    @BindView(R.id.txt_total_amount) TextView txt_total_amount;
    @BindView(R.id.layout_payment) LinearLayout layout_payment;

    private int total, subtotal, currency, tax = 0;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);

        setActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout_payment.setVisibility(View.INVISIBLE);
    }

    void setActivity() {
        Cart.getCartWithUID(AppData.user.ID, new Interface.OnResult<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                carts = result; counts.clear();
                for (int i = 0; i < carts.size(); i++) counts.add(1);
                adapter = new CartAdapter(CheckoutActivity.this, carts, counts,CheckoutActivity.this);
                recycler_view.setLayoutManager(new LinearLayoutManager(CheckoutActivity.this));
                recycler_view.setAdapter(adapter);
            }
            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_continue) void onClickContinue() {
        Intent intent = new Intent(this, AddressActivity.class);
        intent.putExtra(Constants.INTENT_ORDER, order);
        startActivity(intent);
    }

    @Override
    public void onClickItem(int index) {
        layout_payment.setVisibility(View.VISIBLE);
        order = new Order();
        order.PID = carts.get(index).ID;
        order.SID = carts.get(index).UID;
        order.BID = AppData.user.ID;
        order.type = carts.get(index).payment_type;
        order.count = counts.get(index);

        if (carts.get(index).payment_type == 0) {
            total = 0;
            subtotal = 0;
            currency = 0;
            txt_subtotal_amount.setText("Free");
            txt_total_amount.setText("Free");
        } else if (carts.get(index).payment_type == 1) {
            subtotal = (int)carts.get(index).price * counts.get(index);
            total = subtotal;
            currency = 0;
            txt_subtotal_amount.setText(subtotal + " ETH");
            txt_total_amount.setText(total + " ETH");
        } else if (carts.get(index).payment_type == 2){
            subtotal = carts.get(index).bonus_chain * counts.get(index);
            total = subtotal;
            currency = 1;
            txt_subtotal_amount.setText(subtotal + " B");
            txt_total_amount.setText(total + " B");
        }

        order.total = total;
        order.subtotal = subtotal;
        order.tax = tax;
    }

    @Override
    public void onClickRemove(int index) {
        Cart.deleteCart(AppData.user.ID, carts.get(index).ID);
        carts.remove(index);
        counts.remove(index);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClickPlus(int index) {
        counts.set(index, counts.get(index) + 1);
        adapter.notifyItemChanged(index);
        onClickItem(index);
    }

    @Override
    public void onClickMinus(int index) {
        if (counts.get(index) > 1) {
            counts.set(index, counts.get(index) - 1);
            adapter.notifyItemChanged(index);
        }
        onClickItem(index);
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
