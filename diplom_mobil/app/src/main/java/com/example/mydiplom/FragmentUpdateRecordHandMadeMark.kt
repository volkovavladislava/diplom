package com.example.mydiplom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mydiplom.data.Mark
import com.example.mydiplom.data.MarkUpdate
import com.example.mydiplom.databinding.FragmentUpdateRecordHandMadeMarkBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class FragmentUpdateRecordHandMadeMark : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

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

    private var binding: FragmentUpdateRecordHandMadeMarkBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateRecordHandMadeMarkBinding.inflate(inflater, container, false)

        var handMadeMarkRecordId = viewModel.handMadeMarkRecordId.value ?: 1
        var handMadeMarkId = viewModel.handMadeMarkId.value ?: 1

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        val call: Call<Mark> = service.getMarkById(handMadeMarkRecordId)

        call.enqueue(object : Callback<Mark> {
            override fun onResponse(call: Call<Mark>, response: Response<Mark>) {
                if (response.isSuccessful) {
                    var markData = response.body()
                    markData?.let {
                        binding!!.updateRecordHandMadeMarkValue.setText(it.value_string.toString())
                        binding!!.updateRecordHandMadeMarkDate.setText(formatDate(it.date.toString()))

                    }
                }
                else{}
            }
            override fun onFailure(call: Call<Mark>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })







        val context = activity ?: return binding!!.root
        binding!!.bthAddDateUpdateRecordHandMadeMark.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }




        binding!!.bthUpdateRecordHandMadeMarkValue.setOnClickListener {

            if(!binding!!.updateRecordHandMadeMarkValue.text.isNullOrEmpty() && !binding!!.updateRecordHandMadeMarkDate.text.isNullOrEmpty()){
                val value = binding!!.updateRecordHandMadeMarkValue.text.toString()
                val date = binding!!.updateRecordHandMadeMarkDate.text.toString()


                val markUpdate = MarkUpdate(userId =1,kind_of_mark_id = handMadeMarkId,  date = date, null,null,null, value,null)
                val call: Call<Void> = service.updateMark(handMadeMarkRecordId, markUpdate)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Данные успешно обновлены", Toast.LENGTH_SHORT).show()
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
            else{
                Toast.makeText(context, "Сначала добавьте значение и дату", Toast.LENGTH_SHORT).show()
            }
        }


        binding!!.bthDeleteRecordHandMadeMarkValue.setOnClickListener {
            val call: Call<Void> = service.deleteMark(handMadeMarkRecordId)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Удалено", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.d("RetrofitClient", "Receive user from server problem " + t)
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                }
            })
            findNavController().navigate(R.id.fragmentDetailedHandMadeMark)
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
        binding!!.updateRecordHandMadeMarkDate.setText(formattedDate)

    }



    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

}