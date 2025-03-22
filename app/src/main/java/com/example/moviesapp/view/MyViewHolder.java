package com.example.moviesapp.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moviesapp.R;
//ViewHolder class that holds the views for displaying a movie item in a RecyclerView.
public class MyViewHolder extends RecyclerView.ViewHolder {

    public ImageView moviePoster;
    public TextView movieTitle;
    public TextView movieYear;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        // Initialize the views by finding them using their IDs
        moviePoster = itemView.findViewById(R.id.moviePoster);
        movieTitle = itemView.findViewById(R.id.movieTitle);
        movieYear = itemView.findViewById(R.id.movieYear);

    }
}
