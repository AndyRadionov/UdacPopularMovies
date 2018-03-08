package io.github.andyradionov.udacpopularmovies.moviedetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.github.andyradionov.udacpopularmovies.R;
import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.model.MovieYoutubeTrailer;

/**
 * @author Andrey Radionov
 */

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.TrailerViewHolder> {
    private static final String TAG = MovieTrailersAdapter.class.getSimpleName();

    private MovieYoutubeTrailer[] mTrailers;
    private OnItemClickListener mClickListener;

    public interface OnItemClickListener {
        void onMovieItemClick(String trailerSource);
    }

    public MovieTrailersAdapter(MovieYoutubeTrailer[] trailers, OnItemClickListener clickListener) {
        Log.d(TAG, "MovieTrailersAdapter constructor call");

        mTrailers = trailers;
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelativeLayout trailerView = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trailer, parent, false);
        return new TrailerViewHolder(trailerView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mTrailers.length;
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private View trailerView;

        TrailerViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "TrailerViewHolder constructor call");
            trailerView = itemView;
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Log.d(TAG, "TrailerViewHolder bind");

            MovieYoutubeTrailer trailer = mTrailers[position];

            ImageView trailerThumbnail = trailerView.findViewById(R.id.iv_trailer_thumbnail);
            Picasso.with(trailerView.getContext())
                    .load(String.format(App.TRAILER_THUMBNAIL_BASE_URL, trailer.getSource()))
                    .into(trailerThumbnail);

            TextView trailerText = trailerView.findViewById(R.id.tv_trailer_text);
            trailerText.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick Trailer");
            MovieYoutubeTrailer trailer = mTrailers[getAdapterPosition()];
            mClickListener.onMovieItemClick(trailer.getSource());
        }
    }
}
