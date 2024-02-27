package com.agriculture.myapplication.Utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.agriculture.myapplication.R

class Utils(var activity:Activity) : AppCompatActivity() {
    private lateinit var isProgress:AlertDialog

    companion object{
        fun showToast(context:Context ,message: String){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun intent(currentActivity:Context, activityDes: Class<out Activity>){
            val intent = Intent(currentActivity, activityDes)
            currentActivity.startActivity(intent)
        }
    }

    fun showProgress(){
        val layout = activity.layoutInflater.inflate(R.layout.scroll_bar_layout,null)
        val dialog = AlertDialog.Builder(activity)
        dialog.setView(layout)
        dialog.setCancelable(false)
        isProgress = dialog.create()
        isProgress.show()
    }

    fun dismissProgress(){
        isProgress.dismiss()
    }

}