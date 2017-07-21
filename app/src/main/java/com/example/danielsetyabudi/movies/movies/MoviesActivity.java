package com.example.danielsetyabudi.movies.movies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.danielsetyabudi.movies.R;
import com.example.danielsetyabudi.movies.data.Movie;
import com.example.danielsetyabudi.movies.data.MovieRepositories;
import com.example.danielsetyabudi.movies.data.MoviesServiceApiImpl;
import com.example.danielsetyabudi.movies.moviedetail.MovieDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_POPULAR;
import static com.example.danielsetyabudi.movies.util.MovieConstant.MODE_TOP_RATED;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.View {

    private static final String SAVE_STATE_LAST_VISIBLE_ITEM = "save_state_last_visible_item";
    private static final String SAVE_STATE_MOVIE_MODE = "save_state_movie_mode";

    private MoviesAdapter mMoviesAdapter;
    private ArrayAdapter<CharSequence> mSpinnerAdapter;
    private MoviesContract.UserActionsListener mActionsListener;

    private int mMovieMode = MODE_POPULAR;

    private MovieItemListener mMovieItemListener = new MovieItemListener() {
        @Override
        public void onMovieClick(int movieId) {
            mActionsListener.handleMovieClicked(movieId);
        }
    };

    @Override
    public void intentToDetail(int movieId, int movieMode) {
        Intent intent = MovieDetailActivity.newIntent(MoviesActivity.this, movieId, movieMode);
        startActivity(intent);
    }

    @BindView(R.id.rv_movies) RecyclerView mMoviesRecyclerView;
    @BindView(R.id.toolbar_movies) Toolbar mMoviesToolbar;
    @BindView(R.id.spinner_movies) Spinner mMoviesSpinner;
    @BindView(R.id.swipe_refresh_movies) SwipeRefreshLayout mMoviesSwipeRefresh;

    @OnItemSelected(R.id.spinner_movies)
    void onItemSelected(int position) {
        if(position == MODE_POPULAR){
            mMovieMode = MODE_POPULAR;
        }else if(position == MODE_TOP_RATED){
            mMovieMode = MODE_TOP_RATED;
        }
        mActionsListener.loadMovies(false, mMovieMode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);

        setSupportActionBar(mMoviesToolbar);
        mSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.movies_options_array, R.layout.movie_mode_spinner_item);

        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMoviesSpinner.setAdapter(mSpinnerAdapter);


        mActionsListener = new MoviesPresenter(MovieRepositories.getInMemoryRepoInstance(new MoviesServiceApiImpl()), this);

        final int numColumns = this.getResources().getInteger(R.integer.num_movies_columns);

        mMoviesRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numColumns);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mMoviesAdapter.getItemViewType(position)){
                    case MoviesAdapter.VIEW_TYPE_ITEM:
                        return 1;
                    case MoviesAdapter.VIEW_TYPE_LOADING:
                        return numColumns;
                    default:
                        return -1;
                }
            }
        });
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);

        mMoviesAdapter = new MoviesAdapter(mMovieItemListener, this);

        if(savedInstanceState != null){
            if(savedInstanceState.getInt(SAVE_STATE_LAST_VISIBLE_ITEM, -1) != -1){
                mMoviesAdapter.setLastVisibleItem(savedInstanceState.getInt(SAVE_STATE_LAST_VISIBLE_ITEM, -1));
            }
            if(savedInstanceState.getInt(SAVE_STATE_MOVIE_MODE, -1) != -1){
                mMovieMode = savedInstanceState.getInt(SAVE_STATE_MOVIE_MODE, -1);
            }
        }

        mMoviesRecyclerView.setAdapter(mMoviesAdapter);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.rv_list_movie_spacing);
        mMoviesRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        mMoviesAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(List<Movie> movies) {
                if(movies != null){
                    if(movies.get(movies.size()-1) != null){
                        movies.add(null);
                        mMoviesAdapter.insertData(movies, movies.size() - 1);
                    }
                    mActionsListener.loadNextPageMovies();
                }
            }
        });

        mMoviesSwipeRefresh.setColorSchemeColors(
                ContextCompat.getColor(MoviesActivity.this, R.color.colorPrimary),
                ContextCompat.getColor(MoviesActivity.this, R.color.colorAccent),
                ContextCompat.getColor(MoviesActivity.this, R.color.colorPrimaryDark));
        mMoviesSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mActionsListener.loadMovies(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionsListener.loadMovies(false, mMovieMode);
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mMoviesAdapter.replaceData(movies);
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        // Make sure setRefreshing() is called after the layout is done with everything else.
        mMoviesSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mMoviesSwipeRefresh.setRefreshing(active);
            }
        });
    }

    @Override
    public void showMessageNoInternetConnection() {
        Snackbar.make(this.mMoviesRecyclerView, getString(R.string.no_internet_connection), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void deleteProgressBarRecyclerView() {
        List<Movie>movies = mMoviesAdapter.getMovies();
        if(movies.get(movies.size()-1) == null){
            movies.remove(movies.size()-1);
            mMoviesAdapter.removeData(movies, movies.size());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_LAST_VISIBLE_ITEM, mMoviesAdapter.getLastVisibleItem());
        //di comment karena ketika mainactivity, launch mode = "single top" di manifest,
        //maka tidak perlu menyimpan di savedInstanceState
        //outState.putInt(SAVE_STATE_MOVIE_MODE, mMovieMode);
    }

    public interface MovieItemListener {
        void onMovieClick(int movieId);
    }
    public interface OnLoadMoreListener {
        void onLoadMore(List<Movie> movies);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //di comment karena ketika onConfigurationChanged,
        //activity dibuat ulang dari oncreate,
        //sehingga alur yang ada akan secara otomatis memanggil setLoaded ketika movies sudah di load.
        //mMoviesAdapter.setLoaded();
    }

    private class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private static final int VIEW_TYPE_ITEM = 0;
        private static final int VIEW_TYPE_LOADING = 1;

        private OnLoadMoreListener mOnLoadMoreListener;

        private boolean isLoading = true;
        private int visibleThreshold = 5;
        private int lastVisibleItem = 0, totalItemCount;

        private MovieItemListener mMovieItemListener;
        private List<Movie> mMovies;
        private Context mContext;

        int scrollXPopular = -1;
        int scrollYPopular = -1;
        int scrollXTopRated = -1;
        int scrollYTopRated = -1;

        public MoviesAdapter(MovieItemListener movieItemListener, Context context) {
            mMovieItemListener = movieItemListener;
            mContext = context;

            final GridLayoutManager gridLayoutManager = (GridLayoutManager) mMoviesRecyclerView.getLayoutManager();

            mMoviesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

//                    Log.d("tes123X", String.valueOf(recyclerView.getScrollX()));
//                    Log.d("tes123Y", String.valueOf(recyclerView.getScrollY()));

                    totalItemCount = gridLayoutManager.getItemCount();
                    int tempLastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
//                    Log.d("tes123", String.valueOf(lastVisibleItem));
//                    Log.d("tes123=", String.valueOf(tempLastVisibleItem - lastVisibleItem));
                    if(tempLastVisibleItem - lastVisibleItem < 10){
//                        if(mode == MODE_POPULAR){
//                            scrollXPopular = dx;
//                            scrollYPopular = dy;
//                        }else if(mode == MODE_TOP_RATED){
//                            scrollXTopRated = dx;
//                            scrollYTopRated = dy;
//                        }


                        lastVisibleItem = tempLastVisibleItem;
//                        Log.d("tes123total", String.valueOf(totalItemCount));
//                        Log.d("tes123last", String.valueOf(lastVisibleItem));
                        if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            if (mOnLoadMoreListener != null) {
//                                Log.d("tes123load", "masuk");
                                mOnLoadMoreListener.onLoadMore(mMovies);

                            }
                        }
                    }
                }
            });
        }

        public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
            this.mOnLoadMoreListener = mOnLoadMoreListener;
        }

        public void scrollRecyclerView() {
//            if(mode == MODE_POPULAR){
//                if(scrollXPopular != -1 && scrollYPopular != -1)
//                    mMoviesRecyclerView.scrollTo(scrollXPopular, scrollYPopular);
//            }else if(mode == MODE_TOP_RATED){
//                if(scrollXTopRated != -1 && scrollYTopRated != -1)
//                    mMoviesRecyclerView.scrollTo(scrollXTopRated, scrollYTopRated);
//            }
        }

        public void setLoaded() {
            this.isLoading = false;
        }

        public int getLastVisibleItem() {
            return lastVisibleItem;
        }

        public void setLastVisibleItem(int lastVisibleItem) {
            this.lastVisibleItem = lastVisibleItem;
        }

        //insert data untuk view holder loading
        public void insertData(List<Movie>movies, int position){
            mMovies = movies;
            notifyItemInserted(position);
            isLoading = true;
        }

        //remove data untuk view holder loading
        public void removeData(List<Movie>movies, int position){
            mMovies = movies;
            notifyItemRemoved(position);
            setLoaded();
        }

        public void replaceData(List<Movie>movies){
            mMovies = movies;
            notifyDataSetChanged();
            setLoaded();
        }

        public List<Movie> getMovies() {
            return mMovies;
        }

        @Override
        public int getItemViewType(int position) {
            return mMovies.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            if(viewType == VIEW_TYPE_ITEM){
                View view = inflater.inflate(R.layout.movie_list_item, parent, false);
//                int width = view.getMeasuredWidth();
//                int height = 278 / 185 * width;
//                view.setMinimumHeight(height);
                return new MovieViewHolder(view);
            }else if(viewType == VIEW_TYPE_LOADING){
                View view = inflater.inflate(R.layout.movie_list_loading_item, parent, false);
                return new MoviesLoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof MovieViewHolder){
                MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
                movieViewHolder.bind(mMovies.get(position));
            }else if(holder instanceof MoviesLoadingViewHolder){
                MoviesLoadingViewHolder moviesLoadingViewHolder = (MoviesLoadingViewHolder)holder;
                moviesLoadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            if(mMovies == null) return 0;
            else return mMovies.size();
        }

        public Movie getItem(int position) {
            return mMovies.get(position);
        }

        public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            ImageView mPosterImageView;
            public MovieViewHolder(View itemView) {
                super(itemView);
                mPosterImageView = (ImageView)itemView.findViewById(R.id.iv_poster);
                itemView.setOnClickListener(this);
            }

            public void bind(Movie movie){
                Picasso.with(mContext).load(movie.getPosterPath()).into(mPosterImageView);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Movie movie = getItem(position);
                mMovieItemListener.onMovieClick(movie.getId());
            }
        }

        public class MoviesLoadingViewHolder extends RecyclerView.ViewHolder{
            ProgressBar progressBar;
            public MoviesLoadingViewHolder(View itemView) {
                super(itemView);
                progressBar = (ProgressBar) itemView.findViewById(R.id.pb_loading_movies_item);
            }
        }
    }
}
