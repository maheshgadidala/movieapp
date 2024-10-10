package org.movieflix.movieapp.Model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "movie_data")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movieId;

    @Column(nullable = false)
    @NotBlank(message = "please provide movie name")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "please provide movie Director name")
    private String director;

    @JsonDeserialize(using = org.movieflix.movieapp.util.StringToSetDeserializer.class)
    @ElementCollection
    @CollectionTable(name = "movie_cast")
    private Set<String> movieCast;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private Integer releaseYear;

    private String poster;

    // Ensure constructors correctly assign fields
    public Movie(@NotBlank(message = "please provide movie name") String title,
                 @NotBlank(message = "please provide movie Director name") String director,
                 Set<String> movieCast,
                 Double rating,
                 Integer releaseYear,
                 @NotBlank(message = "please provide movie url!") String poster) {
        this.title = title;
        this.director = director;
        this.movieCast = movieCast;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.poster = poster;
    }
}
