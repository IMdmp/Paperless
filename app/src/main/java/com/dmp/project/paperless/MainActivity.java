package com.dmp.project.paperless;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dmp.project.paperless.utils.WebServer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button btn_toggleServer;
    private WebServer webServer;
    private static final int PORT = 8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_toggleServer = findViewById(R.id.btn_toggleServer);

        try {
            webServer = new WebServer(PORT,this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        btn_toggleServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    webServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
