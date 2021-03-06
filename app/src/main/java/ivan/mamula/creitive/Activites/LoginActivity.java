package ivan.mamula.creitive.Activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import ivan.mamula.creitive.Utils.Models.LoginRequestBody;
import ivan.mamula.creitive.Utils.Models.LoginResponseModel;
import ivan.mamula.creitive.Utils.Network.RetrofitClient;
import ivan.mamula.creitive.R;
import ivan.mamula.creitive.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar mProgressBar;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private InputMethodManager inputMethodManager;
    private Dialog mNetworkDialog;
    private BroadcastReceiver mBroadcastReceiver;
    private IntentFilter mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mProgressBar = findViewById(R.id.pb_login_progress_bar);
        mEmail = findViewById(R.id.et_login_email);
        mPassword = findViewById(R.id.et_login_password);
        mLogin = findViewById(R.id.bt_login_login);
        mLogin.setOnClickListener(this);
        makeNoInternetDialog();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNetworkAvailable()) {
                    hideNoInternetDialog();
                } else {
                    showNoInternetDialog();
                }
            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_login:
                if (mEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.please_enter_email,
                            Toast.LENGTH_SHORT).show();
                    mEmail.requestFocus();
                    inputMethodManager.showSoftInput(mEmail, InputMethodManager.SHOW_IMPLICIT);
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()
                        .trim()).matches()) {
                    Toast.makeText(getApplicationContext(), R.string.please_enter_valid_email,
                            Toast.LENGTH_SHORT).show();
                    mEmail.requestFocus();
                    inputMethodManager.showSoftInput(mEmail, InputMethodManager.SHOW_IMPLICIT);
                    mEmail.setSelection(mEmail.getText().toString().length());
                } else if (mPassword.getText().length() < Constants.PASSWORD_MIN_CHARS) {

                    String message = getString(R.string.password_should_be_at_least) + " " +
                            Constants.PASSWORD_MIN_CHARS + " " + getString(R.string.chars_long);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    mPassword.requestFocus();
                    inputMethodManager.showSoftInput(mPassword, InputMethodManager.SHOW_IMPLICIT);
                    mPassword.setSelection(mPassword.getText().toString().length());
                } else {
                    inputMethodManager.hideSoftInputFromWindow(mEmail.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    login(mEmail.getText().toString().trim(), mPassword.getText().toString());
                }
        }
    }

    private void login(String email, String password) {
        setMenuEnabled(false);
        LoginRequestBody loginRequestBody = new LoginRequestBody(email, password);
        RetrofitClient.getRetrofitClient(getApplicationContext())
                .login(loginRequestBody, Constants.LOGIN_CONTENT_TYPE_HEADER_VALUE)
                .enqueue(new Callback<LoginResponseModel>() {
                    @Override
                    public void onResponse(Call<LoginResponseModel> call
                            , Response<LoginResponseModel> response) {
                        if (response != null && response.body() != null
                                && response.body().getToken() != null
                                && !response.body().getToken().isEmpty()) {
                            Constants.saveToken(response.body().getToken()
                                    , getApplicationContext());
                            Intent showBlogsActivityIntent = new Intent(LoginActivity.this,
                                    BlogsListActivity.class);
                            startActivity(showBlogsActivityIntent);
                            finish();
                        } else if (response != null && response.code() == 401) {
                            Toast.makeText(getApplicationContext(), R.string.wrong_credentials,
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), R.string.something_went_wrong,
                                    Toast.LENGTH_SHORT).show();
                        }
                        setMenuEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong,
                                Toast.LENGTH_SHORT).show();
                        setMenuEnabled(true);
                    }
                });
    }

    private void setMenuEnabled(boolean enabled) {
        if (enabled) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mEmail.setEnabled(enabled);
        mPassword.setEnabled(enabled);
        mLogin.setEnabled(enabled);
    }

    private void makeNoInternetDialog() {
        AlertDialog.Builder aDBNet = new AlertDialog.Builder(this);
        aDBNet.setTitle(R.string.no_internet_title);
        aDBNet.setMessage(R.string.no_internet_message);
        aDBNet.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        aDBNet.setCancelable(false);
        mNetworkDialog = aDBNet.create();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = ((ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void showNoInternetDialog() {
        if (mNetworkDialog != null && !mNetworkDialog.isShowing()) {
            mNetworkDialog.show();
        }
    }

    public void hideNoInternetDialog() {
        if (mNetworkDialog != null && mNetworkDialog.isShowing()) {
            mNetworkDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mBroadcastReceiver, mIntentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBroadcastReceiver);
    }
}
