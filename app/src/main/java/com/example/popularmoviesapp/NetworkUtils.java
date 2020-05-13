package com.example.popularmoviesapp;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.popularmoviesapp.DataModel.Movie;
import com.example.popularmoviesapp.DataModel.Review;
import com.example.popularmoviesapp.DataModel.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.apache.commons.io.IOUtils;


public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();



    public static ArrayList<Movie> fetchMovieData(String url) throws IOException {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        try {

            URL new_url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            parseMovieJson(results,movies);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
    }
    public static ArrayList<Trailer> fetchTrailerData(String url) throws IOException {
        ArrayList<Trailer> trailer = new ArrayList<Trailer>();
        try {

            URL new_url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            parseTrailersJson(results,trailer);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return trailer;
    }
    public static ArrayList<Review> fetchReviewData(String url) throws IOException {
        ArrayList<Review> review = new ArrayList<Review>();
        try {

            URL new_url = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) new_url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            String results = IOUtils.toString(inputStream);
            parseReviewsJson(results,review);
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return review;
    }

    public static void parseMovieJson(String data, ArrayList<Movie> list){


        try {
            JSONObject mainObject = new JSONObject(data);
            Log.v(TAG,mainObject.toString());
            JSONArray resArray = mainObject.getJSONArray("results");
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setId(jsonObject.getString("id"));
                movie.setRating(jsonObject.getString("vote_average"));
                movie.setTitle(jsonObject.getString("title"));
                movie.setOverView(jsonObject.getString("overview"));
                movie.setReleaseDate(jsonObject.getString("release_date"));
                movie.setPoster(jsonObject.getString("poster_path"));
                list.add(movie);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Erro occurred during JSON Parsing");
        }

    }
    public static void parseReviewsJson(String json,ArrayList<Review> reviews) {
        try {

            Review review;
            JSONObject json_object = new JSONObject(json); //(json.replace("\\",""));

            JSONArray resultsArray = new JSONArray(json_object.optString("results","[\"\"]"));

            for (int i = 0; i < resultsArray.length(); i++) {
                String thisitem = resultsArray.optString(i, "");
                JSONObject movieJson = new JSONObject(thisitem);

                review = new Review(
                        movieJson.optString("author","Not Available"),
                        movieJson.optString("content","Not Available"),
                        movieJson.optString("id","Not Available"),
                        movieJson.optString("url","Not Available")
                );

                reviews.add(review);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void parseTrailersJson(String json, ArrayList<Trailer> trailers) {
        try {
            Trailer trailer;
            JSONObject json_object = new JSONObject(json); //(json.replace("\\",""));
            JSONArray resultsArray = new JSONArray(json_object.optString("results","[\"\"]"));

            for (int i = 0; i < resultsArray.length(); i++) {
                String thisitem = resultsArray.optString(i, "");
                JSONObject movieJson = new JSONObject(thisitem);

                trailer = new Trailer(
                        movieJson.optString("name","Not Available"),
                        movieJson.optString("site","Not Available"),
                        movieJson.optString("key","Not Available")
                );

                trailers.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Boolean networkStatus(Context context){
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }
}