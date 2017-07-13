package com.example.dominic.paperless.Model;

/**
 * Created by Dominic on 3/29/2017.
 */

public class Event {


    public static final String TABLE = "eventform";
    public static final String COLUMN_ID = "_eventformID";
    public static final String COLUMN_TIMESTAKEN = "timestaken";
    public static final String COLUMN_HTMLNAME = "htmlname";
    public static final String COLUMN_ORGANIZERNAME="organizername";
    public static final String COLUMN_FORMNAME = "eventformName";
    public static final String COLUMN_ANSWERCLUSTER = "answercluster";

    private int id,timesTaken,answerCluster;
    private String formName,organizerName,htmlName;

    public Event(){

    }



    public Event(int id, String formName, String organizerName, String htmlName) {
        this.id = id;
        this.formName = formName;
        this.organizerName = organizerName;
        this.htmlName = htmlName;

    }

    public Event(String formName, String organizerName, String htmlName) {
        this.formName = formName;
        this.organizerName = organizerName;
        this.htmlName = htmlName;
    }

    public Event(int id, String formName, String organizerName, String htmlName, int timesTaken) {
        this.id =id;
        this.formName = formName;
        this.organizerName = organizerName;
        this.htmlName = htmlName;
        this.timesTaken=timesTaken;
    }

    public Event(int id, String formName, String organizerName, String htmlName,int timesTaken, int answerCluster) {
        this.id = id;
        this.timesTaken = timesTaken;
        this.answerCluster = answerCluster;
        this.formName = formName;
        this.organizerName = organizerName;
        this.htmlName = htmlName;
    }

    public int getAnswerCluster() {
        return answerCluster;
    }

    public void setAnswerCluster(int answerCluster) {
        this.answerCluster = answerCluster;
    }
    public int getTimesTaken() {
        return timesTaken;
    }

    public void setTimesTaken(int timesTaken) {
        this.timesTaken = timesTaken;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getHtmlName() {
        return htmlName;
    }

    public void setHtmlName(String htmlName) {
        this.htmlName = htmlName;
    }
}
