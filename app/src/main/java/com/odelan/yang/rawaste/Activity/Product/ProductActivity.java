package com.odelan.yang.rawaste.Activity.Product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.odelan.yang.rawaste.Activity.Chat.ChatActivity;
import com.odelan.yang.rawaste.Activity.ContactSellerActivity;
import com.odelan.yang.rawaste.Activity.Payment.CheckoutActivity;
import com.odelan.yang.rawaste.Activity.Profile.OtherProfileActivity;
import com.odelan.yang.rawaste.Adapter.ProductDetailsAdapter;
import com.odelan.yang.rawaste.Model.Cart;
import com.odelan.yang.rawaste.Model.Product;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductActivity extends AppCompatActivity implements ProductDetailsAdapter.EventListener {

    @BindView(R.id.img_product) ImageView img_product;
    @BindView(R.id.txt_name) TextView txt_name;
    @BindView(R.id.txt_bonus_count) TextView txt_bonus_count;
    @BindView(R.id.txt_description) ReadMoreTextView txt_description;

    @BindView(R.id.btn_free) Button btn_free;
    @BindView(R.id.btn_paid) Button btn_paid;
    @BindView(R.id.btn_bonuschain) RelativeLayout btn_bonuschain;

    @BindView(R.id.img_photo) ImageView img_photo;
    @BindView(R.id.img_online) ImageView img_online;
    @BindView(R.id.txt_username) TextView txt_username;
    @BindView(R.id.txt_positive_feedback) TextView txt_positive_feedback;
    @BindView(R.id.txt_items) TextView txt_items;
    @BindView(R.id.txt_evaluation) TextView txt_evaluation;

    @BindView(R.id.recycler_view) RecyclerView recycler_view;
    ProductDetailsAdapter adapter;
    List<String> urls = new ArrayList<>();

    private Product product;
    private User seller = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        product = intent.getParcelableExtra(Constants.INTENT_PRODUCT);
        setActivity();
    }

    void setActivity() {
        urls = product.images;
        Glide.with(this).load(urls.get(0)).into(img_product);
        txt_name.setText(product.name);
        txt_description.setText(product.description);
        btn_free.setVisibility(View.INVISIBLE);
        btn_paid.setVisibility(View.INVISIBLE);
        btn_bonuschain.setVisibility(View.INVISIBLE);
        if (product.payment_type == 0) {
            btn_free.setVisibility(View.VISIBLE);
        } else if (product.payment_type == 1) {
            btn_paid.setVisibility(View.VISIBLE);
            btn_paid.setText(String.valueOf((int)product.price));
        } else if (product.payment_type == 2) {
            btn_bonuschain.setVisibility(View.VISIBLE);
            txt_bonus_count.setText(String.valueOf(product.bonus_chain));
        }

        User.checkUserWithID(product.UID, new Interface.OnResult<User>() {
            @Override
            public void onSuccess(User user) {
                seller = user;
                if (!user.photo.isEmpty()) {
                    Glide.with(ProductActivity.this).load(user.photo).into(img_photo);
                }
                txt_username.setText(user.firstname + " " + user.lastname);
                txt_positive_feedback.setText(user.review + "%");
                txt_items.setText(String.valueOf(user.product_count));
                txt_evaluation.setText(String.valueOf(user.evaluation));
            }
            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });

        adapter = new ProductDetailsAdapter(this, urls, this);
        recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recycler_view.setAdapter(adapter);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_cart) void onClickCart() {
        Cart.checkCart(AppData.user.ID, product.ID, new Interface.OnResult<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result == true) {
                    showToast(getString(R.string.cart_already_added));
                } else {
                    Cart.addCart(AppData.user.ID, product.ID);
                    showToast(getString(R.string.cart_success));
                }
            }
            @Override
            public void onFailure(String error) {
                showToast(error);
            }
        });
    }

    @OnClick(R.id.btn_view_profile) void onClickViewProfile() {
        if (seller != null) {
            Intent intent = new Intent(this, OtherProfileActivity.class);
            intent.putExtra(Constants.INTENT_USER, seller);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btn_report_seller) void onClickReportSeller() {
    }

    @OnClick(R.id.btn_order) void onClickOrder() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_contact_seller) void onClickContactSeller() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constants.INTENT_USER, seller);
        startActivity(intent);
    }

    @Override
    public void onClickItem(int index) {
        Glide.with(this).load(urls.get(index)).into(img_product);
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
