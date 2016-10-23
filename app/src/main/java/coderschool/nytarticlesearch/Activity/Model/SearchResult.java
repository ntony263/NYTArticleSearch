package coderschool.nytarticlesearch.Activity.Model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    @SerializedName("docs")
    private List<Article> articles;

    public List<Article> getArticles() {
        if (articles == null){
            return new ArrayList<>();
        }
        return articles;
    }
}
