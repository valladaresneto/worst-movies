package com.walter.worstmovies.configuration;

import com.walter.worstmovies.entity.Movie;
import com.walter.worstmovies.entity.Producer;
import com.walter.worstmovies.entity.Studio;
import com.walter.worstmovies.repository.MovieRepository;
import com.walter.worstmovies.repository.ProducerRepository;
import com.walter.worstmovies.repository.StudioRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

@Configuration
class LoadDatabase {

    @Bean
    @Transactional
    CommandLineRunner initDatabase(MovieRepository movieRepository, ProducerRepository producerRepository, StudioRepository studioRepository) {
        return args -> {
            loadMoviesFromCSV(movieRepository, producerRepository, studioRepository);
        };
    }

    private void loadMoviesFromCSV(MovieRepository movieRepository, ProducerRepository producerRepository, StudioRepository studioRepository)
            throws IOException {
        File file = ResourceUtils.getFile("classpath:csv/movielist.csv");
        Map<String, Movie> movies = new HashMap<>();
        Map<String, Producer> producers = new HashMap<>();
        Map<String, Studio> studios = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.lines().skip(1).forEach(line -> {
                String[] values = line.split(";");
                Movie movie = new Movie();
                movie.setYear(Integer.valueOf(values[0]));
                movie.setTitle(values[1]);

                if (movies.get(movie.getTitle()) == null) {
                    fillInStudios(studioRepository, studios, values, movie);
                    fillInProducers(producerRepository, producers, values, movie);

                    movie.setWinner(values.length >= 5 && "yes".equalsIgnoreCase(values[4]));
                    movieRepository.save(movie);
                    movies.put(movie.getTitle(), movie);
                }
            });
        }
    }

    private void fillInProducers(ProducerRepository producerRepository, Map<String, Producer> producers, String[] values, Movie movie) {
        String[] producersArray = values[2] != null ? values[3].split(",| and ") : null;
        movie.setProducers(new ArrayList<>());
        if (producersArray != null) {
            for (String s : producersArray) {
                Producer producer = producers.get(s.trim());
                if (producer != null) {
                    movie.getProducers().add(producer);
                    continue;
                }
                producer = new Producer(s.trim());
                producer = producerRepository.save(producer);
                producers.put(producer.getName(), producer);
                movie.getProducers().add(producer);
            }
        }
    }

    private void fillInStudios(StudioRepository studioRepository, Map<String, Studio> studios, String[] values, Movie movie) {
        String[] studiosArray = values[2] != null ? values[2].split(",| and ") : null;
        movie.setStudios(new ArrayList<>());
        if (studiosArray != null) {
            for (String s : studiosArray) {
                Studio studio = studios.get(s.trim());
                if (studio != null) {
                    movie.getStudios().add(studio);
                    continue;
                }
                studio = new Studio(s.trim());
                studio = studioRepository.save(studio);
                studios.put(studio.getName(), studio);
                movie.getStudios().add(studio);
            }
        }
    }
}
