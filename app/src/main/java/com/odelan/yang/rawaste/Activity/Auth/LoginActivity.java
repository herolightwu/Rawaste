package com.odelan.yang.rawaste.Activity.Auth;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.odelan.yang.rawaste.Activity.Main.MainActivity;
import com.odelan.yang.rawaste.Util.AppData;
import com.odelan.yang.rawaste.Util.Constants;
import com.odelan.yang.rawaste.Util.Interface;
import com.odelan.yang.rawaste.Model.User;
import com.odelan.yang.rawaste.R;
import com.odelan.yang.rawaste.Util.TinyDB;
import com.odelan.yang.rawaste.Util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import org.json.JSONException;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;



    @BindView(R.id.edit_email) EditText edit_email;
    @BindView(R.id.edit_password) EditText edit_password;
    @BindView(R.id.chk_remember_password) CheckBox chk_remember_password;

    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        tinyDB = new TinyDB(this);
        if (tinyDB.getBoolean(Constants.LOGIN_ISLOGGEDIN)) {
            chk_remember_password.setChecked(true);
            edit_email.setText(tinyDB.getString(Constants.LOGIN_EMAIL));
            edit_password.setText(tinyDB.getString(Constants.LOGIN_PASSWORD));
            onClickSignin();
        }

        //  facebook login
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) LoginManager.getInstance().logOut();

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }
                    @Override
                    public void onCancel() {
                        showToast("Cancel");
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        showToast(exception.toString());
                    }
                });

        // google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        final KProgressHUD hud = KProgressHUD.create(LoginActivity.this).setLabel("Please wait");
        hud.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    hud.dismiss();
                    if (task.isSuccessful()) {
                        User user = new User();
                        user.googleID = acct.getId();
                        user.email = acct.getEmail();
                        user.firstname = acct.getGivenName();
                        user.lastname = acct.getFamilyName();
                        user.photo = acct.getPhotoUrl().toString();
                        user.ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        checkGoogleUser(user);
                    }
                })
                .addOnFailureListener(e -> {
                    hud.dismiss();
                    showToast(e.getMessage());
                });
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        final KProgressHUD hud = KProgressHUD.create(LoginActivity.this).setLabel("Please wait");
        hud.show();
        GraphRequest request = GraphRequest.newMeRequest(token,
                (object, response) -> {
                    try {
                        final User user = new User();
                        user.facebookID = object.getString("id");
                        user.email = object.getString("email");
                        user.firstname = object.getString("first_name");
                        user.lastname = object.getString("last_name");
                        user.photo = "http://graph.facebook.com/" + user.facebookID + "/picture?type=large";

                        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                        FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener(LoginActivity.this, task -> {
                                    hud.dismiss();
                                    if (task.isSuccessful()) {
                                        user.ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        checkFacebookUser(user);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    hud.dismiss();
                                    showToast(e.getMessage());
                                });
                    } catch (JSONException e) {
                        hud.dismiss();
                        showToast(e.getMessage());
                        e.printStackTrace();
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    void checkFacebookUser(final User user) {
        final KProgressHUD hud = KProgressHUD.create(LoginActivity.this).setLabel("Please wait");
        hud.show();
        User.checkUserWithFacebook(user.facebookID, new Interface.OnResult<User>() {
            @Override
            public void onSuccess(User result) {
                hud.dismiss();
                if (result == null) {
                    AppData.user = user;
                    startActivity(new Intent(LoginActivity.this, SignupSocialActivity.class));
                } else {
                    tinyDB.putBoolean(Constants.LOGIN_ISLOGGEDIN, false);
                    AppData.user = result;
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(String error) {
                hud.dismiss();
                showToast(error);
            }
        });
    }

    void checkGoogleUser(final User user) {
        final KProgressHUD hud = KProgressHUD.create(LoginActivity.this).setLabel("Please wait");
        hud.show();
        User.checkUserWithGoogle(user.googleID, new Interface.OnResult<User>() {
            @Override
            public void onSuccess(User result) {
                hud.dismiss();
                if (result == null) {
                    AppData.user = user;
                    startActivity(new Intent(LoginActivity.this, SignupSocialActivity.class));
                } else {
                    tinyDB.putBoolean(Constants.LOGIN_ISLOGGEDIN, false);
                    AppData.user = result;
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(String error) {
                hud.dismiss();
                showToast(error);
            }
        });
    }

    void checkUser(final User user) {
        final KProgressHUD hud = KProgressHUD.create(LoginActivity.this).setLabel("Please wait");
        hud.show();
        User.checkUserWithID(user.ID, new Interface.OnResult<User>() {
            @Override
            public void onSuccess(User result) {
                hud.dismiss();
                if (result == null) {
                    AppData.user = user;
                    startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                } else {
                    if (chk_remember_password.isChecked()) {
                        tinyDB.putBoolean(Constants.LOGIN_ISLOGGEDIN, true);
                        tinyDB.putString(Constants.LOGIN_EMAIL, edit_email.getText().toString());
                        tinyDB.putString(Constants.LOGIN_PASSWORD, edit_password.getText().toString());
                    }
                    if (result.address.equals("")) {
                        result.address = Constants.SERVER_ADDRESS;
                        result.publicKey = Constants.SERVER_PUBLIC_KEY;
                        result.privateKey = Constants.SERVER_PRIVATE_KEY;
                    }
                    User.updateUser(result);
                    AppData.user = result;
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onFailure(String error) {
                hud.dismiss();
                showToast(error);
            }
        });
    }

    @OnClick(R.id.btn_see) void onClickSeePassword() {
        edit_password.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    @OnClick(R.id.btn_forgot_password) void onClickForgotPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint("Email");
        builder.setView(input);

        builder.setPositiveButton("Send", (dialog, which) -> FirebaseAuth.getInstance().sendPasswordResetEmail(input.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Email sent.");
                    } else {
                        showToast("Something went wrong");
                    }
                }));
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @OnClick(R.id.btn_signin) void onClickSignin() {
        if (edit_email.getText().toString().isEmpty()) {
            showToast("Please input email");
            return;
        }
        if (!Utils.isValidEmail(edit_email.getText().toString())) {
            showToast("Invalid email");
            return;
        }
        if (edit_password.getText().toString().isEmpty()) {
            showToast("Please input password");
            return;
        }
        final KProgressHUD hud = KProgressHUD.create(LoginActivity.this).setLabel("Please wait");
        hud.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                edit_email.getText().toString(),
                edit_password.getText().toString())
                .addOnCompleteListener(task -> {
                    hud.dismiss();
                    if (task.isSuccessful()) {
                        User user = new User();
                        user.ID = task.getResult().getUser().getUid();
                        user.email = edit_email.getText().toString();
                        checkUser(user);
                    }
                })
                .addOnFailureListener(e -> {
                    hud.dismiss();
                    showToast(e.getMessage());
                });
    }

//    @OnClick(R.id.btn_facebook) void onClickFacebook() {
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
//    }
//
//    @OnClick(R.id.btn_google) void onClickGoogle() {
//        Intent intent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(intent, RC_SIGN_IN);
//    }

    @OnClick(R.id.btn_signup) void onClickSignup() {
        startActivity(new Intent(this, SignupActivity.class));
    }

    void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
