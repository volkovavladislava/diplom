package com.example.mydiplom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.material3.DatePickerDialog
import com.example.mydiplom.data.AddPrompt
import com.example.mydiplom.data.UserUpdate
import com.example.mydiplom.databinding.FragmentAddPromptBinding
import com.example.mydiplom.databinding.FragmentListRemindersBinding
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

    private var binding: FragmentAddPromptBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            val name = binding!!.addpromptName.text.toString()
            val description = binding!!.addpromptDescription.text.toString()
            val date = binding!!.addpromptDate.text.toString()

            Log.d("RetrofitClient","date " + date)

            val addPrompt = AddPrompt(userId = 1, name = name, description = description, date = date)
            val call: Call<Void> = service.addPrompt(addPrompt.userId, addPrompt)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Данные успешно добавлены", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("RetrofitClient","Receive user from server problem " + t)
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                }
            })
        }


        return binding!!.root

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


}