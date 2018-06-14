package com.howard.retrofitdemo.mapper;

import com.howard.retrofitdemo.api.APILink;
import com.howard.retrofitdemo.entity.MovieListingDTO;
import com.howard.retrofitdemo.model.Movie;

import java.util.ArrayList;
import java.util.List;


/**
 * This class knows how to transform a movieList entity to a model entity
 */
public class DTOModelEntitiesDataMapper {
    /**
     * Transform a movie list details movieList entity to a business details model
     *
     * @param dto
     * @return
     */
    public Movie transform(MovieListingDTO.MovieListDTO dto) {
        return new Movie(dto.getId(),
                         dto.getTitle(),
                         dto.getOriginalTitle(),
                         dto.getReleaseDate(),
                         createPosterLink(dto.getPosterPath()),
                         dto.getVoteAverage(),
                         dto.getPopularity()
        );
    }

    /**
     * Transform a movie list movieList entity to a business list model
     *
     * @param dto
     * @return
     */
    public List<Movie> transform(MovieListingDTO dto) {
        List<Movie> movies = new ArrayList<>();
        List<MovieListingDTO.MovieListDTO> dtoList = dto.getResults();
        for (MovieListingDTO.MovieListDTO movie : dtoList) {
            movies.add(transform(movie));
        }
        return movies;
    }

    /**
     * Transform a relative path to a complete URI poster image
     *
     * @param path
     * @return
     */
    private String createPosterLink(String path) {
        if (path == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(APILink.BASE_IMAGES_URL);
        stringBuilder.append(APILink.POSTER_SIZE);
        stringBuilder.append(path);
        return stringBuilder.toString();
    }


}
