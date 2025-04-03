package com.example.moviesapp.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviesapp.R;
import com.example.moviesapp.databinding.ActivityMovieDetailsBinding;
import com.example.moviesapp.utils.ApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private ActivityMovieDetailsBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using ViewBinding
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userId = currentUser.getUid();

        // Retrieve movie details from the intent
        String title = getIntent().getStringExtra("title");
        String year = getIntent().getStringExtra("year");
        String poster = getIntent().getStringExtra("poster");
        String imdbID = getIntent().getStringExtra("imdbID");

        if (title == null || year == null || poster == null || imdbID == null) {
            Log.e("MovieDetailsActivity", "Received null data in Intent");
            Toast.makeText(this, "Error loading movie details", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        // Set the movie details on the UI
        binding.movieTitle.setText(title);
        binding.movieYear.setText(year);
        // If a poster URL is provided, load the image using Glide
        if (!poster.isEmpty()) {
            Glide.with(this)
                    .load(poster)
                    .into(binding.moviePoster);
        }
        // Fetch additional movie details using the IMDb ID
        fetchImdbDetails(imdbID);

        // Set up the back button to finish the activity when clicked
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });

        binding.addFavBtn.setOnClickListener(v -> addToFavorites(userId, imdbID));
    }

    //Fetches additional movie details from the OMDB API using the IMDb ID.
    private void fetchImdbDetails(String imdbID) {
// Make an API call to fetch the details
        String url = "https://www.omdbapi.com/?apikey=cf13b29b&i=" + imdbID;


        ApiClient.get(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MovieDetailsActivity.this, "Failed to load movie details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                try (response) {
                    // Check if the response was successful
                    if(response.isSuccessful()) {

                        String responseBody = response.body().string();

                        JSONObject jsonObject = new JSONObject(responseBody);
                        // Extract movie details from the response
                        String title = jsonObject.optString("Title", "N/A");
                        String genre = jsonObject.optString("Genre", "N/A");
                        String runtime = jsonObject.optString("Runtime", "N/A");
                        String rating = jsonObject.optString("imdbRating", "N/A");
                        String description = jsonObject.optString("Plot", "N/A");

                        runOnUiThread(() -> {
                            binding.movieGenre.setText(genre);
                            binding.movieTitle.setText(title);
                            binding.movieRuntime.setText(runtime);
                            binding.movieRated.setText(rating);
                            binding.movieDescription.setText(description);

                        });
                    }else {
                        // Handle unsuccessful API call
                        runOnUiThread(() -> {
                            Log.e("MovieDetailsActivity", "API call unsuccessful: " + response.message());
                            Toast.makeText(MovieDetailsActivity.this, "Failed to load additional movie details", Toast.LENGTH_SHORT).show();
                        });
                    }

                } catch (JSONException e) {
                    // Handle JSON parsing errors
                    runOnUiThread(() -> {
                        Toast.makeText(MovieDetailsActivity.this, "Error parsing movie details", Toast.LENGTH_SHORT).show();
                    });
                }
            }


        });
    }
    private void addToFavorites(String userId, String imdbID){
        if (userId == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> movieData = new HashMap<>();
        movieData.put("title", binding.movieTitle.getText().toString());
        movieData.put("year", binding.movieYear.getText().toString());
        movieData.put("poster", getIntent().getStringExtra("poster"));
        movieData.put("imdbID", imdbID);
        movieData.put("genre", binding.movieGenre.getText().toString());
        movieData.put("plot", binding.movieDescription.getText().toString());
        movieData.put("runtime", binding.movieRuntime.getText().toString());
        movieData.put("rating", binding.movieRated.getText().toString());

        db.collection("users")
                .document(userId)
                .collection("favorites")
                .document(imdbID)
                .set(movieData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MovieDetailsActivity.this, "Added to Favorites!", Toast.LENGTH_SHORT).show();
                    Log.i("MovieDetailsActivity", "Movie added to favorites: " + movieData);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MovieDetailsActivity.this, "Failed to add to Favorites", Toast.LENGTH_SHORT).show();
                    Log.e("MovieDetailsActivity", "Failed to add movie to favorites", e);
                });
    }

    private void updateMovieDetails(String userId, String imdbID) {
        String newTitle = binding.movieTitle.getText().toString();

        Map<String, Object> movieData = new HashMap<>();
        movieData.put("title", newTitle);
        movieData.put("year", binding.movieYear.getText().toString());
        movieData.put("poster", getIntent().getStringExtra("poster"));
        movieData.put("imdbID", imdbID);
        movieData.put("genre", binding.movieGenre.getText().toString());
        movieData.put("plot", binding.movieDescription.getText().toString());
        movieData.put("runtime", binding.movieRuntime.getText().toString());
        movieData.put("rating", binding.movieRated.getText().toString());

        db.collection(userId)
                .document(imdbID)
                .set(movieData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Movie updated!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update movie", Toast.LENGTH_SHORT).show();
                    Log.e("MovieDetailsActivity", "Failed to update movie", e);
                });

        // go back to favourites activity
        finish();
    }

}
