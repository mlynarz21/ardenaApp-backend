package com.mlynarz.ardena.payload.Request;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

public class EventRequest {
    @NotBlank
//    @Size(max = 140)
    private String description;

    @NotNull
    @Size(min = 1, max = 6)
    @Valid
    private List<OptionRequest> options;

    @NotNull
    private Instant eventDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OptionRequest> getOptions() {
        return options;
    }

    public void setOptions(List<OptionRequest> options) {
        this.options = options;
    }

    public Instant getEventDate() {
        return eventDate;
    }

    public void setEventDate(Instant eventDate) {
        this.eventDate = eventDate;
    }


}
