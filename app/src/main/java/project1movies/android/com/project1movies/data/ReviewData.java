package project1movies.android.com.project1movies.data;

public class ReviewData {

    public static final String Json_Id = "id";
    public static final String Json_Author = "author";
    public static final String Json_Content = "content";
    public static final String Json_Url = "url";

    public String Id;
    public String Author;
    public String Content;
    public String Url;

    public String getReviewInfo(String value){
        switch (value){
            case Json_Id: return Id;
            case Json_Author: return Author;
            case Json_Content: return Content;
            case Json_Url: return Url;
            default: return "invalid value!";
        }

    }
}
