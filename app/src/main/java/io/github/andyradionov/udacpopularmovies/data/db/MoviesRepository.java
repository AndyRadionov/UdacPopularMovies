package io.github.andyradionov.udacpopularmovies.data.db;


import android.content.ContentUris;
import android.net.Uri;
import android.util.Log;

import io.github.andyradionov.udacpopularmovies.data.model.Movie;
import io.github.andyradionov.udacpopularmovies.moviedetails.MovieDetailsPresenter;
import io.github.andyradionov.udacpopularmovies.movies.MoviesPresenter;

import static io.github.andyradionov.udacpopularmovies.data.db.MoviesContract.MovieEntry;

/**
 * @author Andrey Radionov
 */

public class MoviesRepository {

    private static final String TAG = MoviesRepository.class.getSimpleName();


    public MoviesRepository() {
    }

    public void addMovieToFavourite(MovieDetailsPresenter presenter, Movie movie) {
        Log.d(TAG, "addMovieToFavourite: " + movie);
    }

    public void deleteMovieFromFavourite(MovieDetailsPresenter presenter, Movie movie) {
        Log.d(TAG, "deleteMovieFromFavourite: " + movie);
    }

    public void isMovieFavourite(MovieDetailsPresenter presenter, long movieId) {
        Log.d(TAG, "isMovieFavourite: " + movieId);
        Uri MovieUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movieId);
    }

    public void getFavouriteMovies(MoviesPresenter presenter) {
        Log.d(TAG, "getFavouriteMovies");
    }
}
