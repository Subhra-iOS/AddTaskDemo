
package com.example.addtaskdemo

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.View
import android.widget.*
import com.example.description.TaskDescriptionActivity

import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val PREFS_TASKS = "prefs_tasks"
    private val KEY_TASKS_LIST = "tasks_list"

    private val ADD_TASK_REQUEST = 1
    private val taskList : MutableList<String> = mutableListOf<String>()
    private val listAdapter by lazy { makeListAdapterWith(taskList) }
    private lateinit var timeLable : TextView

    private val timeStampReceiver by lazy { makeBroadCastReceiver() }

    private lateinit var listView : ListView

    /**
     * Time Change broadcast from system
     * @author : Subhra Roy
     */
    private fun makeBroadCastReceiver() : BroadcastReceiver{
        return  object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {

                if(p1?.action == Intent.ACTION_TIME_TICK){

                    if(timeLable != null){ timeLable.text = getCurrentTimeStamp()}
                }

            }
        }
    }

    /**
     * Static Instance
     */
    companion object{
        private const val LOG_TAG = "MainActivityLog"
        private fun getCurrentTimeStamp() : String{
            val currentTimeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            val timeStamp = Date()

            return  currentTimeStamp.format(timeStamp)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        listView = findViewById(R.id.listViewIdentifier) as ListView

        listView.adapter = listAdapter
        timeLable = findViewById(R.id.dateLabelTextView)
        populateDataFromPreferenceStorage()

        listView.onItemClickListener = AdapterView.OnItemClickListener({ _,
        _, position, _ ->

            println("Index : $position")

        })

    }

    /**
     * Populate data from Preference storage
     * @author : Subhra Roy
     */
    private fun populateDataFromPreferenceStorage(){
        val storage = getSharedPreferences(PREFS_TASKS,Context.MODE_PRIVATE)
            .getString(KEY_TASKS_LIST, null)
        if(storage != null){
            val taskItems = storage.split(",".toRegex()).dropLastWhile {
                it.isEmpty()
            }.toTypedArray()
            taskList.addAll(taskItems)

        }
    }

    override fun onResume() {
        super.onResume()

        if(timeLable != null) { timeLable.text = getCurrentTimeStamp() }

        registerReceiver(timeStampReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(timeStampReceiver)
        }catch (exception : Exception){
            Log.v(MainActivity.LOG_TAG,"Time receiver does not register", exception)
        }
    }

    override fun onStop() {
        super.onStop()

        val stringBuilderStorage = StringBuilder()
        for (task in taskList){
            stringBuilderStorage.append(task)
            stringBuilderStorage.append(",")
        }

        getSharedPreferences(PREFS_TASKS, Context.MODE_PRIVATE)
            .getString(KEY_TASKS_LIST,stringBuilderStorage.toString()).apply {
                println(this)
            }
    }

    /**
     * Populate the result for call back Activity
     * @param : requestCode which is bind to Source Activity
     * @param : resultCode represent the status success or failure
     * @param : data that the Activity passes through an Intent
     * @author : Subhra Roy
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ADD_TASK_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                val task = data?.getStringExtra(TaskDescriptionActivity.EXTRA_TASK_DESCRIPTION)
                task?.let {
                    println("Added Task Des : $it")
                    taskList.add(it)
                    listAdapter.notifyDataSetChanged()
                }
            }
        }

    }

    /**
     * Did tap on add task function
     * @author : Subhra Roy
     */
    fun didAddTaskOnClick(view : View){
        val descriptionIntent = Intent(this, TaskDescriptionActivity::class.java)
        startActivityForResult(descriptionIntent,ADD_TASK_REQUEST)
    }

    private fun makeListAdapterWith(list: List<String>): ArrayAdapter<String> =
        ArrayAdapter(this, android.R.layout.simple_list_item_1, list)

}
