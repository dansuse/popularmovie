package com.example.danielsetyabudi.movies.moviedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.danielsetyabudi.movies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Daniel Setyabudi on 02/07/2017.
 */

public class MoviePosterDialogFragment extends DialogFragment {

    private ImageView mMoviePosterImageView;
    private static final String ARGS_IMAGE_URL = "args_image_url";
    private String mImageUrl = "";

    static MoviePosterDialogFragment newInstance(String imageUrl) {
        MoviePosterDialogFragment moviePosterDialogFragment = new MoviePosterDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(ARGS_IMAGE_URL, imageUrl);
        moviePosterDialogFragment.setArguments(args);

        return moviePosterDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            if(getArguments().getString(ARGS_IMAGE_URL) != null){
                mImageUrl = getArguments().getString(ARGS_IMAGE_URL);
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_movie_poster, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Zoom In");
        mMoviePosterImageView = (ImageView)view.findViewById(R.id.iv_dialog_movie_poster);
        Picasso.with(getActivity()).load(mImageUrl).into(mMoviePosterImageView);
    }
}
