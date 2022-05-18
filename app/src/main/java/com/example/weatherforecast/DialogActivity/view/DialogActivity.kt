package com.example.weatherforecast.DialogActivity.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.weatherforecast.Constants.WorkManagerConstants.DESCRIPTION
import com.example.weatherforecast.Constants.WorkManagerConstants.ICON
import com.example.weatherforecast.Constants.WorkManagerConstants.getIcon
import com.example.weatherforecast.R

class DialogActivity : AppCompatActivity() {
    private lateinit var description: String
    private lateinit var icon: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        description = intent.getStringExtra(DESCRIPTION).toString()
        icon = intent.getStringExtra(ICON).toString()

        //                              width                               height
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setFinishOnTouchOutside(false)

        handleUI()
    }

    private fun handleUI() {
        var imageView = findViewById<ImageView>(R.id.image_icon)
        imageView.setImageResource(getIcon(icon))

        findViewById<TextView>(R.id.text_description).text = description
        findViewById<Button>(R.id.btnDismiss).setOnClickListener {
//            deleteAlert()
            finish()
        }
    }
}