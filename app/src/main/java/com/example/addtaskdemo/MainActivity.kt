
package com.example.addtaskdemo

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import com.example.description.TaskDescriptionActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val ADD_TASK_REQUEST = 1
    private val taskList : MutableList<String> = mutableListOf()
    private val listAdapter by lazy { makeListAdapterWith(list = taskList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ADD_TASK_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                val task = data?.getStringExtra(TaskDescriptionActivity.EXTRA_TASK_DESCRIPTION)
                task?.let {
                    println("Added Task Des : $it")
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

    private fun makeListAdapterWith(list : List<String>) : ArrayAdapter<String> =
        ArrayAdapter(this,android.R.layout.activity_list_item,list)
}
