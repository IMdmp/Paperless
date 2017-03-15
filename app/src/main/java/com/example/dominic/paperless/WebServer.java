package com.example.dominic.paperless;

/**
 * Created by Dominic on 2/26/2017.
 */

import android.content.res.AssetManager;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;

public class WebServer extends NanoHTTPD {
    InputStream is;
    String s;
    final String TAG = "LOCAL_COMPUTER: ";
    public WebServer(int server){
        super(server);
    }

    @Override
    public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {
        return super.serve(uri, method, headers, parms, files);
    }


    @Override
    public Response serve(IHTTPSession session) {
        String answer = "";
        Method method = session.getMethod();
        Log.i(TAG,"SESSION START. ");

        //   Log.i(TAG, "local server requesting uri " + uri + " with parameters " + parameters.toString() );
        String msg = createStringHTML();

    //   Dunno how to use this yet.
        final HashMap<String, String> map = new HashMap<String, String>();
        try {
            session.parseBody(map);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }
       // final String json = map.get("postData");
       // Log.i(TAG,"Got POST data: "+json);



        Log.i(TAG,"Got data for name: "+session.getParms().get("name"));
        Log.i(TAG,"Got data for idnum: "+session.getParms().get("idnum"));

        Log.i(TAG,"test"+ session.getHeaders().get("name"));
        return newFixedLengthResponse( msg);




        //return new NanoHTTPD.Response(answer);
        // return new Response(Response.Status.OK, "image/jpg", imageFile);
    }
    public String createStringHTML(){
        String webpage="";
        webpage=s;
        return webpage;
    }
    public void setHTMLFile(String s){
        this.s=s;
    }






//    private Response POST(IHTTPSession session) {
//        String uri = session.getUri();
//
//        try {
//            Map<String, String> files = new HashMap<String, String>();
//            session.parseBody(files);
//
//            Set<String> keys = files.keySet();
//            for(String key: keys){
//                String name = key;
//                String loaction = files.get(key);
//
//                File tempfile = new File(loaction);
//                Files.copy(tempfile.toPath(), new File("destinamtio_path"+name).toPath(), StandardCopyOption.REPLACE_EXISTING);
//            }
//
//
//        } catch (IOException | ResponseException e) {
//            System.out.println("i am error file upload post ");
//            e.printStackTrace();
//        }
//
//        return createResponse(Status.OK, NanoHTTPD.MIME_PLAINTEXT, "ok i am ");
//    } 

    //TODO: RECEIVE DATA FROM CONNECTED DEVICES.
    //TODO: STORE RECEIVED DATA TO DATABASE
}

