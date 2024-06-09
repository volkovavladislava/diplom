package com.example.mydiplom

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.adapters.RecycleAdapterStatisticEnum
import com.example.mydiplom.adapters.RecycleAdapterStatisticNum1
import com.example.mydiplom.data.Mark
import com.example.mydiplom.databinding.FragmentDetailedStatisticEnumBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.datepicker.MaterialDatePicker
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


class FragmentDetailedStatisticEnum : Fragment() {

    private var binding: FragmentDetailedStatisticEnumBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var  datalist: ArrayList<Mark>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedStatisticEnumBinding.inflate(inflater, container, false)

//        findNavController().popBackStack(R.id.fragmentUpdateRecordMarkEnum, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        var kindOfMarkId = viewModel.kindOfMarkIdStatistic.value ?: 1


        val call: Call<List<Mark>> = service.marksForUser(viewModel.userLoginId.value!!, kindOfMarkId)
        call.enqueue(object : Callback<List<Mark>> {
            override fun onResponse(call: Call<List<Mark>>, response: Response<List<Mark>>) {
                if (response.isSuccessful) {
                    var marksData = response.body() ?: emptyList()
//                    Log.d("RetrofitClient","marksData " + marksData)

//                    if (marksData.size == 0) {
//                        findNavController().popBackStack()
//                        findNavController().navigate(R.id.fragmentEmpty)
//                    } else {
                        recyclerView = binding!!.recycleListMarksEnum
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.setHasFixedSize(true)

                        datalist = arrayListOf<Mark>()


                        for (i in marksData.indices) {
                            val dataClass = Mark(
                                marksData[i].id,
                                marksData[i].user_id,
                                marksData[i].kind_of_mark_id,
                                marksData[i].date,
                                marksData[i].situation,
                                marksData[i].value_number,
                                marksData[i].value_string,
                                marksData[i].value_enum,
                                marksData[i].value
                            )
                            datalist.add(dataClass)

                        }

                        datalist.reverse()
                        recyclerView.adapter = RecycleAdapterStatisticEnum(datalist, viewModel)

//                    }
                }
                else{}
            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })


        binding!!.bthDatePickedEnum.setOnClickListener{
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Выберите период")
                .setSelection(Pair(null, null))
                .build()

            picker.show(this.childFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                binding!!.labelDatePickedEnum.setText(convertTimeToDate(it.first) + " - " + convertTimeToDate(it.second))
                updateData(convertTimeToDate(it.first),convertTimeToDate(it.second))

            }
            picker.addOnNegativeButtonClickListener{
                binding!!.labelDatePickedEnum.setText("Выберите период сортировки")

                updateData("1900-01-01","2030-01-01")
                picker.dismiss()
            }
        }


        return binding!!.root
    }






    class DateAxisValueFormatter : ValueFormatter() {
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        override fun getFormattedValue(value: Float): String {
            val t = simpleDateFormat.format(Date(value.toLong()))
            return t

        }
    }

    fun convertDateStringToMilliseconds(inputDate: String): Float {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val date = dateFormat.parse(inputDate)
        return date.time.toFloat()
    }

    private fun convertTimeToDate(time: Long):String{
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format =  SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(utc.time)
    }



    private fun updateData(date1: String, date2:String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        var kindOfMarkId = viewModel.kindOfMarkIdStatistic.value ?: 1

        val call: Call<List<Mark>> = service.marksForUserByDate(viewModel.userLoginId.value!!, kindOfMarkId, date1, date2)
        call.enqueue(object : Callback<List<Mark>> {
            override fun onResponse(call: Call<List<Mark>>, response: Response<List<Mark>>) {
                if (response.isSuccessful) {
                    var marksData = response.body()?: emptyList()

                    recyclerView = binding!!.recycleListMarksEnum
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    datalist = arrayListOf<Mark>()

                    Log.d("RetrofitClient","marksData  " + marksData)
                    for(i in marksData.indices){
                        val dataClass = Mark(
                            marksData[i].id,
                            marksData[i].user_id,
                            marksData[i].kind_of_mark_id,
                            marksData[i].date,
                            marksData[i].situation,
                            marksData[i].value_number,
                            marksData[i].value_string,
                            marksData[i].value_enum,
                            marksData[i].value)
                        datalist.add(dataClass)

                    }

                    datalist.reverse()
                    recyclerView.adapter = RecycleAdapterStatisticEnum(datalist,  viewModel)

                }
                else{}
            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })
    }
}



