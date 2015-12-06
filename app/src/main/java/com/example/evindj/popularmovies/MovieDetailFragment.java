package com.example.evindj.popularmovies;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evindj.popularmovies.dummy.DummyContent;
import com.example.evindj.popularmovies.movie.MovieContentProvider;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
import com.example.evindj.popularmovies.movie.*;
import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Movie movie;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            movie = MovieContentProvider.movieMap.get(Integer.valueOf(getArguments().getString(ARG_ITEM_ID)));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(movie.getTitle());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (movie != null) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            Picasso.with(this.getContext()).load(movie.getCompleteUrl()).into(imageView);
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(movie.getTitle());
            ((TextView) rootView.findViewById(R.id.movie_desc)).setText(movie.getPlotAnalysis());
            ((TextView) rootView.findViewById(R.id.movie_date)).setText(movie.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.movie_rating)).setText(String.valueOf(movie.getRating()));


        }

        return rootView;
    }
}
