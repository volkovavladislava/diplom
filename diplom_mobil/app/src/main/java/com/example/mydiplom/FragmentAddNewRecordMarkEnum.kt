package com.example.mydiplom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mydiplom.data.AddMark
import com.example.mydiplom.data.KindOfMarkValues
import com.example.mydiplom.databinding.FragmentAddNewRecordMarkEnumBinding
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



    private var binding: FragmentAddNewRecordMarkEnumBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    var dataArrayList = ArrayList<String?>()
    val mutableMap = mutableMapOf<String, Int>()
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
        var kindOfMarkName = viewModel.kindOfMarkNameAddMark.value

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        binding!!.addMarkEnumDate.setText(SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()))

        binding!!.textField3.setText(listOfSituationsForLabel[0])
        (binding!!.addMarkEnumChooseSituation.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(listOfSituationsForLabel.toTypedArray())


        val call: Call<List<KindOfMarkValues>> = service.getKindOfMarkValues(kindOfMarkId)
        call.enqueue(object : Callback<List<KindOfMarkValues>> {
            override fun onResponse(call: Call<List<KindOfMarkValues>>, response: Response<List<KindOfMarkValues>>) {
                if (response.isSuccessful) {
                    var kindOfMarkValuesData = response.body() ?: emptyList()
                    Log.d("RetrofitClient","kindOfMarkValuesData" + kindOfMarkValuesData)

                    dataArrayList.clear()
                    for(i in kindOfMarkValuesData.indices){
                        dataArrayList.add(kindOfMarkValuesData[i].value)
                        mutableMap[kindOfMarkValuesData[i].value] = kindOfMarkValuesData[i].id
                    }
                    (binding!!.menu.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(dataArrayList.toTypedArray())
                }
                else{}
            }
            override fun onFailure(call: Call<List<KindOfMarkValues>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })

        val context = activity ?: return binding!!.root


        binding!!.bthAddDateMarkEnum.setOnClickListener{
            getDateTimeCalendar()
            DatePickerDialog(context, this, year, month, day).show()
        }


        binding!!.bthAddMark.setOnClickListener {
            if( !binding!!.textField.text.isNullOrEmpty()  && !binding!!.addMarkEnumDate.text.isNullOrEmpty()) {
                val enumValue = binding!!.textField.text.toString()
                val date = binding!!.addMarkEnumDate.text.toString()
                val situation = binding!!.textField3.text.toString()
//                Log.d("RetrofitClient", "enumValue " + enumValue)


                val mark = AddMark(
                    userId = viewModel.userLoginId.value!!,
                    kind_of_mark_id = kindOfMarkId,
                    date = date,
                    situation = listOfSituations[situation],
                    null,
                    null,
                    mutableMap[enumValue]
                )

                val call: Call<Void> = service.addMark(mark.kind_of_mark_id, mark)

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
                Toast.makeText(context, "Сначала добавьте значение и дату", Toast.LENGTH_SHORT).show()
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
        binding!!.addMarkEnumDate.setText(formattedDate)
//        binding!!.addMarkEnumDate.setText("$savedYear-$savedMonth-$savedDay $savedHour:$savedMinute:00")
    }


}