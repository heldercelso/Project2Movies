package project1movies.android.com.project1movies.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import project1movies.android.com.project1movies.db.FavoriteTableDb.FavoriteEntry;

@SuppressWarnings("ConstantConditions")
public class DBContentProvider extends ContentProvider {

    private static final String PROVIDER_NAME = "moviesdb.favorites";
    private static final String URL = "content://" + PROVIDER_NAME + "/movies";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    private SQLiteDatabase db;

    private static final int MOVIES = 1;
    private static final int MOVIE_ID = 2;

    private static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "movies", MOVIES);
        uriMatcher.addURI(PROVIDER_NAME, "movies/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DbConnection dbHelper = new DbConnection(context);

        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {

        int match = uriMatcher.match(uri);
        Cursor cursor;

        switch (match){
            case MOVIES:
                cursor = db.query(
                        FavoriteEntry.TABLE_NAME,
                        null,null,null,null,null, sortOrder);
                break;
            case MOVIE_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "movie_id=?";
                String[] mSelectionArgs = new String[]{id};

                cursor = db.query(
                        FavoriteEntry.TABLE_NAME,
                        null,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long rowID = db.insert(FavoriteEntry.TABLE_NAME, "", values);

        // If record is added successfully
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)){
            case MOVIES:
                count = db.delete(FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case MOVIE_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( FavoriteEntry.TABLE_NAME, FavoriteEntry._ID +  " = " + id +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        return 0;
    }

}
