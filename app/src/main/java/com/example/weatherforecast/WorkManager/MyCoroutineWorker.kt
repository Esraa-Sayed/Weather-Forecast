package com.example.weatherforecast.WorkManager

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherforecast.Constants.AlertsConstants
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.Constants.WorkManagerConstants.ALERT
import com.example.weatherforecast.Constants.WorkManagerConstants.DESCRIPTION
import com.example.weatherforecast.Constants.WorkManagerConstants.FROM_TIME_IN_MILLIS
import com.example.weatherforecast.Constants.WorkManagerConstants.ICON
import com.example.weatherforecast.Constants.WorkManagerConstants.convertToUserAlert
import com.example.weatherforecast.Constants.WorkManagerConstants.openNotification
import com.example.weatherforecast.DialogActivity.view.DialogActivity
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.R
import com.example.weatherforecast.WorkManager.WorkRequestManager.removeWork
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.repo.Repository
import com.example.weatherforecast.repo.RepositoryInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MyCoroutineWorker(private val context: Context, parameters: WorkerParameters): CoroutineWorker(context, parameters) {

    private lateinit var notificationRepository: RepositoryInterface
    override suspend fun doWork(): Result {
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("TAG", "doWork: *************" )
            val myAlert = convertToUserAlert(inputData.getString(ALERT)!!)
            val description = inputData.getString(DESCRIPTION)
            val icon = inputData.getString(ICON)
            val fromTimeInMillis = inputData.getLong(FROM_TIME_IN_MILLIS, 0L)

            notificationRepository =
                Repository.getInstance(null, ConcreteLocalSource(applicationContext))

            if (checkTime(myAlert)) {
                Log.e("TAG", "doWork: *************222" )
                if (notificationRepository.readBooleanFromSharedPreferences(SharedPrefrencesKeys.notification,context) || myAlert.alertOption == AlertsConstants.NOTIFICATION){
                    openNotification(context, myAlert, description!!, icon!!, context.getString(R.string.app_name))
                    Log.e("TAG", "doWork: *************333" )
                }

                if (myAlert.alertOption == AlertsConstants.ALARM) {
                    if (Settings.canDrawOverlays(context)) {
                        Log.e("TAG", "doWork: *************4444" )
                        withContext(Dispatchers.Main) {
                            val intent = Intent(context, DialogActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_INCLUDE_STOPPED_PACKAGES or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            intent.putExtra(DESCRIPTION, description)
                            intent.putExtra(ICON, icon)
                            context.startActivity(intent)
                        }
                    }
                }

                WorkRequestManager.createWorkRequest(
                    myAlert,
                    description.toString(),
                    icon.toString(),
                    context,
                    (fromTimeInMillis + 86400000)
                )
            } else {
                notificationRepository.deleteUserAlert(myAlert)
                removeWork("${myAlert.id}", context)
            }
        }

        return Result.success()
    }

    private fun checkTime(alert: UserAlerts): Boolean {
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        return currentTimeInMillis >= alert.startLongDate&& currentTimeInMillis <= alert.endLongDate
    }
}