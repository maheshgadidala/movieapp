package org.movieflix.movieapp.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.movieflix.movieapp.Model.Movie;
import org.movieflix.movieapp.Service.MoviePageResponse;
import org.movieflix.movieapp.Service.MovieService;
import org.movieflix.movieapp.Util.AppConstants;
import org.movieflix.movieapp.dto.MovieDto;
import org.movieflix.movieapp.exception.EmptyFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @Value("${file.upload.path}")
    private String uploadPath;

    // Add movie
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add-movie")
    public ResponseEntity<MovieDto> addMovie(@RequestPart MultipartFile file,
                                             @RequestPart String movieDto) throws IOException, EmptyFileException {

        //throw exception if file is empty
        if (file.isEmpty()) {
            throw new EmptyFileException("File is empty,Please send another file");
        }
        MovieDto dto = convertMovieDto(movieDto);
        return new ResponseEntity<>(movieService.addMovie(dto, file), HttpStatus.CREATED);
    }

    // Get movie by ID
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Integer movieId) {
        MovieDto movieDto = movieService.getMovieById(movieId);
        return new ResponseEntity<>(movieDto, HttpStatus.OK);
    }

    // Get all movies
    @GetMapping("/AllMovies")
    public ResponseEntity<String> getAllMovies() throws JsonProcessingException {
        List<MovieDto> movieDtoList = movieService.getAllMovies();
        ObjectMapper objectMapper = new ObjectMapper();
        return new ResponseEntity<>(objectMapper.writeValueAsString(movieDtoList), HttpStatus.OK);
    }

    // Update movie by ID
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/movies/{movieId}", consumes = {"multipart/form-data"})
    public ResponseEntity<MovieDto> updateMovieById(
            @PathVariable Integer movieId,
            @RequestPart("movieDto") String movieDtoString,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        MovieDto movieDto = convertMovieDto(movieDtoString);
        MovieDto updatedMovie = movieService.updateMovieById(movieId, movieDto, file);
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    // Delete movie by ID
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/movies/{movieId}")
    public ResponseEntity<String> deleteMovieById(@PathVariable Integer movieId) throws IOException {
        movieService.deleteMovieById(movieId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete all movies
    @DeleteMapping("/DeleteAllMovies")
    public ResponseEntity<String> deleteAllMovies() {
        movieService.deleteAllMovies();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Serve movie poster image
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(uploadPath).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        //add all types of content types
                        .header(HttpHeaders.CONTENT_TYPE, "image/png")
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //get all movies in pagination with page number
    @GetMapping("/allMoviesPage")
    public ResponseEntity<MoviePageResponse> getMoviesWithPagination(
            @RequestParam(defaultValue = AppConstants.CURRENT_PAGE_NUMBER, required = false) Integer currentPageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize) {
        MoviePageResponse moviePageResponse = movieService.getAllMoviesWithPagination(currentPageNumber, pageSize);
        return ResponseEntity.ok(moviePageResponse);
    }
    @GetMapping("/allMoviesPageAndSort")
    public ResponseEntity<MoviePageResponse> getMoviesWithPaginationAndSort(
            @RequestParam(defaultValue = AppConstants.CURRENT_PAGE_NUMBER, required = false) Integer currentPageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR, required = false) String dir) {
        MoviePageResponse moviePageResponse = movieService.getAllMoviesWithPaginationAndSorting(currentPageNumber, pageSize, sortBy, dir);
        return ResponseEntity.ok(moviePageResponse);
    }

    // Convert String to MovieDto object
    private MovieDto convertMovieDto(String movieDtoObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return objectMapper.readValue(movieDtoObj, MovieDto.class);
    }
}