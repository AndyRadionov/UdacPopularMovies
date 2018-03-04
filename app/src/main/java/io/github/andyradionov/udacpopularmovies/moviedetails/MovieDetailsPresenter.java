package io.github.andyradionov.udacpopularmovies.moviedetails;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.db.MoviesRepository;
import io.github.andyradionov.udacpopularmovies.data.model.Movie;

/**
 * @author Andrey Radionov
 */

@InjectViewState
public class MovieDetailsPresenter extends MvpPresenter<MovieDetailsView> {

    private static final String TAG = MovieDetailsPresenter.class.getSimpleName();
    private MoviesRepository mMoviesRepository = App.getMoviesRepository();

    public void setMovieFavourite(Movie movie, boolean isFavourite) {
        Log.d(TAG, "setMovieFavourite: " + movie + " isFavourite: " + isFavourite);
        if (isFavourite) {
            mMoviesRepository.addMovieToFavourite(this, movie);
        } else {
            mMoviesRepository.deleteMovieFromFavourite(this, movie);
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
}
