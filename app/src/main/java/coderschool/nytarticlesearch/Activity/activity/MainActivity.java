package coderschool.nytarticlesearch.Activity.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import coderschool.nytarticlesearch.Activity.API.ArticleApi;
import coderschool.nytarticlesearch.Activity.Adapter.CustomAdapter;
import coderschool.nytarticlesearch.Activity.Model.Article;
import coderschool.nytarticlesearch.Activity.Model.SearchRequest;
import coderschool.nytarticlesearch.Activity.Model.SearchResult;
import coderschool.nytarticlesearch.Activity.Utils.RetrofitUtils;
import coderschool.nytarticlesearch.Activity.Utils.ShowSearchDialogFragment;
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
    private String datePickerResult;
    private boolean setIsArt;
    private boolean setIsFS;
    private boolean setIsSport;
    private String setIsNewest;


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

    private void search() {
        mSearchRequest.resetPage();
        pbLoading.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                if (searchResult != null) {
                    mCustomAdapter.setArticle(searchResult.getArticles());
                    rvArticleList.scrollToPosition(0);
                }
            }
        });
    }


    private void searchMore() {
        mSearchRequest.nextPage();
        pbLoadMore.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                if (searchResult != null) {
                    mCustomAdapter.addArticle(searchResult.getArticles());
                }
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
                mSearchRequest.setQuery(query);
                submitSetting();
                search();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void submitSetting() {
        if (datePickerResult != null) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date startDate;
            try {
                startDate = df.parse(datePickerResult);
                df = new SimpleDateFormat("yyyyMMdd");
                String newDateString = df.format(startDate);
                Log.d("New date", newDateString);
                mSearchRequest.setBeginDate(newDateString);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mSearchRequest.setHasArts(setIsArt);
        mSearchRequest.setHasFashionStyle(setIsFS);
        mSearchRequest.setHasSports(setIsSport);
        mSearchRequest.setOrder(setIsNewest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                ShowSearchDialogFragment mDialogFragment = new ShowSearchDialogFragment();
                FragmentManager fm = getSupportFragmentManager();
                mDialogFragment.show(fm, "Search");
                mDialogFragment.resultDialogListener(new ShowSearchDialogFragment.EditDateDialogListener() {
                    @Override
                    public void onFinishEditDialog (String pickDate,
                                                    boolean isArt,
                                                    boolean isFS,
                                                    boolean isSport,
                                                    String isNewest) {
                        datePickerResult = pickDate;
                        setIsArt = isArt;
                        setIsFS = isFS;
                        setIsSport=isSport;
                        setIsNewest= isNewest;
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
