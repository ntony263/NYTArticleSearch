package coderschool.nytarticlesearch.Activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import coderschool.nytarticlesearch.Activity.API.ArticleApi;
import coderschool.nytarticlesearch.Activity.Adapter.CustomAdapter;
import coderschool.nytarticlesearch.Activity.Model.SearchRequest;
import coderschool.nytarticlesearch.Activity.Utils.RetrofitUtils;
import coderschool.nytarticlesearch.R;

public class MainActivity extends AppCompatActivity {

    private SearchRequest mSearchRequest;
    private CustomAdapter mCustomAdapter;
    private ArticleApi mArticleApi;
    private StaggeredGridLayoutManager mLayoutManager;
    //private MenuItem miActionProgressItem, miSearch;

    @BindView(R.id.rvArticleList)
    RecyclerView rvArticleList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        ButterKnife.bind(this);
        setUpApi();
        setUpViews();
    }

    private void setUpApi() {
        mSearchRequest = new SearchRequest();
        mArticleApi = RetrofitUtils.get().create(ArticleApi.class);
    }

    private void setUpViews() {
        setSupportActionBar(toolbar);
        mCustomAdapter = new CustomAdapter();
        mCustomAdapter.setListener(new CustomAdapter().Listener() {
            @Override
            public void onItemClick(Ar book) {
                Toast.makeText(BookListActivity.this, book.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lvBooks.setAdapter(new SlideInBottomAnimationAdapter(mCustomAdapter));
        lvBooks.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        lvBooks.setLayoutManager(mLayoutManager);
        lvBooks.addOnScrollListener(new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mSearchRequest.setPage(page + 1);
                fetchMoreBooks();
            }
        });
    }

    private void fetchMoreBooks() {
        miActionProgressItem.setVisible(true);
        miSearch.setVisible(false);
        mArticleApi.search(mSearchRequest.toQueryMay()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.body() != null) {
                    mCustomAdapter.addBooks(response.body().getBooks());
                }
                handleComplete();
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("Error", t.getMessage());
                handleComplete();
            }
        });
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks() {
        miActionProgressItem.setVisible(true);
        miSearch.setVisible(false);
        mArticleApi.search(mSearchRequest.toQueryMay()).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                handleResponse(response.body());
                handleComplete();
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("Error", t.getMessage());
                handleComplete();
            }
        });
    }

    private void handleComplete() {
        miActionProgressItem.setVisible(false);
        miSearch.setVisible(true);
    }

    private void handleResponse(SearchResult searchResult) {
        if (searchResult != null) {
            mCustomAdapter.setBooks(searchResult.getBooks());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        miSearch = menu.findItem(R.id.miSearch);
        return true;
    }

    private void setUpSearchView() {
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(miSearch);
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setHint("Search...");
        et.setHintTextColor(Color.parseColor("#50FFFFFF"));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                mSearchRequest.setPage(1);
                mSearchRequest.setQuery(query);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setUpSearchView();
        fetchBooks();
        return super.onPrepareOptionsMenu(menu);
    }
}
