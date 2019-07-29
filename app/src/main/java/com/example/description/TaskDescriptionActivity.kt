package com.example.description

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.addtaskdemo.R
import kotlinx.android.synthetic.main.activity_task_description.view.*

class TaskDescriptionActivity : AppCompatActivity() {

    companion object{
        val EXTRA_TASK_DESCRIPTION = "task"
    }

    private lateinit var editText : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_description)
    }

    /**
     * Did done oprion
     * @param Button view
     * @author : Subhra Roy
     */
    fun didTapDoneOnClick(view : View){

        editText = findViewById(R.id.descriptionTextView) as EditText
        if (editText != null) {
            val finalText = editText.text.toString()
            if(!finalText.isEmpty()){

                val result = Intent()
                result.putExtra(EXTRA_TASK_DESCRIPTION,finalText)
                setResult(Activity.RESULT_OK,result)
            }else{
                setResult(Activity.RESULT_CANCELED)
            }
        }

        finish()
    }
}
