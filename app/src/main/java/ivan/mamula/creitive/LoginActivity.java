package ivan.mamula.creitive;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar mProgressBar;
    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;
    private InputMethodManager inputMethodManager;

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


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_login:
                if (mEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.please_enter_email, Toast.LENGTH_SHORT).show();
                    mEmail.requestFocus();
                    inputMethodManager.showSoftInput(mEmail, InputMethodManager.SHOW_IMPLICIT);
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString().trim()).matches()) {
                    Toast.makeText(getApplicationContext(), R.string.please_enter_valid_email, Toast.LENGTH_SHORT).show();
                    mEmail.requestFocus();
                    inputMethodManager.showSoftInput(mEmail, InputMethodManager.SHOW_IMPLICIT);
                    mEmail.setSelection(mEmail.getText().toString().length());
                } else if (mPassword.getText().length() < 6) {
                    Toast.makeText(getApplicationContext(), R.string.short_password, Toast.LENGTH_SHORT).show();
                    mPassword.requestFocus();
                    inputMethodManager.showSoftInput(mPassword, InputMethodManager.SHOW_IMPLICIT);
                    mPassword.setSelection(mPassword.getText().toString().length());
                } else {
                    inputMethodManager.hideSoftInputFromWindow(mEmail.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
        }
    }
}
