package io.github.andyradionov.udacpopularmovies.data.db;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import io.github.andyradionov.udacpopularmovies.data.model.Movie;
import io.github.andyradionov.udacpopularmovies.moviedetails.MovieDetailsPresenter;

import static io.github.andyradionov.udacpopularmovies.data.db.MoviesContract.MovieEntry;

/**
 * @author Andrey Radionov
 */

public class MoviesRepository {

    private static final String TAG = MoviesRepository.class.getSimpleName();

    private Context mContext;

    public MoviesRepository(Context context) {
        mContext = context;
    }

    public void addMovieToFavourite(MovieDetailsPresenter presenter, Movie movie) {
        Log.d(TAG, "addMovieToFavourite: " + movie);

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry._ID, movie.getId());
        contentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());

        Uri insertedUri = mContext
                .getContentResolver()
                .insert(MovieEntry.CONTENT_URI, contentValues);

        if (insertedUri != null) {
            presenter.showIconFavourite(true);
        }
    }

    public void deleteMovieFromFavourite(MovieDetailsPresenter presenter, long movieId) {
        Log.d(TAG, "deleteMovieFromFavourite: " + movieId);

        String where = " _id = ? ";
        String[] selectionArgs = new String[]{String.valueOf(movieId)};
        int deletedNum = mContext
                .getContentResolver()
                .delete(MovieEntry.CONTENT_URI, where, selectionArgs);

        if (deletedNum > 0) {
            presenter.showIconFavourite(false);
        }
    }

    public void isMovieFavourite(MovieDetailsPresenter presenter, long movieId) {
        Log.d(TAG, "isMovieFavourite: " + movieId);
        Uri MovieUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movieId);
        Cursor cursor = mContext
                .getContentResolver()
                .query(MovieUri, null, null, null, null);
        if (cursor == null) {
            presenter.showIconFavourite(false);
            return;
        }
        boolean isFavourite = cursor.getCount() > 0;
        cursor.close();
        presenter.showIconFavourite(isFavourite);
    }
}
