package project1movies.android.com.project1movies.data;

import java.io.Serializable;

public class MovieData implements Serializable{

    public static final String Json_Id = "id";
    public static final String Json_Title = "title";
    public static final String Json_Release_Date = "release_date";
    public static final String Json_Vote_Average = "vote_average";
    public static final String Json_Overview = "overview";
    public static final String Json_Poster_Path = "poster_path";

    public String Id;
    public String Title;
    public String Release_Date;
    public String Vote_Average;
    public String Overview;
    public String Poster_Path;

    public MovieData(){}

    public MovieData(String Id, String Title, String Release_Date,
                     String Vote_Average, String Overview, String Poster_Path){
        this.Id = Id;
        this.Title = Title;
        this.Release_Date = Release_Date;
        this.Vote_Average = Vote_Average;
        this.Overview = Overview;
        this.Poster_Path = Poster_Path;
    }

    public String getMovieInfo(String value){
        switch (value){
            case Json_Id: return Id;
            case Json_Title: return Title;
            case Json_Release_Date: return Release_Date;
            case Json_Vote_Average: return Vote_Average;
            case Json_Overview: return Overview;
            case Json_Poster_Path: return Poster_Path;
            default: return "invalid value!";
        }

    }
}
