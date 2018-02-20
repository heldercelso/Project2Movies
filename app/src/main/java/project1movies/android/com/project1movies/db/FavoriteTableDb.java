package project1movies.android.com.project1movies.db;

import android.provider.BaseColumns;

public class FavoriteTableDb {

    public static final class FavoriteEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_YEAR = "release_date";
        public static final String COLUMN_RATING = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER = "poster_path";

    }

}
