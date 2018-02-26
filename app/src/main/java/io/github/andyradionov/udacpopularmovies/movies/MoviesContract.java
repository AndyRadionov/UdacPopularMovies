package io.github.andyradionov.udacpopularmovies.movies;

import java.util.List;

import io.github.andyradionov.udacpopularmovies.data.Movie;

/**
 * @author Andrey Radionov
 */

public interface MoviesContract {

    interface View {

        void showLoadingIndicator();

        void showMovies(List<Movie> movies);

        void showError();
    }

    interface Presenter {

        void loadMovies(String sortOrder);
    }
}
