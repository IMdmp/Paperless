package com.imdmp.paperless;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.imdmp.paperless.model.Answer;
import com.imdmp.paperless.model.Event;
import com.imdmp.paperless.model.Questions;
import com.imdmp.paperless.model.SurveyTaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import static android.R.attr.button;
import static android.R.attr.data;


public class MainActivity extends AppCompatActivity {
    private static final int DEFAULT_PORT = 8080;
    private static final String TAG = "LOCAL_SERVER";


    // INSTANCE OF ANDROID WEB SERVER  = WebServer
    private BroadcastReceiver broadcastReceiverNetworkState;
    private static boolean isStarted = false;
    private WebServer adr;


    //VIEW
    Button button_startserver, button_stopserver, button_select,button_checkdb,button_test;
    TextView tv_message;
    RecyclerView rv_task;
    String textfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CONNECT VARIABLES
        button_startserver = (Button) findViewById(R.id.button_startserver);
        button_stopserver = (Button) findViewById(R.id.button_stopserver);
        tv_message = (TextView) findViewById(R.id.tv_message);
        rv_task = (RecyclerView) findViewById(R.id.rv_task);
        button_select = (Button) findViewById(R.id.button_select);
        button_checkdb = (Button) findViewById(R.id.button_dbcheck);
        button_test = (Button) findViewById(R.id.button_test);
        tv_message.setVisibility(View.INVISIBLE);

        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDatabase();
                Log.i(TAG, "testing database...");
            }
        });
        button_startserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiate new web server
                try {
                    adr = new WebServer(DEFAULT_PORT,getBaseContext(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
                InputStream is;
                try {
                    is = getResources().getAssets().open("sample_form.html");
                    textfile = convertStreamToString(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                adr.setHTMLFile(textfile);
//                adr.setContext(getBaseContext());
//                adr.setDbHelper(databaseHelper);


            //  testDatabase();

                try {
                    //start
                    if (!isStarted) {
                        adr.start();
                        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                        String ip = android.text.format.Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                        Log.i(TAG, "Server running at " + ip + ":8080");

                        tv_message.setText("\"Server running at \" " + ip + "\":8080\" \n Connected phones ip address: 192.168.42.1:8080");
                        tv_message.setVisibility(View.VISIBLE);
                        isStarted = true;
                    }
                } catch (IOException ioe) {
                    Log.w(TAG, "The server could not start.");
                }
            }
        });


        button_checkdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbmanager = new Intent(getBaseContext(),AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        } );


        button_stopserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //stop
                    if (isStarted) {
                        adr.stop();
                        Log.i(TAG, "Server STOPPED");
                        tv_message.setText("Server stopped.");
                        isStarted = false;
                    }
                } catch (Exception ioe) {
                    Log.w(TAG, "The server could not be stopped");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    // DON'T FORGET to stop the server just in case
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adr != null) {
            adr.stop();
            System.out.println("stopping server.");
        }
    }
    public void testDatabase(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        databaseHelper.test();
        Event e = new Event();
        e.setFormName("test");
        databaseHelper.createEvent(e);

        int eventID = databaseHelper.getEventID(e.getFormName());
        Log.i(TAG, "eventID taken: "+eventID);
        SurveyTaker st = new SurveyTaker();
        st.setDateAnswered("September");
        st.setIdNumber("11303247");
        st.setRespondentEmail("pags@gmail.com");
        st.setRespondentName("Doms");
       // databaseHelper.createSurveyTaker(st,eventID);

        Questions q  = new Questions();
        q.setQuestion("Rate your experience so far: ");
        databaseHelper.createSurveyTaker(st,eventID);
        int surveyTakerID=  databaseHelper.getSurveyTakerID(Integer.parseInt(st.getIdNumber()));


        int questionID =  1;


        Answer a = new Answer();
        a.setAnswer("4");
        a.setQualitative(Boolean.FALSE);



        databaseHelper.addQuestion(q,eventID);
        databaseHelper.addAnswer(a,questionID);
        //q.getId();
    }

    public boolean isConnectedInWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = ((ConnectivityManager)  getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()
                && wifiManager.isWifiEnabled() && networkInfo.getTypeName().equals("WIFI")) {
            return true;
        }
        return false;
    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        Writer writer = new StringWriter();

        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }
        String text = writer.toString();
        return text;
    }
}
