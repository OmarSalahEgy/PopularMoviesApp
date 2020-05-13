package com.example.popularmoviesapp.DataBase.database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="FavoriteMovies")
public class FavoriteMovie {

    @PrimaryKey
    private int id;
    private String  title;
    private String  poster;
    private String  overView;
    private String  rating;
    private String  releaseDate;

    public FavoriteMovie(int id, String title, String poster, String overView, String rating, String releaseDate) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.overView = overView;
        this.rating = rating;
        this.poster = poster;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }



}

