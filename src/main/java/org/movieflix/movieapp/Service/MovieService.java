package org.movieflix.movieapp.Service;


import org.movieflix.movieapp.Model.Movie;
import org.movieflix.movieapp.dto.MovieDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface MovieService {

    //add movie
    MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException;
    //get movie by id
    MovieDto getMovieById(Integer movieId);
    //get all movies
    List<MovieDto>getAllMovies();

    //delete all movies
    List<MovieDto>deleteAllMovies();
    //delete movie by id
    String deleteMovieById(Integer movieId) throws IOException;

    //update movie by id
    MovieDto updateMovieById(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException;


}



