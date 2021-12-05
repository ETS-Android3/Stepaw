package com.butterflies.stepaw.userActions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.butterflies.stepaw.R
import com.butterflies.stepaw.adapters.NotificationsAdapter
import com.butterflies.stepaw.roomORM.ReminderDB
import com.butterflies.stepaw.roomORM.ReminderEntity
import com.butterflies.stepaw.utils.StepawUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


class NotificationsActivity : AppCompatActivity() {

    private lateinit var dataset: MutableList<NotificationsDataModel>
    private lateinit var reminders:MutableList<ReminderEntity>
    private lateinit var notificationsAdapter:NotificationsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.custom_back_button)
        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.notifications_title)
        val recycler = findViewById<RecyclerView>(R.id.notifications_recycler)
notificationsAdapter= NotificationsAdapter(this, mutableListOf())
//Create Room db instance
        val db = Room.databaseBuilder(this, ReminderDB::class.java, "remindersDB").build()
        val dao = db.reminderdao()
//
//        Text1 List
        val list1 = listOf(
            "Did you notice frank didn't walk last two days",
            "Did you notice frank missed dog cafe meetup",
            "You missed walmate time today"
        )
//
//        Text 2 list

        val list2 = listOf<String>(
            "Let frank enjoy fresh air today",
            "Frank is waiting for you",
            "Let frank enjoy his social life"
        )

//

//        Stepaw utils function
        val s = StepawUtils()

        val userData = s.extractUser(this)
//


//        Setting up RecyclerView

        suspend fun getRemindersData() {
           reminders = dao.getAll()

            if(this::reminders.isInitialized&&reminders.size>=1){
                dataset= mutableListOf()

                for (i in reminders.indices){
                    dataset.add(NotificationsDataModel(reminders[i].date,"Hi ${userData!!.FirstName}",list1[Random.nextInt(list1.size)],list2[Random.nextInt(list2.size)]))
                }

                withContext(Dispatchers.IO){
                    recycler.adapter=NotificationsAdapter(this@NotificationsActivity, dataset)
                    recycler.setHasFixedSize(true)
                }
            }
//

        }

//                Launch Suspend function and now populate the data
        val job= lifecycleScope.launch(Dispatchers.IO){
            getRemindersData()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}