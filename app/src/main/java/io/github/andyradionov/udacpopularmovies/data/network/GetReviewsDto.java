package io.github.andyradionov.udacpopularmovies.data.network;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

import io.github.andyradionov.udacpopularmovies.data.model.MovieReview;

/**
 * @author Andrey Radionov
 */

public class GetReviewsDto {

    private long id;
    private int page;
    private int totalPages;
    private int totalResults;
    @SerializedName("results")
    private MovieReview[] reviews;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public MovieReview[] getReviews() {
        return reviews;
    }

    public void setReviews(MovieReview[] reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "GetReviewsDto{" +
                "id=" + id +
                ", page=" + page +
                ", totalPages=" + totalPages +
                ", totalResults=" + totalResults +
                ", reviews=" + Arrays.toString(reviews) +
                '}';
    }
}
