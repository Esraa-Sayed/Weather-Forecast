package com.example.weatherforecast.DialogActivity.view

import android.media.MediaPlayer
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
    var mMediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        startSound()
        description = intent.getStringExtra(DESCRIPTION).toString()
        icon = intent.getStringExtra(ICON).toString()

        //                              width                               height
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setFinishOnTouchOutside(false)

        handleUI()
    }

    private fun startSound() {
        if (mMediaPlayer == null) { //mMediaPkayer is your variable
            mMediaPlayer = MediaPlayer.create(this, R.raw.weather_sound) //raw is the folder where you have the audio files or sounds, water is the audio file (is a example right)
            mMediaPlayer!!.isLooping = true //to repeat again n again
            mMediaPlayer!!.start() //to start the sound
        }
    }

    private fun handleUI() {
        var imageView = findViewById<ImageView>(R.id.image_icon)
        imageView.setImageResource(getIcon(icon))

        findViewById<TextView>(R.id.text_description).text = description
        findViewById<Button>(R.id.btnDismiss).setOnClickListener {
            mMediaPlayer!!.stop()
            finish()
        }
    }
}