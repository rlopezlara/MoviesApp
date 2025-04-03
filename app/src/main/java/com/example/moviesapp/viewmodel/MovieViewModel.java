package com.example.moviesapp.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviesapp.model.MovieModel;
import com.example.moviesapp.utils.ApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
/**
 * ViewModel class for handling movie search functionality.
 * It fetches movie data from an external API and provides it to the UI.
 */
public class MovieViewModel extends ViewModel {

    private final MutableLiveData<List<MovieModel>> movieData = new MutableLiveData<>();

    public LiveData<List<MovieModel>> getMovieData() {
        return movieData;
    }
//Searches for movies based on the provided movie title.
// Sends an API request to the OMDB API and updates the movieData with the results.
    public void searchMovies(String movieTitle) {

        String urlString = "https://www.omdbapi.com/?apikey=cf13b29b&type=movie&s=" + movieTitle;
    //Send a GET request to the API
        ApiClient.get(urlString, new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("MovieViewModel", "API call failed: " + e.getMessage());
                movieData.postValue(new ArrayList<>());
            }

            //handles the API response by parsing the JSON data and updating movieData.
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null) {
                    Log.e("MovieViewModel", "Response body is null");
                    movieData.postValue(new ArrayList<>());
                    return;
                }
                // Convert response body to a string for further processing
                String responseData = response.body().string();
                Log.i("MovieViewModel", responseData);

                try{
                    // Parse the JSON response
                    JSONObject jsonObject = new JSONObject(responseData);


                    if(jsonObject.has("Search")) {

                        JSONArray movieArray = jsonObject.getJSONArray("Search");

                        // Create a list to store movie models
                        List<MovieModel> movieList = new ArrayList<>();
                        for (int i = 0; i < movieArray.length(); i++) {
                            JSONObject movieObject = movieArray.getJSONObject(i);
                            MovieModel movie = new MovieModel(
                                    movieObject.getString("Title"),
                                    movieObject.getString("Year"),
                                    movieObject.getString("Poster"));
                            // Set the IMDb ID for the movie
                            movie.setImdbID(movieObject.getString("imdbID")
                            );
                            movieList.add(movie);
                        }

                        // Update the movieData with the fetched list of movies
                        movieData.postValue(movieList);

                    } else {
                        Log.i("MovieViewModel", "No movies found");
                        movieData.postValue(new ArrayList<>());
                    }
                } catch (JSONException e) {
                    Log.e("MovieViewModel", "JSON parsing error: " + e.getMessage());
                    movieData.postValue(new ArrayList<>());
                }
            }
        });
    }
}
