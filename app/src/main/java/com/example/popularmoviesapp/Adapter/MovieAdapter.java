package com.example.popularmoviesapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.DataModel.Movie;
import com.example.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
        ImageView imageView;

        private  final  MovieAdapterOnClickHandler mClickHandler;


        private ArrayList<Movie> mMovie;

        public MovieAdapter(MovieAdapterOnClickHandler clickHandler,ArrayList<Movie> movie){
           mClickHandler =  clickHandler;
           mMovie =movie;
    }

        public interface MovieAdapterOnClickHandler {
            void onClick(Movie movie);
        }


        public class MovieAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

            public MovieAdapterViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.image_view);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Movie movie =mMovie.get(getAdapterPosition());
                mClickHandler.onClick(movie);
            }


        }


        @Override
        public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            Context context = viewGroup.getContext();
            int layoutIdForListItem = R.layout.movie_item;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;

            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new MovieAdapterViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieAdapter.MovieAdapterViewHolder holder, int position) {

            Movie movie =mMovie.get(position);
            Picasso.get()
                    .load("http://image.tmdb.org/t/p/w185/"+movie.getPoster())
                    .into(imageView);

        }


        @Override
        public int getItemCount() {
            if (null == mMovie) return 0;
            return mMovie.size();
        }


        public void setImage(ArrayList<Movie> movies) {
            mMovie = movies;
            notifyDataSetChanged();
        }
    }