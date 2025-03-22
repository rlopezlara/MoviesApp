package com.example.moviesapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviesapp.databinding.ActivityMainBinding;
import com.example.moviesapp.model.MovieModel;
import com.example.moviesapp.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Binding for the activity layout using ViewBinding
    ActivityMainBinding binding;
    // ViewModel to manage movie data
    MovieViewModel viewModel;
    // Adapter for the RecyclerView
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the ViewModel to observe movie data
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Setup RecyclerView with adapter and click listener
        myAdapter = new MyAdapter(this, new ArrayList<>(), movie -> {

            // Navigate to the details screen, passing the movie data
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.putExtra("title", movie.getTitle());
            intent.putExtra("year", movie.getYear());
            intent.putExtra("poster", movie.getPoster());
            intent.putExtra("imdbID", movie.getImdbID());
            startActivity(intent);
        });

        // Set up RecyclerView with a LinearLayoutManager for vertical scrolling
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // Set the adapter for RecyclerView
        binding.recyclerView.setLayoutManager(layoutManager);
        // Observe changes in the movie data (LiveData)
        binding.recyclerView.setAdapter(myAdapter);

        // Observe the movie data
        viewModel.getMovieData().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieList) {
                if (movieList != null && !movieList.isEmpty()) {
                    // Update the RecyclerView with the new movie data
                    myAdapter.updateMovies(movieList);
                    Log.i("MainActivity", "Movies updated: " + movieList.size());
                } else {
                    // Display a message if no movies were found
                    Toast.makeText(MainActivity.this, "No movies found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up the search button click listener
        binding.searchBtn.setOnClickListener(v -> {
            // Get the search query from the input field
            String searchMovie = binding.searchTitle.getText().toString().trim();
            // Check if the search query is empty
            if (searchMovie.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a movie title", Toast.LENGTH_SHORT).show();
                return;
            }

            // Trigger the movie search in the ViewModel
            viewModel.searchMovies(searchMovie);
            Log.i("MainActivity", "Searching for: " + searchMovie);
        });
    }
}
