package com.example.dominic.paperless;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.dominic.paperless.Model.Event;
import com.example.dominic.paperless.Model.Questions;

import static android.R.attr.x;

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
         Event e = new Event();
         e.setFormName("Alumni Homecoming");
         e.setOrganizerName("Student Lasallian Animators");
         e.setHtmlName("event_eval_survey.html");

        Event e1 = new Event();
        e1.setFormName("Social Demographics");
        e1.setOrganizerName("The surveyors");
        e1.setHtmlName("demographic_survey.html");

        Event e2 = new Event();
        e2.setFormName("Eval Survey");
        e2.setOrganizerName("Event solutions");
        e2.setHtmlName("recollection_eval_survey.html");


        databaseHelper.createEvent(e);
        databaseHelper.createEvent(e1);
        databaseHelper.createEvent(e2);


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
}
