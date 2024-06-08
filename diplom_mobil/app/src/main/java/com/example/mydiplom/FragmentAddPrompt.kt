package com.example.mydiplom

import android.Manifest
import android.accounts.Account
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.database.getStringOrNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mydiplom.data.AddPrompt
import com.example.mydiplom.databinding.FragmentAddPromptBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class FragmentAddPrompt : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{


    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    var id: Long = 0

//    private val permissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestMultiplePermissions()
//    ) { permissions ->
//        val cameraGranted = permissions[Manifest.permission.READ_CALENDAR] ?: false
//        val locationGranted = permissions[Manifest.permission.WRITE_CALENDAR] ?: false
//
//        if (cameraGranted && locationGranted) {
//            id = addEventToCalendar()
//            Log.d("RetrofitClient","id " + id)
//        } else {
//
//        }
//    }
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var continuation: CancellableContinuation<Long>? = null

    private var binding: FragmentAddPromptBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val cameraGranted = permissions[Manifest.permission.READ_CALENDAR] ?: false
            val locationGranted = permissions[Manifest.permission.WRITE_CALENDAR] ?: false

            if (cameraGranted && locationGranted) {
                val id = addEventToCalendar()
                continuation?.resume(id)
                Log.d("RetrofitClient","id " + id)
            } else {
                continuation?.resumeWithException(Exception("Permissions denied"))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPromptBinding.inflate(inflater, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

//        val timeZone: String = TimeZone.getDefault().id
//        Log.d("RetrofitClient","timeZone " + timeZone)

        val context = activity ?: return binding!!.root
        binding!!.bthAddDatePrompt.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }

        binding!!.addpromptDate.setText(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()))

        binding!!.bthAddPromptToBD.setOnClickListener {
            if( !binding!!.addpromptName.text.isNullOrEmpty()  && !binding!!.addpromptDate.text.isNullOrEmpty()) {
                val name = binding!!.addpromptName.text.toString()
                val description = binding!!.addpromptDescription.text.toString()
                val date = binding!!.addpromptDate.text.toString()

                val check = binding!!.checkBox.isChecked

//                if (check) {
//                    permissionLauncher.launch(
//                        arrayOf(
//                            Manifest.permission.READ_CALENDAR,
//                            Manifest.permission.WRITE_CALENDAR
//                        )
//                    )
//                }
//
//                val addPrompt =
//                    AddPrompt(userId = 1, name = name, description = description, date = date, id)
//                val call: Call<Void> = service.addPrompt(addPrompt.userId, addPrompt)
//
//                call.enqueue(object : Callback<Void> {
//                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                        if (response.isSuccessful) {
//                            Toast.makeText(context, "Данные успешно добавлены", Toast.LENGTH_SHORT)
//                                .show()
//                        } else {
//                            Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Void>, t: Throwable) {
//                        Log.d("RetrofitClient", "Receive user from server problem " + t)
//                        Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
//                    }
//                })



            GlobalScope.launch {
                try {
                    if (check) {
                        val calendarId = requestPermissionsIfNeeded()
                        Log.d("RetrofitClient","calendarId " + calendarId)
                        val addPrompt = AddPrompt(userId = viewModel.userLoginId.value!!, name = name, description = description, date = date, calendar_id = calendarId)
                        val call: Call<Void> = service.addPrompt(addPrompt.userId, addPrompt)

                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Данные успешно добавлены", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.d("RetrofitClient", "Receive user from server problem " + t)
                                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                            }
                        })

                    }else{
                        val addPrompt = AddPrompt(userId = viewModel.userLoginId.value!!, name = name, description = description, date = date, calendar_id = null)
                        val call: Call<Void> = service.addPrompt(addPrompt.userId, addPrompt)

                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Данные успешно добавлены", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.d("RetrofitClient", "Receive user from server problem " + t)
                                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } catch (e: Exception) {
                    // Обработка отказа в разрешениях
                    Log.d("RetrofitClient", "Permissions denied")
                }
            }

            }
            else{
                Toast.makeText(context, "Сначала добавьте название и дату", Toast.LENGTH_SHORT).show()
            }
        }


        return binding!!.root

    }

    private suspend fun requestPermissionsIfNeeded(): Long {
        return if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            suspendCancellableCoroutine { cont ->
                continuation = cont
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR
                    )
                )
            }
        } else {
            addEventToCalendar() // Если разрешения уже предоставлены
        }
    }










    private fun getDateTimeCalendar(){
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()
        TimePickerDialog(context, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        val formattedDate = String.format(
            Locale.getDefault(),
            "%04d-%02d-%02d %02d:%02d",
            savedYear, savedMonth+1, savedDay, savedHour, savedMinute
        )
        binding!!.addpromptDate.setText(formattedDate)

//        binding!!.addpromptDate.setText("$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00")
    }




    private fun addEventToCalendar(): Long {

        val name = binding!!.addpromptName.text.toString()
        val description = binding!!.addpromptDescription.text.toString()
        val date = convertDateTimeToMilliseconds(binding!!.addpromptDate.text.toString())


        Log.d("RetrofitClient","date " + date)
        val calendars = getCalendars()

        val cr = requireContext().contentResolver
        val values = ContentValues().apply {
            put(CalendarContract.Events.TITLE, name)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.DTSTART, date)
            put(CalendarContract.Events.DTEND, date + 60 * 60 * 1000)
            put(CalendarContract.Events.CALENDAR_ID, calendars.first().id)
            put(CalendarContract.Events.EVENT_LOCATION, "")
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }
        val uri = cr.insert(CalendarContract.Events.CONTENT_URI, values)
        syncCalendar(calendars.first())
        val eventId = uri?.lastPathSegment?.toLongOrNull() ?: -1


        return eventId
    }

    class ListCalendars {
        var id : Long = 0
        var name = ""
        var accountName = ""
        var accountType = ""
    }

    fun getCalendars(): ArrayList<ListCalendars> {
        val calList = ArrayList<ListCalendars>()
//        if (checkPermission()) {
            val projection = arrayOf(
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.NAME,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE
            )
            val selection = "${CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL} = ${CalendarContract.Calendars.CAL_ACCESS_OWNER}"
            val cursor: Cursor? = requireContext().contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                selection,
                null,
                CalendarContract.Calendars._ID + " ASC"
            )
            if (cursor != null) while (cursor.moveToNext()){
                val calendar = ListCalendars()
                calendar.id = cursor.getLong(0)
                calendar.name = cursor.getStringOrNull(1) ?: ""
                calendar.accountName = cursor.getString(2)
                calendar.accountType = cursor.getString(3)
                calList.add(calendar)
            }
            cursor?.close()
//        }
        return calList
    }

    private fun syncCalendar(calendar: ListCalendars) {
        val account = Account(calendar.accountName, calendar.accountType)
        val extras = Bundle()
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true)
        val authority = CalendarContract.Calendars.CONTENT_URI.authority
        ContentResolver.requestSync(account, authority, extras)
    }

    fun convertDateTimeToMilliseconds(dateTime: String, pattern: String = "yyyy-MM-dd HH:mm"): Long {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date = dateFormat.parse(dateTime)
        return date?.time ?: -1

    }


    }