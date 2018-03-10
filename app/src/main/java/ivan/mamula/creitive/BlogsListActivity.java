package ivan.mamula.creitive;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import ivan.mamula.creitive.Network.Models.BlogListItem;
import ivan.mamula.creitive.Network.RetrofitClient;
import ivan.mamula.creitive.Utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogsListActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private Dialog mNetworkDialog;
    private BroadcastReceiver mBroadcastReceiver;
    private IntentFilter mIntentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blogs_list);
        mProgressBar = findViewById(R.id.pb_blogs_list_progress_bar);
        mRecyclerView = findViewById(R.id.rv_blogs_list_blogs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration
                .Builder(getApplicationContext())
                .marginResId(R.dimen.margin_72dp, R.dimen.margin_0dp)
                .build();
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        makeNoInternetDialog();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNetworkAvailable()) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    if (mRecyclerView.getAdapter() == null
                            || mRecyclerView.getAdapter().getItemCount() == 0) {
                        getBlogs();
                    }
                    hideNoInternetDialog();
                } else {
                    showNoInternetDialog();
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }
            }
        };
        getBlogs();
    }

    private void getBlogs() {
        String token = Constants.getToken(getApplicationContext());
        if (token == null) {
            return;
        }
        setMenuEnabled(false);
        RetrofitClient.getRetrofitClient(getApplicationContext()).getBlogs(token)
                .enqueue(new Callback<List<BlogListItem>>() {
                    @Override
                    public void onResponse(Call<List<BlogListItem>> call,
                                           final Response<List<BlogListItem>> response) {
                        if (response != null && response.body() != null
                                && response.body().size() > 0) {
                            final BlogsAdapter adapter = new BlogsAdapter(response.body()
                                    , new BlogsAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    int id = response.body().get(position).getId();
                                    String title = response.body().get(position).getTitle();
                                    Intent showBlogIntent = new Intent(BlogsListActivity.this,
                                            BlogActivity.class);
                                    showBlogIntent.putExtra(Constants.KEY_BLOG_ID, id);
                                    showBlogIntent.putExtra(Constants.KEY_BLOG_TITLE, title);
                                    startActivity(showBlogIntent);
                                }
                            });
                            mRecyclerView.setAdapter(adapter);
                        }
                        setMenuEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<List<BlogListItem>> call, Throwable t) {
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
        mRecyclerView.setEnabled(enabled);
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
