package org.movieflix.movieapp.Repository;


import jakarta.validation.constraints.NotBlank;
import org.movieflix.movieapp.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Integer> {


}
