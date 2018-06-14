package com.howard.retrofitdemo.api;

import com.howard.retrofitdemo.entity.MovieDTO;
import com.howard.retrofitdemo.entity.MovieListingDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APILink {
    String API_KEY = "3b4c65c3780fc1ef44ec5500b186d833";
    String BASE_URL = "http://api.themoviedb.org";
    String BASE_IMAGES_URL = "http://image.tmdb.org/t/p/";
    String POSTER_SIZE = "w185";
    /**
     * Retrieves a specific movie through his {@code id}
     *
     * @param id      movie id
     * @param api_key api_key used to retrieve information
     * @param lang    to retrieve in this language (ISO 639-1 code)
     * @return
     */
    @GET("/3/movie/{id}")
    Call<MovieDTO> getMovie(@Path("id") int id,
                            @Query("api_key") String api_key,
                            @Query("language") String lang);

    /**
     * Retrieves a list of movies that are now on cinema.
     *
     * @param API_KEY  api_key used to retrieve information
     * @param page     page (Minimum 1, maximum 1000)
     * @param language to retrieve in this language (ISO 639-1 code)
     * @return
     */
    @GET("/3/movie/now_playing")
    Call<MovieListingDTO> getNowPlaying(@Query("api_key") String API_KEY,
                                        @Query("page") int page,
                                        @Query("language") String language);

}
