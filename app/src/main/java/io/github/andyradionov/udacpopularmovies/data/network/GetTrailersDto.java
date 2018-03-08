package io.github.andyradionov.udacpopularmovies.data.network;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import io.github.andyradionov.udacpopularmovies.data.model.MovieYoutubeTrailer;

/**
 * @author Andrey Radionov
 */

public class GetTrailersDto {
    private long id;
    @SerializedName("youtube")
    private MovieYoutubeTrailer[] trailers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MovieYoutubeTrailer[] getTrailers() {
        return trailers;
    }

    public void setTrailers(MovieYoutubeTrailer[] trailers) {
        this.trailers = trailers;
    }

    @Override
    public String toString() {
        return "GetTrailersDto{" +
                "id=" + id +
                ", trailers=" + Arrays.toString(trailers) +
                '}';
    }
}
