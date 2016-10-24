package coderschool.nytarticlesearch.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest implements Parcelable{
    private int page = 0;
    private String query ;
    private String beginDate;
    private String order = "newest";
    private boolean hasArts;
    private boolean hasFashionStyle;
    private boolean hasSports;

    public SearchRequest (){

    }

    protected SearchRequest(Parcel in){
        page = in.readInt();
        query = in.readString();
        beginDate = in.readString();
        order = in.readString();
        hasArts = in.readByte() !=0;
        hasFashionStyle = in.readByte() !=0;
        hasSports = in.readByte() !=0;
    }

    public static final Creator<SearchRequest> CREATOR = new Creator<SearchRequest>() {
        @Override
        public SearchRequest createFromParcel(Parcel in) {
            return new SearchRequest(in);
        }

        @Override
        public SearchRequest[] newArray(int size) {
            return new SearchRequest[size];
        }
    };

    public String getOrder() {
        return order;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public boolean isHasArts() {
        return hasArts;
    }


    public boolean isHasSports() {
        return hasSports;
    }

    public boolean isHasFashionStyle() {
        return hasFashionStyle;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setHasArts(boolean hasArts) {
        this.hasArts = hasArts;
    }

    public void setHasFashionStyle(boolean hasFashionStyle) {
        this.hasFashionStyle = hasFashionStyle;
    }

    public void setHasSports(boolean hasSports) {
        this.hasSports = hasSports;
    }

    public void nextPage (){
        page = page + 1;
    }
    public void resetPage (){
        page = 0;}

    public void setQuery(String newQuery) {
        query = newQuery;
    }

    public Map<String, String> toQueryMap (){
        Map<String, String> options = new HashMap<>();
        if (query!=null) options.put("q", query);
        if (beginDate!=null) options.put("begindate", beginDate);
        if (order!= null) options.put("order",order.toLowerCase());
        if (getNewDesk() != null) options.put("fq", "news_desk("+getNewDesk()+")");
        options.put("page", String.valueOf(page));
        return options;
    }

    private String getNewDesk(){
        if (!hasArts && !hasFashionStyle && !hasSports) return null;
        String value = "";
        if (hasArts) value += "\"Arts\" ";
        if (hasSports) value += "\"Sports\" ";
        if (hasFashionStyle) value += "\"Fashion & Style\"";
        return value.trim();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeString(query);
        dest.writeString(beginDate);
        dest.writeString(order);
        dest.writeByte((byte) (hasArts ? 1:0));
        dest.writeByte((byte) (hasFashionStyle ? 1:0));
        dest.writeByte((byte) (hasSports ? 1:0));
    }
}
