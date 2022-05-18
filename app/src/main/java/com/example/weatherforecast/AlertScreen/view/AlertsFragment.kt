package com.example.weatherforecast.AlertScreen.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.AlertScreen.viewModel.AlertsViewModel
import com.example.weatherforecast.AlertScreen.viewModel.AlertsViewModelFactory
import com.example.weatherforecast.Constants.AlertsConstants
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.Model.WeatherModel
import com.example.weatherforecast.WorkManager.WorkRequestManager.createWorkRequest
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.repo.Repository
import java.text.SimpleDateFormat


class AlertsFragment : Fragment(),OnButtonClickListener {
    lateinit var alertsViewModel: AlertsViewModel
    lateinit var alertsViewModelFactory:AlertsViewModelFactory

    private  lateinit var alertsRecyclerView: RecyclerView
    private  lateinit var alertsRecyclerViewAdapter: AlertsRecyclerViewAdapter
    private  lateinit var addAlertsFloatingActionButton: FloatingActionButton
    private lateinit var myView: View
    private lateinit var dialog: Dialog
    private lateinit var startDateid: EditText
    private lateinit var saveAlertsData: Button
    private lateinit var endDateid: EditText
    private lateinit var alertOptions: RadioGroup
    private lateinit var  userAlerts: UserAlerts
    private lateinit var  weatherModel: WeatherModel
    private var startDate: Calendar = Calendar.getInstance()
    private var endDate: Calendar =  Calendar.getInstance()
    private var option = "alarm"
    private var startLongDate:Long = 0
    private var endLongDate:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        init()
    }
    private fun init() {

        alertsViewModelFactory =  AlertsViewModelFactory(
            Repository.getInstance(null, ConcreteLocalSource(myView.context))
        )
        alertsViewModel = ViewModelProvider(this, alertsViewModelFactory)[AlertsViewModel::class.java]

        alertsRecyclerView = myView.findViewById(R.id.alertsRecyclerView)
        alertsRecyclerViewAdapter = AlertsRecyclerViewAdapter(myView.context,this, emptyList(),alertsViewModel.getAppLanguage(myView.context))
        var layoutMan = LinearLayoutManager(activity)
        alertsRecyclerView.apply {
            setHasFixedSize(true)
            layoutMan.orientation = RecyclerView.VERTICAL
            layoutManager = layoutMan
            adapter = alertsRecyclerViewAdapter
        }

        addAlertsFloatingActionButton = myView.findViewById(R.id.addAlertsFloatingActionButton)
        dialog = Dialog(myView.context)

        dialog.setContentView(R.layout.take_alert_time_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        startDateid = dialog.findViewById(R.id.startDateid)
        alertOptions = dialog.findViewById(R.id.alertOptions)
        saveAlertsData = dialog.findViewById(R.id.saveAlertsData)
        addListeners()

        alertsViewModel.getUserAlerts().observe(viewLifecycleOwner, {
            updateAdapter(it)
        })
        alertsViewModel.id.observe(viewLifecycleOwner, { alert ->
            userAlerts.id = alert
            
            alertsViewModel.getLocalWeatherModele().observe(viewLifecycleOwner,{
                weatherModel = it[0]
                if (weatherModel.alerts.isNullOrEmpty()){
                    setOneTimeWorkRequest(userAlerts, getString(R.string.NoAlerts), weatherModel.current.weather[0].icon)
                }else{
                    setOneTimeWorkRequest(userAlerts, weatherModel.alerts!![0].tags[0],weatherModel.current.weather[0].icon)
                }
            })
        })
        

    }
    private fun setOneTimeWorkRequest(alert: UserAlerts, description: String, icon: String) {
        createWorkRequest(alert, description, icon, myView.context,userAlerts.startLongDate)
    }

    private fun updateAdapter(userAlerts: List<UserAlerts>) {
            alertsRecyclerViewAdapter.updateData(userAlerts)
    }

    private fun  addListeners(){
        addAlertsFloatingActionButton.setOnClickListener {

            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val intent = Intent()
//            val packageName: String = myView.context.packageName
//            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
//            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
//                intent.data = Uri.parse("package:$packageName")
//                startActivity(intent)
//            }
//        }
            val pm = view!!.context.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!Settings.canDrawOverlays(view!!.context)) {
                    askForDrawOverlaysPermission()
            }
            else if (!pm.isIgnoringBatteryOptimizations(myView.context.packageName)) {
                Log.e("TAG", "addListeners: *******" )
                val intent = Intent()
                intent.action = ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:" + view!!.context.packageName)
                startActivity(intent)
            } else {
                dialog.show()
            }

        }
        startDateid.setOnClickListener {
             showDateTimePicker(AlertsConstants.START_EDIT_TEXT)
        }
        endDateid = dialog.findViewById(R.id.endDateid)
        endDateid.setOnClickListener {
             showDateTimePicker(AlertsConstants.END_EDIT_TEXT)

        }
        alertOptions.setOnCheckedChangeListener{_,checkedId->
                val radioButton:RadioButton = dialog.findViewById(checkedId)
                option = radioButton.getText().toString()
                if (option == getString(R.string.alarm)) {
                    option = AlertsConstants.ALARM
                }
                else {
                    option = AlertsConstants.NOTIFICATION
                }
        }

        saveAlertsData.setOnClickListener {
            if (startDateid.text.toString().isEmpty()) {
                startDateid.error = "Field is required"
            }
            else if (endDateid.text.toString().isEmpty()) {
                endDateid.error = "Field is required"
            }
            else{
                storeDataInRoom()
                Log.e("TAG", "startDate :$startLongDate")
                Log.e("TAG", "endDate :$endLongDate")
                Log.e("TAG", "option :$option")

                startDateid.setText("")
                endDateid.setText("")
                dialog.cancel()
            }
        }
    }
    private fun askForDrawOverlaysPermission() {
        if (!Settings.canDrawOverlays(view!!.context)) {
            if ("xiaomi" == Build.MANUFACTURER.lowercase(Locale.ROOT)) {
                val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                intent.setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity"
                )
                intent.putExtra("extra_pkgname", view!!.context.packageName)
                AlertDialog.Builder(view!!.context)
                    .setTitle(R.string.draw_overlays)
                    .setMessage(R.string.draw_overlays_description)
                    .setPositiveButton(R.string.go_to_settings) { dialog, which ->
                        startActivity(
                            intent
                        )
                    }
                    .setIcon(R.drawable.ic_warning)
                    .show()
            } else {
                AlertDialog.Builder(view!!.context)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.error_msg_permission_required)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        val permissionIntent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + view!!.context.packageName)
                        )
                        runtimePermissionResultLauncher.launch(permissionIntent)
                    }
                    .setIcon(R.drawable.ic_warning)
                    .show()
            }
        }

    }
    private val runtimePermissionResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    private fun storeDataInRoom(){
         userAlerts = UserAlerts(startLongDate = startLongDate,endLongDate = endLongDate, alertOption = option)
        alertsViewModel.insertUserAlerts(userAlerts)
    }
    private fun showDateTimePicker(label:String){
        val currentDate = Calendar.getInstance()
        var date: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context!!,
            { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(context,
                    { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        if (label == AlertsConstants.START_EDIT_TEXT) {
                            startDate = date
                            startLongDate = updateLabel(startDate,startDateid)
                        }
                        else{
                            endDate = date
                            endLongDate = updateLabel(endDate,endDateid)
                        }
                        Log.v("TAG", "The choosen one " + date.getTime())
                    }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false
                ).show()
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE]
        )
        datePickerDialog.datePicker.minDate = currentDate.timeInMillis;
        datePickerDialog.show();
    }
    private fun updateLabel(calendar: Calendar,editText: EditText):Long {
        val myFormat = "HH:mm a\ndd/MM/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale(alertsViewModel.getAppLanguage(myView.context)))
        val milliseconds: Long = calendar.timeInMillis

        editText.setText(dateFormat.format(calendar.time))
        return milliseconds
    }

    override fun onDeleteButtonClickListener(userAlert: UserAlerts) {
        alertsViewModel.deleteUserAlerts(userAlert)
    }
}