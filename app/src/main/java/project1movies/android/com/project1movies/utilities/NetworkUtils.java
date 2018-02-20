package project1movies.android.com.project1movies.utilities;

import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.util.Scanner;

import project1movies.android.com.project1movies.activities.MainActivity;

import static project1movies.android.com.project1movies.activities.MainActivity.DEFAULT_SORTBY;

public class NetworkUtils {

    //MovieDB address
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    public static final String POSTER_URL = "http://image.tmdb.org/t/p/";
    public static final String POSTER_SIZE_URL = "w185";

    public static final String MOVIES_TOP_RATED = "/top_rated";
    public static final String MOVIES_BY_POPULARITY = "/popular";

    private static final String MOVIE_TRAILER = "/videos";
    private static final String MOVIE_REVIEW = "/reviews";

    private static final String API_KEY = "";
    private final static String API_PARAM = "api_key";

    private static final String LANGUAGE_PARAM = "language";
    private static final String LANGUAGE = "en";//"pt-BR";

    //Youtube Address
    public static final String YT_BASE_URL = "https://www.youtube.com/watch?v=";

    public static URL buildUrl(String UrlFunction) {

        String URL = null;
        switch (UrlFunction){
            case "MainActivity":
                URL = BASE_URL + DEFAULT_SORTBY;
                break;
            case "Trailers":
                URL = BASE_URL + "/" + MainActivity.movie.Id + MOVIE_TRAILER;
                break;
            case "Reviews":
                URL = BASE_URL + "/" + MainActivity.movie.Id + MOVIE_REVIEW;
                break;

        }

        return appendApiKeyToURL(URL);
    }

    private static URL appendApiKeyToURL(String URL) {
        Uri builtUri = Uri.parse(URL).buildUpon()
                .appendQueryParameter(API_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
