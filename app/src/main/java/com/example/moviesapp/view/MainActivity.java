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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
//that allows users to search for movies and view the results in a RecyclerView
// It integrates with a MovieViewModel to handle the business logic of fetching movie data,
// displays the data using a RecyclerView, and allows users to click on a movie to navigate to a details screen.
public class MainActivity extends AppCompatActivity {
    // Binding for the activity layout using ViewBinding
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    // ViewModel to manage movie data
    MovieViewModel viewModel;
    // Adapter for the RecyclerView
    MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Initialize the ViewModel to observe movie data
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        //false the adapter is used for general movie search results.
        myAdapter = new MyAdapter(this, new ArrayList<>(), false);


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
        //Sign out the user
        binding.signOutBtn.setOnClickListener(v -> {
            Intent intentObj = new Intent(getApplicationContext(), Login.class);
            startActivity(intentObj);

            finish(); // Finish current activity
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
    //Favourite tab btn to navigate to the favourite list
        binding.favTabBtn.setOnClickListener(v -> {
            Intent intentObj = new Intent(getApplicationContext(), Favorites.class);
            startActivity(intentObj);
            finish();
        });
    }
}
