package com.mlynarz.ardena.payload.Response;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public class PassResponse{

    private Long id;

    @NotNull
    private Instant expirationDate;

    @NotNull
    private int usedRides;

    @NotNull
    private int noOfRidesPermitted;

    public PassResponse(){

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

}
