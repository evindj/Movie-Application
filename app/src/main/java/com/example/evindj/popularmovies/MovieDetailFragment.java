package com.example.evindj.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evindj.popularmovies.dummy.DummyContent;
import com.example.evindj.popularmovies.movie.MovieContentProvider;
import com.example.evindj.popularmovies.movie.*;
import com.example.evindj.popularmovies.data.*;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
import com.example.evindj.popularmovies.movie.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class MovieDetailFragment extends Fragment  {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public final  String  baseUrl ="http://api.themoviedb.org/3/";

    LayoutInflater mInflater;
    ReviewsArrayAdapter tAdapterReview;
    TrailerArrayAdapter tAdapterTrailer;

    /**
     * The dummy content this fragment is presenting.
     */
    private Movie movie;
    private ListView trailersView;
    private ListView reviewView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    public void requestTrailers(){
        Gson gson = new GsonBuilder()
                .create();
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setConverter(new GsonConverter(gson))
                .build();
        MovieApi api = adapter.create(MovieApi.class);
        api.getTrailers(movie.getId(), new Callback<TrailerResponse>() {
            @Override
            public void success(final TrailerResponse trailers, Response response) {
                if(trailers!=null){
                    tAdapterTrailer = new TrailerArrayAdapter(getContext(),trailers.getArray());
                    trailersView.setAdapter(tAdapterTrailer);
                    setListViewSize(trailersView);
                    setListener(trailersView);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                showNotification("Unkwon Error");
            }
        });
    }
    public void setListener(ListView view){
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showNotification("Click callled");
                Trailer trailer = (Trailer) tAdapterTrailer.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailer.getYoutubeKey()));
                switch (parent.getId()) {
                    case R.id.trailer_view:
                        break;
                    case R.id.review_view:
                        break;
                }
            }
        });

    }
    public void requestReviews(){
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .build();
        MovieApi api = adapter.create(MovieApi.class);
        api.getReviews(movie.getId(), new Callback<ReviewResponse>() {
            @Override
            public void success(ReviewResponse reviews, Response response) {
                tAdapterReview = new ReviewsArrayAdapter(getContext(), reviews.getArray());
                reviewView.setAdapter(tAdapterReview);
                setListViewSize(reviewView);

            }

            @Override
            public void failure(RetrofitError error) {
                showNotification("Unkwon Error");
            }
        });
    }
    private void setListViewSize(ListView listView){
        ListAdapter mListAdapter = listView.getAdapter();
        if(mListAdapter==null) return;
        int totalHeight = 0;
        for(int size = 0; size <mListAdapter.getCount();size++){
            View item = mListAdapter.getView(size,null,listView);
            item.measure(0,0);
            totalHeight += item.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + listView.getDividerHeight() *(mListAdapter.getCount()-1);
        listView.setLayoutParams(params);
    }
    private int readSortPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortPref = preferences.getString("movies", "0");
        return  Integer.parseInt(sortPref);
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
        mInflater = inflater;
        // Show the dummy content as text in a TextView.
        if (movie != null) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            Picasso.with(this.getContext()).load(movie.getCompleteUrl()).into(imageView);
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(movie.getTitle());
            ((TextView) rootView.findViewById(R.id.movie_desc)).setText(movie.getPlotAnalysis());
            ((TextView) rootView.findViewById(R.id.movie_date)).setText(movie.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.movie_rating)).setText(String.valueOf(movie.getRating()));
            trailersView = (ListView) rootView.findViewById(R.id.trailer_view);
            reviewView = (ListView) rootView.findViewById(R.id.review_view);
            Button btnFav = (Button) rootView.findViewById(R.id.btn_favorite);
            if(readSortPreferences() == 2){
                btnFav.setClickable(false);
                ArrayList<Trailer> trailers = Trailer.getTrailers(getContext(), movie.getId());
                if(trailers!=null){
                    tAdapterTrailer = new TrailerArrayAdapter(getContext(),trailers);
                    trailersView.setAdapter(tAdapterTrailer);
                    setListViewSize(trailersView);
                    setListener(trailersView);
                }
                ArrayList<Review> reviews = Review.getReviews(getContext(), movie.getId());
                tAdapterReview = new ReviewsArrayAdapter(getContext(), reviews);
                reviewView.setAdapter(tAdapterReview);
                setListViewSize(reviewView);
            }
            else {
                requestTrailers();
                requestReviews();
            }
            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movie.save(v.getContext());
                    for(Trailer t:tAdapterTrailer.trailers){
                        t.setIdMovie(movie.getId());
                        t.save(v.getContext());
                    }
                    for(Review r:tAdapterReview.reviews){
                        r.setMovieId(movie.getId());
                        r.save(v.getContext());
                    }
                }
            });
        }

        return rootView;
    }
    private void showNotification(String message){
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
    public class TrailerArrayAdapter extends ArrayAdapter<Trailer>{
        private Context context;
        List<Trailer> trailers;
        public TrailerArrayAdapter(Context context, List<Trailer> trailers){
            super(context,-1,trailers);
            this.trailers = trailers;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){


            View rowView = mInflater.inflate(R.layout.trailer_item, parent,false);
            TextView textView = (TextView)rowView.findViewById(R.id.trailer_name);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.trailer_image);
            Trailer trailer = trailers.get(position);
            textView.setText(trailer.getName());
            imageView.setImageResource(R.drawable.ic_action_playback_play);
            //load icon here
           // Picasso.with(context).load(mValues.get(position).getCompleteUrl()).into(holder.mImageView);
            return rowView;

        }
    }
    public class ReviewsArrayAdapter extends ArrayAdapter<Review>{
        private Context context;
        public  List<Review> reviews;
        public ReviewsArrayAdapter(Context context, List<Review> reviews){
            super(context, -1, reviews);
            this.reviews = reviews;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View rowView = mInflater.inflate(R.layout.review_item, parent,false);
            TextView textView = (TextView)rowView.findViewById(R.id.review_text);
            ImageView imageView = (ImageView)rowView.findViewById(R.id.review_image);
            Review review = reviews.get(position);
            textView.setText(review.getReviewText());
            imageView.setImageResource(R.drawable.ic_action_star_0);
            return rowView;

        }

    }

    public class TrailerCursorAdapter extends CursorAdapter{
        public TrailerCursorAdapter(Context context, Cursor cursor, int flags){
            super(context,cursor,0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.trailer_item,parent,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textView = (TextView)view.findViewById(R.id.trailer_name);
            ImageView imageView = (ImageView) view.findViewById(R.id.trailer_image);
            textView.setText(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.TrailerEntry.COLUMN_TRAIL_NAME)));
            imageView.setImageResource(R.drawable.ic_action_playback_play);
        }
    }
    public class ReviewCursorAdapter extends CursorAdapter{
        public ReviewCursorAdapter(Context context, Cursor cursor, int flags){
            super(context,cursor,0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.trailer_item,parent,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textView = (TextView)view.findViewById(R.id.trailer_name);
            ImageView imageView = (ImageView) view.findViewById(R.id.trailer_image);
            textView.setText(cursor.getString(cursor.getColumnIndexOrThrow(MovieContract.ReviewEntry.COLUMN_REV_TEXT)));
            imageView.setImageResource(R.drawable.ic_action_playback_play);
        }
    }
}
