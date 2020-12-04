package com.walter.worstmovies;

import static org.assertj.core.api.BDDAssertions.then;

import com.walter.worstmovies.entity.Movie;
import com.walter.worstmovies.entity.Producer;
import com.walter.worstmovies.entity.Studio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MoviesEndpointTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldReturn200AndMoviesListWhenGettingMoviesList() {
        String resorce = this.testRestTemplate.getRootUri() + "/movies";
        ResponseEntity entity = this.testRestTemplate.getForEntity(resorce, List.class);
        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List movies = (ArrayList) entity.getBody();
        then(movies.size()).isEqualTo(201);
    }

    @Test
    public void shouldReturn201AndMovieCreatedWhenPostingNewMovie() {
        List<Producer> producers = new ArrayList<>();
        String producerName = "Producer 123456";
        producers.add(new Producer(producerName));
        List<Studio> studios = new ArrayList<>();
        String studioName = "Studio 123456";
        studios.add(new Studio(studioName));
        String title = "Teste 123456";
        int year = 2000;
        Movie movie = new Movie(year, title, studios, producers, true);

        String resorce = this.testRestTemplate.getRootUri() + "/movies";
        ResponseEntity entity = this.testRestTemplate.postForEntity(resorce, movie, Movie.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(entity.getBody()).isInstanceOf(Movie.class);
        Movie movieCreated = (Movie) entity.getBody();
        then(movieCreated).isNotNull();
        then(movieCreated.getId()).isNotNull();
        then(movieCreated.getTitle()).isEqualTo(title);
        then(movieCreated.getYear()).isEqualTo(year);
        then(movieCreated.getWinner()).isEqualTo(true);
        then(movieCreated.getProducers().size()).isEqualTo(1);
        then(movieCreated.getProducers().get(0).getName()).isEqualTo(producerName);
        then(movieCreated.getStudios().size()).isEqualTo(1);
        then(movieCreated.getStudios().get(0).getName()).isEqualTo(studioName);
    }

    @Test
    public void shouldReturn400AndValidationMessageWhenPostingNewMovieWithoutTitle() {
        Movie movie = new Movie(null, null, null, null, null);

        String resorce = this.testRestTemplate.getRootUri() + "/movies";
        ResponseEntity entity = this.testRestTemplate.postForEntity(resorce, movie, String.class);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        then(entity.getBody()).isEqualTo("Title is required");
    }

    @Test
    public void shouldReturn200AndMovieWithIdWhenDeletingMovie() {
        Map<String, Integer> params = new HashMap<>();
        params.put("id", 3);

        String resorce = this.testRestTemplate.getRootUri() + "/movies/{id}";
        ResponseEntity<Movie> entity = this.testRestTemplate.exchange(resorce, HttpMethod.DELETE, null, Movie.class, params);

        then(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(entity.getBody()).isNotNull();
        then(entity.getBody()).isInstanceOf(Movie.class);
        then(entity.getBody().getId()).isEqualTo(3);
    }

}
