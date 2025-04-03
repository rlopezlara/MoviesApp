package com.example.moviesapp.model;

public class MovieModel {
    private String title;
    private String year;
    private String poster;
    private String imdbID;

// Represents a movie object with attributes like title, year, poster, and imdbID.
//	Provides getters and setters to access and modify movie data



    public MovieModel() {

    }
    public MovieModel(String title, String year, String poster,String imdbID) {
        this.title = title;
        this.year = year;
        this.poster = poster;
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }
}
