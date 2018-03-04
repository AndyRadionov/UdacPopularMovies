package io.github.andyradionov.udacpopularmovies.data.db;

import com.pushtorefresh.storio3.contentresolver.annotations.StorIOContentResolverColumn;
import com.pushtorefresh.storio3.contentresolver.annotations.StorIOContentResolverType;

import static io.github.andyradionov.udacpopularmovies.data.db.MoviesContract.MovieEntry;

/**
 * Entity class for Storio library
 *
 * @author Andrey Radionov
 */

@StorIOContentResolverType(uri = MoviesContract.STRING_CONTENT_URI)
public class MovieEntity {

    @StorIOContentResolverColumn(name = MovieEntry._ID, key = true)
    long id;

    @StorIOContentResolverColumn(name = MovieEntry.COLUMN_TITLE)
    String title;

    @StorIOContentResolverColumn(name = MovieEntry.COLUMN_RELEASE_DATE)
    String releaseDate;

    @StorIOContentResolverColumn(name = MovieEntry.COLUMN_POSTER_PATH)
    String posterPath;

    @StorIOContentResolverColumn(name = MovieEntry.COLUMN_VOTE_AVERAGE)
    double voteAverage;

    @StorIOContentResolverColumn(name = MovieEntry.COLUMN_OVERVIEW)
    String overview;

    MovieEntity() {
    }

    public MovieEntity(long id, String title, String releaseDate, String posterPath, double voteAverage, String overview) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieEntity that = (MovieEntity) o;

        if (id != that.id) return false;
        if (Double.compare(that.voteAverage, voteAverage) != 0) return false;
        if (!title.equals(that.title)) return false;
        if (!releaseDate.equals(that.releaseDate)) return false;
        if (!posterPath.equals(that.posterPath)) return false;
        return overview.equals(that.overview);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + releaseDate.hashCode();
        result = 31 * result + posterPath.hashCode();
        temp = Double.doubleToLongBits(voteAverage);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + overview.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MovieEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", voteAverage=" + voteAverage +
                ", overview='" + overview + '\'' +
                '}';
    }
}
