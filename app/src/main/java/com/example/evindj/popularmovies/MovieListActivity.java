package com.example.evindj.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.evindj.popularmovies.dummy.DummyContent;
import com.example.evindj.popularmovies.movie.Movie;
import com.example.evindj.popularmovies.movie.MovieContentProvider;
import com.example.evindj.popularmovies.movie.MovieService;
import com.example.evindj.popularmovies.movie.SortOrder;
import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */

public class MovieListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    RecyclerView recyclerView;
    MovieItemRecyclerViewAdapter adapter;
    SortOrder sortOrder = SortOrder.MOSTPOPULAR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;

        Stetho.initialize(
                Stetho.newInitializerBuilder(context).enableDumpapp( new SampleDumperPluginsProvider(context))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(context)).build()
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.movie_list);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        if(isConnectedToInternet())
             new DownloadFilesTask().execute();
        else
            showNotification("Check your internet connection");


        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }
    private void showNotification(String message){
        Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
        toast.show();
    }
    private boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    @Override
    public void onStart(){
        super.onStart();
        if(isConnectedToInternet())
            new DownloadFilesTask().execute();
        else
            showNotification("Check your internet connection");

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setLayoutManager( new GridLayoutManager(this,2));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public  boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id== R.id.action_settings){
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    private static class SampleDumperPluginsProvider implements DumperPluginsProvider{
        private final Context context;
        public SampleDumperPluginsProvider(Context context){
            this.context= context;
        }
        @Override
        public  Iterable<DumperPlugin> get(){
            ArrayList<DumperPlugin> plugins = new ArrayList<>();
            for(DumperPlugin defaultPlugin:Stetho.defaultDumperPluginsProvider(context).get()){
                plugins.add(defaultPlugin);
            }
            return plugins;
        }
    }
    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
           // public final TextView mIdView;
          //  public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
               // mIdView = (TextView) view.findViewById(R.id.id);
               // mContentView = (TextView) view.findViewById(R.id.content);

            }

            @Override
            public String toString() {
                return super.toString();
            }
        }
    }


    public class MovieItemRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieItemRecyclerViewAdapter.ViewHolder> {

        public  List<Movie> mValues;
        public MovieItemRecyclerViewAdapter(ArrayList<Movie> movies) {
            mValues = movies;
        }
        public MovieItemRecyclerViewAdapter() {
            mValues = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
           // holder.mIdView.setText(mValues.get(position).id);
           // holder.mContentView.setText(mValues.get(position).content);
            //holder.titleView.setText(mValues.get(position).getTitle());
            Context context = holder.mImageView.getContext();
            Picasso.with(context).load(mValues.get(position).getCompleteUrl()).into(holder.mImageView);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()));
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getId()));

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
           // public final TextView titleView;
            public Movie mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.imageView);
              // titleView = (TextView)view.findViewById(R.id.titleView);
            }

            @Override
            public String toString() {
                return super.toString() ;
            }
        }
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, ArrayList<Movie>> {
        private int readSortPreferences(){
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String sortPref = preferences.getString("movies","0");
            return  Integer.parseInt(sortPref);
        }
        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            MovieService srv = MovieService.getInstance();
            int i = readSortPreferences();
            if(i==0) sortOrder = SortOrder.MOSTPOPULAR;
            if(i==1) sortOrder =SortOrder.HIGHESTRATED;
            return  (ArrayList<Movie>)srv.getMovies(sortOrder);
        }
        @Override
        protected void onProgressUpdate(Integer... progress) {
        }
        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if(result!=null) {
                MovieContentProvider.movies = result;
                MovieContentProvider.movieMap = new HashMap<>();
                for (Movie m : MovieContentProvider.movies) {
                    MovieContentProvider.movieMap.put(m.getId(), m);
                    //adapter.mValues.add(m);
                }
                // adapter.notifyDataSetChanged();
                recyclerView.setAdapter(new MovieItemRecyclerViewAdapter(result));
            }
            else
                showNotification("No Data returned");
        }
    }

}
