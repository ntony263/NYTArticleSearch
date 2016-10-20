package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import Model.Article;
import butterknife.BindView;
import butterknife.ButterKnife;
import coderschool.nytarticlesearch.R;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.Adapter> {
    private List<Article> mArticle;
    Context mContext;


    public CustomAdapter(){
        mArticle = new ArrayList<>();
    }

    public void setArticle (List<Article> article, Context context){
        this.mArticle = article;
        this.mContext = context;
    }

    public void addArticle (List<Article> article){
        this.mArticle.addAll(article);
    }

    @Override
    public RecyclerView.Adapter onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View articleView = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.Adapter holder, int position) {
        Article article = mArticle.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.getTvArticleText().setText(article.getTextOfArticle());
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
            ButterKnife(this, itemView)
        }
    }
}
