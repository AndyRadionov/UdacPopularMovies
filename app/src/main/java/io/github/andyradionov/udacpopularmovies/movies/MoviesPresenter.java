package io.github.andyradionov.udacpopularmovies.movies;

import android.util.Log;

import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.MoviesData;

/**
 * @author Andrey Radionov
 */

public class MoviesPresenter implements MoviesContract.Presenter {

    private static final String TAG = MoviesPresenter.class.getSimpleName();

    private MoviesContract.View mMoviesView;
    private MoviesData mMoviesData = App.getMoviesData();

    public MoviesPresenter(MoviesContract.View moviesView) {
        Log.d(TAG, "MoviesPresenter constructor call");
        mMoviesView = moviesView;
    }

    @Override
    public void loadMovies(String sortOrder) {
        Log.d(TAG, "loadMovies with sortOrder: " + sortOrder);

        mMoviesView.showLoadingIndicator();

        if (mMoviesData.isCached(sortOrder)) {
            Log.d(TAG, "returns cached movies");
            mMoviesView.showMovies(mMoviesData.getMoviesFromCache());
            return;
        }

        mMoviesData.getMovies(sortOrder)
                .doOnError(throwable -> mMoviesView.showError())
                .subscribe(movies -> {
                    if (movies.isEmpty()) {
                        mMoviesView.showError();
                    } else {
                        mMoviesData.setCache(movies, sortOrder);
                        mMoviesView.showMovies(movies);
                    }
                }, throwable -> mMoviesView.showError());
    }
}
