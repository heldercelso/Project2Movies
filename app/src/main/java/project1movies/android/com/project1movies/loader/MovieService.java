package project1movies.android.com.project1movies.loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import java.net.URL;

import project1movies.android.com.project1movies.data.MovieData;
import project1movies.android.com.project1movies.db.DBContentProvider;
import project1movies.android.com.project1movies.utilities.JsonFilterUtils;
import project1movies.android.com.project1movies.utilities.NetworkUtils;

@SuppressLint("StaticFieldLeak")
public class MovieService extends AsyncTaskLoader<Object[]> {

    private Object[] mData = null;
    private final String UrlFunction;
    private final Context context;


    public MovieService(Context context, String UrlFunction){
        super(context);
        this.context = context;
        this.UrlFunction = UrlFunction;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        } else {
            forceLoad();
        }
    }

    @Override
    public Object[] loadInBackground() {

        // Load Favorite Movies from db
        if (UrlFunction.equals("Db")) {
            return getAllMoviesFromDb();

        } else { // Load Movies from internet
            URL RequestUrl;
            RequestUrl = NetworkUtils.buildUrl(UrlFunction);

            try {
                String jsonResponse = NetworkUtils
                        .getResponseFromHttpUrl(RequestUrl);

                return JsonFilterUtils
                        .getSimpleJson(UrlFunction, jsonResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    public void deliverResult(Object[] data) {
        mData = data;
        super.deliverResult(data);
    }

    private MovieData[] getAllMoviesFromDb(){
        //used when content provider was not implemented
        //Cursor DbCursor = MainActivity.mDbConnection.getAll();

        // querying with content provider
        Cursor DbCursor = context.getContentResolver().query(
                DBContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null);
        assert DbCursor != null;
        DbCursor.moveToLast();
        MovieData[] movies = null;

        if (DbCursor.getCount() > 0){
            movies = new MovieData[DbCursor.getCount()];
            int i=0;
            DbCursor.moveToFirst();
            while (i < DbCursor.getCount()){
                DbCursor.moveToPosition(i);

                String MovieId = DbCursor.getString(1);
                String Title = DbCursor.getString(2);
                String Year = DbCursor.getString(3);
                String Rating = DbCursor.getString(4);
                String Overview = DbCursor.getString(5);
                String Poster = DbCursor.getString(6);

                movies[i] = new MovieData(MovieId, Title, Year, Rating, Overview, Poster);
                i++;
            }
        }
        DbCursor.close();

        return movies;
    }

}
