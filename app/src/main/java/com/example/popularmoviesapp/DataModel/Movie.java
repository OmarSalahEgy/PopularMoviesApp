package com.example.popularmoviesapp.DataModel;

public class Movie {
    private String  id;
    private String  title;
    private String  poster;
    private String  overView;
    private String  rating;
    private String  releaseDate;


    public Movie(String id,String  title,String  poster,String  overView,String  rating,String  releaseDate){
             this.id=id;
             this.title=title;
             this.poster=poster;
             this.overView=overView;
             this.rating=rating;
             this.releaseDate=releaseDate;
    }
    public Movie(){}
    public String getTitle(){return title;}

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId(){return id;}

    public void setId(String id) {
        this.id = id;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
