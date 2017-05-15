package com.example.dominic.paperless.Model;

/**
 * Created by Dominic on 3/29/2017.
 */

public class SurveyTaker {
    public static final String TABLE = "surveytaker";
    public static final String COLUMN_ID ="_surveytakerID";
    public static final String COLUMN_EVENTID = "eventID";
    public static final String COLUMN_DATEANSWERED = "dateAnswered";
    public static final String COLUMN_RESPONDENTNAME="respondentName";
    public static final String COLUMN_RESPONDENTEMAIL="respondentEmail";
    public static final String COLUMN_IDNUMBER ="idNumber";

    private int id;
    private String dateAnswered;
    private String respondentName;
    private String respondentEmail;
    private String idNumber;

    public SurveyTaker(int id, String dateAnswered, String respondentName, String respondentEmail, String idNumber) {
        this.id = id;
        this.dateAnswered = dateAnswered;
        this.respondentName = respondentName;
        this.respondentEmail = respondentEmail;
        this.idNumber = idNumber;
    }

    public SurveyTaker(String dateAnswered, String respondentName, String respondentEmail, String idNumber) {
        this.dateAnswered = dateAnswered;
        this.respondentName = respondentName;
        this.respondentEmail = respondentEmail;
        this.idNumber = idNumber;
    }

    public SurveyTaker() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateAnswered() {
        return dateAnswered;
    }

    public void setDateAnswered(String dateAnswered) {
        this.dateAnswered = dateAnswered;
    }

    public String getRespondentName() {
        return respondentName;
    }

    public void setRespondentName(String respondentName) {
        this.respondentName = respondentName;
    }

    public String getRespondentEmail() {
        return respondentEmail;
    }

    public void setRespondentEmail(String respondentEmail) {
        this.respondentEmail = respondentEmail;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
