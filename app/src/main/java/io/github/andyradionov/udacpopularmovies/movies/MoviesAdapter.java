package io.github.andyradionov.udacpopularmovies.movies;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import io.github.andyradionov.udacpopularmovies.R;
import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.model.Movie;

/**
 * @author Andrey Radionov
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private static final String TAG = MoviesAdapter.class.getSimpleName();

    private List<Movie> mMovies;
    private OnItemClickListener mClickListener;

    public interface OnItemClickListener {
        void onMovieItemClick(Movie movie);
    }

    public MoviesAdapter(OnItemClickListener clickListener) {
        Log.d(TAG, "MoviesAdapter constructor call");

        mMovies = Collections.emptyList();
        mClickListener = clickListener;
    }

    public void setData(List<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");

        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_card, parent, false);
        return new MovieViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private CardView movieCard;

        public MovieViewHolder(CardView itemView) {
            super(itemView);
            Log.d(TAG, "MovieViewHolder constructor call");
            movieCard = itemView;
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            Log.d("ArticleViewHolder", "bind");

            Movie movie = mMovies.get(position);

            ImageView moviePoster = movieCard.findViewById(R.id.iv_movie_poster);
            Picasso.with(movieCard.getContext())
                    .load(App.SMALL_IMAGE_BASE_URL + movie.getPosterPath())
                    .into(moviePoster);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick Movie Poster");
            Movie movie = mMovies.get(getAdapterPosition());
            mClickListener.onMovieItemClick(movie);
        }
    }
}
