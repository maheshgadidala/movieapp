package org.movieflix.movieapp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    private Integer movieId;

    @NotBlank(message = "Please provide movie name")
    private String title;

    @NotBlank(message = "Please provide movie director name")
    private String director;

    private Set<String> movieCast;

    @NotNull(message = "Please provide movie rating")
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "Rating must not exceed 10.0")
    private Double rating;


    @NotNull(message = "Please provide release year")
    @Min(value = 1888, message = "Release year must be after the invention of movies")
    private Integer releaseYear;

    @NotBlank(message = "Please provide movie poster URL")
    private String poster;

    private String posterUrl;

    // Removed the faulty constructor
}
