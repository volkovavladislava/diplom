package com.example.mydiplom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.data.KindOfMarkValues
import com.example.mydiplom.data.Mark
import com.example.mydiplom.data.User
import com.example.mydiplom.databinding.FragmentAddNewRecordMark1numberBinding
import com.example.mydiplom.databinding.FragmentAddNewRecordMarkEnumBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class FragmentAddNewRecordMarkEnum : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

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

    private var binding: FragmentAddNewRecordMarkEnumBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    var dataArrayList = ArrayList<String?>()
    private lateinit var spinner:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewRecordMarkEnumBinding.inflate(inflater, container, false)

        var kindOfMarkId = viewModel.kindOfMarkIdAddMark.value ?: 1


        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)


        val call: Call<List<KindOfMarkValues>> = service.getKindOfMarkValues(kindOfMarkId)
        call.enqueue(object : Callback<List<KindOfMarkValues>> {
            override fun onResponse(call: Call<List<KindOfMarkValues>>, response: Response<List<KindOfMarkValues>>) {
                if (response.isSuccessful) {
                    var kindOfMarkValuesData = response.body() ?: emptyList()
                    Log.d("RetrofitClient","kindOfMarkValuesData" + kindOfMarkValuesData)

                    dataArrayList.clear()
                    for(i in kindOfMarkValuesData.indices){
                        dataArrayList.add(kindOfMarkValuesData[i].value)
                    }
                }
                else{}
            }
            override fun onFailure(call: Call<List<KindOfMarkValues>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })

        val context = activity ?: return binding!!.root
        spinner =  binding!!.addMarkEnumSpinner


        val arrayAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item , dataArrayList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }





        binding!!.bthAddDateMarkEnum.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }


        binding!!.bthAddMark.setOnClickListener {
            val enumValue = binding!!.addMarkEnumSpinner
            val date = binding!!.addMarkEnumDate.text.toString()



            val mark = Mark(userId=1, kind_of_mark_id=kindOfMarkId,date=date,null,null,null,null,null)

            val call: Call<Void> = service.addMark(mark.kind_of_mark_id, mark)

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

        binding!!.addMarkEnumDate.setText("$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00")
    }


}