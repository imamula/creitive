package ivan.mamula.creitive;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ivan.mamula.creitive.Network.Models.BlogListItem;

/**
 * Created by Ivan Mamula on 3/9/18.
 * mamula82@gmail.com
 * Skype id: mamula82
 * +381(0)642035511
 */
public class BlogsAdapter extends RecyclerView.Adapter<BlogsAdapter.BlogViewHolder> {

    private List<BlogListItem> blogsItems;

    public BlogsAdapter(List<BlogListItem> blogsItems) {
        this.blogsItems = blogsItems;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_blog, parent, false);

        return new BlogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BlogViewHolder holder, int position) {
        BlogListItem item=getItemAtPostion(position);
        if(item.getTitle()!=null&&!item.getTitle().isEmpty()) {
            holder.title.setText(item.getTitle());
        }
        if(item.getDescription()!=null&&!item.getDescription().isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.description.setText(Html.fromHtml(item.getDescription(),Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.description.setText(Html.fromHtml(item.getDescription()));
            }
        }
        if(item.getImageUrl()!=null&&!item.getImageUrl().isEmpty())
        {
            Glide.with(holder.imageView.getContext()).load(item.getImageUrl())
                    .apply(RequestOptions.circleCropTransform()).into(holder.imageView);
        }


    }

    @Override
    public int getItemCount() {
        return blogsItems.size();
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public ImageView imageView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_item_blog_title);
            description = itemView.findViewById(R.id.tv_item_blog_description);
            imageView = itemView.findViewById(R.id.iv_item_blog);
        }
    }

    public BlogListItem getItemAtPostion(int postion)
    {
        return blogsItems.get(postion);
    }
}

