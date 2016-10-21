package coderschool.nytarticlesearch.Activity.Model;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest {
    private int page = 1;
    private String query = "Tennis";

    public void setPage(int page) {
        this.page = page;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, String> toQueryMay() {
        Map<String, String> options = new HashMap<>();
        options.put("page", String.valueOf(page));
        options.put("q", query);
        return options;
    }
}
