package com.example.popularmoviesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.popularmoviesapp.Adapter.MovieAdapter;
import com.example.popularmoviesapp.DataBase.database.FavoriteMovie;
import com.example.popularmoviesapp.DataModel.Movie;
import com.example.popularmoviesapp.Adapter.MovieAdapter.MovieAdapterOnClickHandler;
import com.squareup.picasso.BuildConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler {
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    ArrayList<Movie> mPopularList,mTopTopRatedList;



    final String URL="http://api.themoviedb.org/3/movie/popular?api_key=";
    static final String API = BuildConfig.API_KEY;
    final String POP_URL = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=";
    final String  TOP_URL = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key=";
    final String  KEY_INSTANCE_STATE_RV_POSITION="save";
    private Parcelable savedRecyclerLayoutState;


    private List<FavoriteMovie> favMovs;
    ArrayList<Movie> movies;

   public GridLayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mRecyclerView =  findViewById(R.id.recyclerview_movie);
        if(savedRecyclerLayoutState!=null){

            layoutManager.onRestoreInstanceState(savedRecyclerLayoutState);

        }else{

             layoutManager
                    = new GridLayoutManager(this, MainActivity.calculateNoOfColumns(this));
        }
            layoutManager.setStackFromEnd(false);
            mRecyclerView.setLayoutManager(layoutManager);


            mRecyclerView.setHasFixedSize(true);


            mMovieAdapter = new MovieAdapter( this,movies);


            mRecyclerView.setAdapter(mMovieAdapter);

            favMovs = new ArrayList<FavoriteMovie>();


        if(NetworkUtils.networkStatus(MainActivity.this)){


            new FetchMovieTask().execute();
    }else{
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        setupViewModel();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_INSTANCE_STATE_RV_POSITION, layoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            savedRecyclerLayoutState=savedInstanceState.getParcelable(KEY_INSTANCE_STATE_RV_POSITION);
        }
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favs) {
                if(favs.size()>0) {
                    favMovs.clear();
                    favMovs = favs;
                }

            }
        });
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetails.class;
        Intent intentToStartMovieDetails = new Intent(context, destinationClass);
        intentToStartMovieDetails.putExtra("title",movie.getTitle());
        intentToStartMovieDetails.putExtra("poster",movie.getPoster());
        intentToStartMovieDetails.putExtra("overView",movie.getOverView());
        intentToStartMovieDetails.putExtra("rating",movie.getRating());
        intentToStartMovieDetails.putExtra("releaseDate",movie.getReleaseDate());
        intentToStartMovieDetails.putExtra("id",movie.getId());
        startActivity(intentToStartMovieDetails);
    }

    public class FetchMovieTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            try {
                   movies= NetworkUtils.fetchMovieData(URL+API);
                    mPopularList = NetworkUtils.fetchMovieData(POP_URL+API); //Get popular movies
                    mTopTopRatedList = NetworkUtils.fetchMovieData(TOP_URL+API); //Get top rated movies

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (movies!=null){
                try{
                    mMovieAdapter.setImage(movies);
                }catch (Exception e){
                    e.getStackTrace();
                }}
        }
        }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.pop_movies) {
            refreshList(mPopularList);
        }
        if (id == R.id.top_movies) {
            refreshList(mTopTopRatedList);
        }
        if (id==R.id.fav_movies){
            loadFav();
            MovieAdapter adapter = new MovieAdapter(MainActivity.this, movies);
            mRecyclerView.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }
    private void refreshList(ArrayList<Movie> list) {
        MovieAdapter adapter = new MovieAdapter(MainActivity.this, list);
        mRecyclerView.setAdapter(adapter);
    }
    private void ClearMovieItemList() {
        if (movies != null) {
            movies.clear();
        } else {
            movies = new ArrayList<Movie>();
        }
    }
    private void loadFav(){
        ClearMovieItemList();
        if (favMovs==null)
            Toast.makeText(MainActivity.this,"No movies to show",Toast.LENGTH_LONG).show();
        else{
        for (int i = 0; i< favMovs.size(); i++) {
            Movie mov = new Movie(
                    String.valueOf(favMovs.get(i).getId()),
                    favMovs.get(i).getTitle(),
                    favMovs.get(i).getPoster(),
                    favMovs.get(i).getOverView(),
                    favMovs.get(i).getRating(),
                    favMovs.get(i).getReleaseDate()
            );
            movies.add( mov );
    }}
}
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }
}
