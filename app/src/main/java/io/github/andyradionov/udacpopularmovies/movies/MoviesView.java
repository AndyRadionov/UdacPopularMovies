package io.github.andyradionov.udacpopularmovies.movies;

import com.arellomobile.mvp.MvpView;

import java.util.List;

import io.github.andyradionov.udacpopularmovies.data.model.Movie;

/**
 * @author Andrey Radionov
 */

public interface MoviesView extends MvpView {

    void showLoadingIndicator();

    void showMovies(List<Movie> movies);

    void showError();
}
