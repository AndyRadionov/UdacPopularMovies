package io.github.andyradionov.udacpopularmovies.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.github.andyradionov.udacpopularmovies.R;
import io.github.andyradionov.udacpopularmovies.data.network.MoviesApi;
import io.github.andyradionov.udacpopularmovies.data.network.MoviesData;
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

    private static MoviesApi sMoviesApi;
    private static MoviesData sMoviesData;
    private static String sApiKey;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        sMoviesApi = createApi();
        sMoviesData = new MoviesData();
        sApiKey = getString(R.string.api_key);
    }

    public static MoviesApi getMoviesApi() {
        return sMoviesApi;
    }

    public static MoviesData getMoviesData() {
        return sMoviesData;
    }

    public static String getApiKey() {
        return sApiKey;
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
}
