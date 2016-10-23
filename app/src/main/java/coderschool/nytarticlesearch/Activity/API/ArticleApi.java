package coderschool.nytarticlesearch.Activity.API;


import java.util.Map;

import coderschool.nytarticlesearch.Activity.Model.SearchResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ArticleApi {
    @GET("articlesearch.json")
    Call<SearchResult> search(@QueryMap(encoded = true) Map<String, String> options);
}
