package com.example.dominic.paperless.Model;

/**
 * Created by Dominic on 3/29/2017.
 */

public class Event {


    public static final String TABLE = "eventform";
    public static final String COLUMN_ID = "_eventformID";

    public static final String COLUMN_ORGANIZERNAME="organizername";
    public static final String COLUMN_FORMNAME = "eventformName";

    private int id;
    private String formName,organizerName;

    public Event(String formName, String organizerName) {
        this.formName = formName;
        this.organizerName = organizerName;
    }

    public Event(int id, String formName, String organizerName) {

        this.id = id;
        this.formName = formName;
        this.organizerName = organizerName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public Event(){}

    public Event(int id, String formName) {
        this.id = id;
        this.formName = formName;
    }

    public Event(String formName) {
        this.formName = formName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
