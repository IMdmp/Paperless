package com.example.dominic.paperless;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dominic.paperless.Model.Answer;
import com.example.dominic.paperless.Model.Event;
import com.example.dominic.paperless.Model.Questions;
import com.example.dominic.paperless.utils.ApManager;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.example.dominic.paperless.MainActivity.convertStreamToString;
import static com.example.dominic.paperless.R.drawable.power_button_off;
import static com.example.dominic.paperless.R.id.ib_startserver;
import static com.example.dominic.paperless.R.id.tv_message;

public class StartServerActivity extends Activity {
    private static final int DEFAULT_PORT = 8080;
    private static final String TAG = "LOCAL_SERVER";

    ImageButton ibBack, ibStartServer;
    Button button_checkdb;
    TextView tvServerStatus, tvEventName, tvConnected, tvEvaluated,tv_export,tv_test;
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
        ibStartServer = (ImageButton) findViewById(ib_startserver);

        tvServerStatus = (TextView) findViewById(R.id.tv_server_status);
        tvEventName = (TextView) findViewById(R.id.tv_event_name);
        llViewStats = (LinearLayout) findViewById(R.id.ll_view_stats);
        button_checkdb = (Button) findViewById(R.id.button_dbcheck);
        etIPAdress = (EditText) findViewById(R.id.et_ip_address);
        tv_export = (TextView) findViewById(R.id.tv_export);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int eventID = getIntent().getIntExtra("eventID", 2);
        Event e =  dbHelper.getEvent(eventID);
        tvEventName.setText(e.getFormName());
        tv_test = (TextView) findViewById(R.id.tv_test);
        //    String htmlName = getIntent().getStringExtra("htmlName");

        try {
            adr = new WebServer(DEFAULT_PORT, getBaseContext(), eventID);
        } catch (IOException error) {
            error.printStackTrace();
        }

        Log.i(TAG, "GOT EVENT ID: " + eventID);
        tv_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ApManager.configApState(StartServerActivity.this);
            }
        });
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


                DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());


                int eventID = (int) v.getTag(R.string.eventID);
//                    adr.setHTMLFile(textfile);
//                    adr.setContext(getBaseContext());
//                    adr.setDbHelper(databaseHelper);
//                    adr.setEventID(eventID);

                //  testDatabase();
                DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
                try {
                    //start
                    if (!isStarted) {
                        ApManager.configApState(StartServerActivity.this);
//                        adr = new WebServer(DEFAULT_PORT, getBaseContext(), id);
                        System.out.println("trying to start again.");
                        Event e = dbHelper.getEvent(id);

                        int answerCluster = e.getAnswerCluster();
                        Log.i(TAG, "answer cluster before: " + e.getAnswerCluster());
                        answerCluster++;
                        e.setAnswerCluster(answerCluster);
                        dbHelper.updateAnswerCluster(e);
                        Log.i(TAG, "answer cluster now: " + e.getAnswerCluster());

                        adr.start();
//                        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                        WifiInfo wifiinfo = manager.getConnectionInfo();
//                        byte[] myIPAddress = BigInteger.valueOf(wifiinfo.getIpAddress()).toByteArray();
//            // you must reverse the byte array before conversion. Use Apache's commons library
//                        ArrayUtils.reverse(myIPAddress);
//                        InetAddress myInetIP = InetAddress.getByAddress(myIPAddress);
//                        String myIP = myInetIP.getHostAddress();
//
//                        ///TODO: IF DOESN WORK DELETE SHIT
                        String ip2 = "Server running at: " + "192.168.43.1" + ":8080";
//                        Log.i(TAG, "Server running at " + myIP + ":8080");
                        etIPAdress.setText(ip2);
                        //    ibStartServer.setImageResource(R.drawable.power_button_on);
                        ibStartServer.setBackground(getApplication().getResources().getDrawable(R.drawable.power_button_on));
//                            tv_message.setText("\"Server running at \" " + ip + "\":8080\" \n Connected phones ip address: 192.168.42.1:8080");
//                            tv_message.setVisibility(View.VISIBLE);
                        isStarted = true;
                    } else {
                        isStarted = false;
                        if (adr != null) {
                            ApManager.configApState(StartServerActivity.this);
                            adr.stop();
                            etIPAdress.setText("Server Stopped");
                            Log.i(TAG, "Server Stopped.");
                            ibStartServer.setBackground(getApplication().getResources().getDrawable(R.drawable.power_button_off));
                        }

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
        tv_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"exporting...",Toast.LENGTH_SHORT).show();
                Log.i(TAG,"exporting db");
                exportDB();
                Toast.makeText(getBaseContext(),"successfully exported! ",Toast.LENGTH_SHORT).show();
            }
        });
        llViewStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbmanager = new Intent(getBaseContext(), AndroidDatabaseManager.class);
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

    private void exportDB() {

        File dbFile = getDatabasePath("MyDBName.db");
        DatabaseHelper dbhelper = new DatabaseHelper(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "paperlessSurveys.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM "+ Answer.TABLE, null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                int columnCount = curCSV.getColumnCount();
                //Which column you want to exprort
                String arrStr[] = new String[columnCount];
                for(int i =0 ;i<columnCount;i++){
                    arrStr[i] = curCSV.getString(i);
                }
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
}
