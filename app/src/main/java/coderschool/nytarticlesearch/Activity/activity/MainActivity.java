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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;
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
    private TextView tvDatePicker;
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
                if (searchResult !=null) {
                    mCustomAdapter.setArticle(searchResult.getArticles());
                    rvArticleList.scrollToPosition(0);
                    //Log.d("Main activity Debug", String.valueOf(searchResult.getArticles().get(0).getTextOfArticle()));
                }
            }
        });
    }


    private void searchMore (){
        mSearchRequest.nextPage();
        pbLoadMore.setVisibility(View.VISIBLE);
        fetchArticles(new Listener() {
            @Override
            public void onResult(SearchResult searchResult) {
                if (searchResult!=null) {
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
                DialogFragment mDialogFragment = new ShowSearchDialogFragment();
                FragmentManager fm = getSupportFragmentManager();
                mDialogFragment.show(fm, "Search");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class ShowSearchDialogFragment extends DialogFragment {
        private View mView;
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mView = inflater.inflate(R.layout.item_advance_search, container, false);
            TextView tvDataPicker;
            CheckBox cbArt;
            CheckBox cbFS;
            CheckBox cbSport;
            ToggleButton tbSort;

            tvDataPicker = (TextView) mView.findViewById(R.id.tvDataPicker);
            cbArt = (CheckBox) mView.findViewById(R.id.cbArt);
            cbFS = (CheckBox) mView.findViewById(R.id.cbFS);
            cbSport = (CheckBox) mView.findViewById(R.id.cbSport);
            tbSort = (ToggleButton) mView.findViewById(R.id.tbSort);

            return mView;
        }
    }

    class SelectDateFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, mYear, mMonth,
                    mDay);//it will return dialog setting date with mYear,MMonth and MDay

        }
        @Override
        public void onDateSet(android.widget.DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {
            System.out.println("year=" + year + "day=" + dayOfMonth + "month="
                    + monthOfYear);
            String yearString = Integer.toString(year);
            String monthString = Integer.toString(monthOfYear+1);
            String dayString = Integer.toString(dayOfMonth);
            String newText = dayString+"/"+monthString+"/"+ yearString;
            tvDatePicker = (TextView)findViewById(R.id.tvDataPicker);
            tvDatePicker.setText(newText);
        }
    }


}
