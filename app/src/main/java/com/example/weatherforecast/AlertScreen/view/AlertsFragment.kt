package com.example.weatherforecast.AlertScreen.view

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
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforecast.AlertScreen.viewModel.AlertsViewModel
import com.example.weatherforecast.AlertScreen.viewModel.AlertsViewModelFactory
import com.example.weatherforecast.Constants.AlertsConstants
import com.example.weatherforecast.Constants.SharedPrefrencesKeys
import com.example.weatherforecast.LocaleHelperChangeLanguage.LocaleHelper
import com.example.weatherforecast.Model.UserAlerts
import com.example.weatherforecast.db.ConcreteLocalSource
import com.example.weatherforecast.repo.Repository
import com.example.weatherforecast.viewModel.MainSettingFavouriteViewModel
import com.example.weatherforecast.viewModel.MainSettingFavouriteViewModelFactory
import java.text.ParseException
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
            Repository.getInstance(null, ConcreteLocalSource(myView.context),myView.context)
        )
        alertsViewModel = ViewModelProvider(this, alertsViewModelFactory)[AlertsViewModel::class.java]

        alertsRecyclerView = myView.findViewById(R.id.alertsRecyclerView)
        alertsRecyclerViewAdapter = AlertsRecyclerViewAdapter(myView.context,this, emptyList(),alertsViewModel.getAppLanguage())
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

    }

    private fun updateAdapter(userAlerts: List<UserAlerts>) {
            alertsRecyclerViewAdapter.updateData(userAlerts)
    }

    private fun  addListeners(){
        addAlertsFloatingActionButton.setOnClickListener {
            dialog.show()
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
                    option = "alarm"
                }
                else {
                    option = "notification"
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
    private fun storeDataInRoom(){
        val userAlerts = UserAlerts(startLongDate = startLongDate,endLongDate = endLongDate, alertOption = option)
        alertsViewModel.insertUserAlerts(userAlerts)
    }
    private fun showDateTimePicker(label:String){
        val currentDate = Calendar.getInstance()
        var date: Calendar = Calendar.getInstance()
        DatePickerDialog(
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
        ).show()
    }
    private fun updateLabel(calendar: Calendar,editText: EditText):Long {
        val myFormat = "HH:mm a\ndd/MM/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale(alertsViewModel.getAppLanguage()))
        val milliseconds: Long = calendar.timeInMillis

        editText.setText(dateFormat.format(calendar.time))
        return milliseconds
    }

    override fun onDeleteButtonClickListener(userAlert: UserAlerts) {
        alertsViewModel.deleteUserAlerts(userAlert)
    }
}