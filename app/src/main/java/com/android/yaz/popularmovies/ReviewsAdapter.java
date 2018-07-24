package com.android.yaz.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.yaz.popularmovies.model.MovieReview;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>{

    private MovieReview[] mReviewsData;

    public ReviewsAdapter() {}

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.written_by_tv) TextView mWrittenByTextView;
        @BindView(R.id.review_tv) TextView mReviewTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.reviews_list_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder reviewViewHolder, int position) {
        MovieReview reviewData = mReviewsData[position];
        reviewViewHolder.mWrittenByTextView.setText(reviewData.getAuthor());
        reviewViewHolder.mReviewTextView.setText(reviewData.getReview());
    }

    @Override
    public int getItemCount() {
        if(mReviewsData == null) {
            return 0;
        } else {
            return  mReviewsData.length;
        }
    }

    public void setReviewsAdapter(MovieReview[] reviewsData) {
        mReviewsData = reviewsData;
        notifyDataSetChanged();
    }
}
