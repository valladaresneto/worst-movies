package com.walter.worstmovies.service;

import com.walter.worstmovies.entity.Movie;
import com.walter.worstmovies.entity.Producer;
import com.walter.worstmovies.repository.MovieRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

    public Map<String, List<Map<String, String>>> periodBetweenAwards() {
        Map<String, List<Map<String, String>>> result = new HashMap<>();
        result.put("min", getProducersWithPeriodBetweenAwards(false));
        result.put("max", getProducersWithPeriodBetweenAwards(true));
        return result;
    }

    private List<Map<String, String>> getProducersWithPeriodBetweenAwards(boolean bigger) {
        List<Movie> moviesWithAward = movieRepository.findAllWithAward();
        Map<Producer, List<Movie>> moviesWithAwardByProducer = new HashMap<>();
        moviesWithAward.forEach(movie -> {
            movie.getProducers().forEach(producer -> {
                List<Movie> movieList = moviesWithAwardByProducer.computeIfAbsent(producer, k -> new ArrayList<>());
                movieList.add(movie);
            });
        });
        int count = bigger ? 0 : 9999;

        Set<Entry<Producer, List<Movie>>> producersWithMoreThanOneAward = moviesWithAwardByProducer.entrySet().stream()
                .filter(producerListEntry -> producerListEntry.getValue().size() > 1).collect(Collectors.toSet());

        List<Map<String, String>> result = new ArrayList<>();
        for(Entry<Producer, List<Movie>> entry: producersWithMoreThanOneAward) {
            Integer first = entry.getValue().stream().map(Movie::getYear).reduce(9999, Integer::min);
            Integer last = entry.getValue().stream().map(Movie::getYear).reduce(0, Integer::max);
            if(last - first == count) {
                result.add(populateMapWithProducerInfo(entry, first, last));
                continue;
            }
            if(bigger ? last - first > count : last - first < count) {
                result.clear();
                result.add(populateMapWithProducerInfo(entry, first, last));
                count = last - first;
            }
        }
        return result;
    }

    private Map<String, String> populateMapWithProducerInfo(Entry<Producer, List<Movie>> entry, Integer first, Integer last) {
        Map<String, String> resultItem = new LinkedHashMap<>();
        resultItem.put("producer", entry.getKey().getName());
        resultItem.put("interval", String.valueOf(last - first));
        resultItem.put("previousWin", String.valueOf(first));
        resultItem.put("followingWin", String.valueOf(last));
        return resultItem;
    }
}
