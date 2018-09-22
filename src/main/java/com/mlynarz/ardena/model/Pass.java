package com.mlynarz.ardena.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "passes")
public class Pass implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Instant expirationDate;

    @NotNull
    private int usedRides;

    @NotNull
    private int noOfRidesPermitted;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    public Pass(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getUsedRides() {
        return usedRides;
    }

    public void setUsedRides(int usedRides) {
        this.usedRides = usedRides;
    }

    public int getNoOfRidesPermitted() {
        return noOfRidesPermitted;
    }

    public void setNoOfRidesPermitted(int noOfRidesPermitted) {
        this.noOfRidesPermitted = noOfRidesPermitted;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pass pass = (Pass) o;
        return usedRides == pass.usedRides &&
                noOfRidesPermitted == pass.noOfRidesPermitted &&
                Objects.equals(expirationDate, pass.expirationDate) &&
                Objects.equals(owner, pass.owner);
    }

    @Override
    public int hashCode() {

        return Objects.hash(expirationDate, usedRides, noOfRidesPermitted, owner);
    }
}
