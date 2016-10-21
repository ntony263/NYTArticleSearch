package coderschool.nytarticlesearch.Activity.Model;


import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("response")
    private JsonObject response;

    @SerializedName("status")
    private String status;

    public JsonObject getResponse() {
        if (response==null){
            return new JsonObject();
        }
        return response;
    }

    public String getStatus() {
        return status;
    }
}
