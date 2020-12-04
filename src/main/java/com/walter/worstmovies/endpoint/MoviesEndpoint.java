package com.walter.worstmovies.endpoint;

import com.google.gson.Gson;
import com.walter.worstmovies.entity.Movie;
import com.walter.worstmovies.exception.ValidationException;
import com.walter.worstmovies.service.MovieService;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("movies")
public class MoviesEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesEndpoint.class);

    @Autowired
    private MovieService movieService;

    private static final Gson GSON = new Gson();

    @GetMapping
    public ResponseEntity findAll() {
        try {
            return ResponseEntity.ok(movieService.findAll());
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return ResponseEntity.status(500).body("Error on find all movies.");
        }
    }

    @PostMapping
    public ResponseEntity save(@RequestBody Movie movie) {
        try {
            return ResponseEntity.status(201).body(movieService.save(movie));
        } catch (ValidationException e) {
            LOGGER.error("Validation error", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return ResponseEntity.status(500).body("Error on save movie.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        try {
            Optional<Movie> optMovie = movieService.findById(id);
            if (!optMovie.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            Movie movie = optMovie.get();
            ResponseEntity<String> response = ResponseEntity.ok(GSON.toJson(movie));
            movieService.delete(movie);
            return response;
        } catch (ValidationException e) {
            LOGGER.error("Validation error", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error", e);
            return ResponseEntity.status(500).body("Error on delete movie.");
        }
    }
}
