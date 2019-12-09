package com.odelan.yang.rawaste.Activity.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.edit_first_name) EditText edit_first_name;
    @BindView(R.id.edit_last_name) EditText edit_last_name;
    @BindView(R.id.edit_phone_number) EditText edit_phone_number;
    @BindView(R.id.edit_duns) EditText edit_duns;
    @BindView(R.id.edit_activity_area) EditText edit_activity_area;
    @BindView(R.id.edit_waste_type) EditText edit_waste_type;
    @BindView(R.id.country_code) CountryCodePicker country_code;
    @BindView(R.id.txt_type) TextView txt_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        setActivity();
    }

    void setActivity() {
        edit_first_name.setText(AppData.user.firstname);
        edit_last_name.setText(AppData.user.lastname);
        edit_phone_number.setText(AppData.user.phone);
        country_code.setCountryForPhoneCode(Integer.parseInt(AppData.user.country_code));
        edit_duns.setText(AppData.user.duns);
        edit_activity_area.setText(AppData.user.activity_area);
        edit_waste_type.setText(AppData.user.type_waste);
        txt_type.setText(AppData.user.type);
    }

    @OnClick(R.id.txt_type) void onClickType() {
        PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), txt_type);
        dropDownMenu.getMenuInflater().inflate(R.menu.user_type, dropDownMenu.getMenu());
        dropDownMenu.setOnMenuItemClickListener(menuItem -> {
            txt_type.setText(menuItem.getTitle());
            return true;
        });
        dropDownMenu.show();
    }

    @OnClick(R.id.btn_save) void onClickSave() {
        if (edit_first_name.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_firstname));
            return;
        }
        if (edit_last_name.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_lastname));
            return;
        }
        if (edit_phone_number.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_phone_number));
            return;
        }

        AppData.user.firstname = edit_first_name.getText().toString();
        AppData.user.lastname = edit_last_name.getText().toString();
        AppData.user.type = txt_type.getText().toString();
        AppData.user.country_code = country_code.getSelectedCountryCode();
        AppData.user.phone = edit_phone_number.getText().toString();
        AppData.user.duns = edit_duns.getText().toString();
        AppData.user.activity_area = edit_activity_area.getText().toString();
        AppData.user.type_waste = edit_waste_type.getText().toString();

        User.updateUser(AppData.user);
        finish();
    }

    @OnClick(R.id.btn_back) void onClickBack() { finish(); }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
