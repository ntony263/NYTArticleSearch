package coderschool.nytarticlesearch.Activity.Utils;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final Gson = new Gson();

    public static Retrofit get() {
        return new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient client() {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor())
                .addInterceptor(loggingInterceptor())
                .build();
    }

    private static Interceptor responseInterceptor (){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

            }
        }
    }
}
