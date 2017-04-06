package com.example.dominic.paperless.Model;

/**
 * Created by Dominic on 3/29/2017.
 */

public class Answer {
    public static final String TABLE ="answers";
    public static final String COLUMN_ID="_answerID";
    public static final String COLUMN_QUESTIONID = "questionID";
    public static final String COLUMN_SURVEYID="surveyID";
    public static final String COLUMN_ISQUALITATIVE="isQualitative";
    public static final String COLUMN_ANSWER="answer";

    private int id;
    private String answer;
    private Boolean isQualitative;


    public Answer(String answer, Boolean isQualitative) {
        this.answer = answer;
        this.isQualitative = isQualitative;
    }

    public Answer() {
    }

    public Answer(int id, String answer, Boolean isQualitative) {

        this.id = id;
        this.answer = answer;
        this.isQualitative = isQualitative;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getQualitative() {
        return isQualitative;
    }

    public void setQualitative(Boolean qualitative) {
        isQualitative = qualitative;
    }
}
