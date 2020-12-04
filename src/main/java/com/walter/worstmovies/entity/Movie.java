package com.walter.worstmovies.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Movie implements Comparable<Movie> {

    @Id
    @GeneratedValue
    private Long id;
    private Integer year;
    private String title;
    private Boolean winner;

    @ManyToMany
    @JoinTable(name = "movie_studio", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "studio_id"))
    private List<Studio> studios;

    @ManyToMany
    @JoinTable(name = "movie_producer", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns = @JoinColumn(name = "producer_id"))
    private List<Producer> producers;

    public Movie() {
    }

    public Movie(Integer year, String title, List<Studio> studios, List<Producer> producers, Boolean winner) {
        this.year = year;
        this.title = title;
        this.studios = studios;
        this.producers = producers;
        this.winner = winner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Studio> getStudios() {
        return studios;
    }

    public void setStudios(List<Studio> studios) {
        this.studios = studios;
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public void setProducers(List<Producer> producers) {
        this.producers = producers;
    }

    public Boolean getWinner() {
        return winner;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        Movie movie = (Movie) o;
        boolean sameValidIds = getId() != null && Objects.equals(getId(), movie.getId());
        return sameValidIds || (Objects.equals(getYear(), movie.getYear()) && Objects.equals(getTitle(), movie.getTitle()) && Objects
                .equals(getStudios(), movie.getStudios()) && Objects.equals(getProducers(), movie.getProducers()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getYear(), getTitle(), getStudios(), getProducers());
    }

    @Override
    public int compareTo(Movie movie) {
        if (this.getTitle() == null) {
            return -1;
        }
        if (movie.getTitle() == null) {
            return 1;
        }
        return this.getTitle().compareTo(movie.getTitle());
    }
}
