package org.movieflix.movieapp.Repository;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.movieflix.movieapp.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Integer> {
   // boolean existMovieTitleAndReleaseYear(@NotBlank(message = "Please provide movie name") String title, @NotNull(message = "Please provide release year") @Min(value = 1888, message = "Release year must be after the invention of movies") Integer releaseYear);


    boolean existsByTitleAndReleaseYear(@NotBlank(message = "Please provide movie name") String title, @NotNull(message = "Please provide release year") Integer releaseYear);
}
