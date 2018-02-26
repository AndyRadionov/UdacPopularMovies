package io.github.andyradionov.udacpopularmovies.data;

import android.text.TextUtils;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import io.github.andyradionov.udacpopularmovies.app.App;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Andrey Radionov
 */

public class MoviesData {
    private static final String TAG = MoviesData.class.getSimpleName();

    private MoviesApi mMoviesApi;
    private List<Movie> mCachedMovies;
    private String mCachedSortOrder;

    public MoviesData() {
        Log.d(TAG, "MoviesData constructor call");
        mMoviesApi = App.getMoviesApi();
        mCachedMovies = Collections.emptyList();
    }

    public Observable<List<Movie>> getMovies(String sortOrder) {
        Log.d(TAG, "getMovies sorted by: " + sortOrder);
        return mMoviesApi.getMovies(sortOrder, App.getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(GetMoviesDto::getResults);
    }

    public boolean isCached(String sortOrder) {
        return !TextUtils.isEmpty(mCachedSortOrder)
                && mCachedSortOrder.equals(sortOrder)
                && mCachedMovies != null
                && !mCachedMovies.isEmpty();
    }

    public List<Movie> getMoviesFromCache() {
        return mCachedMovies;
    }

    public void setCache(List<Movie> movies, String sortOrder) {
        mCachedMovies = movies;
        mCachedSortOrder = sortOrder;
    }
}
