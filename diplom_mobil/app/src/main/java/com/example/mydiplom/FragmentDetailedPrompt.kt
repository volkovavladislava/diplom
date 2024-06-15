package com.example.mydiplom

import android.Manifest
import android.accounts.Account
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.navigation.fragment.findNavController
import com.example.mydiplom.data.Prompt
import com.example.mydiplom.data.PromptUpdate
import com.example.mydiplom.databinding.FragmentDetailedPromptBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FragmentDetailedPrompt : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

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

    private var binding: FragmentDetailedPromptBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    private var initialDate: String? = null
    private var initialCalendarId: Long? = null

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var continuation: CancellableContinuation<Long>? = null

    private var isAdd: Int? = null
    private var isUpdate: Int? = null
    private var isDelete: Int? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val cameraGranted = permissions[Manifest.permission.READ_CALENDAR] ?: false
            val locationGranted = permissions[Manifest.permission.WRITE_CALENDAR] ?: false

            if (cameraGranted && locationGranted) {
                var id : Long? = null
                if(isAdd !=  null){
                     id = addEventToCalendar()
                }
                else{
                    if(isDelete !=  null){
                         id = deleteEvent(initialCalendarId!!)
                    }
                    else{
                         id = updateEvent(initialCalendarId!!)
                    }
                }
                continuation?.resume(id)
//                Log.d("RetrofitClient","id " + id)
            } else {
                Toast.makeText(context, "Проблемы с доступом к календарю. Проверьте вход в аккаунт", Toast.LENGTH_SHORT).show()
                continuation?.resumeWithException(Exception("Permissions denied"))

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailedPromptBinding.inflate(inflater, container, false)

        var promptId = viewModel.promptId.value ?: 1

        val client = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-access-token", viewModel.token.value)
                    .build()
                val result = chain.proceed(request)
                if (result.code() == 403 || result.code() == 401) {
                    viewModel.notifyTokenExpired()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                result
            })
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://37.46.130.221:3000")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        val call: Call<Prompt> = service.getPromptById(promptId)


        call.enqueue(object : Callback<Prompt> {
            override fun onResponse(call: Call<Prompt>, response: Response<Prompt>) {
                if (response.isSuccessful) {
                    var promptData = response.body()

                    promptData?.let {
                        binding!!.detailedPromptName.setText(it.name.toString())
                        binding!!.detailedPromptDescription.setText(it.description.toString())
                        binding!!.detailedPromptDate.setText(formatDate(it.date.toString()))
                        if(it.calendar_id != null){
                            binding!!.checkBoxDetailedPrompt.isChecked = true
                            initialCalendarId = it.calendar_id.toString().toLong()
                        }

                        initialDate = formatDate(it.date.toString())
                    }
                }
                else{}
            }
            override fun onFailure(call: Call<Prompt>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })





        binding!!.bthAddDetailedPromptToBD.setOnClickListener {
            if( !binding!!.detailedPromptName.text.isNullOrEmpty()  && !binding!!.detailedPromptDate.text.isNullOrEmpty()) {
                val name = binding!!.detailedPromptName.text.toString()
                val description = binding!!.detailedPromptDescription.text.toString()
                val date = binding!!.detailedPromptDate.text.toString()



                val check = binding!!.checkBoxDetailedPrompt.isChecked

                GlobalScope.launch {
                    try {


                        if(check && initialCalendarId == null){
                            //добавить событие
                            isAdd = 1
                            isDelete = null
                            isUpdate = null
                            val calendarId = requestPermissionsIfNeeded()
                            val promptUpdate = PromptUpdate(name = name, description = description, date = date, calendar_id = calendarId)
                            val call: Call<Void> = service.updatePrompt(promptId, promptUpdate)
                            call.enqueue(object : Callback<Void> {
                                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "Данные успешно обновлены", Toast.LENGTH_SHORT).show()
                                        initialCalendarId = calendarId
                                        isAdd = null
                                        isDelete = null
                                        isUpdate = null
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
                        else{
                            if(check && initialCalendarId != null){
                                //обновить событие и бд
                                isAdd = null
                                isDelete = null
                                isUpdate = 1

                                val calendarId = requestPermissionsIfNeeded()

                                val promptUpdate = PromptUpdate(name = name, description = description, date = date, calendar_id = calendarId)
                                val call: Call<Void> = service.updatePrompt(promptId, promptUpdate)
                                call.enqueue(object : Callback<Void> {
                                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Данные успешно обновлены", Toast.LENGTH_SHORT).show()
                                            initialCalendarId = calendarId
                                            isAdd = null
                                            isDelete = null
                                            isUpdate = null
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
                            if(!check && initialCalendarId != null){
                                // удалить событие и обновить бд
                                isAdd = null
                                isDelete = 1
                                isUpdate = null


                                val calendarId = requestPermissionsIfNeeded()
                                val promptUpdateDel = PromptUpdate(name = name, description = description, date = date, calendar_id = null)

                                val call: Call<Void> = service.updatePrompt(promptId, promptUpdateDel)

                                call.enqueue(object : Callback<Void> {
                                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Данные успешно обновлены", Toast.LENGTH_SHORT).show()
                                            initialCalendarId = null
                                            isAdd = null
                                            isDelete = null
                                            isUpdate = null
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

                            if(!check && initialCalendarId == null){

                                val promptUpdate = PromptUpdate(name = name, description = description, date = date, calendar_id = null)

                                val call: Call<Void> = service.updatePrompt(promptId, promptUpdate)

                                call.enqueue(object : Callback<Void> {
                                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Данные успешно обновлены", Toast.LENGTH_SHORT).show()
                                            initialCalendarId = null
                                            isAdd = null
                                            isDelete = null
                                            isUpdate = null
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
                        }



                    }
                    catch (e: Exception) {
                        // Обработка отказа в разрешениях
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, " Проверьте аккаунт в календаре или разрешения в настройках", Toast.LENGTH_LONG).show()
                        }
                        Log.d("RetrofitClient", "Permissions denied")
                    }
                }

            }else{
                Toast.makeText(context, "Сначала добавьте значение и дату", Toast.LENGTH_SHORT).show()
            }
        }

        val context = activity ?: return binding!!.root
        binding!!.bthAddDateDetailedPrompt.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }





        binding!!.bthDeleteDetailedPrompt.setOnClickListener{

            GlobalScope.launch {
                try {
                    val check = binding!!.checkBoxDetailedPrompt.isChecked
                    if(check || initialCalendarId!=null){
                        isAdd = null
                        isDelete = 1
                        isUpdate = null

                        val calendarId = requestPermissionsIfNeeded()
                        val call: Call<Void> = service.deletePrompt(promptId)
                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                                    isAdd = null
                                    isDelete = null
                                    isUpdate = null
                                    findNavController().popBackStack()
                                } else {
                                    Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.d("RetrofitClient", "Receive user from server problem " + t)
                                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    else{
                        val call: Call<Void> = service.deletePrompt(promptId)
                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                                    isAdd = null
                                    isDelete = null
                                    isUpdate = null
                                    findNavController().popBackStack()
                                } else {
                                    Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                Log.d("RetrofitClient", "Receive user from server problem " + t)
                                Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }

                }
                catch (e: Exception) {
                    // Обработка отказа в разрешениях
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, " Проверьте аккаунт в календаре или разрешения в настройках", Toast.LENGTH_LONG).show()
                    }
                    Log.d("RetrofitClient", "Permissions denied")
                }
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
            if(isAdd !=  null){
//                Log.d("RetrofitClient", "isAdd" )
                addEventToCalendar()

            }
            else{
                if(isDelete !=  null){
//                    Log.d("RetrofitClient", "isDelete" )
                    deleteEvent(initialCalendarId!!)
                }
                else{
//                    Log.d("RetrofitClient", "isUpdate" )
                    updateEvent(initialCalendarId!!)
                }
            }

             // Если разрешения уже предоставлены
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
        binding!!.detailedPromptDate.setText(formattedDate)

//        binding!!.detailedPromptDate.setText("$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00")
    }

    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

    fun convertDateTimeToMilliseconds(dateTime: String, pattern: String = "yyyy-MM-dd HH:mm"): Long {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        val date = dateFormat.parse(dateTime)
        return date?.time ?: -1

    }




    private fun addEventToCalendar(): Long {

        val name = binding!!.detailedPromptName.text.toString()
        val description = binding!!.detailedPromptDescription.text.toString()
        val date = convertDateTimeToMilliseconds(binding!!.detailedPromptDate.text.toString())



        val calendars = getCalendars()

//        Log.d("RetrofitClient","calendars " + calendars)

        val cr = requireContext().contentResolver
        val values = ContentValues().apply {
            put(CalendarContract.Events.TITLE, name)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.DTSTART, date - 60 * 60 * 1000*24)
            put(CalendarContract.Events.DTEND, date )
            put(CalendarContract.Events.CALENDAR_ID, calendars.first().id)
            put(CalendarContract.Events.EVENT_LOCATION, "")
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }
        val uri = cr.insert(CalendarContract.Events.CONTENT_URI, values)
        syncCalendar(calendars.first())
        val eventId = uri?.lastPathSegment?.toLongOrNull() ?: -1


        return eventId
    }


    private fun deleteEvent(eventId: Long): Long  {
//        Log.d("RetrofitClient", "deleteEvent " + eventId)
        val calendars = getCalendars()
        val cr = requireContext().contentResolver
        val eventUri = CalendarContract.Events.CONTENT_URI
        val row = cr.delete(eventUri, "${CalendarContract.Events._ID} = $eventId", null)
        syncCalendar(calendars.first())
        if(row >1 ){
//            Log.d("RetrofitClient", "row > 1 " )
            return 1
        }else{
//            Log.d("RetrofitClient", "row 0 " )
            return 0
        }

    }


    private fun updateEvent(eventId: Long): Long  {
//        Log.d("RetrofitClient", "updateeEvent " + eventId)

        val name = binding!!.detailedPromptName.text.toString()
        val description = binding!!.detailedPromptDescription.text.toString()
        val date = convertDateTimeToMilliseconds(binding!!.detailedPromptDate.text.toString())

        val calendars = getCalendars()
        val cr = requireContext().contentResolver

        val values = ContentValues().apply {
            put(CalendarContract.Events.TITLE, name)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.DTSTART, date - 60 * 60 * 1000*24)
            put(CalendarContract.Events.DTEND, date )
            put(CalendarContract.Events.CALENDAR_ID, calendars.first().id)
            put(CalendarContract.Events.EVENT_LOCATION, "")
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        val uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
        cr.update(uri, values, null, null)
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
}