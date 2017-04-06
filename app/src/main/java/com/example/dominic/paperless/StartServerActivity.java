package com.example.dominic.paperless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StartServerActivity extends AppCompatActivity {

    ImageButton ibBack, ibStartServer;
    TextView tvServerStatus,tvEventName, tvConnected, tvEvaluated;
    EditText etIPAdress;
    LinearLayout llViewStats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_server);


        //find views by id
            ibBack = (ImageButton) findViewById(R.id.ib_back);
            ibStartServer = (ImageButton) findViewById(R.id.ib_startserver);

            tvServerStatus = (TextView) findViewById(R.id.tv_server_status);
            tvEventName = (TextView) findViewById(R.id.tv_event_name);
            tvConnected = (TextView) findViewById(R.id.tv_connected);
            tvEvaluated = (TextView) findViewById(R.id.tv_evaluated);

            etIPAdress = (EditText) findViewById(R.id.et_ip_address);

            llViewStats = (LinearLayout) findViewById(R.id.ll_view_stats);


        //set on click listeners
            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ibStartServer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            tvServerStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            llViewStats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
    }
}
