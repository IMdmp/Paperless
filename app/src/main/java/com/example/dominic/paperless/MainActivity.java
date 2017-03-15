package com.example.dominic.paperless;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;


public class MainActivity extends AppCompatActivity {
    private static final int DEFAULT_PORT = 8080;
    private static final String TAG = "LOCAL_SERVER";


    // INSTANCE OF ANDROID WEB SERVER  = WebServer
    private BroadcastReceiver broadcastReceiverNetworkState;
    private static boolean isStarted = false;
    private WebServer adr;


    //VIEW
    Button button_startserver, button_stopserver, button_select;
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

        tv_message.setVisibility(View.INVISIBLE);


        button_startserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiate new web server
                adr = new WebServer(DEFAULT_PORT);
                InputStream is;
                try {
                    is = getResources().getAssets().open("sample_form.html");
                    textfile = convertStreamToString(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                adr.setHTMLFile(textfile);
                try {
                    is = getAssets().open("terms.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    //start
                    if (!isStarted) {
                        adr.start();
                        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
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


    public boolean isConnectedInWifi() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
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
