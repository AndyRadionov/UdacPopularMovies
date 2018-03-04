package io.github.andyradionov.udacpopularmovies.moviedetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.andyradionov.udacpopularmovies.R;
import io.github.andyradionov.udacpopularmovies.app.App;
import io.github.andyradionov.udacpopularmovies.data.model.Movie;

public class MovieDetailsActivity extends MvpAppCompatActivity implements MovieDetailsView {

    public static final String MOVIE_EXTRA = "movie_extra";
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @BindView(R.id.iv_poster)
    ImageView mPoster;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDate;
    @BindView(R.id.tv_vote_average)
    TextView mVoteAverage;
    @BindView(R.id.tv_overview)
    TextView mOverview;

    @InjectPresenter
    MovieDetailsPresenter mPresenter;
    private Unbinder mUnbinder;
    private Movie mMovie;
    private boolean mIsFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mUnbinder = ButterKnife.bind(this);
        setupViews(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        outState.putParcelable(MOVIE_EXTRA, mMovie);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            finish();
        } else if (item.getItemId() == R.id.btn_favourite) {
            mIsFavourite = !mIsFavourite;
            mPresenter.setMovieFavourite(mMovie, mIsFavourite);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public void setFavouriteIcon(boolean isFavourite) {
        Log.d(TAG, "setFavouriteIcon: " + isFavourite);
        mIsFavourite = isFavourite;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu");
        int iconId = mIsFavourite ? R.drawable.ic_favourite : R.drawable.ic_not_favourite;
        menu.findItem(R.id.btn_favourite).setIcon(iconId);
        return super.onPrepareOptionsMenu(menu);
    }

    private void setupViews(Bundle savedInstanceState) {
        Log.d(TAG, "setupViews");

        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(MOVIE_EXTRA);
        }

        if (mMovie == null) {
            Intent startIntent = getIntent();
            mMovie = startIntent.getParcelableExtra(MOVIE_EXTRA);
        }

        setTitle(mMovie.getTitle());

        Picasso.with(this)
                .load(App.BIG_IMAGE_BASE_URL + mMovie.getPosterPath())
                .into(mPoster);

        mReleaseDate.setText(mMovie.getReleaseDate());
        mVoteAverage.setText(String.valueOf(mMovie.getVoteAverage()));
        mOverview.setText(mMovie.getOverview());

        mPresenter.checkIsFavorite(mMovie.getId());
    }
}
