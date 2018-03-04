package io.github.andyradionov.udacpopularmovies.moviedetails;

import com.arellomobile.mvp.MvpView;

/**
 * @author Andrey Radionov
 */

public interface MovieDetailsView extends MvpView {

    void setFavouriteIcon(boolean isFavourite);
}