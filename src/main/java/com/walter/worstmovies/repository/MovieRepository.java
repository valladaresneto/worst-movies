package com.walter.worstmovies.repository;

import com.walter.worstmovies.entity.Movie;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("select m from Movie m where m.winner = true")
    List<Movie> findAllWithAward();
}
