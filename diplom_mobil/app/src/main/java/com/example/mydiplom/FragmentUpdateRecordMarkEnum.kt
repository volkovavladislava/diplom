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
import com.example.mydiplom.data.AddMark
import com.example.mydiplom.data.KindOfMarkValues
import com.example.mydiplom.data.MarkUpdate
import com.example.mydiplom.databinding.FragmentUpdateRecordMarkEnumBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FragmentUpdateRecordMarkEnum : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
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

    private var binding: FragmentUpdateRecordMarkEnumBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()


    var dataArrayList = ArrayList<String?>()
    val mutableMap = mutableMapOf<String, Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateRecordMarkEnumBinding.inflate(inflater, container, false)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        var updateEnumId = viewModel.updateEnumId.value
        var updateEnumUserId = viewModel.updateEnumUserId.value
        var updateEnumKindOfMarkId = viewModel.updateEnumKindOfMarkId.value
        var updateEnumDate = viewModel.updateEnumDate.value
        var updateEnumValueEnum = viewModel.updateEnumValueEnum.value
        var updateEnumValue = viewModel.updateEnumValue.value

        binding!!.updateMarkEnumDate.setText(formatDate(updateEnumDate.toString()))
        binding!!.textField1.setText(updateEnumValue)


        val call: Call<List<KindOfMarkValues>> = service.getKindOfMarkValues(updateEnumKindOfMarkId!!)
        call.enqueue(object : Callback<List<KindOfMarkValues>> {
            override fun onResponse(call: Call<List<KindOfMarkValues>>, response: Response<List<KindOfMarkValues>>) {
                if (response.isSuccessful) {
                    var kindOfMarkValuesData = response.body() ?: emptyList()

                    dataArrayList.clear()
                    for(i in kindOfMarkValuesData.indices){
                        dataArrayList.add(kindOfMarkValuesData[i].value)
                        mutableMap[kindOfMarkValuesData[i].value] = kindOfMarkValuesData[i].id
                    }
                    (binding!!.updateMarkMenu.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(dataArrayList.toTypedArray())
                }
                else{}
            }
            override fun onFailure(call: Call<List<KindOfMarkValues>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })

        val context = activity ?: return binding!!.root


        binding!!.bthUpdateDateMarkEnum.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }


        binding!!.bthUpdateMarkEnum.setOnClickListener {
            if( !binding!!.textField1.text.isNullOrEmpty()  && !binding!!.updateMarkEnumDate.text.isNullOrEmpty()) {
                val enumValue = binding!!.textField1.text.toString()
                val date = binding!!.updateMarkEnumDate.text.toString()


                val markUpdate = MarkUpdate(user_id = updateEnumUserId!!,kind_of_mark_id = updateEnumKindOfMarkId,  date = date, null, null,mutableMap[enumValue])

                val call: Call<Void> = service.updateMark(updateEnumId!!, markUpdate)
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



        binding!!.bthDeleteMarkEnum.setOnClickListener {
            val call: Call<Void> = service.deleteMark(updateEnumId!!)
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
            findNavController().popBackStack()
//            findNavController().navigate(R.id.fragmentDetailedStatisticEnum, bundle)

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
        binding!!.updateMarkEnumDate.setText(formattedDate)
    }


    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }


}