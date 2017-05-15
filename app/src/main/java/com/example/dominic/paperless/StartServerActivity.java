package com.example.dominic.paperless;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dominic.paperless.Model.Questions;

import java.io.IOException;
import java.io.InputStream;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.example.dominic.paperless.MainActivity.convertStreamToString;
import static com.example.dominic.paperless.R.drawable.power_button_off;
import static com.example.dominic.paperless.R.id.tv_message;

public class StartServerActivity extends Activity {
    private static final int DEFAULT_PORT = 8080;
    private static final String TAG = "LOCAL_SERVER";

    ImageButton ibBack, ibStartServer;
    Button button_checkdb;
    TextView tvServerStatus,tvEventName, tvConnected, tvEvaluated;
    EditText etIPAdress;
    LinearLayout llViewStats;
    String textfile;
    private WebServer adr;

    private static boolean isStarted = false;
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
        button_checkdb = (Button) findViewById(R.id.button_dbcheck);
        etIPAdress = (EditText) findViewById(R.id.et_ip_address);

            llViewStats = (LinearLayout) findViewById(R.id.ll_view_stats);

        Questions q1 = new Questions();
        q1.setQuestion("Full Name");
        q1.setIsQualitative(Boolean.TRUE);


        Questions q2 = new Questions();
        q2.setQuestion("ID Number");
        q2.setIsQualitative(Boolean.TRUE);

        Questions q3 = new Questions();
        q3.setQuestion("How would you rate this event?");
        q3.setIsQualitative(Boolean.FALSE);

        Questions q4 = new Questions();
        q4.setQuestion("How would you rate your host?");
        q4.setIsQualitative(Boolean.FALSE);

        String htmlName = getIntent().getStringExtra("htmlName");
        int eventID = getIntent().getIntExtra("eventID",2);
        Log.i(TAG, "GOT EVENT ID: "+eventID);

        Questions q5 = new Questions();
        q5.setQuestion("Sex");
        q5.setIsQualitative(Boolean.TRUE);

        Questions q6 = new Questions();
        q6.setQuestion("Hobbies");
        q6.setIsQualitative(Boolean.TRUE);

        Questions q7 = new Questions();
        q7.setQuestion("Biography");
        q7.setIsQualitative(Boolean.TRUE);

        Questions q8 = new Questions();
        q8.setQuestion("How would you rate your recollection?");
        q8.setIsQualitative(Boolean.FALSE);

        Questions q9 = new Questions();
        q9.setQuestion("How would you rate the facilitator?");
        q9.setIsQualitative(Boolean.FALSE);

        Questions q10 = new Questions();
        q10.setQuestion("How would you rate the co-facilitator?");
        q10.setIsQualitative(Boolean.FALSE);


        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        databaseHelper.addQuestion(q1,1);
        databaseHelper.addQuestion(q2,1);
        databaseHelper.addQuestion(q3,1);
        databaseHelper.addQuestion(q4,1);

     //   DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        databaseHelper.addQuestion(q1,2);
        databaseHelper.addQuestion(q2,2);
        databaseHelper.addQuestion(q5,2);
        databaseHelper.addQuestion(q6,2);
        databaseHelper.addQuestion(q7,2);
      //  databaseHelper.addQuestion(q4,eventID);

        databaseHelper.addQuestion(q1,3);
        databaseHelper.addQuestion(q2,3);
        databaseHelper.addQuestion(q8,3);
        databaseHelper.addQuestion(q9,3);
        databaseHelper.addQuestion(q10,3);



             //set on click listeners
            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    adr.stop();
                    isStarted=false;
                }
            });
            ibStartServer.setTag(R.string.htmlName,htmlName);
            ibStartServer.setTag(R.string.eventID,eventID);
            ibStartServer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    adr = new WebServer(DEFAULT_PORT);

                    DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
                    InputStream is;
                    try {
                       String html= (String) v.getTag(R.string.htmlName);
                        is = getResources().getAssets().open(html);
                        textfile = convertStreamToString(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int eventID = (int) v.getTag(R.string.eventID);
                    adr.setHTMLFile(textfile);
                    adr.setContext(getBaseContext());
                    adr.setDbHelper(databaseHelper);
                    adr.setEventID(eventID);

                    //  testDatabase();

                    try {
                        //start
                        if (!isStarted) {
                            adr.start();
                            WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                            String ip = android.text.format.Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

                            ///TODO: IF DOESN WORK DELETE SHIT
                           String ip2 = "Server running at " + ip + ":8080";
                            Log.i(TAG, "Server running at " + ip + ":8080");
                            etIPAdress.setText(ip2);
                            ibStartServer.setImageResource(R.drawable.power_button_on);
//                            tv_message.setText("\"Server running at \" " + ip + "\":8080\" \n Connected phones ip address: 192.168.42.1:8080");
//                            tv_message.setVisibility(View.VISIBLE);
                            isStarted = true;
                        }
                        else {
//                            isStarted = false;
//                            adr.stop();
//                            Log.i(TAG, "Server Stopped.");
//                            ibStartServer.setImageResource(R.drawable.power_button_off);
                        }
                    } catch (IOException ioe) {
                        Log.w(TAG, "The server could not start.");
                    }



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
                    Intent dbmanager = new Intent(getBaseContext(),AndroidDatabaseManager.class);
                    startActivity(dbmanager);
                }
            });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adr != null) {
            adr.stop();
            System.out.println("stopping server.");
        }
    }
}
