package com.udacity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val statusText = intent.getStringExtra("status")
        val fileNameText = intent.getStringExtra("fileName")

        if (statusText == "Success"){
            status.setTextColor(Color.GREEN)
        }else{
            status.setTextColor(Color.RED)
        }

        file_name.setText(fileNameText)
        status.setText(statusText)
        btnOk.setOnClickListener {
            this.finish()
        }
    }

}
