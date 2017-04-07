package com.example.dominic.paperless;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dominic.paperless.Model.Answer;
import com.example.dominic.paperless.Model.Event;
import com.example.dominic.paperless.Model.Questions;
import com.example.dominic.paperless.Model.SurveyTaker;

import java.util.ArrayList;

import static android.R.attr.id;
import static android.content.ContentValues.TAG;

/**
 * Created by Dominic on 3/29/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String SCHEMA  = "paperless";
    public static final int    VERSION = 11;

    public DatabaseHelper(Context context) {

        super(context, SCHEMA, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE " + Event.TABLE + " ( "
                +    Event.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +    Event.COLUMN_ORGANIZERNAME + " TEXT NOT NULL , "
                +    Event.COLUMN_HTMLNAME + " TEXT NOT NULL , "
                +    Event.COLUMN_FORMNAME + " TEXT NOT NULL ); ";

        String sql2 = "CREATE TABLE " + SurveyTaker.TABLE + " ( "
                +     SurveyTaker.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +     SurveyTaker.COLUMN_EVENTID + " INTEGER, "
                +     SurveyTaker.COLUMN_DATEANSWERED + " TEXT NOT NULL, "
                +     SurveyTaker.COLUMN_RESPONDENTNAME +" TEXT NOT NULL, "
                +     SurveyTaker.COLUMN_RESPONDENTEMAIL+" TEXT NOT NULL, "
                +     SurveyTaker.COLUMN_IDNUMBER + " TEXT, "
                +     "FOREIGN KEY" + " (" + SurveyTaker.COLUMN_EVENTID+ " ) "+ " REFERENCES "
                +     Event.TABLE+"("+Event.COLUMN_ID+ ") );";


        String sql3 = "CREATE TABLE " + Questions.TABLE + " ( "
                +     Questions.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +     Questions.COLUMN_QUESTION + " TEXT NOT NULL, "
                +     Questions.COLUMN_EVENTID + " INTEGER, "
                +     Questions.COLUMN_SURVEYID +" INTEGER, "
                +     "FOREIGN KEY" + " (" + Questions.COLUMN_EVENTID+ " ) "+ " REFERENCES "
                +     Event.TABLE+"("+Event.COLUMN_ID+ "),"
                +     "FOREIGN KEY" + " (" + Questions.COLUMN_SURVEYID + " ) "+ " REFERENCES "
                +     SurveyTaker.TABLE+"("+SurveyTaker.COLUMN_ID+ ") );";

        String sql4 = "CREATE TABLE " + Answer.TABLE + " ( "
                +     Answer.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +     Answer.COLUMN_ISQUALITATIVE + " BOOLEAN NOT NULL, "
                +     Answer.COLUMN_QUESTIONID + " INTEGER, "
                +     Answer.COLUMN_SURVEYID + " INTEGER, "
                +     Answer.COLUMN_ANSWER + " TEXT NOT NULL ,"
                +     "FOREIGN KEY " + " (" + Answer.COLUMN_QUESTIONID + " )" + " REFERENCES "
                +     Questions.TABLE+"(" +Questions.COLUMN_ID+ "), "
                +     "FOREIGN KEY" + " (" + Answer.COLUMN_SURVEYID + " ) "+ " REFERENCES "
                +     SurveyTaker.TABLE+"("+SurveyTaker.COLUMN_ID+ ") );";


        db.execSQL(sql);
        Log.i(TAG,"database1 created successfully!" );
        db.execSQL(sql2);
        Log.i(TAG,"database2 created successfully!" );
        db.execSQL(sql3);
        Log.i(TAG,"database3 created successfully!" );
        db.execSQL(sql4);
        Log.i(TAG,"database4 created successfully!" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql ="DROP TABLE IF EXISTS " +Event.TABLE+ ";"



                +    SurveyTaker.TABLE + ","
                +    Answer.TABLE + ";";
        String sql2  ="DROP TABLE IF EXISTS "  +    Questions.TABLE+ ";";
        String sql3  ="DROP TABLE IF EXISTS "  +    SurveyTaker.TABLE+ ";";
        String sql4 = "DROP TABLE IF EXISTS "   +   Answer.TABLE +";";

            db.execSQL(sql);
            db.execSQL(sql2);
            db.execSQL(sql3);
        db.execSQL(sql4);
        onCreate(db);
    }

   // public void addAnswer

    public void test(){
        Log.i(TAG,"database log test.   " );
    }

    public long createEvent(Event e){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Event.COLUMN_FORMNAME,e.getFormName());
        cv.put(Event.COLUMN_ORGANIZERNAME,e.getOrganizerName());
        cv.put(Event.COLUMN_HTMLNAME,e.getHtmlName());
        long id= db.insert(Event.TABLE,null,cv);
        db.close();
        return  id;
    }

    public int getEventID(String eventName){
        SQLiteDatabase db = getReadableDatabase();

        //TODO: FIX SQL INJECTION SECURITY ISSUE
        String sql = "SELECT "+Event.COLUMN_ID + "  FROM " + Event.TABLE + " WHERE "+Event.COLUMN_FORMNAME+ " = "+"'" +eventName+"';";

        System.out.println("sql generated: "+sql);
        Cursor mCursor = db.rawQuery(sql, null);

        if (mCursor != null)
            mCursor.moveToFirst();

        int id = mCursor.getInt(mCursor.getColumnIndex(Event.COLUMN_ID));

        System.out.println("event id taken from query: "+id);

        db.close();
        return  id;
    }

    public int getSurveyTakerID(int idnumber) {

        SQLiteDatabase db = getReadableDatabase();

        //TODO: FIX SQL INJECTION SECURITY ISSUE
        String sql = "SELECT "+SurveyTaker.COLUMN_ID + "  FROM " + SurveyTaker.TABLE + " WHERE "+SurveyTaker.COLUMN_IDNUMBER+ " = "+"'" +idnumber+"';";

        System.out.println("sql generated: "+sql);
        Cursor mCursor = db.rawQuery(sql, null);

        if (mCursor != null)
            mCursor.moveToFirst();

        int id = mCursor.getInt(mCursor.getColumnIndex(SurveyTaker.COLUMN_ID));

        System.out.println("survey taker id taken from query: "+id);

        db.close();
        return id;

    }


        public int getQuestionID(String question){

        SQLiteDatabase db = getReadableDatabase();

        //TODO: FIX SQL INJECTION SECURITY ISSUE
        String sql = "SELECT "+Questions.COLUMN_ID + "  FROM " + Questions.TABLE + " WHERE "+Questions.COLUMN_QUESTION+ " LIKE "+"'%" +question+"%';";

        System.out.println("sql generated: "+sql);
        Cursor mCursor = db.rawQuery(sql, null);

        if (mCursor != null)
            mCursor.moveToFirst();

        int id = mCursor.getInt(mCursor.getColumnIndex(Questions.COLUMN_ID));

        System.out.println("question id taken from query: "+id);

        db.close();
        return id;
    }
    public long createSurveyTaker(SurveyTaker st,int EventID){
        SQLiteDatabase db  = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SurveyTaker.COLUMN_DATEANSWERED,st.getDateAnswered());
        cv.put(SurveyTaker.COLUMN_IDNUMBER,st.getIdNumber());
        cv.put(SurveyTaker.COLUMN_RESPONDENTEMAIL,st.getRespondentEmail());
        cv.put(SurveyTaker.COLUMN_RESPONDENTNAME,st.getRespondentName());
        cv.put(SurveyTaker.COLUMN_EVENTID,EventID);


        long id= db.insert(SurveyTaker.TABLE,null,cv);
        db.close();
        return  id;

    }


    public long addQuestion(Questions q,int eventID,int surveyID ){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Questions.COLUMN_QUESTION,q.getQuestion());
        cv.put(Questions.COLUMN_EVENTID,eventID);
        cv.put(Questions.COLUMN_SURVEYID,surveyID);

        long id= db.insert(Questions.TABLE,null,cv);
        db.close();
        return  id;

    }
    public long addAnswer(Answer a,int questionID,int surveyTakerID){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Answer.COLUMN_ANSWER,a.getAnswer());


        cv.put(Answer.COLUMN_ISQUALITATIVE,a.getQualitative());
        cv.put(Answer.COLUMN_QUESTIONID,questionID);
        cv.put(Answer.COLUMN_SURVEYID,surveyTakerID);



        long id= db.insert(Answer.TABLE,null,cv);
        db.close();
        return  id;
    }
    public Cursor getData(Questions q,Boolean isQualitative){
        SQLiteDatabase db = getReadableDatabase();

        //TODO: FIX SQL INJECTION SECURITY ISSUE
        String sql = "SELECT * " + "  FROM " + Answer.TABLE + " WHERE (("+Answer.COLUMN_ISQUALITATIVE +" = " + isQualitative+") AND (" +Answer.COLUMN_QUESTIONID+ " = " +q.getId()+ "));";

        Cursor  c = db.rawQuery(sql,null);
        return c;
    }


    public Cursor getAllEvents(){
        SQLiteDatabase db = getReadableDatabase();

        //SELECT * FROM TASK;
        return db.query(Event.TABLE,
                null,
                null,
                null,
                null,null,null);
    }
//    public int[] getQuantitativeAnswersFromQuestion(Questions q){
//        SQLiteDatabase db = getReadableDatabase();
//
//        Cursor c = db.query(Answer.TABLE,
//                null,
//               Answer.COLUMN_ID+ "=?",
//                new String[]{q.getId()+""},
//                null,//group by
//                null,// having
//                null);//orderby
//
//
//
//    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

}
