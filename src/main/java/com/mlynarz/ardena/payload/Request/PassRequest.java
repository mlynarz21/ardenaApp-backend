package com.mlynarz.ardena.payload.Request;

import javax.validation.constraints.NotNull;

public class PassRequest{

    @NotNull
    private int noOfRidesPermitted;

    public PassRequest(){

    }

    public PassRequest(@NotNull int noOfRidesPermitted) {
        this.noOfRidesPermitted = noOfRidesPermitted;
    }

    public int getNoOfRidesPermitted() {
        return noOfRidesPermitted;
    }

    public void setNoOfRidesPermitted(int noOfRidesPermitted) {
        this.noOfRidesPermitted = noOfRidesPermitted;
    }

}
