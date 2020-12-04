package com.walter.worstmovies.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Studio implements Comparable<Studio> {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Studio() {
    }

    public Studio(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Studio)) {
            return false;
        }
        Studio studio = (Studio) o;
        boolean sameValidIds = getId() != null && studio.getId() != null && Objects.equals(getId(), studio.getId());
        return sameValidIds || Objects.equals(getName(), studio.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public int compareTo(Studio studio) {
        if (this.getName() == null) {
            return -1;
        }
        if (studio.getName() == null) {
            return 1;
        }
        return this.getName().compareTo(studio.getName());
    }
}
