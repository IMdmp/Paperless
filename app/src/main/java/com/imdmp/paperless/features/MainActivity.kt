package com.imdmp.paperless.features

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.util.Log
import android.view.View
import com.imdmp.paperless.AndroidDatabaseManager
import com.imdmp.paperless.DatabaseHelper
import com.imdmp.paperless.WebServer
import com.imdmp.paperless.databinding.ActivityMainBinding
import com.imdmp.paperless.model.Answer
import com.imdmp.paperless.model.Event
import com.imdmp.paperless.model.Questions
import com.imdmp.paperless.model.SurveyTaker
import timber.log.Timber
import java.io.*
import java.lang.Boolean
import java.lang.Exception

const val DEFAULT_PORT = 8080
class MainActivity : Activity() {

    lateinit var broadcastReceiverNetworkState :BroadcastReceiver
    var isStarted = false
    lateinit var adr :WebServer

    lateinit var binding :ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonTest.setOnClickListener {
            testDatabase()
        }

        binding.buttonStartserver.setOnClickListener {
            try{
                adr = WebServer(DEFAULT_PORT,this,1)
            }catch (e:IOException){
                e.printStackTrace()
            }

            val databaseHelper  = DatabaseHelper(this)
            val inputStream  = resources.assets.open("sample_form.html")
            val file = convertStreamToString(inputStream)

//                adr.setHTMLFile(textfile);
//                adr.setContext(getBaseContext());
//                adr.setDbHelper(databaseHelper);


            //  testDatabase();

            try{
                if(isStarted.not()){
                    adr.start()
                    val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                    val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)
                    Timber.i(
                        "Server running at $ip:8080"
                    )

                    binding.tvMessage.setText("\"Server running at \" $ip\":8080\" \n Connected phones ip address: 192.168.42.1:8080")
                    binding.tvMessage.visibility = View.VISIBLE
                    isStarted = true

                }
            }catch (e:IOException){
                Timber.e( "The server could not start.")

            }
        }

        binding.buttonDbcheck.setOnClickListener {
            val dbmanager = Intent(this, AndroidDatabaseManager::class.java)
            startActivity(dbmanager)
        }

        binding.buttonStopserver.setOnClickListener {
            try {
                //stop
                if (isStarted) {
                    adr.stop()
                    Timber.i( "Server STOPPED")
                    binding.tvMessage.setText("Server stopped.")
                    isStarted = false
                }
            } catch (ioe: Exception) {
                Timber.e( "The server could not be stopped")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if(adr !=null){
            adr.stop()
            Timber.d("Stopping server.")
        }
    }

    fun isConnectedInWifi(): kotlin.Boolean {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        val networkInfo =
            (applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return (networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
                && wifiManager.isWifiEnabled && networkInfo.typeName == "WIFI")
    }

    @Throws(IOException::class)
    fun convertStreamToString(`is`: InputStream): String? {
        val writer: Writer = StringWriter()
        val buffer = CharArray(2048)
        try {
            val reader: Reader = BufferedReader(
                InputStreamReader(
                    `is`,
                    "UTF-8"
                )
            )
            var n: Int
            while (reader.read(buffer).also { n = it } != -1) {
                writer.write(buffer, 0, n)
            }
        } finally {
            `is`.close()
        }
        return writer.toString()
    }

    private fun testDatabase() {
        val databaseHelper = DatabaseHelper(baseContext)
        databaseHelper.test()
        val e = Event()
        e.formName = "test"
        databaseHelper.createEvent(e)
        val eventID = databaseHelper.getEventID(e.formName)
        Timber.i("eventID taken: $eventID")
        val st = SurveyTaker()
        st.dateAnswered = "September"
        st.idNumber = "11303247"
        st.respondentEmail = "pags@gmail.com"
        st.respondentName = "Doms"
        // databaseHelper.createSurveyTaker(st,eventID);

        // databaseHelper.createSurveyTaker(st,eventID);
        val q = Questions()
        q.question = "Rate your experience so far: "
        databaseHelper.createSurveyTaker(st, eventID)
        val surveyTakerID = databaseHelper.getSurveyTakerID(st.idNumber.toInt())


        val questionID = 1
        val a = Answer()
        a.answer = "4"
        a.qualitative = Boolean.FALSE



        databaseHelper.addQuestion(q, eventID)
        databaseHelper.addAnswer(a, questionID)
        //q.getId();

    }
}