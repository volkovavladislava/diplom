package com.example.mydiplom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
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
import com.example.mydiplom.data.MarkUpdate
import com.example.mydiplom.databinding.FragmentUpdateRecordMarkNum1Binding
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
import java.util.Locale
import java.util.TimeZone


class FragmentUpdateRecordMarkNum1 : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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

    private var binding: FragmentUpdateRecordMarkNum1Binding? = null
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateRecordMarkNum1Binding.inflate(inflater, container, false)

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

        var updateNum1Id = viewModel.updateNum1Id.value
        var updateNum1UserId = viewModel.updateNum1UserId.value
        var updateNum1KindOfMarkId = viewModel.updateNum1KindOfMarkId.value
        var updateNum1Date = viewModel.updateNum1Date.value
        var updateNum1Situation = viewModel.updateNum1Situation.value!!
        var updateNum1ValueDouble = viewModel.updateNum1ValueDouble.value

        binding!!.updateMarkNum1Date.setText(formatDate(updateNum1Date.toString()))
        binding!!.updateMarkValueNum1.setText(updateNum1ValueDouble.toString())
        binding!!.textField6.setText(listOfSituationsForLabel[updateNum1Situation-1])

        (binding!!.updateMarkNum1ChooseSituation.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(listOfSituationsForLabel.toTypedArray())


        val context = activity ?: return binding!!.root


        binding!!.bthUpdateDateMarkNum1.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }


        binding!!.bthUpdateMarkNum1.setOnClickListener {
            if( !binding!!.updateMarkValueNum1.text.isNullOrEmpty()  && !binding!!.updateMarkNum1Date.text.isNullOrEmpty()) {
                val value = binding!!.updateMarkValueNum1.text.toString().toDouble()
                val date = binding!!.updateMarkNum1Date.text.toString()
                val situation = binding!!.textField6.text.toString()

                val markUpdate = MarkUpdate(user_id = updateNum1UserId!!,kind_of_mark_id = updateNum1KindOfMarkId!!,  date = date,situation = listOfSituations[situation], value, null,null)

                val call: Call<Void> = service.updateMark(updateNum1Id!!, markUpdate)
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



        binding!!.bthDeleteMarkNum1.setOnClickListener {
            val call: Call<Void> = service.deleteMark(updateNum1Id!!)
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
//            val bundle = bundleOf("title" to viewModel.kindOfMarkNameStatistic.value )
//            findNavController().navigate(R.id.fragmentDetailedStatisticNum1, bundle)
            findNavController().popBackStack()
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
        binding!!.updateMarkNum1Date.setText(formattedDate)
    }


    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Singapore")
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

}