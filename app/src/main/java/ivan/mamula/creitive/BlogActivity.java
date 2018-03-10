package ivan.mamula.creitive;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import ivan.mamula.creitive.Network.Models.BlogModel;
import ivan.mamula.creitive.Network.RetrofitClient;
import ivan.mamula.creitive.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogActivity extends AppCompatActivity {


    private ProgressBar mProgressBar;
    private WebView mWebView;
    private Dialog mNetworkDialog;
    private BroadcastReceiver mBroadcastReceiver;
    private IntentFilter mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    private int mBlogId;
    private boolean mIsLoadingFinished = false;
    private String mBlogData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        mBlogId = getIntent().getExtras().getInt(Constants.KEY_BLOG_ID, -1);
        String title = getIntent().getExtras().getString(Constants.KEY_BLOG_TITLE, null);
        if (title != null) {
            getSupportActionBar().setTitle(title);
        }
        mProgressBar = findViewById(R.id.pb_blog_progress_bar);
        mWebView = findViewById(R.id.wv_blog_blog);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.toString().equalsIgnoreCase(Constants.HTML_BASE_URL +
                        Constants.HTML_URL_BLOG)) {
                    onBackPressed();
                } else {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(myIntent);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                setMenuEnabled(false);
                mIsLoadingFinished = false;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setMenuEnabled(true);
                mIsLoadingFinished = true;
            }
        });
        makeNoInternetDialog();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNetworkAvailable()) {
                    mWebView.setVisibility(View.VISIBLE);
                    if (!mIsLoadingFinished) {
                        getBlog(mBlogId);
                    }
                    hideNoInternetDialog();
                } else {
                    showNoInternetDialog();
                    mWebView.setVisibility(View.INVISIBLE);
                }
            }
        };

        if (savedInstanceState != null) {
            mBlogData = savedInstanceState.getString(Constants.KEY_BLOG_CONTENT, null);
        }
        if (mBlogData == null) {
            getBlog(mBlogId);
        } else {
            mWebView.loadDataWithBaseURL(Constants.HTML_BASE_URL,
                    mBlogData,
                    Constants.HTML_MIME_TYPE,
                    Constants.HTML_ENCODING,
                    null);
        }

    }

    private void setMenuEnabled(boolean enabled) {
        if (enabled) {
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        mWebView.setEnabled(enabled);
    }

    private void getBlog(final int blogId) {
        String token = Constants.getToken(getApplicationContext());
        if (token == null) {
            return;
        }
        setMenuEnabled(false);
        RetrofitClient.getRetrofitClient(getApplicationContext())
                .getBlog(token, String.valueOf(blogId))
                .enqueue(new Callback<BlogModel>() {
                    @Override
                    public void onResponse(Call<BlogModel> call, Response<BlogModel> response) {
                        if (response != null && response.code() == 200 && response.body() != null
                                && response.body().getContent() != null
                                && !response.body().getContent().isEmpty()) {
                            mBlogData = Constants.HTML_RESIZE_IMAGES +
                                    response.body().getContent();
                            mWebView.loadDataWithBaseURL(Constants.HTML_BASE_URL,
                                    mBlogData,
                                    Constants.HTML_MIME_TYPE,
                                    Constants.HTML_ENCODING,
                                    null);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.something_went_wrong,
                                    Toast.LENGTH_SHORT).show();
                        }
                        setMenuEnabled(true);

                    }

                    @Override
                    public void onFailure(Call<BlogModel> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.something_went_wrong,
                                Toast.LENGTH_SHORT).show();
                        setMenuEnabled(true);
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBlogData != null) {
            outState.putString(Constants.KEY_BLOG_CONTENT, mBlogData);
        }
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
