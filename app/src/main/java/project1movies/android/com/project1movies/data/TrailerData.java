package project1movies.android.com.project1movies.data;

public class TrailerData {

    public static final String Json_Id = "id";
    public static final String Json_Key = "key";
    public static final String Json_Name = "name";
    public static final String Json_Site = "site";

    public String Id;
    public String Key;
    public String Name;
    public String Site;

    public String getTrailerInfo(String value){
        switch (value){
            case Json_Id: return Id;
            case Json_Key: return Key;
            case Json_Name: return Name;
            case Json_Site: return Site;
            default: return "invalid value!";
        }

    }
}
