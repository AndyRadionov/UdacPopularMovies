package io.github.andyradionov.udacpopularmovies.moviedetails;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.db.MoviesRepository;
import io.github.andyradionov.udacpopularmovies.data.model.Movie;
import io.github.andyradionov.udacpopularmovies.data.model.MovieReview;
import io.github.andyradionov.udacpopularmovies.data.model.MovieYoutubeTrailer;
import io.github.andyradionov.udacpopularmovies.data.network.MoviesNetworkData;

/**
 * @author Andrey Radionov
 */

@InjectViewState
public class MovieDetailsPresenter extends MvpPresenter<MovieDetailsView> {

    private static final String TAG = MovieDetailsPresenter.class.getSimpleName();
    private MoviesRepository mMoviesRepository = App.getMoviesRepository();
    private MoviesNetworkData mMoviesNetworkData = App.getMoviesNetworkData();

    public void setMovieFavourite(Movie movie, boolean isFavourite) {
        Log.d(TAG, "setMovieFavourite: " + movie + " isFavourite: " + isFavourite);
        if (isFavourite) {
            mMoviesRepository.addMovieToFavourite(this, movie);
        } else {
            mMoviesRepository.deleteMovieFromFavourite(this, movie.getId());
        }
    }

    public void checkIsFavorite(long movieId) {
        Log.d(TAG, "checkIsFavorite: " + movieId);
        mMoviesRepository.isMovieFavourite(this, movieId);
    }

    public void showIconFavourite(boolean isFavourite) {
        Log.d(TAG, "showIconFavourite: " + isFavourite);
        getViewState().setFavouriteIcon(isFavourite);
    }

    public void loadTrailers(long movieId) {
        Log.d(TAG, "loadTrailers: " + movieId);

        MovieYoutubeTrailer[] cachedTrailers = mMoviesNetworkData.getCachedTrailers(movieId);
        if (cachedTrailers != null) {
            Log.d(TAG, "returns cached trailers");
            getViewState().showTrailers(cachedTrailers);
            return;
        }

        mMoviesNetworkData.getTrailers(movieId)
                .doOnError(throwable -> getViewState().hideTrailers())
                .subscribe(trailers -> {
                    if (trailers == null || trailers.length == 0) {
                        getViewState().hideTrailers();
                    } else {
                        mMoviesNetworkData.setTrailersCache(movieId, trailers);
                        getViewState().showTrailers(trailers);
                    }
                }, throwable -> getViewState().hideTrailers());
    }

    public void loadReviews(long movieId) {
        Log.d(TAG, "loadReviews: " + movieId);

        MovieReview[] cachedReviews = mMoviesNetworkData.getCachedReviews(movieId);
        if (cachedReviews != null) {
            Log.d(TAG, "returns cached reviews");
            getViewState().showReviews(cachedReviews);
            return;
        }

        mMoviesNetworkData.getReviews(movieId)
                .doOnError(throwable -> getViewState().hideReviews())
                .subscribe(reviews -> {
                    if (reviews == null || reviews.length == 0) {
                        getViewState().hideReviews();
                    } else {
                        mMoviesNetworkData.setReviewsCache(movieId, reviews);
                        getViewState().showReviews(reviews);
                    }
                }, throwable -> getViewState().hideReviews());
    }
}
