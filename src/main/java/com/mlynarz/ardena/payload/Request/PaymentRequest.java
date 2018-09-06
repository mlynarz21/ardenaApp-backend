package com.mlynarz.ardena.payload.Request;
import com.mlynarz.ardena.model.Status;

public class PaymentRequest {

    private Status status;

    public PaymentRequest() {

    }

    public PaymentRequest(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
