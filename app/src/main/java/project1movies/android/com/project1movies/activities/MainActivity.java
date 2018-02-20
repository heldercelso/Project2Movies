package project1movies.android.com.project1movies.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import project1movies.android.com.project1movies.R;
import project1movies.android.com.project1movies.adapters.MoviesAdapter;
import project1movies.android.com.project1movies.data.MovieData;
import project1movies.android.com.project1movies.loader.MovieService;
import project1movies.android.com.project1movies.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity implements
        MoviesAdapter.MoviesAdapterOnClickHandler {

    private TextView mErrorMessageDisplay;
    private TextView mEmpty;
    private Button mRetryButton;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mRecyclerView;
    private static MoviesAdapter mMoviesAdapter;
    public static MovieData movie;

    private static final int LOADER_ID = 0;
    public static String DEFAULT_SORTBY = NetworkUtils.MOVIES_BY_POPULARITY;
    public static final String MOVIE_KEY = "movie";
    private static String NET_OR_DB = "NET";

    //used when content provider was not implemented to access db directly
    //public static DbConnection mDbConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmpty = findViewById(R.id.empty);
        mRetryButton = findViewById(R.id.retry_button);
        mErrorMessageDisplay = findViewById(R.id.error_message);
        mLoadingIndicator = findViewById(R.id.loading_bar);
        mRecyclerView = findViewById(R.id.movies_recycler);

        int DefineMoviesInColumn = getScreenOrientation() + 1;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this, DefineMoviesInColumn, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        //used when content provider was not implemented
        //mDbConnection = new DbConnection(this);

        if (NET_OR_DB.equals("NET")) {
            loadMovies(true);
        }
        if (NET_OR_DB.equals("DB")) {
            loadFavorites();
        }
    }

    private void loadMovies(boolean create_callback){
        Context context = MainActivity.this;

        if (NetworkUtils.isNetworkConnected(context)) {
            showMoviesDataView();
            //Async Loader with new callback
            if (create_callback) {
                getSupportLoaderManager().initLoader(LOADER_ID, null, MoviesLoaderListener);
            } else {
                getSupportLoaderManager().restartLoader(LOADER_ID, null, MoviesLoaderListener);
            }
        } else {
            showErrorMessage();
        }
    }

    private void loadFavorites() {
        getSupportLoaderManager().restartLoader(LOADER_ID, null, MoviesLoaderListener);
    }

    public void OnClickRetryButton(View view){
        loadMovies(false);
    }

    private int getScreenOrientation()
    {
        Display getOrient = getWindowManager().getDefaultDisplay();
        int orientation;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.sort_by_popularity:
                DEFAULT_SORTBY = NetworkUtils.MOVIES_BY_POPULARITY;
                NET_OR_DB = "NET";
                loadMovies(false);
                break;
            case R.id.sort_by_average_rate:
                DEFAULT_SORTBY = NetworkUtils.MOVIES_TOP_RATED;
                NET_OR_DB = "NET";
                loadMovies(false);
                break;
            case R.id.sort_by_favorites:
                NET_OR_DB = "DB";
                loadFavorites();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMoviesDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRetryButton.setVisibility(View.INVISIBLE);
        /* Then, make sure the movies data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRetryButton.setVisibility(View.VISIBLE);

        View view = this.findViewById(R.id.error_message);
        Snackbar snackbar = Snackbar.make(view, getString(R.string.check_your_internet_connection), Snackbar.LENGTH_LONG);
        snackbar.setAction(getString(R.string.retry), new View.OnClickListener() {
            //Try to reconnect if clicked
            @Override
            public void onClick(View view) {
                loadMovies(true);
            }
        });
        snackbar.show();
    }

    // Open the Movie detail activity
    public void onClick(int movie_position){
        Context context = MainActivity.this;
        if (NetworkUtils.isNetworkConnected(context) || NET_OR_DB.equals("DB")) {
            movie = mMoviesAdapter.getMovie(movie_position);
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MOVIE_KEY, movie);
            startActivity(intent);
        } else {
            showErrorMessage();
        }
    }

    // AsyncTaskLoader to load the movies from internet or from db
    private final LoaderManager.LoaderCallbacks<MovieData[]> MoviesLoaderListener
            = new LoaderManager.LoaderCallbacks<MovieData[]>() {
        public void onLoaderReset(Loader<MovieData[]> loader){}
        @SuppressWarnings("unchecked")
        public Loader<MovieData[]> onCreateLoader(int id, final Bundle loaderArgs) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
            //AsyncTaskLoader - Created in the class MovieService
            Object myLoader = null;
            switch (NET_OR_DB){
                case "NET":
                    myLoader =
                            new MovieService(MainActivity.this, "MainActivity");
                    break;
                case "DB":
                    myLoader =
                            new MovieService(MainActivity.this, "Db");
                    break;
            }

            return (Loader<MovieData[]>) myLoader;
        }
        public void onLoadFinished(Loader<MovieData[]> loader, MovieData[] Data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            mMoviesAdapter.setMoviesData(Data);
            if (Data == null) {
                if (NET_OR_DB.equals("DB")){
                    mEmpty.setVisibility(View.VISIBLE);
                }
                if (NET_OR_DB.equals("NET")){
                    showErrorMessage();
                }
            } else {
                mEmpty.setVisibility(View.INVISIBLE);
                showMoviesDataView();
            }
        }
    };

    @Override
    protected void onResume() {
        if (NET_OR_DB.equals("DB")){
            loadFavorites();
        }
        super.onResume();
    }

}
