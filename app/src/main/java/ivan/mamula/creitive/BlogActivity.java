package ivan.mamula.creitive;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        int blogId = getIntent().getExtras().getInt(Constants.KEY_BLOG_ID, -1);
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

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setMenuEnabled(true);
            }
        });
        getBlog(blogId);

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
                            mWebView.loadDataWithBaseURL(Constants.HTML_BASE_URL,
                                    Constants.HTML_RESIZE_IMAGES +
                                            response.body().getContent(),
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
}
