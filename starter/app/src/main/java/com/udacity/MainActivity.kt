package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var selected: Int = 0
    private lateinit var description: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        var downloadUrl: String = ""
        var fileName: String = ""
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(
                getString(R.string.download_notification_channel_id),
                getString(R.string.download_notification_channel_name)
        )
        custom_button.setOnClickListener {
            custom_button.animateArc()
            Log.d("FLUX", "selected " + radioGroup.checkedRadioButtonId)
            if (radioGroup.checkedRadioButtonId != -1){
                when(radioGroup.checkedRadioButtonId){
                    R.id.glide -> {
                        description = GLIDE_FILE_NAME
                        downloadUrl = GLIDE_URL
                        fileName = "glide.zip"
                    }
                    R.id.udacity -> {
                        description = UDACITY_FILE_NAME
                        downloadUrl = UDACITY_URL
                        fileName = "udacity.zip"
                    }
                    R.id.retrofit -> {
                        description = RETROFIT_FILE_NAME
                        downloadUrl = RETROFIT_URL
                        fileName = "retrofit.zip"
                    }
                }
                download(downloadUrl, fileName)
            }else{
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
            )
                    .apply {
                        setShowBadge(false)
                    }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download completed"
            val notificationManager = getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            Log.d("FLUX", "Download completed " + id)
            val status = if (id != -1L) "Success" else "Failed"

            showNotification(status)
        }
    }

    private fun showNotification(status: String){
        Log.d("FLUX", "Show notification")
        val notificationManager = ContextCompat.getSystemService(
                this,
                NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
                this.getText(R.string.download_complete).toString(),
                this,
                status,
                description
        )
    }

    private fun download(url: String, fileName: String) {
        //val splitted = url.split("/")
        //Log.d("FLUX", splitted[splitted.size-1])
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)


    }

    companion object {
        private const val GLIDE_URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val UDACITY_URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/masterz.zip"
        private const val RETROFIT_URL = "https://github.com/square/retrofit/archive/refs/heads/master.zip"

        private const val GLIDE_FILE_NAME = "Glide - Image Loading Library by BumpTech"
        private const val UDACITY_FILE_NAME = "LoadApp - Current repository by Udacity"
        private const val RETROFIT_FILE_NAME = "Retrofit - Type-safe HTTP client for Android and Java by Square Inc"


        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
