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
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mydiplom.data.MarkDeleteDavlenie
import com.example.mydiplom.data.MarkUpdate
import com.example.mydiplom.data.MarkUpdateDavlenie
import com.example.mydiplom.databinding.FragmentUpdateRecordMarkNum2Binding
import com.example.mydiplom.viewmodel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentUpdateRecordMarkNum2 : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

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

    private var binding: FragmentUpdateRecordMarkNum2Binding? = null
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateRecordMarkNum2Binding.inflate(inflater, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)


        var updateNum2Id1 = viewModel.updateNum2Id1.value
        var updateNum2Id2 = viewModel.updateNum2Id2.value
        var updateNum2UserId = viewModel.updateNum2UserId.value
        var updateNum2KindOfMarkId = viewModel.updateNum2KindOfMarkId.value
        var updateNum2Date = viewModel.updateNum2Date.value
        var updateNum2Value1Double = viewModel.updateNum2Value1Double.value
        var updateNum2Value2Double = viewModel.updateNum2Value2Double.value

        binding!!.updateMarkNum2Date.setText(formatDate(updateNum2Date.toString()))
        binding!!.updateMarkValue1Num2.setText(updateNum2Value1Double.toString())
        binding!!.updateMarkValue2Num2.setText(updateNum2Value2Double.toString())

        val context = activity ?: return binding!!.root


        binding!!.bthUpdateDateMarkNum2.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }







        binding!!.bthUpdateMarkNum2.setOnClickListener {
            if( !binding!!.updateMarkValue1Num2.text.isNullOrEmpty()  && !binding!!.updateMarkValue2Num2.text.isNullOrEmpty()  && !binding!!.updateMarkNum2Date.text.isNullOrEmpty()) {
                val value1 = binding!!.updateMarkValue1Num2.text.toString().toDouble()
                val value2 = binding!!.updateMarkValue2Num2.text.toString().toDouble()
                val date = binding!!.updateMarkNum2Date.text.toString()


                val markUpdate = MarkUpdateDavlenie(
                    id1 = updateNum2Id1!!,
                    id2 = updateNum2Id2!!,
                    user_id = updateNum2UserId!!,
                    kind_of_mark_id1 = 1,
                    kind_of_mark_id2 = 2,
                    date = date,
                    value_number1 = value1 ,
                    value_number2 = value2 )

                Log.d("RetrofitClient","markUpdate " + markUpdate)

                val call: Call<Void> = service.updateMarkDavlenie(markUpdate)
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



        binding!!.bthDeleteMarkNum2.setOnClickListener {
            val delMark  = MarkDeleteDavlenie(updateNum2Id1!!, updateNum2Id2!!)
            val call: Call<Void> = service.deleteMarkDavlenie(delMark)
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
            val bundle = bundleOf("title" to viewModel.kindOfMarkNameStatistic.value )
            findNavController().navigate(R.id.fragmentDetailedStatisticNum2, bundle)
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
        binding!!.updateMarkNum2Date.setText(formattedDate)
    }


    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }


}