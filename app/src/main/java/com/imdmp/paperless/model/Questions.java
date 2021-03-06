package com.imdmp.paperless.model;

/**
 * Created by Dominic on 3/29/2017.
 */

public class Questions {


    public static final String TABLE ="questions";
    public static final String COLUMN_ID="_questionID";
    public static final String COLUMN_EVENTID = "eventID";
    public static final String COLUMN_SURVEYID="surveyID";
    public static final String COLUMN_ISQUALITATIVE = "isQualitatives";
    public static final String COLUMN_STRINGID= "stringID";
    public static final String COLUMN_QUESTION="question";



    private String question,parameterName,stringID;
    private Boolean isQualitative;
    private int id;

    public Questions() {
    }

    public Questions(String question, String parameterName, int id) {
        this.question = question;
        this.parameterName = parameterName;
        this.id = id;
    }

    public Questions(String question, String parameterName) {
        this.question = question;
        this.parameterName = parameterName;
    }

    public String getStringID(){
        return this.stringID;
    }
    public void setStringID(String stringID){
        this.stringID = stringID;
    }
    public Boolean getIsQualitative(){
        return  isQualitative;
    }
    public void setIsQualitative(Boolean isQualitative){
        this.isQualitative = isQualitative;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
