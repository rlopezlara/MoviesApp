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


public class Favorites extends AppCompatActivity {
    private ActivityFavoritesBinding binding;
    private MyAdapter myAdapter;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private MovieViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

            // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Please log in to view favorites", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();

        if (userId == null) {
            Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        myAdapter = new MyAdapter(this, new ArrayList<>(), null);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myAdapter);

        fetchFavoriteMovies(userId);

        binding.searchBtn.setOnClickListener(v -> {
            // go to MainActivity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

    }

    private void fetchFavoriteMovies(String userId) {
        db.collection("users")
                .document(userId)
                .collection("favorites")

                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Toast.makeText(Favorites.this, "Error fetching favorite movies", Toast.LENGTH_SHORT).show();
                        Log.e("FavouritesActivity", "Snapshot listener error", e);
                        return;
                    }

                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
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