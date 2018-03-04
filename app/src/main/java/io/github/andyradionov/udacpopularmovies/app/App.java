package io.github.andyradionov.udacpopularmovies.app;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pushtorefresh.storio3.contentresolver.ContentResolverTypeMapping;
import com.pushtorefresh.storio3.contentresolver.StorIOContentResolver;
import com.pushtorefresh.storio3.contentresolver.impl.DefaultStorIOContentResolver;

import io.github.andyradionov.udacpopularmovies.R;
import io.github.andyradionov.udacpopularmovies.data.db.MovieEntity;
import io.github.andyradionov.udacpopularmovies.data.db.MovieEntityStorIOContentResolverDeleteResolver;
import io.github.andyradionov.udacpopularmovies.data.db.MovieEntityStorIOContentResolverGetResolver;
import io.github.andyradionov.udacpopularmovies.data.db.MovieEntityStorIOContentResolverPutResolver;
import io.github.andyradionov.udacpopularmovies.data.db.MoviesRepository;
import io.github.andyradionov.udacpopularmovies.data.network.MoviesApi;
import io.github.andyradionov.udacpopularmovies.data.network.MoviesNetworkData;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Andrey Radionov
 */

public class App extends Application {
    public static final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String SMALL_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w342";
    public static final String BIG_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";

    private static final String TAG = App.class.getSimpleName();

    private static Context context;
    private static String sApiKey;
    private static MoviesApi sMoviesApi;
    private static MoviesNetworkData sMoviesNetworkData;
    private static StorIOContentResolver sStorIOContentResolver;
    private static MoviesRepository sMoviesRepository;


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        App.context = getApplicationContext();
        sApiKey = getString(R.string.api_key);
        sMoviesApi = createApi();
        sMoviesNetworkData = new MoviesNetworkData();
        sStorIOContentResolver = buildContentResolver();
        sMoviesRepository = new MoviesRepository(sStorIOContentResolver);
    }

    public static String getApiKey() {
        return sApiKey;
    }

    public static MoviesApi getMoviesApi() {
        return sMoviesApi;
    }

    public static MoviesNetworkData getMoviesNetworkData() {
        return sMoviesNetworkData;
    }

    public static MoviesRepository getMoviesRepository() {
        return sMoviesRepository;
    }

    /**
     * Check if Internet is Available on device
     *
     * @param context of application
     * @return internet status
     */
    public static boolean isInternetAvailable(Context context) {
        Log.d(TAG, "isInternetAvailable");
        ConnectivityManager mConMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return mConMgr != null
                && mConMgr.getActiveNetworkInfo() != null
                && mConMgr.getActiveNetworkInfo().isAvailable()
                && mConMgr.getActiveNetworkInfo().isConnected();
    }

    private static MoviesApi createApi() {
        Log.d(TAG, "createApi");

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        return new Retrofit.Builder()
                .baseUrl(MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MoviesApi.class);
    }

    private static DefaultStorIOContentResolver buildContentResolver() {
        ContentResolver contentResolver = App.context.getContentResolver();
        return DefaultStorIOContentResolver.builder()
                .contentResolver(contentResolver)
                .addTypeMapping(MovieEntity.class, ContentResolverTypeMapping.<MovieEntity>builder()
                        .putResolver(new MovieEntityStorIOContentResolverPutResolver())
                        .getResolver(new MovieEntityStorIOContentResolverGetResolver())
                        .deleteResolver(new MovieEntityStorIOContentResolverDeleteResolver())
                        .build())
                .build();
    }
}
