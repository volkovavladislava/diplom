package com.example.mydiplom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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
import com.example.mydiplom.data.AddMark
import com.example.mydiplom.data.AddMarkDavlenie
import com.example.mydiplom.databinding.FragmentAddNewRecordMark2numberBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FragmentAddNewRecordMark2number : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

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

    val listOfSituations = mutableMapOf(
        "спокойное" to 1,
        "после нагрузки" to 2,
        "после еды" to 3,
        "после стресса" to 4,
        "после сна" to 5,
        "после приема лекарства" to 6
    )

    val listOfSituationsForLabel: ArrayList<String?> = arrayListOf(
        "спокойное",
        "после нагрузки" ,
        "после еды",
        "после стресса",
        "после сна",
        "после приема лекарства"
    )


    private var binding: FragmentAddNewRecordMark2numberBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewRecordMark2numberBinding.inflate(inflater, container, false)

        var kindOfMarkId = viewModel.kindOfMarkIdAddMark.value ?: 1
        var kindOfMarkName = viewModel.kindOfMarkNameAddMark.value

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
            .baseUrl("http://192.168.0.32:3000")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)



        val context = activity ?: return binding!!.root
        binding!!.bthAddDateMarkNum2.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }
        binding!!.addMarkNum2Date.setText(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()))

        binding!!.textField4.setText(listOfSituationsForLabel[0])
        (binding!!.addMarkNum2ChooseSituation.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(listOfSituationsForLabel.toTypedArray())


        binding!!.bthAddMark.setOnClickListener {

            if (!binding!!.addMarkNum1.text.isNullOrEmpty() && !binding!!.addMarkNum2.text.isNullOrEmpty() && !binding!!.addMarkNum2Date.text.isNullOrEmpty()) {

                val number1Value = binding!!.addMarkNum1.text.toString().toDouble()
                val number2Value = binding!!.addMarkNum2.text.toString().toDouble()
                val date = binding!!.addMarkNum2Date.text.toString()
                val situation = binding!!.textField4.text.toString()

                val mark = AddMarkDavlenie(
                    userId = viewModel.userLoginId.value!!,
                    date = date,
                    situation = listOfSituations[situation],
                    value_number1 = number1Value,
                    value_number2 = number2Value
                )

                val call: Call<Void> = service.addMarkDavlenie(mark)

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
            else{
                Toast.makeText(context, "Сначала заполните значения и дату", Toast.LENGTH_SHORT).show()
            }
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
        binding!!.addMarkNum2Date.setText(formattedDate)
//        binding!!.addMarkNum2Date.setText("$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00")
    }


}