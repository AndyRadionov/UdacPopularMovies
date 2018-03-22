package io.github.andyradionov.udacpopularmovies.movies;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.ArrayList;
import java.util.List;

import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.model.Movie;
import io.github.andyradionov.udacpopularmovies.data.network.MoviesNetworkData;

import static io.github.andyradionov.udacpopularmovies.data.db.MoviesContract.MovieEntry;

/**
 * @author Andrey Radionov
 */

@InjectViewState
public class MoviesPresenter extends MvpPresenter<MoviesView>
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MoviesPresenter.class.getSimpleName();

    private MoviesNetworkData mMoviesNetworkData = App.getMoviesNetworkData();

    public void fetchMoviesFromApi(String sortOrder, int page) {
        Log.d(TAG, "fetchMoviesFromApi with sortOrder: " + sortOrder);

        getViewState().showLoadingIndicator();

        if (mMoviesNetworkData.isCached(sortOrder, page)) {
            Log.d(TAG, "returns cached movies");
            getViewState().showMovies(mMoviesNetworkData.getMoviesFromCache());
            return;
        }

        mMoviesNetworkData.getMovies(sortOrder, page)
                .doOnError(throwable -> getViewState().showError())
                .subscribe(movies -> {
                    if (movies.isEmpty()) {
                        getViewState().showError();
                    } else {
                        mMoviesNetworkData.setMoviesCache(movies, sortOrder);
                        getViewState().showMovies(movies);
                    }
                }, throwable -> getViewState().showError());
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        getViewState().showLoadingIndicator();
        switch (loaderId) {
            case MoviesActivity.ID_FAVOURITE_MOVIES_LOADER:
                return new CursorLoader(
                        App.getAppContext(),
                        MovieEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        MovieEntry._ID
                );
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0) {
            List<Movie> movies = parseDbResponse(data);
            getViewState().showMovies(movies);
        } else {
            getViewState().showError();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    private List<Movie> parseDbResponse(Cursor cursor) {
        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            long id = cursor.getLong(cursor.getColumnIndex(MovieEntry._ID));
            String title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
            String releaseDate = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
            String posterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
            double voteAverage = cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE));
            String overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));

            movies.add(new Movie(id, title, releaseDate, posterPath, voteAverage, overview));
        }
        return movies;
    }

}
