package io.github.andyradionov.udacpopularmovies.movies;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.network.MoviesData;

/**
 * @author Andrey Radionov
 */

@InjectViewState
public class MoviesPresenter extends MvpPresenter<MoviesView> {

    private static final String TAG = MoviesPresenter.class.getSimpleName();

    private MoviesData mMoviesData = App.getMoviesData();

    public void loadMovies(String sortOrder) {
        Log.d(TAG, "loadMovies with sortOrder: " + sortOrder);

        getViewState().showLoadingIndicator();

        if (mMoviesData.isCached(sortOrder)) {
            Log.d(TAG, "returns cached movies");
            getViewState().showMovies(mMoviesData.getMoviesFromCache());
            return;
        }

        mMoviesData.getMovies(sortOrder)
                .doOnError(throwable -> getViewState().showError())
                .subscribe(movies -> {
                    if (movies.isEmpty()) {
                        getViewState().showError();
                    } else {
                        mMoviesData.setCache(movies, sortOrder);
                        getViewState().showMovies(movies);
                    }
                }, throwable -> getViewState().showError());
    }
}
