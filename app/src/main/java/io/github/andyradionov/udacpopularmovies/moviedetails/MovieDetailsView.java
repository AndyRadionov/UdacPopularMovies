package io.github.andyradionov.udacpopularmovies.moviedetails;

import com.arellomobile.mvp.MvpView;

import io.github.andyradionov.udacpopularmovies.data.model.MovieReview;
import io.github.andyradionov.udacpopularmovies.data.model.MovieYoutubeTrailer;

/**
 * @author Andrey Radionov
 */

public interface MovieDetailsView extends MvpView {

    void setFavouriteIcon(boolean isFavourite);

    void showTrailers(MovieYoutubeTrailer[] trailers);

    void hideTrailers();

    void showReviews(MovieReview[] reviews);

    void hideReviews();
}