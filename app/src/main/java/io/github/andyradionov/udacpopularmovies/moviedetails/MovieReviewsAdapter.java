package io.github.andyradionov.udacpopularmovies.moviedetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.andyradionov.udacpopularmovies.R;
import io.github.andyradionov.udacpopularmovies.data.model.MovieReview;

/**
 * @author Andrey Radionov
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewViewHolder> {
    private static final String TAG = MovieReviewsAdapter.class.getSimpleName();

    private MovieReview[] mReviews;

    public MovieReviewsAdapter(MovieReview[] mReviews) {
        Log.d(TAG, "MovieReviewsAdapter constructor call");
        this.mReviews = mReviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout reviewView = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mReviews.length;
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        private View reviewView;

        ReviewViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ReviewViewHolder constructor call");
            reviewView = itemView;
        }

        void bind(int position) {
            Log.d(TAG, "ReviewViewHolder bind");

            MovieReview review = mReviews[position];

            TextView reviewAuthor = reviewView.findViewById(R.id.tv_review_author);
            TextView reviewContent = reviewView.findViewById(R.id.tv_review_content);
            reviewAuthor.setText(review.getAuthor());
            reviewContent.setText(review.getContent());
        }
    }
}
