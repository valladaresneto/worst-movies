package com.walter.worstmovies.service;

import com.walter.worstmovies.entity.Movie;
import com.walter.worstmovies.entity.Producer;
import com.walter.worstmovies.entity.Studio;
import com.walter.worstmovies.exception.ValidationException;
import com.walter.worstmovies.repository.MovieRepository;
import com.walter.worstmovies.repository.ProducerRepository;
import com.walter.worstmovies.repository.StudioRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private StudioRepository studioRepository;

    @Autowired
    private ProducerRepository producerRepository;

    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    public Optional<Movie> findById(Long id) {
        return movieRepository.findById(id);
    }

    public Map<String, Object> save(Movie movie) throws ValidationException {
        if(movie.getTitle() == null) {
            throw new ValidationException("Title is required");
        }

        if(movie.getStudios() != null && movie.getStudios().size() > 0) {
            List<Studio> studiosPersisted = new ArrayList<>();
            for(Studio studio : movie.getStudios()) {
                Studio savedStudio = studioRepository.findByName(studio.getName());
                studiosPersisted.add(savedStudio != null ? savedStudio : studioRepository.save(studio));
            }
            movie.setStudios(studiosPersisted);
        }
        if(movie.getProducers() != null && movie.getProducers().size() > 0) {
            List<Producer> producersPersisted = new ArrayList<>();
            for(Producer producer : movie.getProducers()) {
                Producer savedProducer = producerRepository.findByName(producer.getName());
                producersPersisted.add(savedProducer != null ? savedProducer : producerRepository.save(producer));
            }
            movie.setProducers(producersPersisted);
        }
        Movie savedMovie = movieRepository.save(movie);
        return transformMovieToMap(savedMovie);
    }

    public Map<String, Object> transformMovieToMap(Movie movie) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", movie.getId());
        result.put("year", movie.getYear());
        result.put("title", movie.getTitle());
        result.put("studios", movie.getStudios().stream().map(Studio::getName).collect(Collectors.toSet()));
        result.put("producers", movie.getProducers().stream().map(Producer::getName).collect(Collectors.toSet()));
        result.put("winner", movie.getWinner());
        return result;
    }

    public void delete(Movie movie) throws ValidationException {
        if(movie.getId() == null) {
            throw new ValidationException("Id is required");
        }
        movieRepository.delete(movie);
    }

}
