package com.mlynarz.ardena.model;

public class OptionVoteCount {
    private Long optionId;
    private Long voteCount;

    public OptionVoteCount(Long optionId, Long voteCount) {
        this.optionId = optionId;
        this.voteCount = voteCount;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }
}

