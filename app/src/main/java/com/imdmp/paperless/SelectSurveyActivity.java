package com.imdmp.paperless;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.imdmp.paperless.model.Event;
import com.imdmp.paperless.model.Questions;

public class SelectSurveyActivity extends Activity {



    RecyclerView rvSelectSurvey;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;
    DatabaseHelper databaseHelper;
 public   int eventID;
    SurveyAdapter surveyAdapter;
    private static final String TAG = "LOCAL_SERVER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_survey);

        rvSelectSurvey = (RecyclerView) findViewById(R.id.rv_select_survey);

        rvLayoutManager = new LinearLayoutManager(getBaseContext());
        rvSelectSurvey.setLayoutManager(rvLayoutManager);

         databaseHelper = new DatabaseHelper(getBaseContext());


        fillDB();


    }

    @Override
    protected void onResume() {
        super.onResume();

        Cursor newData = databaseHelper.getAllEvents();
        if(rvSelectSurvey.getAdapter() ==null) {
            //during the first run

            surveyAdapter
                    = new SurveyAdapter(getBaseContext(),newData);
            surveyAdapter.setOnItemClickListener(new SurveyAdapter.OnItemClickListener() {
                                                     @Override
                                                     public void onItemClick(View v) {
                                                         Intent i = new Intent();
                                                         i.setClass(v.getContext(),StartServerActivity.class);
                                                       eventID = (int) v.getTag(R.string.eventID);
                                                         i.putExtra("htmlName",(String) v.getTag(R.string.htmlName));
                                                         i.putExtra("eventID",(int) v.getTag(R.string.eventID));
                                                      //   i.putExtra("id", (int) v.getTag());
                                                         v.getContext().startActivity(i);
                                                     }
                                                 });

                    rvSelectSurvey.setAdapter(surveyAdapter);

        }else{
            //during add new task return
            //insert new task at the end of the list

//            surveyAdapter.swapCursor(newData);
   //         surveyAdapter.notifyItemInserted(surveyAdapter.getCursor().getCount());
        }

    }


    //fills up DB with setValues

    private void fillDB(){
        Log.i(TAG,"filling db...");
        Event e = new Event();
        e.setFormName("Alumni Homecoming");
        e.setOrganizerName("Student Lasallian Animators");
        e.setHtmlName("event_eval_survey.html");
        e.setTimesTaken(1);
        e.setAnswerCluster(1);
        e.setId(1);

        Event e1 = new Event();
        e1.setFormName("Social Demographics");
        e1.setOrganizerName("The surveyors");
        e1.setHtmlName("demographic_survey.html");
        e1.setTimesTaken(1);
        e1.setAnswerCluster(1);

        Event e2 = new Event();
        e2.setFormName("Eval Survey");
        e2.setOrganizerName("Event solutions");
        e2.setHtmlName("recollection_eval_survey.html");
        e2.setTimesTaken(1);
        e2.setAnswerCluster(1);


        databaseHelper.createEvent(e);
        databaseHelper.createEvent(e1);
        databaseHelper.createEvent(e2);


        Questions q1 = new Questions();
        q1.setQuestion("Full Name");
        q1.setIsQualitative(Boolean.TRUE);
        q1.setStringID("name");

        Questions q2 = new Questions();
        q2.setQuestion("ID Number");
        q2.setIsQualitative(Boolean.TRUE);
        q2.setStringID("idnum");

        Questions q3 = new Questions();
        q3.setQuestion("How would you rate this event?");
        q3.setIsQualitative(Boolean.FALSE);
        q3.setStringID("question1");

        Questions q4 = new Questions();
        q4.setQuestion("How would you rate your host?");
        q4.setIsQualitative(Boolean.FALSE);
        q4.setStringID("question2");

        Questions q5 = new Questions();
        q5.setQuestion("Sex");
        q5.setIsQualitative(Boolean.TRUE);
        q5.setStringID("question1");

        Questions q6 = new Questions();
        q6.setQuestion("Hobbies");
        q6.setIsQualitative(Boolean.TRUE);
        q6.setStringID("question2");

        Questions q7 = new Questions();
        q7.setQuestion("Biography");
        q7.setIsQualitative(Boolean.TRUE);
        q7.setStringID("question3");

        Questions q8 = new Questions();
        q8.setQuestion("How would you rate your recollection?");
        q8.setIsQualitative(Boolean.FALSE);
        q8.setStringID("question1");

        Questions q9 = new Questions();
        q9.setQuestion("How would you rate the facilitator?");
        q9.setIsQualitative(Boolean.FALSE);
        q9.setStringID("question2");

        Questions q10 = new Questions();
        q10.setQuestion("How would you rate the co-facilitator?");
        q10.setIsQualitative(Boolean.FALSE);
        q10.setStringID("question3");

        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        int i = databaseHelper.getEventID(e.getFormName());
        Log.i(TAG,"event ID : "+i);
        databaseHelper.addQuestion(q1,i);

        databaseHelper.addQuestion(q2,i);
        databaseHelper.addQuestion(q3,i);
        databaseHelper.addQuestion(q4,i);

        //   DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());


        i = databaseHelper.getEventID(e1.getFormName());
        databaseHelper.addQuestion(q1,i);
        databaseHelper.addQuestion(q2,i);
        databaseHelper.addQuestion(q5,i);
        databaseHelper.addQuestion(q6,i);
        databaseHelper.addQuestion(q7,i);
        //  databaseHelper.addQuestion(q4,eventID);

        i = databaseHelper.getEventID(e2.getFormName());

        databaseHelper.addQuestion(q1,i);
        databaseHelper.addQuestion(q2,i);
        databaseHelper.addQuestion(q8,i);
        databaseHelper.addQuestion(q9,i);
        databaseHelper.addQuestion(q10,i);


        Log.i(TAG,"Db filled!");

    }
}
