package com.example.moviesapp.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.R;
import com.example.moviesapp.model.MovieModel;

import java.util.ArrayList;
import java.util.List;
//RecyclerView.Adapter subclass for binding movie data to the RecyclerView.
//This adapter binds a list of MovieModel objects to the views in the RecyclerView.
public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private final List<MovieModel> movies;
    private final Context context;
    private MovieClickListener clickListener;

    // Constructor to initialize the adapter with context, movie list, and click listener.
    public MyAdapter(Context context, List<MovieModel> movies, MovieClickListener listener) {
        this.context = context;
        this.movies = movies;
        this.clickListener = listener;
    }
 
 
    //Called to create a new ViewHolder to represent a movie item.
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Inflate the movie item layout and return a ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_result,parent, false);
        return new MyViewHolder(itemView);
    }
    //Called to bind movie data to the views in the ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MovieModel movie = movies.get(position);

        // Set movie data to views
        holder.movieTitle.setText(movie.getTitle());
        holder.movieYear.setText(movie.getYear());

        // Load the movie poster using Glide
        Glide.with(holder.itemView.getContext())
                .load(movie.getPoster())
                .into(holder.moviePoster);

        // Set up an OnClickListener to launch MovieDetailsActivity with movie data
        holder.itemView.setOnClickListener(v -> {
            // Create intent to navigate to movie details activity
            Intent intent = new Intent(v.getContext(), MovieDetailsActivity.class);
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("year", movie.getYear());
                intent.putExtra("poster", movie.getPoster());
                intent.putExtra("imdbID", movie.getImdbID());


            Log.d("MyAdapter", "Clicked movie: " + movie.getTitle() + ", IMDb ID: " + movie.getImdbID());

            v.getContext().startActivity(intent); //start the activity
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the movie list
        return movies.size();
    }

    // Updates the movie list and notifies the adapter of the change.
    public void updateMovies(List<MovieModel> newMovies) {
        this.movies.clear();
        this.movies.addAll(newMovies);
        notifyDataSetChanged();
    }
}
