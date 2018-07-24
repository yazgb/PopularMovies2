package com.android.yaz.popularmovies.model;

public class MovieReview {

    private String idMovie;
    private String author;
    private String review;

    public MovieReview(String idMovie, String author, String review){
        this.idMovie = idMovie;
        this.author = author;
        this.review = review;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
