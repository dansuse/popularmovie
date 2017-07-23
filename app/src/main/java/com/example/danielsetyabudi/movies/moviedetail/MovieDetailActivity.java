package com.example.danielsetyabudi.movies.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.danielsetyabudi.movies.R;
import com.example.danielsetyabudi.movies.adapter.ReviewsAdapter;
import com.example.danielsetyabudi.movies.adapter.TrailersAdapter;
import com.example.danielsetyabudi.movies.data.MovieRepositories;
import com.example.danielsetyabudi.movies.data.MoviesServiceApiImpl;
import com.example.danielsetyabudi.movies.model.Review;
import com.example.danielsetyabudi.movies.model.Trailer;
import com.example.danielsetyabudi.movies.widgets.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
//di java, tidak dapat implement interface yang di deklarasi di
//class itu sendiri
public class MovieDetailActivity
        extends AppCompatActivity implements MovieDetailContract.View
{

    public interface TrailerItemListener{
        void onTrailerClick(Uri uriToWatchTrailer);
    }
    TrailerItemListener mTrailerItemListener = new TrailerItemListener() {
        @Override
        public void onTrailerClick(Uri uriToWatchTrailer) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uriToWatchTrailer);
            startActivity(intent);
        }
    };
    private static final String EXTRA_MOVIE_ID = "extra_movie_id";
    private static final String EXTRA_MOVIE_MODE = "extra_movie_mode";
    private static final String DIALOG_MOVIE_POSTER_TAG = "dialog_movie_poster_tag";
    private MovieDetailContract.UserActionsListener mActionsListener;

    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private MenuItem mFavMenuItem;

    @Override
    public void showDialogFragmentMoviePoster(String imageUrl) {
        MoviePosterDialogFragment moviePosterDialogFragment = MoviePosterDialogFragment.newInstance(imageUrl);
        moviePosterDialogFragment.show(getSupportFragmentManager(), DIALOG_MOVIE_POSTER_TAG);
    }

    @BindView(R.id.tv_movie_title) TextView mMovieTitleTextView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_synopsis) TextView mSynopsisTextView;
    @BindView(R.id.tv_user_rating) TextView mUserRatingTextView;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.square_iv_movie_poster) SquareImageView mMoviePosterSquareImageView;
    @BindView(R.id.iv_movie_poster) ImageView mMoviePosterImageView;
    @BindView(R.id.cv_trailers) CardView mTrailersCardView;
    @BindView(R.id.rv_trailers) RecyclerView mTrailersRecyclerView;
    @BindView(R.id.rv_reviews) RecyclerView mReviewsRecyclerView;
    @BindView(R.id.ll_reviews) LinearLayout mReviewsLinearLayout;

    @OnClick(R.id.iv_movie_poster)
    public void klikMoviePosterImageView(ImageView imageView){
        mActionsListener.handleKlikMoviePosterImageView();
    }

    public static Intent newIntent(Context context, int movieId, int movieMode){
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movieId);
        intent.putExtra(EXTRA_MOVIE_MODE, movieMode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mActionsListener = new MovieDetailPresenter(MovieRepositories.getInMemoryRepoInstance(new MoviesServiceApiImpl()), this, this);
        mTrailersAdapter = new TrailersAdapter(this, mTrailerItemListener);
        mReviewsAdapter = new ReviewsAdapter();

        mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);
        mTrailersRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mTrailersRecyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL);
        mTrailersRecyclerView.addItemDecoration(dividerItemDecoration);

        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra(EXTRA_MOVIE_ID) && intent.hasExtra(EXTRA_MOVIE_MODE)){
                int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1);
                int movieMode = intent.getIntExtra(EXTRA_MOVIE_MODE, -1);
                if(movieId != -1 && movieMode != -1){
                    mActionsListener.openMovie(movieMode, movieId);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        mFavMenuItem = menu.findItem(R.id.action_favorite);
        mActionsListener.checkIfMovieIsFavorite();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_favorite:
                mActionsListener.setMovieAsFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void turnFavorite(boolean favorite) {
        mFavMenuItem.setIcon((favorite) ? R.drawable.ic_favorite_on : R.drawable.ic_favorite_off);
    }

    @Override
    public void showTrailers(List<Trailer> trailers) {
        mTrailersCardView.setVisibility(View.VISIBLE);
        mTrailersAdapter.replaceData(trailers);
    }

    @Override
    public void showReviews(List<Review> reviews) {
        mReviewsLinearLayout.setVisibility(View.VISIBLE);
        mReviewsAdapter.replaceData(reviews);
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setSubtitle(String subtitle) {
        getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void showMovieTitle(String title) {
        mMovieTitleTextView.setVisibility(View.VISIBLE);
        mMovieTitleTextView.setText(title);
    }

    @Override
    public void hideMovieTitle() {
        mMovieTitleTextView.setVisibility(View.GONE);
    }

    @Override
    public void showSynopsis(String synopsis) {
        mSynopsisTextView.setVisibility(View.VISIBLE);
        mSynopsisTextView.setText(getString(R.string.synopsis, synopsis));
    }

    @Override
    public void hideSynopsis() {
        mSynopsisTextView.setVisibility(View.GONE);
    }

    @Override
    public void showReleaseDate(String releaseDate) {
        mReleaseDateTextView.setVisibility(View.VISIBLE);
        mReleaseDateTextView.setText(getString(R.string.release_date, releaseDate));
    }

    @Override
    public void hideReleaseDate() {
        mReleaseDateTextView.setVisibility(View.GONE);
    }

    @Override
    public void showUserRating(String userRating) {
        mUserRatingTextView.setVisibility(View.VISIBLE);
        mUserRatingTextView.setText(getString(R.string.user_rating, userRating));
    }

    @Override
    public void hideUserRating() {
        mUserRatingTextView.setVisibility(View.GONE);
    }

    @Override
    public void showMoviePoster(String imageUrl) {
        mMoviePosterImageView.setVisibility(View.VISIBLE);
        Picasso.with(this).load(imageUrl).into(mMoviePosterImageView);
        Picasso.with(this).load(imageUrl).into(mMoviePosterSquareImageView);
    }

    @Override
    public void hideMoviePoster() {
        mMoviePosterImageView.setVisibility(View.GONE);
    }
}
