package com.walter.worstmovies.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Producer implements Comparable<Producer> {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Producer() {
    }

    public Producer(String name) {
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
        if (!(o instanceof Producer)) {
            return false;
        }
        Producer producer = (Producer) o;
        boolean sameValidIds = getId() != null && producer.getId() != null && Objects.equals(getId(), producer.getId());
        return sameValidIds || Objects.equals(getName(), producer.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public int compareTo(Producer producer) {
        if (this.getName() == null) {
            return -1;
        }
        if (producer.getName() == null) {
            return 1;
        }
        return this.getName().compareTo(producer.getName());
    }
}
