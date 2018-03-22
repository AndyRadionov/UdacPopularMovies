package io.github.andyradionov.udacpopularmovies.data.network;

import android.text.TextUtils;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.model.Movie;
import io.github.andyradionov.udacpopularmovies.data.model.MovieReview;
import io.github.andyradionov.udacpopularmovies.data.model.MovieYoutubeTrailer;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Andrey Radionov
 */

public class MoviesNetworkData {
    private static final String TAG = MoviesNetworkData.class.getSimpleName();

    private MoviesApi mMoviesApi;
    private List<Movie> mCachedMovies;
    private String mCachedSortOrder;
    private long mCachedTrailersMovieId;
    private long mCachedReviewsMovieId;
    private MovieYoutubeTrailer[] mCachedTrailers;
    private MovieReview[] mCachedReviews;
    private int currentPage;

    public MoviesNetworkData() {
        Log.d(TAG, "MoviesNetworkData constructor call");
        mMoviesApi = App.getMoviesApi();
        mCachedMovies = Collections.emptyList();
    }

    public Observable<List<Movie>> getMovies(String sortOrder, int page) {
        Log.d(TAG, "getMovies sorted by: " + sortOrder);
        currentPage = page;
        return mMoviesApi.getMovies(sortOrder, App.getApiKey(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(GetMoviesDto::getResults);
    }

    public boolean isCached(String sortOrder, int page) {
        return currentPage == page
                && !TextUtils.isEmpty(mCachedSortOrder)
                && mCachedSortOrder.equals(sortOrder)
                && mCachedMovies != null
                && !mCachedMovies.isEmpty();
    }


    public Observable<MovieYoutubeTrailer[]> getTrailers(long movieId) {
        Log.d(TAG, "getTrailers for movie: " + movieId);
        return mMoviesApi.getMovieTrailers(movieId, App.getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(GetTrailersDto::getTrailers);
    }

    public Observable<MovieReview[]> getReviews(long movieId) {
        Log.d(TAG, "getReviews for movie: " + movieId);
        return mMoviesApi.getMovieReviews(movieId, App.getApiKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(GetReviewsDto::getReviews);
    }

    public void setMoviesCache(List<Movie> movies, String sortOrder) {
        mCachedMovies = movies;
        mCachedSortOrder = sortOrder;
    }

    public List<Movie> getMoviesFromCache() {
        return mCachedMovies;
    }

    public void setTrailersCache(long movieId, MovieYoutubeTrailer[] trailers) {
        mCachedTrailersMovieId = movieId;
        mCachedTrailers = trailers;
    }

    public MovieYoutubeTrailer[] getCachedTrailers(long movieId) {
        if (movieId == mCachedTrailersMovieId) {
            return mCachedTrailers;
        }
        return null;
    }

    public void setReviewsCache(long movieId, MovieReview[] reviews) {
        mCachedReviewsMovieId = movieId;
        mCachedReviews = reviews;
    }

    public MovieReview[] getCachedReviews(long movieId) {
        if (movieId == mCachedReviewsMovieId) {
            return mCachedReviews;
        }
        return null;
    }
}
