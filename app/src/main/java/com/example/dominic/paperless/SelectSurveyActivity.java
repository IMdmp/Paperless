package com.example.dominic.paperless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class SelectSurveyActivity extends AppCompatActivity {

    RecyclerView rvSelectSurvey;
    RecyclerView.Adapter rvAdapter;
    RecyclerView.LayoutManager rvLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_survey);

        rvSelectSurvey = (RecyclerView) findViewById(R.id.rv_select_survey);

        rvLayoutManager = new LinearLayoutManager(getBaseContext());
        rvSelectSurvey.setLayoutManager(rvLayoutManager);


    }
}
