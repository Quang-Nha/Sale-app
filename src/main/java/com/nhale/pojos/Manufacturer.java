package com.nhale.pojos;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "manufacturer")
public class Manufacturer implements Serializable {
    private static final long serialVersionUID = -8262758284732799410L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Column(name = "country", length = 45)
    private String country;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "manufacturer_id")
    private Set<ProMan> promen = new LinkedHashSet<>();

    public Set<ProMan> getPromen() {
        return promen;
    }

    public void setPromen(Set<ProMan> promen) {
        this.promen = promen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}