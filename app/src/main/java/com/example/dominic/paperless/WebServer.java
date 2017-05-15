package com.example.dominic.paperless;

/**
 * Created by Dominic on 2/26/2017.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.dominic.paperless.Model.Answer;
import com.example.dominic.paperless.Model.Questions;
import com.example.dominic.paperless.Model.SurveyTaker;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;

import static android.R.attr.name;

public class WebServer extends NanoHTTPD {
    InputStream is;
    String s;
    final String TAG = "LOCAL_COMPUTER: ";
    Context c;
    int eventID;
    DatabaseHelper dbHelper;
    Boolean lock;
    ArrayList<Questions> parameters;


    public WebServer(int server){
        super(server);
        parameters = new ArrayList<Questions>();
        Questions q1 = new Questions();
        q1.setParameterName("name");
        q1.setIsQualitative(Boolean.TRUE);
     //   dbHelper.getQuestionID(q1.)
        Questions q2 = new Questions();
        q2.setParameterName("idnum");
        q2.setIsQualitative(Boolean.TRUE);

        Questions q3 = new Questions();
        q3.setParameterName("question1");
        q3.setIsQualitative(Boolean.FALSE);

        Questions q4 = new Questions();
        q4.setParameterName("question2");
        q4.setIsQualitative(Boolean.FALSE);


        Questions q5 = new Questions();
        q5.setParameterName("question3");
        q5.setIsQualitative(Boolean.FALSE);


        parameters.add(q1);
        parameters.add(q2);
        parameters.add(q3);
        parameters.add(q4);
        parameters.add(q5);

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

        //wait for responses; take data and store in database


        int numParameters = parameters.size();
        for(int i=0;i<numParameters;i++){
            lock =false;
            Log.i(TAG,"ITERATION: "+i);
            Questions q1 = parameters.get(i);
            String parameter = q1.getParameterName();
            Log.i(TAG,"parameter: "+parameter);


            if(session.getParms().get(parameter)!=null) {
                Log.i(TAG,"Got dynamic data for "+parameter+ ": " +session.getParms().get(parameter));
                SurveyTaker st  = new SurveyTaker();

                st.setRespondentName(session.getParms().get("name"));
                st.setIdNumber(session.getParms().get("idnum"));
                Log.i(TAG,"set respondent name: "+st.getRespondentName());
                Log.i(TAG,"set respondent idnum: "+st.getIdNumber());
                Log.i(TAG,"test map:" +  map.get("name"));

                if(i<=1){
                    if(!lock){
                dbHelper.createSurveyTaker(st,eventID);}

                lock =true;
                }
                int surveyTakerID = dbHelper.getSurveyTakerID(Integer.parseInt(st.getIdNumber()));
                Answer a =new Answer();

                a.setAnswer(session.getParms().get(parameter));
                a.setQualitative(q1.getIsQualitative());
                if(i>1) {
                    dbHelper.addAnswer(a, 1, surveyTakerID);
                    Log.i(TAG, "set answer: " + a.getAnswer());
                }

            }

        }

//        if(session.getParms().get("name")!=null) {
//            System.out.println("RECEIVED NON NULL ANSWER");
//
//            Answer a = new Answer();
//            a.setQualitative(Boolean.FALSE);
//            a.setAnswer("4");
//
//            System.out.println("adding to db...");
//            addAnswertoDB(a);
//        }
       // Log.i(TAG,"Got data for name: "+session.getParms().get("name"));
       // Log.i(TAG,"Got data for idnum: "+session.getParms().get("idnum"));

        //   Log.i(TAG,"test"+ session.getHeaders().get("name"));
        return newFixedLengthResponse( msg);




        //return new NanoHTTPD.Response(answer);
        // return new Response(Response.Status.OK, "image/jpg", imageFile);
    }



    private Response POST(IHTTPSession session){

        String uri = session.getUri();
        System.out.println("WENT HERE>>>>>>>TEST");
      //  return createResponse(Status.OK, NanoHTTPD.MIME_PLAINTEXT, "ok i am ");
        return newFixedLengthResponse("test");
    }
    public String createStringHTML(){
        String webpage="";
        webpage=s;
        return webpage;
    }

    public void addAnswertoDB(Answer a){

        dbHelper.addAnswer(a,1,1);

    }
    public void setQuestions(ArrayList<Questions> q){
        this.parameters = q;
    }
    public void setContext(Context c ){
        this.c = c;
    }

    public void setHTMLFile(String s){
        this.s=s;
    }

    public void setDbHelper(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }

    public void setEventID(int eventID){
        this.eventID = eventID;
        Log.i(TAG,"Event ID got: "+eventID);
    }


    public void testParameters(){

    }
    private int getCategoryPos(String category) {

        return parameters.indexOf(category);
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

