package io.github.andyradionov.udacpopularmovies.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.andyradionov.udacpopularmovies.R;
import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.model.Movie;
import io.github.andyradionov.udacpopularmovies.moviedetails.MovieDetailsActivity;

public class MoviesActivity extends MvpAppCompatActivity implements
        AdapterView.OnItemSelectedListener,
        MoviesAdapter.OnItemClickListener,
        MoviesView {

    public static final int ID_FAVOURITE_MOVIES_LOADER = 42;
    private static final String TAG = MoviesActivity.class.getSimpleName();
    private static final String SORT_ORDER = "sort_order";
    private static final int PORTRAIT_COLUMNS = 2;
    private static final int LANDSCAPE_COLUMNS = 3;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.tv_error)
    TextView mErrorDisplay;

    @BindView(R.id.rv_movies_container)
    RecyclerView mMoviesContainer;

    @InjectPresenter
    MoviesPresenter mPresenter;
    private MoviesAdapter mMoviesAdapter;
    private Unbinder mUnbinder;
    private String[] mSortKeys;
    private int mSortOrder;
    private boolean mIsSpinnerLoaded;
    private String mFavouriteKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mUnbinder = ButterKnife.bind(this);

        mSortKeys = getResources().getStringArray(R.array.movies_sort_types_keys);
        mSortOrder = savedInstanceState == null ? 0 : savedInstanceState.getInt(SORT_ORDER);

        mFavouriteKey = getString(R.string.favourite_key);
        setupRecycler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(SORT_ORDER, mSortOrder);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.movies_menu, menu);

        MenuItem item = menu.findItem(R.id.sp_movies_sort);
        Spinner mMoviesSortSpinner = (Spinner) item.getActionView();
        mMoviesSortSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movies_sort_types_values, R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner);

        mMoviesSortSpinner.setAdapter(adapter);
        mMoviesSortSpinner.setSelection(mSortOrder);
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d(TAG, "onItemSelected position: " + position);
        if (!mIsSpinnerLoaded) {
            mIsSpinnerLoaded = true;
            return;
        }
        mSortOrder = position;
        loadMovies();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public void onMovieItemClick(Movie movie) {
        Log.d(TAG, "onMovieItemClick: " + movie.getTitle());
        Intent showDetailsIntent = new Intent(this, MovieDetailsActivity.class);
        showDetailsIntent.putExtra(MovieDetailsActivity.MOVIE_EXTRA, movie);
        startActivity(showDetailsIntent);
    }

    @Override
    public void showLoadingIndicator() {
        Log.d(TAG, "showLoadingIndicator");
        setViewsVisibility(View.VISIBLE, View.INVISIBLE, View.GONE);
    }

    @Override
    public void showMovies(List<Movie> movies) {
        Log.d(TAG, "showMovies");
        mMoviesAdapter.setData(movies);
        setViewsVisibility(View.GONE, View.VISIBLE, View.GONE);
    }

    @Override
    public void showError() {
        Log.d(TAG, "showError");
        setViewsVisibility(View.GONE, View.GONE, View.VISIBLE);
    }

    private void loadMovies() {
        Log.d(TAG, "loadMovies");
        getSupportLoaderManager().destroyLoader(ID_FAVOURITE_MOVIES_LOADER);
        if (mSortKeys[mSortOrder].equals(mFavouriteKey)) {
            getSupportLoaderManager().initLoader(ID_FAVOURITE_MOVIES_LOADER, null, mPresenter);
        } else {
            fetchMoviesFromApi();
        }
    }

    private void setupRecycler() {
        Log.d(TAG, "setupRecycler");
        mMoviesAdapter = new MoviesAdapter(this);
        mMoviesContainer.setAdapter(mMoviesAdapter);

        int orientation = getResources().getConfiguration().orientation;
        int spanCount = orientation == 1 ? PORTRAIT_COLUMNS : LANDSCAPE_COLUMNS;
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        mMoviesContainer.setLayoutManager(layoutManager);
    }

    private void fetchMoviesFromApi() {
        Log.d(TAG, "fetchMoviesFromApi");
        if (!App.isInternetAvailable(this)) {
            showError();
            return;
        }
        mPresenter.fetchMoviesFromApi(mSortKeys[mSortOrder]);
    }

    private void setViewsVisibility(int progressBar, int recycler, int errorText) {
        Log.d(TAG, "setViewsVisibility");
        mLoadingIndicator.setVisibility(progressBar);
        mMoviesContainer.setVisibility(recycler);
        mErrorDisplay.setVisibility(errorText);
    }
}
