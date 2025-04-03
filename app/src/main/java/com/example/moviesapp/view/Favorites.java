package com.example.moviesapp.view;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviesapp.databinding.ActivityFavoritesBinding;
import com.example.moviesapp.model.MovieModel;
import com.example.moviesapp.viewmodel.MovieViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

//Favorites Activity allows users to view their saved favorite movies.
public class Favorites extends AppCompatActivity {
    private ActivityFavoritesBinding binding;
    private MyAdapter myAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private MovieViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get the currently logged-in user
      FirebaseUser currentUser = mAuth.getCurrentUser();

        // Get the unique user ID
        String userId = currentUser.getUid();

        // If the user ID is null, show an error and close the activity
        if (userId == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Set up RecyclerView with an empty list initially
        myAdapter = new MyAdapter(this, new ArrayList<>(), true); // true, the adapter handles displaying favorite movies.

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myAdapter);

        // Fetch the user's favorite movies from Firestore
        fetchFavoriteMovies(userId);

        // Set click listener for the search button to navigate back to the main activity
        binding.searchBtn.setOnClickListener(v -> {
            // go to MainActivity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

    }
    //Fetches the list of favorite movies from Firebase Firestore for the given user
    private void fetchFavoriteMovies(String userId) {
        db.collection("users")
                .document(userId)
                .collection("favorites")
                // Handle errors while fetching data
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(Favorites.this, "Error fetching favorite movies", Toast.LENGTH_SHORT).show();
                        Log.e("FavouritesActivity", "Snapshot listener error", e);
                        return;
                    }
                    // If the snapshot contains data, update the RecyclerView
                    if (querySnapshot != null) {
                        List<MovieModel> movieList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            MovieModel movie = document.toObject(MovieModel.class);
                            if (movie != null) {
                                movieList.add(movie);
                            }
                        }
                        myAdapter.updateMovies(movieList);
                    } else {
                        Toast.makeText(Favorites.this, "No movies found", Toast.LENGTH_SHORT).show();
                    }
                });
        }
}