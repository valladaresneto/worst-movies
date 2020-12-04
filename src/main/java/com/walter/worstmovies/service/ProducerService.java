package com.walter.worstmovies.service;

import com.walter.worstmovies.entity.Movie;
import com.walter.worstmovies.entity.Producer;
import com.walter.worstmovies.repository.MovieRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Producer> biggerPeriodBetweenAwards() {
        return getProducersWithPeriodBetweenAwards(true);
    }

    public List<Producer> shorterPeriodBetweenAwards() {
        return getProducersWithPeriodBetweenAwards(false);
    }

    private List<Producer> getProducersWithPeriodBetweenAwards(boolean bigger) {
        List<Movie> moviesWithAward = movieRepository.findAllWithAward();
        Map<Producer, List<Movie>> moviesWithAwardByProducer = new HashMap<>();
        moviesWithAward.forEach(movie -> {
            movie.getProducers().forEach(producer -> {
                List<Movie> movieList = moviesWithAwardByProducer.computeIfAbsent(producer, k -> new ArrayList<>());
                movieList.add(movie);
            });
        });
        List<Producer> producers = new ArrayList<>();
        int count = bigger ? 0 : 9999;

        Set<Entry<Producer, List<Movie>>> filteredEntries = moviesWithAwardByProducer.entrySet().stream()
                .filter(producerListEntry -> producerListEntry.getValue().size() > 1).collect(Collectors.toSet());

        for(Entry<Producer, List<Movie>> entry: filteredEntries) {
            Integer first = entry.getValue().stream().map(Movie::getYear).reduce(9999, Integer::min);
            Integer last = entry.getValue().stream().map(Movie::getYear).reduce(0, Integer::max);
            if(last - first == count) {
                producers.add(entry.getKey());
                continue;
            }
            if(bigger ? last - first > count : last - first < count) {
                producers.clear();
                producers.add(entry.getKey());
                count = last - first;
            }
        }
        return producers;
    }
}
