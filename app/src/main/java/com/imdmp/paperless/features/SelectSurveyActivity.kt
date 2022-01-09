package com.imdmp.paperless.features

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.imdmp.paperless.DatabaseHelper
import com.imdmp.paperless.R
import com.imdmp.paperless.SurveyAdapter
import com.imdmp.paperless.databinding.ActivitySelectSurveyBinding
import com.imdmp.paperless.model.Event
import com.imdmp.paperless.model.Questions
import timber.log.Timber

class SelectSurveyActivity : Activity() {

    lateinit var databaseHelper: DatabaseHelper
    lateinit var surveyAdapter: SurveyAdapter
    lateinit var binding: ActivitySelectSurveyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectSurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseHelper = DatabaseHelper(this)

        fillDb()
        val newData = databaseHelper.allEvents
        surveyAdapter = SurveyAdapter(this, newData)
        binding.rvSelectSurvey.adapter = surveyAdapter
        surveyAdapter.setOnItemClickListener {
            val i = Intent(it.context, StartServerActivity::class.java)

            val eventID = it.getTag(R.string.eventID) as Int
            i.putExtra("htmlName", it.getTag(R.string.htmlName) as String)
            i.putExtra("eventID", it.getTag(R.string.eventID) as Int)
            //   i.putExtra("id", (int) v.getTag());
            //   i.putExtra("id", (int) v.getTag());
            it.context.startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()


    }

    private fun fillDb() {
        val e = Event()
        e.formName = "Alumni Homecoming"
        e.organizerName = "Student Lasallian Animators"
        e.htmlName = "event_eval_survey.html"
        e.timesTaken = 1
        e.answerCluster = 1
        e.id = 1

        val e1 = Event()
        e1.formName = "Social Demographics"
        e1.organizerName = "The surveyors"
        e1.htmlName = "demographic_survey.html"
        e1.timesTaken = 1
        e1.answerCluster = 1

        val e2 = Event()
        e2.formName = "Eval Survey"
        e2.organizerName = "Event solutions"
        e2.htmlName = "recollection_eval_survey.html"
        e2.timesTaken = 1
        e2.answerCluster = 1

        databaseHelper.createEvent(e)
        databaseHelper.createEvent(e1)
        databaseHelper.createEvent(e2)

        val q1 = Questions()
        q1.question = "Full Name"
        q1.isQualitative = true
        q1.stringID = "name"

        val q2 = Questions()
        q2.question = "ID Number"
        q2.isQualitative = true
        q2.stringID = "idnum"

        val q3 = Questions()
        q3.question = "How would you rate this event?"
        q3.isQualitative = true
        q3.stringID = "question1"

        val q4 = Questions()
        q4.question = "How would you rate your host?"
        q4.isQualitative = false
        q4.stringID = "question2"

        val q5 = Questions()
        q5.question = "Sex"
        q5.isQualitative = false
        q5.stringID = "question1"

        val q6 = Questions()
        q6.question = "Hobbies"
        q6.isQualitative = true
        q6.stringID = "question2"

        val q7 = Questions()
        q7.question = "Biography"
        q7.isQualitative = true
        q7.stringID = "question3"

        val q8 = Questions()
        q8.question = "How would you rate your recollection?"
        q8.isQualitative = false
        q8.stringID = "question1"

        val q9 = Questions()
        q9.question = "How would you rate the facilitator?"
        q9.isQualitative = false
        q9.stringID = "question2"

        val q10 = Questions()
        q10.question = "How would you rate the co-facilitator?"
        q10.isQualitative = false
        q10.stringID = "question3"

        val databaseHelper = DatabaseHelper(baseContext)
        var i = databaseHelper.getEventID(e.formName)
        Timber.i("event ID : $i")
        databaseHelper.addQuestion(q1, i)

        databaseHelper.addQuestion(q2, i)
        databaseHelper.addQuestion(q3, i)
        databaseHelper.addQuestion(q4, i)

        //   DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
        i = databaseHelper.getEventID(e1.formName)
        databaseHelper.addQuestion(q1, i)
        databaseHelper.addQuestion(q2, i)
        databaseHelper.addQuestion(q5, i)
        databaseHelper.addQuestion(q6, i)
        databaseHelper.addQuestion(q7, i)
        //  databaseHelper.addQuestion(q4,eventID);

        //  databaseHelper.addQuestion(q4,eventID);
        i = databaseHelper.getEventID(e2.formName)

        databaseHelper.addQuestion(q1, i)
        databaseHelper.addQuestion(q2, i)
        databaseHelper.addQuestion(q8, i)
        databaseHelper.addQuestion(q9, i)
        databaseHelper.addQuestion(q10, i)


        Timber.i("Db filled!")
    }
}