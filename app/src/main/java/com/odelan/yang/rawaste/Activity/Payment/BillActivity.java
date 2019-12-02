package com.odelan.yang.rawaste.Activity.Payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.odelan.yang.rawaste.Activity.Product.ProductActivity;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillActivity extends AppCompatActivity {

    @BindView(R.id.txt_name) TextView txt_name;
    @BindView(R.id.txt_address) TextView txt_address;
    @BindView(R.id.txt_order_number) TextView txt_order_number;
    @BindView(R.id.txt_subtotal_amount) TextView txt_subtotal_amount;
    @BindView(R.id.txt_total_amount) TextView txt_total_amount;

    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        order = intent.getParcelableExtra(Constants.INTENT_ORDER);
        setActivity();
    }

    void setActivity() {
        txt_name.setText(AppData.user.firstname + " " + AppData.user.lastname);
        txt_address.setText(order.apartment + "\n" + order.street + "\n" + order.city);
        txt_order_number.setText(order.ID);
        txt_subtotal_amount.setText(String.valueOf(order.subtotal));
        txt_total_amount.setText(String.valueOf(order.total));
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_print) void onClickPrint() {
    }

    @OnClick(R.id.btn_visit_product) void onClickVisitProduct() {
        Product.getProductWithID(order.PID, new Interface.OnResult<Product>() {
            @Override
            public void onSuccess(Product result) {
                Intent intent = new Intent(BillActivity.this, ProductActivity.class);
                intent.putExtra(Constants.INTENT_PRODUCT, result);
                startActivity(intent);
            }
            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}

