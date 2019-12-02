package com.odelan.yang.rawaste.Activity.Auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.rawaste.Activity.Main.MainActivity;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.OwnWalletUtils;
import com.odelan.yang.rawaste.Util.Utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.Security;


public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.edit_email) EditText edit_email;
    @BindView(R.id.edit_first_name) EditText edit_first_name;
    @BindView(R.id.edit_last_name) EditText edit_last_name;
    @BindView(R.id.edit_password) EditText edit_password;
    @BindView(R.id.edit_password_confirm) EditText edit_password_confirm;
    @BindView(R.id.edit_phone_number) EditText edit_phone_number;
    @BindView(R.id.edit_duns) EditText edit_duns;
    @BindView(R.id.edit_activity_area) EditText edit_activity_area;
    @BindView(R.id.edit_waste_type) EditText edit_waste_type;
    @BindView(R.id.country_code) CountryCodePicker country_code;
    @BindView(R.id.txt_type) TextView txt_type;
    @BindView(R.id.chk_accept) CheckBox chk_accept;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        setActivity();
        setupBouncyCastle();
    }

    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            return;
        }
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    void setActivity() {
        txt_type.setText(Constants.USER_TYPE_INDIVIDUAL);
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

    @OnClick(R.id.btn_terms_and_conditions) void onClickTermsAndConditions() {
    }

    @OnClick(R.id.btn_register) void onClickRegister() {
        if (edit_email.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_email));
            return;
        }
        if (!Utils.isValidEmail(edit_email.getText().toString())) {
            showToast(getString(R.string.invalid_email));
            return;
        }
        if (edit_first_name.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_firstname));
            return;
        }
        if (edit_last_name.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_lastname));
            return;
        }
        if (edit_password.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_password));
            return;
        }
        if (!edit_password.getText().toString().equals(edit_password_confirm.getText().toString())) {
            showToast(getString(R.string.incorrect_confirm_password));
            return;
        }
        if (edit_phone_number.getText().toString().isEmpty()) {
            showToast(getString(R.string.input_phone_number));
            return;
        }
        if (!chk_accept.isChecked()) {
            showToast(getString(R.string.accept_terms_conditions));
            return;
        }

        final User user = new User();
        user.email = edit_email.getText().toString();
        user.firstname = edit_first_name.getText().toString();
        user.lastname = edit_last_name.getText().toString();
        user.type = txt_type.getText().toString();
        user.country_code = country_code.getSelectedCountryCode();
        user.phone = edit_phone_number.getText().toString();
        user.duns = edit_duns.getText().toString();
        user.activity_area = edit_activity_area.getText().toString();
        user.type_waste = edit_waste_type.getText().toString();

        final KProgressHUD hud = KProgressHUD.create(SignupActivity.this).setLabel("Please wait");
        hud.show();

        ECKeyPair ecKeyPair;
        try {
            ecKeyPair = Keys.createEcKeyPair();
            user.publicKey = ecKeyPair.getPublicKey().toString();
            user.privateKey = ecKeyPair.getPrivateKey().toString();
            Credentials cs = Credentials.create(ecKeyPair);
            user.address = cs.getAddress();

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    edit_email.getText().toString(),
                    edit_password.getText().toString())
                    .addOnCompleteListener(task -> {
                        hud.dismiss();
                        if (task.isSuccessful() && !task.getResult().getUser().getUid().isEmpty()) {
                            user.ID = task.getResult().getUser().getUid();
                            AppData.user = user;
                            User.createUser(AppData.user);
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        } else {

                        }
                    })
                    .addOnFailureListener(e -> {
                        hud.dismiss();
                        showToast(e.getMessage());
                    });
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException e) {
            hud.dismiss();
            e.printStackTrace();
            showToast(e.getLocalizedMessage());
        }
    }

    @OnClick(R.id.btn_back) void onClickBack() { finish(); }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
