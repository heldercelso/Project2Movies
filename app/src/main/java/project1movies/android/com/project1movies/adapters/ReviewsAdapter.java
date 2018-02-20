package project1movies.android.com.project1movies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import project1movies.android.com.project1movies.R;
import project1movies.android.com.project1movies.data.ReviewData;

public class ReviewsAdapter extends
        RecyclerView.Adapter<ReviewsAdapter.ReviewAdapterViewHolder> {

    private ReviewData[] mReviewData;

    public class ReviewAdapterViewHolder extends
            RecyclerView.ViewHolder {
        private final TextView mAuthor;
        private final TextView mContent;

        ReviewAdapterViewHolder(View view) {
            super(view);
            mAuthor = view.findViewById(R.id.author_name);
            mContent = view.findViewById(R.id.review);
        }

        void bind(ReviewData review) {
            mAuthor.setText(review.getReviewInfo(ReviewData.Json_Author));
            mContent.setText(review.getReviewInfo(ReviewData.Json_Content));
        }
    }

    @Override
    public ReviewsAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_infos;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ReviewsAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder reviewAdapterViewHolder, int position) {
        reviewAdapterViewHolder.bind(mReviewData[position]);
    }

    @Override
    public int getItemCount() {
        if (mReviewData == null) {
            return 0;
        }
        return mReviewData.length;
    }

    public void setReviewData(ReviewData[] ReviewData) {
        mReviewData = ReviewData;
        notifyDataSetChanged();
    }

}
