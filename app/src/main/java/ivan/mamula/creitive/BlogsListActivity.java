package ivan.mamula.creitive;

import android.os.Bundle;
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
                                           Response<List<BlogListItem>> response) {
                        if (response != null && response.body() != null
                                && response.body().size() > 0) {
                            BlogsAdapter adapter = new BlogsAdapter(response.body());
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
}
