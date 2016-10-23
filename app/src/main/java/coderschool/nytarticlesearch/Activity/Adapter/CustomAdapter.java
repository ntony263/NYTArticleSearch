package coderschool.nytarticlesearch.Activity.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import coderschool.nytarticlesearch.Activity.Model.Article;
import coderschool.nytarticlesearch.R;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Article> mArticle;
    private final int NORMAL = 0;
    private final int NO_IMAGE = 1;
    private Listener mListener;

    public interface Listener {
        void onLoadMore();
    }

    public CustomAdapter() {
        this.mArticle = new ArrayList<>();
    }

    public void setListener(Listener Listener) {
        mListener = Listener;
    }

    public void setArticle(List<Article> articles) {
        mArticle.clear();
        mArticle.addAll(articles);
        notifyDataSetChanged();
    }

    public void addArticle(List<Article> articles) {
        int startPosition = articles.size();
        mArticle.addAll(articles);
        notifyItemRangeInserted(startPosition, articles.size());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case NO_IMAGE:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_no_image, parent, false);
                return new NoImageViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
                return new ViewHolder(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return mArticle.size();
    }

    @Override
    public int getItemViewType(int position) {
        Article articles = mArticle.get(position);
        if (articles.getMultimedia() != null && !articles.getMultimedia().isEmpty()) {
            return NORMAL;
        }
        return NO_IMAGE;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = mArticle.get(position);
        //Log.d("URL IMG=", String.valueOf(article));
        if (holder instanceof NoImageViewHolder) {
            bindNoImage(article, (NoImageViewHolder) holder);
            //Log.d("URL IMG=", "To this");
        } else {
            bindNormal(article, (ViewHolder) holder);
        }

        if (position == mArticle.size() - 1 && mListener!=null) {
            mListener.onLoadMore();
        }
    }

    private void bindNormal(Article article, ViewHolder holder) {
        holder.tvArticleText.setText(article.getTextOfArticle());
        Glide.with(holder.itemView.getContext())
                .load(article.getMultimedia().get(0).getUrl())
                .into(holder.ivPicture);
    }

    private void bindNoImage(Article article, NoImageViewHolder holder) {
        holder.tvArticleText.setText(article.getTextOfArticle());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class NoImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvArticleText)
        TextView tvArticleText;

        public NoImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

