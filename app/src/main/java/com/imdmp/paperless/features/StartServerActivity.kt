package com.imdmp.paperless.features

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Environment
import android.text.format.Formatter
import android.util.Log
import com.imdmp.paperless.*
import com.imdmp.paperless.databinding.ActivityStartServerBinding
import com.imdmp.paperless.model.Answer
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.Exception


class StartServerActivity : Activity() {

    lateinit var adr: WebServer
    var isStarted: Boolean = false

    lateinit var binding: ActivityStartServerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartServerBinding.inflate(layoutInflater)
        binding.ibBack.setOnClickListener {
            finish()
            adr.stop()
            isStarted = false
        }

        binding.ibStartserver.setTag(R.string.eventID)
        binding.ibStartserver.setOnClickListener { v ->
            val id = v.getTag(R.string.eventID) as Int

            try {
                adr = WebServer(DEFAULT_PORT, baseContext, id)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val databaseHelper = DatabaseHelper(baseContext)


            val eventID = v.getTag(R.string.eventID) as Int
//                    adr.setHTMLFile(textfile);
//                    adr.setContext(getBaseContext());
//                    adr.setDbHelper(databaseHelper);
//                    adr.setEventID(eventID);

            //  testDatabase();

            try{

                if(!isStarted){
                    val dbHelper = DatabaseHelper(this)

                    val e = dbHelper.getEvent(id)

                    var answerCluster = e.answerCluster
                    Timber.i( "answer cluster before: " + e.answerCluster)
                    answerCluster++

                    e.answerCluster = answerCluster
                    dbHelper.updateAnswerCluster(e)
                   Timber.i("answer cluster now: " + e.answerCluster)

                    adr.start()
                    val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
                    val ip = Formatter.formatIpAddress(wm.connectionInfo.ipAddress)

                    ///TODO: IF DOESN WORK DELETE SHIT

                    ///TODO: IF DOESN WORK DELETE SHIT
                    val ip2 = "$ip:8080"
                    Timber.i(
                        "Server running at $ip:8080"
                    )
                    binding.etIpAddress.setText(ip2)
                    //    ibStartServer.setImageResource(R.drawable.power_button_on);
                    //    ibStartServer.setImageResource(R.drawable.power_button_on);
                    binding.ibStartserver.setBackground(application.resources.getDrawable(R.drawable.power_button_on))
//                            tv_message.setText("\"Server running at \" " + ip + "\":8080\" \n Connected phones ip address: 192.168.42.1:8080");
//                            tv_message.setVisibility(View.VISIBLE);
                    //                            tv_message.setText("\"Server running at \" " + ip + "\":8080\" \n Connected phones ip address: 192.168.42.1:8080");
//                            tv_message.setVisibility(View.VISIBLE);
                   isStarted = true

                }else{
                    isStarted = false
                    if (adr != null) {
                        adr.stop()
                        binding.etIpAddress.setText("")
                        Timber.i( "Server Stopped.")
                    }
                    binding.ibStartserver.setBackground(application.resources.getDrawable(R.drawable.power_button_off))
                }
            }catch (e:IOException){
                Timber.e("The server cannot start")
            }
        }

        binding.tvServerStatus.setOnClickListener{

        }

        binding.llViewStats.setOnClickListener {
            val dbmanager = Intent(baseContext, AndroidDatabaseManager::class.java)
            startActivity(dbmanager)
        }

        binding.tvConnected.setOnClickListener {
            Timber.i("Exporting db")
            exportDb()
        }
    }

    private fun exportDb() {
        val dbFile = getDatabasePath("MyDBName.db")
        val dbhelper = DatabaseHelper(applicationContext)
        val exportDir = File(Environment.getExternalStorageDirectory(), "")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }

        val file = File(exportDir, "csvname.csv")
        try {
            file.createNewFile()
            val csvWrite = CSVWriter(FileWriter(file))
            val db = dbhelper.readableDatabase
            val curCSV = db.rawQuery("SELECT * FROM " + Answer.TABLE, null)
            csvWrite.writeNext(curCSV.columnNames)
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                val arrStr = arrayOf(curCSV.getString(0), curCSV.getString(1), curCSV.getString(2))
                csvWrite.writeNext(arrStr)
            }
            csvWrite.close()
            curCSV.close()
        } catch (sqlEx: Exception) {
            Log.e("MainActivity", sqlEx.message, sqlEx)
        }
    }
}