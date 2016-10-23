package coderschool.nytarticlesearch.Activity.activity;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import coderschool.nytarticlesearch.Activity.API.ArticleApi;
import coderschool.nytarticlesearch.Activity.Adapter.CustomAdapter;
import coderschool.nytarticlesearch.Activity.Model.Article;
import coderschool.nytarticlesearch.Activity.Model.SearchRequest;
import coderschool.nytarticlesearch.Activity.Model.SearchResult;
import coderschool.nytarticlesearch.Activity.Utils.RetrofitUtils;
import coderschool.nytarticlesearch.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SearchRequest mSearchRequest;
    private CustomAdapter mCustomAdapter;
    private ArticleApi mArticleApi;
    private StaggeredGridLayoutManager mLayoutManager;
    private List<Article> articles;
    private SearchView mSearchView;
    //private MenuItem miActionProgressItem, miSearch;

    @BindView(R.id.rvArticleList)
    RecyclerView rvArticleList;

    @BindView(R.id.pbLoading)
    View pbLoading;

    @BindView(R.id.pbLoadMore)
    ProgressBar pbLoadMore;


    private interface Listener {
        void onResult(SearchResult searchResult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpApi();
        setUpViews();
        search();
    }

    private void setUpApi() {
        mSearchRequest = new SearchRequest();
        mArticleApi = RetrofitUtils.get().create(ArticleApi.class);
    }

    private void search (){
        mSearchRequest.resetPage();
        pbLoading.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                mCustomAdapter.setArticle(searchResult.getArticles());
                rvArticleList.scrollToPosition(0);
            }
        });
    }


    private void searchMore (){
        mSearchRequest.nextPage();
        pbLoadMore.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                mCustomAdapter.addArticle(searchResult.getArticles());
            }
        });
    }



    private void fetchArticles(final Listener listener) {
        mArticleApi.search(mSearchRequest.toQueryMap()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                listener.onResult(response.body());
                handleComplete();
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                handleComplete();
            }
        });
    }

    private void handleComplete() {
        pbLoading.setVisibility(View.GONE);
        pbLoadMore.setVisibility(View.GONE);

    }

    private void setUpViews() {
        mCustomAdapter = new CustomAdapter();
        mCustomAdapter.setListener(new CustomAdapter.Listener() {
            @Override
            public void onLoadMore() {
                searchMore();
            }
        });
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rvArticleList.setLayoutManager(mLayoutManager);
        rvArticleList.setAdapter(mCustomAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        setUpSearchView(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpSearchView(MenuItem menuItem) {
        mSearchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //setUpApi();
                mSearchRequest.setQuery(query);
                search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sort:
                Toast.makeText(this, "Sort", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
