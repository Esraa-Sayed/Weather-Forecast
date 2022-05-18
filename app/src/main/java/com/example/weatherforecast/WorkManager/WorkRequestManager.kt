package com.example.weatherforecast.WorkManager

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.weatherforecast.Constants.WorkManagerConstants.ALERT
import com.example.weatherforecast.Constants.WorkManagerConstants.DESCRIPTION
import com.example.weatherforecast.Constants.WorkManagerConstants.FROM_TIME_IN_MILLIS
import com.example.weatherforecast.Constants.WorkManagerConstants.ICON
import com.example.weatherforecast.Constants.WorkManagerConstants.convertUserAlertToString
import com.example.weatherforecast.Model.UserAlerts
import java.util.*
import java.util.concurrent.TimeUnit

object WorkRequestManager {
    fun createWorkRequest(alert: UserAlerts,
                          description: String,
                          icon: String,
                          context: Context,
                          fromTimeInMillis: Long) {
        val data = Data.Builder()
            .putString(ALERT, convertUserAlertToString(alert))
            .putString(DESCRIPTION, description)
            .putString(ICON, icon)
            .putLong(FROM_TIME_IN_MILLIS, fromTimeInMillis)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyCoroutineWorker::class.java)
            .setInitialDelay(fromTimeInMillis - Calendar.getInstance().timeInMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag(alert.id.toString())
            .build()

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork("${alert.id}", ExistingWorkPolicy.REPLACE, oneTimeWorkRequest)
    }

    fun removeWork(tag: String, context: Context) {
        val worker = WorkManager.getInstance(context)
        worker.cancelAllWorkByTag(tag)
    }
}