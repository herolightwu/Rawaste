package com.odelan.yang.rawaste.Activity.Order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.odelan.yang.rawaste.Model.Order;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderContactSellerActivity extends AppCompatActivity {

    @BindView(R.id.img_product) ImageView img_product;
    @BindView(R.id.txt_product_name) TextView txt_product_name;
    @BindView(R.id.txt_product_description) TextView txt_product_description;
    @BindView(R.id.txt_rate) TextView txt_rate;
    @BindView(R.id.txt_order_date) TextView txt_order_date;
    @BindView(R.id.txt_order_number) TextView txt_order_number;

    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_contact_seller);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        order = intent.getParcelableExtra(Constants.INTENT_ORDER);

        setActivity();
    }

    void setActivity() {
        Product.getProductWithID(order.PID, new Interface.OnResult<Product>() {
            @Override
            public void onSuccess(Product result) {
                Glide.with(OrderContactSellerActivity.this).load(result.images.get(0)).into(img_product);
                txt_product_name.setText(result.name);
                txt_product_description.setText(result.description);
                txt_rate.setText("95%");
            }
            @Override
            public void onFailure(String error) {
            }
        });
        Date myDate = new Date(order.timestamp * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        txt_order_date.setText(dateFormat.format(myDate));
        txt_order_number.setText(order.ID);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }
}
