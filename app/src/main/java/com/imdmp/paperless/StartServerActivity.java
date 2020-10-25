package com.imdmp.paperless;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imdmp.paperless.model.Answer;
import com.imdmp.paperless.model.Event;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StartServerActivity extends Activity {
    private static final int DEFAULT_PORT = 8080;
    private static final String TAG = "LOCAL_SERVER";

    ImageButton ibBack, ibStartServer;
    Button button_checkdb;
    TextView tvServerStatus, tvEventName, tvConnected, tvEvaluated;
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

        //    String htmlName = getIntent().getStringExtra("htmlName");
        int eventID = getIntent().getIntExtra("eventID", 2);


        Log.i(TAG, "GOT EVENT ID: " + eventID);

        //set on click listeners
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                adr.stop();
                isStarted = false;
            }
        });
        //      ibStartServer.setTag(R.string.htmlName,htmlName);
        ibStartServer.setTag(R.string.eventID, eventID);
        ibStartServer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // String html= (String) v.getTag(R.string.htmlName);
                int id = (int) v.getTag(R.string.eventID);
                try {
                    adr = new WebServer(DEFAULT_PORT, getBaseContext(), id);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());


                int eventID = (int) v.getTag(R.string.eventID);
//                    adr.setHTMLFile(textfile);
//                    adr.setContext(getBaseContext());
//                    adr.setDbHelper(databaseHelper);
//                    adr.setEventID(eventID);

                //  testDatabase();

                try {
                    //start
                    if (!isStarted) {
                        DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
                        Event e = dbHelper.getEvent(id);

                        int answerCluster = e.getAnswerCluster();
                        Log.i(TAG, "answer cluster before: " + e.getAnswerCluster());
                        answerCluster++;
                        e.setAnswerCluster(answerCluster);
                        dbHelper.updateAnswerCluster(e);
                        Log.i(TAG, "answer cluster now: " + e.getAnswerCluster());

                        adr.start();
                        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                        String ip = android.text.format.Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

                        ///TODO: IF DOESN WORK DELETE SHIT
                        String ip2 = "" + ip + ":8080";
                        Log.i(TAG, "Server running at " + ip + ":8080");
                        etIPAdress.setText(ip2);
                        //    ibStartServer.setImageResource(R.drawable.power_button_on);
                        ibStartServer.setBackground(getApplication().getResources().getDrawable(R.drawable.power_button_on));
//                            tv_message.setText("\"Server running at \" " + ip + "\":8080\" \n Connected phones ip address: 192.168.42.1:8080");
//                            tv_message.setVisibility(View.VISIBLE);
                        isStarted = true;
                    } else {
                        isStarted = false;
                        if (adr != null) {
                            adr.stop();
                            etIPAdress.setText("");
                            Log.i(TAG, "Server Stopped.");

                        }
                        ibStartServer.setBackground(getApplication().getResources().getDrawable(R.drawable.power_button_off));
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
                Intent dbmanager = new Intent(getBaseContext(), AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });
        tvConnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"exporting db");
                exportDB();
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

    private void exportDB() {

        File dbFile = getDatabasePath("MyDBName.db");
        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "csvname.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM "+ Answer.TABLE, null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
}
