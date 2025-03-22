package com.example.moviesapp.view;

import com.example.moviesapp.model.MovieModel;
//Interface for handling movie item click events in the RecyclerView.

public interface MovieClickListener {
    void onMovieClick(MovieModel movie);

}
