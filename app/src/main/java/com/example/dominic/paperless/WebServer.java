package com.example.dominic.paperless;

/**
 * Created by Dominic on 2/26/2017.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import android.content.Context;

import com.example.dominic.paperless.Model.Answer;
import com.example.dominic.paperless.Model.Event;
import com.example.dominic.paperless.Model.Questions;
import com.example.dominic.paperless.Model.SurveyTaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

import static android.R.attr.data;
import static android.R.attr.key;
import static android.R.attr.port;
import static android.R.attr.value;
import static android.os.Build.VERSION_CODES.M;

public class WebServer extends NanoHTTPD {
    InputStream is;
    final String TAG = "LOCAL_COMPUTER: ";
    Context mContext;
    String htmlFile;
    DatabaseHelper dbHelper;
    Event e;
    Boolean lock =false;
    /**
     * Common mime types for dynamic content
     */
    public static final String
            MIME_PLAINTEXT = "text/plain",
            MIME_HTML = "text/html",
            MIME_JS = "application/javascript",
            MIME_CSS = "text/css",
            MIME_PNG = "image/png",
            MIME_DEFAULT_BINARY = "application/octet-stream",
            MIME_XML = "text/xml";
    Response.Status HTTP_OK= Response.Status.OK;



    public WebServer(int port,Context ctx,int eventID) throws IOException {

        super(port);
        mContext=ctx;

        dbHelper = new DatabaseHelper(ctx);
        e =  dbHelper.getEvent(eventID);
        this.htmlFile=e.getHtmlName();

        Log.i(TAG,"event html file: " +this.htmlFile);
    }
    @Override
    public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files) {
        return super.serve(uri, method, headers, parms, files);
    }


    @Override
    public Response serve(IHTTPSession session) {
        System.out.println("trying to serve...");

        Method method = session.getMethod();
        //Log.i(TAG,"SESSION START. ");
      //  Log.i(TAG,"SERVE ::  URI "+session.getUri());

        //   Dunno how to use this yet.
        final HashMap<String, String> map = new HashMap<String, String>();
        try {
            session.parseBody(map);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        }
        final String json = map.get("content");
        Log.i(TAG,"JSON: "+json);
        InputStream mbuffer = null;
        if(!session.getParms().isEmpty()){
            Log.i(TAG,"session: "+ session.getParms());
            final Map<String,String> map1= session.getParms();
            AsyncTaskRunner runner = new AsyncTaskRunner(map1);
            runner.execute();
//            addToDb(map1);
            this.htmlFile="sample_form.html";
            try {
                mbuffer = mContext.getAssets().open(htmlFile);

            //    Log.i(TAG,"contwext"+ mbuffer.toString());
            return newFixedLengthResponse(HTTP_OK, MIME_HTML, convertStreamToString(mbuffer));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        }else{
            this.htmlFile=e.getHtmlName();
        }
//        final StringBuilder buf = new StringBuilder();
//        for (Entry<Object, Object> kv : header.entrySet())
//            buf.append(kv.getKey() + " : " + kv.getValue() + "\n");

        Log.i(TAG,"went here." );
        try {
            if(session.getUri()!=null){
                Log.i(TAG,"went here too." );
                if(session.getUri().contains(".js")){
                    mbuffer = mContext.getAssets().open(session.getUri().substring(1));
                    return  newFixedLengthResponse(HTTP_OK, MIME_JS, convertStreamToString(mbuffer));
                }else if(session.getUri().contains(".css")) {
                    mbuffer = mContext.getAssets().open(session.getUri().substring(1));
                    return newFixedLengthResponse(HTTP_OK, MIME_CSS, convertStreamToString(mbuffer));

                }
//                }else if(session.getUri().contains(".png")){
//                    mbuffer = mContext.getAssets().open(session.getUri().substring(1));
//                    // HTTP_OK = "200 OK" or HTTP_OK = Status.OK;(check comments)
//                    return  newFixedLengthResponse(HTTP_OK, MIME_PNG, convertStreamToString(mbuffer));
//                }
                else{
                    Log.i(TAG,"return html:");
                    mbuffer = mContext.getAssets().open(htmlFile);
                //    Log.i(TAG,"contwext"+ mbuffer.toString());
                    return newFixedLengthResponse(HTTP_OK, MIME_HTML, convertStreamToString(mbuffer));
                }
            }
            else{
                Log.i(TAG,"return html:");
                mbuffer = mContext.getAssets().open(htmlFile);
              //  Log.i(TAG,"context"+ mbuffer.toString());
                return newFixedLengthResponse(HTTP_OK, MIME_HTML,convertStreamToString(mbuffer));
            }


        } catch (IOException e) {
            Log.d(TAG,"Error opening file"+session.getUri().substring(1));
            e.printStackTrace();
        }

//        return newFixedLengthResponse(HTTP_OK, MIME_HTML, String.valueOf(mbuffer));
return null;
    }

    //TODO: STORE RECEIVED DATA TO DATABASE
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

    public void addToDb(Map<String,String> data) {
        ArrayList<Questions> questions;
        questions = dbHelper.getEventQuestions(e);
        Boolean lock = false;
        Map<String,String> questionMap = new HashMap<>();
        int timesTaken = e.getTimesTaken();
        Log.i(TAG,"times taken orignally: "+ timesTaken);

        for (int i =0 ;i< questions.size();i++){
            questions.get(i).getStringID();

            questionMap.put(questions.get(i).getStringID(),questions.get(i).getId() +"");
        }
        Log.i(TAG,"question map: "+ questionMap.toString());
        SurveyTaker st = new SurveyTaker();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        st.setDateAnswered(timeStamp);
        st.setRespondentEmail("sample@sample.com");
        Log.i(TAG,"times taken orignally: "+ timesTaken);
            for ( Map.Entry<String, String> entry : data.entrySet()) {
                Answer a = new Answer();


                if(entry.getValue()!= null) {
                    if(entry.getKey().equals("idnum")){
                    st.setIdNumber(entry.getValue());

                    }
                    if(entry.getKey().equals("name")){
                        st.setRespondentName(entry.getValue());
                    }

                    a.setAnswer(entry.getValue());
                    a.setQualitative(true);
                    a.setAnswerCluster(e.getAnswerCluster());
                    Questions q = new Questions();
                    dbHelper.addAnswer(a, Integer.parseInt(questionMap.get(entry.getKey())));
                    Log.i(TAG,"Value "+entry.getValue());
                    Log.i(TAG,"key "+entry.getKey());
                }


            // do something with key and/or tab
        }   dbHelper.createSurveyTaker(st,e.getId());
    }

    public void addToDb2(Map<String,String>data){

    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        final Map<String,String> map;
        AsyncTaskRunner(Map<String,String>map) {
        this.map =map;
            }
        @Override
        protected String doInBackground(String... params) {
            addToDb(this.map);
            return "";


    }
}
}

