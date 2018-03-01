package io.github.andyradionov.udacpopularmovies.data.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Andrey Radionov
 */

public interface MoviesApi {

    @GET("{sort_order}")
    Observable<GetMoviesDto> getMovies(@Path("sort_order") String sortOrder,
                                       @Query("api_key") String apiKey);

}
