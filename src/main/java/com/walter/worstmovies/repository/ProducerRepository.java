package com.walter.worstmovies.repository;

import com.walter.worstmovies.entity.Producer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

    @Query("select p from Producer p where p.name = :name")
    Producer findByName(@Param("name") String name);

}
