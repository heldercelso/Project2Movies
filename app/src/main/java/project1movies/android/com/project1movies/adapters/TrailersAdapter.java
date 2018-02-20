package project1movies.android.com.project1movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import project1movies.android.com.project1movies.R;
import project1movies.android.com.project1movies.data.TrailerData;

public class TrailersAdapter extends
        RecyclerView.Adapter<TrailersAdapter.TrailerAdapterViewHolder> {

    private TrailerData[] mTrailerData;
    private final TrailerAdapterOnClickHandler mClickHandler;

    public interface TrailerAdapterOnClickHandler {
        void onClick(int position);
    }

    public TrailersAdapter(TrailersAdapter.TrailerAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class TrailerAdapterViewHolder extends
            RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mTrailerText;

        TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerText = view.findViewById(R.id.trailer_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            // implemented in MainActivity.java - load the MovieDetailActivity
            mClickHandler.onClick(adapterPosition);
        }

        void bind(TrailerData trailer) {
            mTrailerText.setText(trailer.getTrailerInfo(TrailerData.Json_Name));
        }
    }

    @Override
    public TrailersAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.youtube_button;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new TrailersAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder trailerAdapterViewHolder, int position) {
        trailerAdapterViewHolder.bind(mTrailerData[position]);
    }

    @Override
    public int getItemCount() {
        if (mTrailerData == null) {
            return 0;
        }
        return mTrailerData.length;
    }

    public void setTrailerData(TrailerData[] TrailerData) {
        mTrailerData = TrailerData;
        notifyDataSetChanged();
    }

    public TrailerData getTrailer(int index) {
        return mTrailerData[index];
    }
}
