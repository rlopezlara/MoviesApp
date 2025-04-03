package com.example.moviesapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.moviesapp.databinding.ActivityFavoritesDetailsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FavoritesDetails extends AppCompatActivity {
    private ActivityFavoritesDetailsBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using ViewBinding
        binding = ActivityFavoritesDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve IMDb ID from intent extras
        String imdbID = getIntent().getStringExtra("imdbID");

        // Set up button click listeners for deleting movie details
        binding.deleteBtn.setOnClickListener(v -> deleteMovie(imdbID));

        // Set up button click listeners for updating movie details
        binding.updateBtn.setOnClickListener(v -> updateMovieDescription(imdbID));

        // Get current user from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Ensure the user is logged in and the IMDb ID is not null

        if (currentUser != null && imdbID != null) {
            // Query the Firestore database for the movie details
            db.collection("users")
                    .document(currentUser.getUid())
                    .collection("favorites")
                    .document(imdbID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Extract movie details from Firestore
                            String title = documentSnapshot.getString("title");
                            String year = documentSnapshot.getString("year");
                            String poster = documentSnapshot.getString("poster");
                            String genre = documentSnapshot.getString("genre");
                            String plot = documentSnapshot.getString("plot");
                            String runtime = documentSnapshot.getString("runtime");
                            String rating = documentSnapshot.getString("rating");

                            // Populate UI elements with retrieved movie details

                            binding.movieTitle.setText(title);
                            binding.movieYear.setText(year);
                            Glide.with(this).load(poster).into(binding.moviePoster);
                            binding.movieGenre.setText(genre);
                            binding.movieRuntime.setText(runtime);
                            binding.movieRated.setText(rating);
                            binding.movieDescription.setText(plot);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Log and display an error message if Firestore retrieval fails
                        Log.e("FavoritesDetails", "Failed to load movie details", e);
                        Toast.makeText(this, "Error loading movie details", Toast.LENGTH_SHORT).show();
                    });
        }

        // Set up back button to close the activity when clicked
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    //Deletes the selected movie from the user's favorites collection in Firestore.
    private void deleteMovie(String imdbID) {
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Ensure the user is logged in and the IMDb ID is valid
        if (currentUser != null && imdbID != null) {
            // Remove the movie document from Firestore
            db.collection("users")
                    .document(currentUser.getUid())
                    .collection("favorites")
                    .document(imdbID)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Show a success message and close the activity
                        Toast.makeText(FavoritesDetails.this, "Movie deleted successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Close the activity after deletion
                    })
                    .addOnFailureListener(e -> {
                        // Log and show an error message if deletion fails
                        Log.e("FavoritesDetails", "Error deleting movie", e);
                        Toast.makeText(FavoritesDetails.this, "Error deleting movie.", Toast.LENGTH_SHORT).show();
                    });
        }
    }
    //Updates the movie description in Firestore with the new text entered by the user.
    private void updateMovieDescription(String imdbID) {
        // Get the current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Ensure the user is logged in and the IMDb ID is valid
        if (currentUser != null && imdbID != null) {
            // Get the new description entered by the user
            String newDescription = binding.movieDescription.getText().toString().trim();
            // Validate that the description is not empty
            if (newDescription.isEmpty()) {
                binding.movieDescription.setError("Description cannot be empty");
                return;
            }

            // Update the 'plot' field in Firestore with the new description
            db.collection("users")
                    .document(currentUser.getUid())
                    .collection("favorites")
                    .document(imdbID)
                    .update("plot", newDescription)
                    .addOnSuccessListener(aVoid -> {
                        // Show a success message when the description is updated
                        Toast.makeText(FavoritesDetails.this, "Description updated successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Log and show an error message if the update fails
                        Log.e("FavoritesDetails", "Error updating description", e);
                        Toast.makeText(FavoritesDetails.this, "Failed to update description.", Toast.LENGTH_SHORT).show();
                    });
                    }
    }

}
