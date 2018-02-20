package project1movies.android.com.project1movies.adapters;

import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import project1movies.android.com.project1movies.R;
import project1movies.android.com.project1movies.data.MovieData;
import project1movies.android.com.project1movies.utilities.ImageUtils;
import project1movies.android.com.project1movies.utilities.NetworkUtils;

public class MoviesAdapter extends
        RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private MovieData[] mMoviesData;
    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler {
        void onClick(int position);
    }

    public MoviesAdapter(MoviesAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MoviesAdapterViewHolder extends
            RecyclerView.ViewHolder implements OnClickListener {
        final ImageView mMoviePoster;
        final Context context;

        MoviesAdapterViewHolder(View view) {
            super(view);
            mMoviePoster = view.findViewById(R.id.movie_poster);
            context = itemView.getContext();

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            // implemented in MainActivity.java - load the MovieDetailActivity
            mClickHandler.onClick(adapterPosition);
        }

        void bind(String posterUrl) {
            ImageUtils.setPosterImage(context, posterUrl, mMoviePoster);
        }
    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_poster;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder moviesAdapterViewHolder, int position) {
        String posterUrl = NetworkUtils.POSTER_URL +
                NetworkUtils.POSTER_SIZE_URL +
                mMoviesData[position].getMovieInfo(MovieData.Json_Poster_Path);

        moviesAdapterViewHolder.bind(posterUrl);
    }

    @Override
    public int getItemCount() {
        if (mMoviesData == null) {
            return 0;
        }
        return mMoviesData.length;
    }

    public void setMoviesData(MovieData[] MoviesData) {
        mMoviesData = MoviesData;
        notifyDataSetChanged();
    }

    public MovieData getMovie(int index) {
        return mMoviesData[index];
    }
}
