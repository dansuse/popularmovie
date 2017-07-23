package com.example.danielsetyabudi.movies.moviedetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.danielsetyabudi.movies.R;
import com.example.danielsetyabudi.movies.adapter.ReviewsAdapter;
import com.example.danielsetyabudi.movies.adapter.TrailersAdapter;
import com.example.danielsetyabudi.movies.contentprovider.MovieContract;
import com.example.danielsetyabudi.movies.data.MovieRepositories;
import com.example.danielsetyabudi.movies.data.MoviesServiceApiImpl;
import com.example.danielsetyabudi.movies.model.Movie;
import com.example.danielsetyabudi.movies.model.Review;
import com.example.danielsetyabudi.movies.model.Trailer;
import com.example.danielsetyabudi.movies.util.CursorToList;
import com.example.danielsetyabudi.movies.widgets.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_FAVORITE;

//di java, tidak dapat implement interface yang di deklarasi di
//class itu sendiri
public class MovieDetailActivity
        extends AppCompatActivity
        implements MovieDetailContract.View,
        LoaderManager.LoaderCallbacks<Cursor>
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

    private int mMovieId;

    private Movie mMovie;
    private List<Trailer> mTrailerList = new ArrayList<>();
    private List<Review> mReviewList = new ArrayList<>();

    private ProgressDialog progressDialog;

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


    private static final int ID_MOVIE_LOADER = 45;
    private static final int ID_TRAILER_LOADER = 46;
    private static final int ID_REVIEW_LOADER = 47;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == ID_MOVIE_LOADER){
            Uri uri = MovieContract.MovieEntry.buildMovieUri(mMovieId);
            return new CursorLoader(this, uri, null, null, null, null);
        }else if(id == ID_REVIEW_LOADER){
            Uri uri = MovieContract.ReviewEntry.buildReviewUri(mMovieId);
            return new CursorLoader(this, uri, null, null, null, null);
        }else if(id == ID_TRAILER_LOADER){
            Uri uri = MovieContract.TrailerEntry.buildTrailerUri(mMovieId);
            return new CursorLoader(this, uri, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId() == ID_MOVIE_LOADER){
            if(data != null && !data.isClosed() && data.moveToFirst()){
                String originalTitle = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
                String releaseDate = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                String overview = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
                String posterPath = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
                String userRating = data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_USER_RATING));
                int _id = data.getInt(data.getColumnIndex(MovieContract.MovieEntry._ID));
                setTitle(originalTitle);
                showMovieTitle(originalTitle);
                showReleaseDate(releaseDate);
                showUserRating(userRating);
                showSynopsis(overview);
                Movie movie = new Movie();
                movie.setPosterPath(posterPath);
                movie.setId(_id);
                movie.setOriginalTitle(originalTitle);
                movie.setUserRating(userRating);
                movie.setOverview(overview);
                movie.setReleaseDate(releaseDate);
                mMovie = movie;
                showMoviePoster(movie.getPosterPath());
            }
        }else if(loader.getId() == ID_REVIEW_LOADER){
            if(data != null && !data.isClosed() && data.moveToFirst() && data.getCount() > 0){
                mReviewList = CursorToList.convertCursorToReviewList(data);
                mReviewsLinearLayout.setVisibility(View.VISIBLE);
            }
            mReviewsAdapter.swapCursor(data);
        }else if(loader.getId() == ID_TRAILER_LOADER){
            if(data != null && !data.isClosed() && data.moveToFirst() && data.getCount() > 0){
                mTrailerList = CursorToList.convertCursorToTrailerList(data);
                mTrailersCardView.setVisibility(View.VISIBLE);
            }
            mTrailersAdapter.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId() == ID_MOVIE_LOADER){

        }else if(loader.getId() == ID_REVIEW_LOADER){
            mReviewsAdapter.swapCursor(null);
        }else if(loader.getId() == ID_TRAILER_LOADER){
            mTrailersAdapter.swapCursor(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        //alasan mengapa setTitle dengan string kosong adalah
        //karena jika tidak, maka ketika loader selesai load, title tidak
        //akan keganti menjadi judul film
        //https://stackoverflow.com/questions/26486730/in-android-app-toolbar-settitle-method-has-no-effect-application-name-is-shown
        mToolbar.setTitle("");
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

        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        mReviewsRecyclerView.setHasFixedSize(true);


        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");


        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra(EXTRA_MOVIE_ID) && intent.hasExtra(EXTRA_MOVIE_MODE)){
                int movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1);
                int movieMode = intent.getIntExtra(EXTRA_MOVIE_MODE, -1);
                if(movieId != -1 && movieMode != -1){
                    mMovieId = movieId;
                    if(movieMode == MODE_FAVORITE){
                        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
                        getSupportLoaderManager().initLoader(ID_REVIEW_LOADER, null, this);
                        getSupportLoaderManager().initLoader(ID_TRAILER_LOADER, null, this);
                    }else{
                        mActionsListener.openMovie(movieMode, movieId);
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        mFavMenuItem = menu.findItem(R.id.action_favorite);
        mActionsListener.checkIfMovieIsFavorite(mMovieId);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_favorite:
                mActionsListener.setMovieAsFavorite(mMovie, mTrailerList, mReviewList);
                showProgressDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void turnFavorite(boolean favorite) {
        hideProgressDialog();
        mFavMenuItem.setIcon((favorite) ? R.drawable.ic_favorite_on : R.drawable.ic_favorite_off);
    }

    private void showProgressDialog(){
        mFavMenuItem.setEnabled(false);
        progressDialog.show();
    }

    private void hideProgressDialog(){
        mFavMenuItem.setEnabled(true);
        progressDialog.dismiss();
    }


    @Override
    public void setMovie(Movie movie) {
        mMovie = movie;
    }

    @Override
    public void showTrailers(List<Trailer> trailers) {
        mTrailerList = trailers;
        mTrailersCardView.setVisibility(View.VISIBLE);
        mTrailersAdapter.replaceData(trailers);
    }

    @Override
    public void showReviews(List<Review> reviews) {
        mReviewList = reviews;
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
