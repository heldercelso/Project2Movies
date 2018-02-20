package project1movies.android.com.project1movies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import project1movies.android.com.project1movies.db.FavoriteTableDb.FavoriteEntry;

class DbConnection extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;
    //used when content provider was not implemented
    //private final SQLiteDatabase database_wr = this.getWritableDatabase();
    //private final SQLiteDatabase database_rd = this.getReadableDatabase();

    public DbConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVORITES_TABLE =
                "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                        FavoriteEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteEntry.COLUMN_MOVIE_ID   + " TEXT NOT NULL UNIQUE, "              +
                        FavoriteEntry.COLUMN_TITLE      + " TEXT NOT NULL, "                     +
                        FavoriteEntry.COLUMN_YEAR       + " TEXT NOT NULL, "                     +
                        FavoriteEntry.COLUMN_RATING     + " TEXT NOT NULL, "                     +
                        FavoriteEntry.COLUMN_OVERVIEW   + " TEXT NOT NULL, "                     +
                        FavoriteEntry.COLUMN_POSTER     + " TEXT NOT NULL)";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //functions below were used when content provider was not implemented

    /*public void insert(MovieData movie) {
        ContentValues new_input = new ContentValues();

        new_input.put(FavoriteEntry.COLUMN_MOVIE_ID, movie.getMovieInfo(MovieData.Json_Id));
        new_input.put(FavoriteEntry.COLUMN_TITLE, movie.getMovieInfo(MovieData.Json_Title));
        new_input.put(FavoriteEntry.COLUMN_YEAR, movie.getMovieInfo(MovieData.Json_Release_Date));
        new_input.put(FavoriteEntry.COLUMN_RATING, movie.getMovieInfo(MovieData.Json_Vote_Average));
        new_input.put(FavoriteEntry.COLUMN_OVERVIEW, movie.getMovieInfo(MovieData.Json_Overview));
        new_input.put(FavoriteEntry.COLUMN_POSTER, movie.getMovieInfo(MovieData.Json_Poster_Path));

        database_wr.insert(
                FavoriteEntry.TABLE_NAME,
                null,
                new_input);
    }


    public Cursor getByMovieId(String MovieId) {
        String[] args = {MovieId};

        String query = "SELECT " +
                FavoriteEntry.COLUMN_MOVIE_ID + " " +
                "FROM " + FavoriteEntry.TABLE_NAME + " " +
                "WHERE " + FavoriteEntry.COLUMN_MOVIE_ID + " = ?";
        return database_rd.rawQuery(query, args);
    }


    public String getMovieId(Cursor c) {
        return c.getString(1);
    }
    public String getTitle(Cursor c) {
        return c.getString(2);
    }
    public String getYear(Cursor c) {
        return c.getString(3);
    }
    public String getRating(Cursor c) {
        return c.getString(4);
    }
    public String getOverview(Cursor c) {
        return c.getString(5);
    }
    public String getPoster(Cursor c) {
        return c.getString(6);
    }

    public Cursor getAll() {
        String query = "select " +
                FavoriteEntry._ID + ", " +
                FavoriteEntry.COLUMN_MOVIE_ID + ", " +
                FavoriteEntry.COLUMN_TITLE + ", " +
                FavoriteEntry.COLUMN_YEAR + ", " +
                FavoriteEntry.COLUMN_RATING + ", " +
                FavoriteEntry.COLUMN_OVERVIEW + ", " +
                FavoriteEntry.COLUMN_POSTER + " " +
                "FROM " + FavoriteEntry.TABLE_NAME;

        return database_rd.rawQuery(query,null);
    }

    /*public void delete(MovieData movie) {
        database_wr.delete(
                FavoriteEntry.TABLE_NAME,
                FavoriteEntry.COLUMN_MOVIE_ID + "=" + movie.getMovieInfo(MovieData.Json_Id),
                null);
    }*/
}
