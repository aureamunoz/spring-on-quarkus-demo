package org.acme.spring.web;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Book extends PanacheEntity {

    public String name;

    public Integer publicationYear;

}
