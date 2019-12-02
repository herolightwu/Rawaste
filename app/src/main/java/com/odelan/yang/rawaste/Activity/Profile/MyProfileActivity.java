package com.odelan.yang.rawaste.Activity.Profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.rawaste.Activity.SubscriberSubscriptionActivity;
import com.odelan.yang.rawaste.Model.Storage;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Interface;

import net.alhazmy13.mediapicker.Image.ImagePicker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class MyProfileActivity extends AppCompatActivity {

    @BindView(R.id.img_profile) ImageView img_profile;
    @BindView(R.id.rating_review) MaterialRatingBar rating_review;
    @BindView(R.id.txt_review) TextView txt_review;
    @BindView(R.id.txt_name) TextView txt_name;
    @BindView(R.id.txt_type) TextView txt_type;
    @BindView(R.id.txt_duns) TextView txt_duns;
    @BindView(R.id.txt_activity_area) TextView txt_activity_area;
    @BindView(R.id.txt_type_waste) TextView txt_type_waste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH);

            final KProgressHUD hud =  KProgressHUD.create(this).setLabel("Please wait");
            hud.show();
            Storage.uploadFile(mPaths.get(0), new Interface.OnResult<String>() {
                @Override
                public void onSuccess(String result) {
                    hud.dismiss();
                    AppData.user.photo = result;
                    User.updateUser(AppData.user);
                    Glide.with(MyProfileActivity.this).load(AppData.user.photo).into(img_profile);
                }
                @Override
                public void onFailure(String error) {
                    hud.dismiss();
                    showToast(error);
                }
            });
        }
    }

    void setActivity() {
        if (!AppData.user.photo.isEmpty()) {
            Glide.with(this).load(AppData.user.photo).into(img_profile);
        }
        rating_review.setProgress(AppData.user.review);
        txt_review.setText(String.valueOf(AppData.user.review) + "%");
        txt_name.setText(AppData.user.firstname + " " + AppData.user.lastname);
        txt_type.setText(AppData.user.type);
        txt_duns.setText("Duns : " + AppData.user.duns);
        txt_activity_area.setText("Activity Area : " + AppData.user.activity_area);
        txt_type_waste.setText("Type of Main waste : " + AppData.user.type_waste);
    }

    @OnClick(R.id.img_profile) void onClickPhoto() {
        new ImagePicker.Builder(this)
                .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(ImagePicker.Directory.DEFAULT)
                .extension(ImagePicker.Extension.PNG)
                .allowMultipleImages(true)
                .build();
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_subscriber) void onClickSubscriber() {
        Intent intent = new Intent(this, SubscriberSubscriptionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_information) void onClickMyInformation() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_about) void onClickAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
