package project1movies.android.com.project1movies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import project1movies.android.com.project1movies.data.MovieData;
import project1movies.android.com.project1movies.data.ReviewData;
import project1movies.android.com.project1movies.data.TrailerData;

public class JsonFilterUtils {

    private static final String RESULTS = "results";


    public static Object[] getSimpleJson (String UrlFunction, String MovieJsonStr)
            throws JSONException {
        JSONObject Json = new JSONObject(MovieJsonStr);
        JSONArray JsonArray = Json.getJSONArray(RESULTS);
        Object[] FilteredJson = null;

        switch (UrlFunction){
            case "MainActivity":
                FilteredJson = filterJsonForMovies(JsonArray);
                break;
            case "Trailers":
                FilteredJson = filterJsonForTrailers(JsonArray);
                break;
            case "Reviews":
                FilteredJson = filterJsonForReviews(JsonArray);
                break;
        }

        return FilteredJson;
    }

    private static MovieData[] filterJsonForMovies(JSONArray JsonArray)
            throws JSONException {

        MovieData[] movies = new MovieData[JsonArray.length()];

        for (int i = 0; i < JsonArray.length(); i++) {
            movies[i] = new MovieData();

            JSONObject MovieInfo = JsonArray.getJSONObject(i);
            movies[i].Id = MovieInfo.optString(MovieData.Json_Id);
            movies[i].Title = MovieInfo.optString(MovieData.Json_Title);
            movies[i].Release_Date = MovieInfo.optString(MovieData.Json_Release_Date);
            movies[i].Vote_Average = Double.toString(MovieInfo.optDouble(MovieData.Json_Vote_Average));
            movies[i].Overview = MovieInfo.optString(MovieData.Json_Overview);
            movies[i].Poster_Path = MovieInfo.optString(MovieData.Json_Poster_Path);
        }

        return movies;
    }

    private static TrailerData[] filterJsonForTrailers(JSONArray JsonArray)
            throws JSONException {

        TrailerData[] trailers = new TrailerData[JsonArray.length()];

        for (int i = 0; i < JsonArray.length(); i++) {
            trailers[i] = new TrailerData();

            JSONObject MovieInfo = JsonArray.getJSONObject(i);
            trailers[i].Id = MovieInfo.optString(TrailerData.Json_Id);
            trailers[i].Key = MovieInfo.optString(TrailerData.Json_Key);
            trailers[i].Name = MovieInfo.optString(TrailerData.Json_Name);
            trailers[i].Site = MovieInfo.optString(TrailerData.Json_Site);
        }

        return trailers;
    }

    private static ReviewData[] filterJsonForReviews(JSONArray JsonArray)
            throws JSONException {

        ReviewData[] reviews = new ReviewData[JsonArray.length()];

        for (int i = 0; i < JsonArray.length(); i++) {
            reviews[i] = new ReviewData();

            JSONObject MovieInfo = JsonArray.getJSONObject(i);
            reviews[i].Id = MovieInfo.optString(ReviewData.Json_Id);
            reviews[i].Author = MovieInfo.optString(ReviewData.Json_Author);
            reviews[i].Content = MovieInfo.optString(ReviewData.Json_Content);
            reviews[i].Url = MovieInfo.optString(ReviewData.Json_Url);
        }

        return reviews;
    }

}
