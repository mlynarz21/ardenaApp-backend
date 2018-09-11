package com.mlynarz.ardena.payload.Request;
import javax.validation.constraints.NotNull;

public class VoteRequest {
    @NotNull
    private Long optionId;

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }
}

