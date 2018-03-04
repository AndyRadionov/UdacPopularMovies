package io.github.andyradionov.udacpopularmovies.movies;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import java.util.List;

import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.db.MoviesRepository;
import io.github.andyradionov.udacpopularmovies.data.model.Movie;
import io.github.andyradionov.udacpopularmovies.data.network.MoviesNetworkData;

/**
 * @author Andrey Radionov
 */

@InjectViewState
public class MoviesPresenter extends MvpPresenter<MoviesView> {

    private static final String TAG = MoviesPresenter.class.getSimpleName();

    private MoviesNetworkData mMoviesNetworkData = App.getMoviesNetworkData();
    private MoviesRepository mMoviesRepository = App.getMoviesRepository();

    public void fetchMoviesFromApi(String sortOrder) {
        Log.d(TAG, "fetchMoviesFromApi with sortOrder: " + sortOrder);

        getViewState().showLoadingIndicator();

        if (mMoviesNetworkData.isCached(sortOrder)) {
            Log.d(TAG, "returns cached movies");
            getViewState().showMovies(mMoviesNetworkData.getMoviesFromCache());
            return;
        }

        mMoviesNetworkData.getMovies(sortOrder)
                .doOnError(throwable -> getViewState().showError())
                .subscribe(movies -> {
                    if (movies.isEmpty()) {
                        getViewState().showError();
                    } else {
                        mMoviesNetworkData.setCache(movies, sortOrder);
                        getViewState().showMovies(movies);
                    }
                }, throwable -> getViewState().showError());
    }

    public void showError() {
        Log.d(TAG, "showError");
        getViewState().showError();
    }

    public void loadFavouriteMovies() {
        Log.d(TAG, "loadFavouriteMovies");
        getViewState().showLoadingIndicator();
        mMoviesRepository.getFavouriteMovies(this);
    }

    public void showFavouriteMovies(List<Movie> movies) {
        Log.d(TAG, "showFavouriteMovies: " + movies);
        if (movies == null || movies.isEmpty()) {
            getViewState().showError();
        } else {
            getViewState().showMovies(movies);
        }
    }
}
