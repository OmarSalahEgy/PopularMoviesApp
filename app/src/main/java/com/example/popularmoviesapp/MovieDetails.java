package com.example.popularmoviesapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.Adapter.TrailerAdapter;
import com.example.popularmoviesapp.DataBase.database.FavoriteMovie;
import com.example.popularmoviesapp.DataBase.database.MovieDatabase;
import com.example.popularmoviesapp.DataModel.Movie;
import com.example.popularmoviesapp.DataModel.Review;
import com.example.popularmoviesapp.DataModel.Trailer;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity implements TrailerAdapter.ListItemClickListener{

    TextView title;
    ImageView poster;
    TextView overView;
    TextView rating;
    TextView releaseDate;
    Button favor;

    Movie movie;

    private MovieDatabase mDb;
    private Boolean isFav = false;

    final String URL_BASE="http://api.themoviedb.org/3/movie/";
    final String TRAILER_URL_EX="/videos?api_key=";
    final String REVIEW_URL_EX="/reviews?api_key=";


    String movieId;

    private RecyclerView mTrailerRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TrailerAdapter mTrailerAdapter;




    ArrayList<Trailer> trailers;
    ArrayList<Review> reviews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

       title =(TextView) findViewById(R.id.orginal_title);

        poster =(ImageView) findViewById(R.id.image_poster);

        overView =(TextView) findViewById(R.id.over_view);

        rating =(TextView) findViewById(R.id.rating);

        releaseDate =(TextView) findViewById(R.id.release_date);

        favor=(Button) findViewById(R.id.favButton);



        Intent intent = getIntent();
        String posterImage;

        if (intent!=null){

            title.setText(intent.getStringExtra("title"));

            posterImage = intent.getStringExtra("poster");
            Picasso.get()
                    .load("http://image.tmdb.org/t/p/w185/"+posterImage)
                    .into(poster);

            overView.setText(intent.getStringExtra("overView"));

            rating.setText(intent.getStringExtra("rating"));

            releaseDate.setText(intent.getStringExtra("releaseDate"));
            movieId=intent.getStringExtra("id");

            movie = new Movie(movieId,title.getText().toString(),posterImage,
                    overView.getText().toString(),rating.getText().toString(),releaseDate.getText().toString());

        }
        if(NetworkUtils.networkStatus(MovieDetails.this)){


            new ReviewsQueryTask().execute();
        }else{
            Toast.makeText(MovieDetails.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        mDb = MovieDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final FavoriteMovie fmov = mDb.movieDao().loadMovieById(Integer.parseInt(movie.getId()));
                if(fmov != null) setFavorite(true);
                else setFavorite(false);
            }
        });
    }
    // AsyncTask to perform query
    public class ReviewsQueryTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {


            try {
                reviews=NetworkUtils.fetchReviewData(URL_BASE+movieId+REVIEW_URL_EX+MainActivity.API);
                trailers = NetworkUtils.fetchTrailerData(URL_BASE+movieId+TRAILER_URL_EX+MainActivity.API);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (trailers!=null) {
                mTrailerRecyclerView = findViewById(R.id.rv_trailers);
                mTrailerAdapter = new TrailerAdapter(MovieDetails.this, trailers, MovieDetails.this);
                mTrailerRecyclerView.setAdapter(mTrailerAdapter);
                mLayoutManager = new LinearLayoutManager(MovieDetails.this);
                mTrailerRecyclerView.setLayoutManager(mLayoutManager);
            }
            ((TextView)findViewById(R.id.tv_reviews)).setText("");
            for(int i=0; i<reviews.size(); i++) {
                ((TextView)findViewById(R.id.tv_reviews)).append("\n");
                ((TextView)findViewById(R.id.tv_reviews)).append(reviews.get(i).getContent());
                ((TextView)findViewById(R.id.tv_reviews)).append("\n\n");
                ((TextView)findViewById(R.id.tv_reviews)).append(" - Reviewed by ");
                ((TextView)findViewById(R.id.tv_reviews)).append(reviews.get(i).getAuthor());
                ((TextView)findViewById(R.id.tv_reviews)).append("\n\n--------------\n");
            }

        }

    }

    private void watchYoutubeVideo(String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        webIntent.putExtra("finish_on_ended", true);
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    public void OnListItemClick(Trailer trailerItem) {
        watchYoutubeVideo(trailerItem.getKey());
    }
    public void addFav(final View view){
        final FavoriteMovie mov = new FavoriteMovie(
                Integer.parseInt(movie.getId()),
                movie.getTitle(),
                movie.getPoster(),
                movie.getOverView(),
                movie.getRating(),
                movie.getReleaseDate()
        );
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isFav) {
                    // delete item
                    mDb.movieDao().deleteMovie(mov);
                } else {
                    // insert item
                    mDb.movieDao().insertMovie(mov);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setFavorite(!isFav);
                    }
                });
            }

        });
    }
    private void setFavorite(Boolean fav){
        if (fav) {
            isFav = true;

        } else {
            isFav = false;
        }
    }

}


