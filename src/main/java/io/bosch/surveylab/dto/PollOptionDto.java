package io.bosch.surveylab.dto;

public class PollOptionDto extends BaseDtoEntity{

    private String text;

    private int numberOfVotes;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

}
