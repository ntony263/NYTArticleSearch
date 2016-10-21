package coderschool.nytarticlesearch.Activity.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import coderschool.nytarticlesearch.Activity.Model.Article;
import coderschool.nytarticlesearch.R;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Article> mArticle;
    Context mContext;


    public CustomAdapter(List<Article> articles){
        this.mArticle = articles;
    }

   

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View articleView = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mArticle.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article articles = mArticle.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.getTvArticleText().setText(articles.getTextOfArticle());
        Glide.with(mContext)
                .load(article.getImagePath())
                .into(viewHolder.getIvPicture());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.imPicture)
        ImageView ivPicture;

        @BindView(R.id.tvArticleText)
        TextView tvArticleText;

        public ImageView getIvPicture() {
            return ivPicture;
        }

        public TextView getTvArticleText() {
            return tvArticleText;
        }

        public ViewHolder (View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
