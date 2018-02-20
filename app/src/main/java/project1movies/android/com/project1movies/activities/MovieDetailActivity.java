package project1movies.android.com.project1movies.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import project1movies.android.com.project1movies.R;
import project1movies.android.com.project1movies.adapters.ReviewsAdapter;
import project1movies.android.com.project1movies.adapters.TrailersAdapter;
import project1movies.android.com.project1movies.data.MovieData;
import project1movies.android.com.project1movies.data.ReviewData;
import project1movies.android.com.project1movies.data.TrailerData;
import project1movies.android.com.project1movies.db.DBContentProvider;
import project1movies.android.com.project1movies.db.FavoriteTableDb.FavoriteEntry;
import project1movies.android.com.project1movies.loader.MovieService;
import project1movies.android.com.project1movies.utilities.ImageUtils;
import project1movies.android.com.project1movies.utilities.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity implements
        TrailersAdapter.TrailerAdapterOnClickHandler {

    private TextView mTitle;
    private TextView mYear;
    private TextView mRating;
    private TextView mSynopsis;
    private TextView TrailerLabel;
    private TextView ReviewLabel;
    private TextView TrailerLine;
    private TextView ReviewLine;
    private ImageView mPosterImage;
    private static final int TRAILER_LOADER_ID = 1;
    private static final int REVIEW_LOADER_ID = 2;
    private static TrailersAdapter mTrailersAdapter;
    private static ReviewsAdapter mReviewsAdapter;
    private RecyclerView mTrailersRecyclerView;
    private RecyclerView mReviewsRecyclerView;
    private ToggleButton mToggleButton;
    private ImageView mFavoriteStarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mockup);

        TrailerLabel = findViewById(R.id.label1);
        ReviewLabel = findViewById(R.id.label2);
        TrailerLine = findViewById(R.id.line1);
        ReviewLine = findViewById(R.id.line2);
        mTitle = findViewById(R.id.movie_title);
        mYear = findViewById(R.id.movie_year);
        mRating = findViewById(R.id.movie_rating);
        mSynopsis = findViewById(R.id.movie_synopsis);
        mPosterImage = findViewById(R.id.movie_poster);
        mTrailersRecyclerView = findViewById(R.id.trailer_recycler);
        mReviewsRecyclerView = findViewById(R.id.review_recycler);
        mToggleButton = findViewById(R.id.button);
        mFavoriteStarImage = findViewById(R.id.favorite_star);

        loadMovieInfos();

    }

    private void removeMovieFromDb(MovieData movie){
        mFavoriteStarImage.setImageResource(android.R.drawable.btn_star_big_off);
        mToggleButton.setBackgroundColor(getResources().getColor(R.color.Cyan));
        mToggleButton.setTextColor(getResources().getColor(R.color.Light_Grey));
        mToggleButton.setChecked(false);
        getContentResolver().delete(
                DBContentProvider.CONTENT_URI,
                FavoriteEntry.COLUMN_MOVIE_ID + "=" + movie.getMovieInfo(MovieData.Json_Id),
                null);

        // used when content provider was not implemented - direct access to db
        //MainActivity.mDbConnection.delete(movie);
    }

    private void insertMovieIntoDb(MovieData movie){
        ContentValues new_input = new ContentValues();

        new_input.put(FavoriteEntry.COLUMN_MOVIE_ID, movie.getMovieInfo(MovieData.Json_Id));
        new_input.put(FavoriteEntry.COLUMN_TITLE, movie.getMovieInfo(MovieData.Json_Title));
        new_input.put(FavoriteEntry.COLUMN_YEAR, movie.getMovieInfo(MovieData.Json_Release_Date));
        new_input.put(FavoriteEntry.COLUMN_RATING, movie.getMovieInfo(MovieData.Json_Vote_Average));
        new_input.put(FavoriteEntry.COLUMN_OVERVIEW, movie.getMovieInfo(MovieData.Json_Overview));
        new_input.put(FavoriteEntry.COLUMN_POSTER, movie.getMovieInfo(MovieData.Json_Poster_Path));

        Uri uri = getContentResolver().insert(
                DBContentProvider.CONTENT_URI, new_input);

        assert uri != null;
        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();

        mFavoriteStarImage.setImageResource(android.R.drawable.btn_star_big_on);
        mToggleButton.setBackgroundColor(Color.RED);
        mToggleButton.setTextColor(Color.WHITE);
        mToggleButton.setChecked(true);

        // used when content provider was not implemented - direct access to db
        //MainActivity.mDbConnection.insert(movie);
    }

    public void onClickMarkAsFavorite(View view) {
        Intent intent = getIntent();
        MovieData movie = (MovieData) intent.getSerializableExtra(MainActivity.MOVIE_KEY);

        // remove from DB
        if (mToggleButton.getText().equals(getString(R.string.mark_as_favorite))) {
            removeMovieFromDb(movie);
        } else
        // insert into DB
        if (mToggleButton.getText().equals(getString(R.string.remove_from_favorites))) {
            insertMovieIntoDb(movie);
        }
    }

    public void onClickFavoriteStar(View view) {
        Intent intent = getIntent();
        MovieData movie = (MovieData) intent.getSerializableExtra(MainActivity.MOVIE_KEY);

        // remove from DB
        System.out.println(mToggleButton.getText().toString());
        if (mToggleButton.getText().equals(getString(R.string.remove_from_favorites))) {
            removeMovieFromDb(movie);
        } else
        // insert into DB
        if (mToggleButton.getText().equals(getString(R.string.mark_as_favorite))) {
            insertMovieIntoDb(movie);
        }
    }

    private void loadMovieInfos() {
        Intent intent = getIntent();
        MovieData movie = (MovieData) intent.getSerializableExtra(MainActivity.MOVIE_KEY);

        mTitle.setText(movie.getMovieInfo(MovieData.Json_Title));
        mYear.setText(movie.getMovieInfo(MovieData.Json_Release_Date).substring(0, 7));
        mRating.setText(movie.getMovieInfo(MovieData.Json_Vote_Average).concat("/10"));
        mSynopsis.setText(movie.getMovieInfo(MovieData.Json_Overview));

        // used when content provider was not implemented - direct access to db
        //Cursor DbCursor = MainActivity.mDbConnection.getByMovieId(MovieId);

        String MovieId = movie.getMovieInfo(MovieData.Json_Id);

        // using content provider to access db
        Cursor DbCursor = getContentResolver().query(
                Uri.withAppendedPath(DBContentProvider.CONTENT_URI, MovieId),
                null,
                null,
                null,
                null);

        // Loading favorite button - check if it is in DB
        assert DbCursor != null;
        switch (DbCursor.getCount()){
            case 0:
                mToggleButton.setChecked(false);
                mFavoriteStarImage.setImageResource(android.R.drawable.btn_star_big_off);
                mToggleButton.setBackgroundColor(getResources().getColor(R.color.Cyan));
                mToggleButton.setTextColor(getResources().getColor(R.color.Light_Grey));
            break;
            case 1:
                mToggleButton.setChecked(true);
                mFavoriteStarImage.setImageResource(android.R.drawable.btn_star_big_on);
                mToggleButton.setBackgroundColor(Color.RED);
                mToggleButton.setTextColor(Color.WHITE);
            break;
        }
        DbCursor.close();

        // Loading poster
        String posterUrl = NetworkUtils.POSTER_URL + NetworkUtils.POSTER_SIZE_URL +
                movie.Poster_Path;
        ImageUtils.setPosterImage(this, posterUrl, mPosterImage);

        loadTrailers();
        loadReviews();
    }

    private void loadTrailers() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this, 1, LinearLayoutManager.VERTICAL, false);

        mTrailersRecyclerView.setLayoutManager(gridLayoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);
        mTrailersAdapter = new TrailersAdapter(this);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

        //Async Loader with new callback
        getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, TrailerLoaderListener);
    }

    private void loadReviews() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this, 1, LinearLayoutManager.VERTICAL, false);

        mReviewsRecyclerView.setLayoutManager(gridLayoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsAdapter = new ReviewsAdapter();
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        //Async Loader with new callback
        getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, ReviewLoaderListener);
    }

    // Open youtube application
    public void onClick(int movie_position) {
        TrailerData trailer = MovieDetailActivity.mTrailersAdapter.getTrailer(movie_position);
        Uri uri = Uri.parse(NetworkUtils.YT_BASE_URL +
                trailer.getTrailerInfo(TrailerData.Json_Key));

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    //AsyncTaskLoader for Trailer
    private final LoaderManager.LoaderCallbacks<Object[]> TrailerLoaderListener
            = new LoaderManager.LoaderCallbacks<Object[]>() {
        public void onLoaderReset(Loader<Object[]> loader) {}

        @SuppressWarnings("unchecked")
        public Loader<Object[]> onCreateLoader(int id, final Bundle loaderArgs) {
            //AsyncTaskLoader - Created in the class MovieService
            Object myLoader =
                    new MovieService(MovieDetailActivity.this, "Trailers");

            return (Loader<Object[]>) myLoader;
        }

        public void onLoadFinished(Loader<Object[]> loader, Object[] Data) {
            if (Data.length > 0) {
                mTrailersAdapter.setTrailerData((TrailerData[]) Data);
            } else {
                TrailerLabel.setVisibility(View.INVISIBLE);
                TrailerLine.setVisibility(View.INVISIBLE);
            }
        }
    };

    //AsyncTaskLoader for Review
    private final LoaderManager.LoaderCallbacks<Object[]> ReviewLoaderListener
            = new LoaderManager.LoaderCallbacks<Object[]>() {
        public void onLoaderReset(Loader<Object[]> loader) {
        }

        @SuppressWarnings("unchecked")
        public Loader<Object[]> onCreateLoader(int id, final Bundle loaderArgs) {
            //AsyncTaskLoader - Created in the class MovieService
            Object myLoader =
                    new MovieService(MovieDetailActivity.this, "Reviews");

            return (Loader<Object[]>) myLoader;
        }

        public void onLoadFinished(Loader<Object[]> loader, Object[] Data) {
            if (Data.length > 0) {
                mReviewsAdapter.setReviewData((ReviewData[]) Data);
            } else {
                ReviewLabel.setVisibility(View.INVISIBLE);
                ReviewLine.setVisibility(View.INVISIBLE);
            }
        }
    };

}