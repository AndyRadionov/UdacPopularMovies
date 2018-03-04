package io.github.andyradionov.udacpopularmovies.data.db;


import android.content.ContentUris;
import android.net.Uri;
import android.util.Log;

import com.pushtorefresh.storio3.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio3.contentresolver.queries.Query;

import java.util.ArrayList;
import java.util.List;

import io.github.andyradionov.udacpopularmovies.data.model.Movie;
import io.github.andyradionov.udacpopularmovies.moviedetails.MovieDetailsPresenter;
import io.github.andyradionov.udacpopularmovies.movies.MoviesPresenter;
import io.reactivex.BackpressureStrategy;

import static io.github.andyradionov.udacpopularmovies.data.db.MoviesContract.MovieEntry;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

/**
 * @author Andrey Radionov
 */

public class MoviesRepository {

    private static final String TAG = MoviesRepository.class.getSimpleName();

    private StorIOContentResolver mStorIOContentResolver;

    public MoviesRepository(StorIOContentResolver storIOContentResolver) {
        mStorIOContentResolver = storIOContentResolver;
    }

    public void addMovieToFavourite(MovieDetailsPresenter presenter, Movie movie) {
        Log.d(TAG, "addMovieToFavourite: " + movie);
        mStorIOContentResolver
                .put()
                .object(convertMovieToEntity(movie))
                .prepare()
                .asRxSingle()
                .subscribe(putResult -> presenter.showIconFavourite(putResult.wasInserted()));
    }

    public void deleteMovieFromFavourite(MovieDetailsPresenter presenter, Movie movie) {
        Log.d(TAG, "deleteMovieFromFavourite: " + movie);
        mStorIOContentResolver
                .delete()
                .object(convertMovieToEntity(movie))
                .prepare()
                .asRxCompletable()
                .doOnError(th -> Log.d(TAG, "Error during removing movie: " + movie))
                .subscribe(() -> presenter.showIconFavourite(false),
                        throwable -> Log.d(TAG, "Error during removing movie: " + movie));
    }

    public void isMovieFavourite(MovieDetailsPresenter presenter, long movieId) {
        Log.d(TAG, "isMovieFavourite: " + movieId);
        Uri MovieUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, movieId);
        mStorIOContentResolver
                .get()
                .object(MovieEntity.class)
                .withQuery(Query.builder()
                        .uri(MovieUri)
                        .build())
                .prepare()
                .asRxFlowable(BackpressureStrategy.LATEST)
                .take(1)
                .observeOn(mainThread())
                .doOnError(th -> Log.d(TAG, "Error isMovieFavourite: " + movieId))
                .subscribe(entityOptional -> presenter.showIconFavourite(entityOptional.isPresent()),
                        th -> Log.d(TAG, "Error isMovieFavourite: " + movieId));
    }

    public void getFavouriteMovies(MoviesPresenter presenter) {
        Log.d(TAG, "getFavouriteMovies");
        mStorIOContentResolver
                .get()
                .listOfObjects(MovieEntity.class)
                .withQuery(Query.builder()
                        .uri(MoviesContract.STRING_CONTENT_URI)
                        .build())
                .prepare()
                .asRxFlowable(BackpressureStrategy.LATEST)
                .take(1)
                .observeOn(mainThread())
                .doOnError(throwable -> presenter.showError())
                .map(this::mapEntitiesToMovies)
                .subscribe(presenter::showFavouriteMovies,
                        throwable -> presenter.showError());
    }

    private List<Movie> mapEntitiesToMovies(List<MovieEntity> entities) {
        List<Movie> movies = new ArrayList<>(entities.size());
        for (MovieEntity entity : entities) {
            movies.add(convertEntityToMovie(entity));
        }
        return movies;
    }

    private Movie convertEntityToMovie(MovieEntity entity) {
        return new Movie(
                entity.id,
                entity.title,
                entity.releaseDate,
                entity.posterPath,
                entity.voteAverage,
                entity.overview
        );
    }

    private MovieEntity convertMovieToEntity(Movie movie) {
        return new MovieEntity(
                movie.getId(),
                movie.getTitle(),
                movie.getReleaseDate(),
                movie.getPosterPath(),
                movie.getVoteAverage(),
                movie.getOverview()
        );
    }
}
