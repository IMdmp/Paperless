package com.example.dominic.paperless.Model;

/**
 * Created by Dominic on 3/29/2017.
 */

public class Questions {


    public static final String TABLE ="questions";
    public static final String COLUMN_ID="_questionID";
    public static final String COLUMN_EVENTID = "eventID";
    public static final String COLUMN_SURVEYID="surveyID";
    public static final String COLUMN_QUESTION="question";

    private String question;
    private int id;

    public Questions(String question, int id) {
        this.question = question;
        this.id = id;
    }

    public Questions(String question) {
        this.question = question;
    }

    public Questions() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
