package org.movieflix.movieapp.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.movieflix.movieapp.Model.Movie;
import org.movieflix.movieapp.Repository.MovieRepository;
import org.movieflix.movieapp.dto.MovieDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpls implements MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;

    @Value("${file.upload.path}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    @Override
    @Transactional
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        //if filename already exist throw runtime exception
//        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename())))
//            throw new RuntimeException("File already exists");
        // Upload the file
        String uploadFilename = fileService.uploadFile(path, file);

        // Set the value of field poster as filename
        movieDto.setPoster(uploadFilename);

        // Map DTO to Movie object
        Movie movie = new Movie(
                null,
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getMovieCast(),
                movieDto.getRating(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );

        // Save the Movie object
        Movie savedMovie = movieRepository.save(movie);

        // In getMovieById and getAllMovies methods
        String movieUrl = baseUrl + "/images/" +uploadFilename;
        System.out.println(movieUrl);

        // Map Movie object to DTO and return it
        return new MovieDto(
                savedMovie.getMovieId(),
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getMovieCast(),
                savedMovie.getRating(),
                savedMovie.getReleaseYear(),
                savedMovie.getPoster(),
                movieUrl
        );

    }


    @Override
    public MovieDto getMovieById(Integer movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found: " + movieId));

// In getMovieById and getAllMovies methods
        String movieUrl = baseUrl + "/images/" + movie.getPoster();

        //map to moviedto and return it
        return new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getMovieCast(),
                movie.getRating(),
                movie.getReleaseYear(),
                movie.getPoster(),
                movieUrl
        );
    }

    @Override
    public List<MovieDto> getAllMovies() {

        //fetch all data from db
        List<Movie> movies = movieRepository.findAll();
        List<MovieDto> movieDtoList = new ArrayList<MovieDto>();
        //iterate through the list,generate poster url for each movie object and map to movieobj dto
        for (Movie movie : movies) {
            String movieUrl = baseUrl + "/images/" + movie.getPoster();
            movieDtoList.add(new MovieDto(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getMovieCast(),
                    movie.getRating(),
                    movie.getReleaseYear(),
                    movie.getPoster(),
                    movieUrl
            ));
        }
        return movieDtoList;
    }

    @Override
    public List<MovieDto> deleteAllMovies() {
        movieRepository.deleteAll();
        return getAllMovies();

    }

    @Override
    public String deleteMovieById(Integer movieId) throws IOException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found: " + movieId));
        Integer id = movie.getMovieId();
        Files.deleteIfExists(Paths.get(path + File.separator + movie.getPoster()));
        movieRepository.delete(movie);
        return "movie deleted successfully with id " + id;
    }

    @Override
    @Transactional
    public MovieDto updateMovieById(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found: " + movieId));

        // Update movie properties
        movie.setTitle(movieDto.getTitle());
        movie.setDirector(movieDto.getDirector());
        movie.setMovieCast(movieDto.getMovieCast());
        movie.setRating(movieDto.getRating());
        movie.setReleaseYear(movieDto.getReleaseYear());

        // Handle the file if provided
        if (file != null && !file.isEmpty()) {
            String uploadFilename = fileService.uploadFile(path, file);
            movie.setPoster(uploadFilename);
        }

        Movie updatedMovie = movieRepository.save(movie);

        // Return the updated DTO
        return new MovieDto(
                updatedMovie.getMovieId(),
                updatedMovie.getTitle(),
                updatedMovie.getDirector(),
                updatedMovie.getMovieCast(),
                updatedMovie.getRating(),
                updatedMovie.getReleaseYear(),
                updatedMovie.getPoster(),
                baseUrl + updatedMovie.getPoster()
        );
    }


}




