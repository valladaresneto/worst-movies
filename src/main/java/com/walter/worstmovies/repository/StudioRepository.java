package com.walter.worstmovies.repository;

import com.walter.worstmovies.entity.Studio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudioRepository extends JpaRepository<Studio, Long> {

    @Query("select s from Studio s where s.name = :name")
    Studio findByName(@Param("name") String name);
}
