package coderschool.nytarticlesearch.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import coderschool.nytarticlesearch.Activity.Utils.Constant;

public class Article implements Parcelable{

    @SerializedName("snippet")
    private String textOfArticle;

    @SerializedName("web_url")
    private String webUrl;

    @SerializedName("multimedia")
    private List<Media> multimedia;


    protected Article(Parcel in){
        textOfArticle = in.readString();
        webUrl = in.readString();
        multimedia = in.createTypedArrayList(Media.CREATOR);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(textOfArticle);
        dest.writeString(webUrl);
        dest.writeTypedList(multimedia);
    }

    public static class Media implements Parcelable {
        private String url;
        private String type;
        private int width;
        private int height;

        protected Media(Parcel in){
            url = in.readString();
            type = in.readString();
            width = in.readInt();
            height = in.readInt();
        }

        public static final Creator<Media> CREATOR = new Creator<Media>() {
            @Override
            public Media createFromParcel(Parcel in) {
                return new Media(in);
            }

            @Override
            public Media[] newArray(int size) {
                return new Media[size];
            }
        };

        public String getUrl() {
            return Constant.IMG_BASE_URL+url;
        }

        public String getType() {
            return type;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(url);
            dest.writeString(type);
            dest.writeInt(width);
            dest.writeInt(height);
        }
    }

    public String getTextOfArticle() {
        return textOfArticle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public List<Media> getMultimedia() {
        return multimedia;
    }
}
