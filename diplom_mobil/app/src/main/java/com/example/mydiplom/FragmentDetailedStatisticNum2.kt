package com.example.mydiplom

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.adapters.RecycleAdapterStatisticNum1
import com.example.mydiplom.adapters.RecycleAdapterStatisticNum2
import com.example.mydiplom.data.Mark
import com.example.mydiplom.data.MarkDavlenie
import com.example.mydiplom.databinding.FragmentDetailedStatisticNum2Binding
import com.example.mydiplom.viewmodel.SharedViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import im.dacer.androidcharts.LineView
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
import androidx.core.util.Pair


class FragmentDetailedStatisticNum2 : Fragment() {

    private var binding: FragmentDetailedStatisticNum2Binding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var  datalist: ArrayList<MarkDavlenie>
    private var entries = mutableListOf<Entry>()


    private var callCounter = 2
    private var errorOccurred = false

    private lateinit var  list1: List<Mark>
    private lateinit var  list2: List<Mark>


    private lateinit var  valuelist1: ArrayList<Float>
    private lateinit var  valuelist2: ArrayList<Float>
    val dates = ArrayList<String>()
    private lateinit var values : ArrayList<ArrayList<Float>>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedStatisticNum2Binding.inflate(inflater, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        var kindOfMarkId = viewModel.kindOfMarkIdStatistic.value ?: 1



        val call1: Call<List<Mark>> = service.marksForUser(1, 1)
        call1.enqueue(object : Callback<List<Mark>> {
            override fun onResponse(call: Call<List<Mark>>, response: Response<List<Mark>>) {
                val marksData = response.body() ?: emptyList()
                list1 = marksData
                callCounter -=1
                checkResults()

            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
                errorOccurred = true
            }
        })


        val call2: Call<List<Mark>> = service.marksForUser(1, 2)
        call2.enqueue(object : Callback<List<Mark>> {
            override fun onResponse(call: Call<List<Mark>>, response: Response<List<Mark>>) {
                val marksData = response.body() ?: emptyList()
                list2 = marksData
                callCounter -=1
                checkResults()

            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
                errorOccurred = true
            }
        })



        binding!!.bthDatePickedDetailedStatisticNum2.setOnClickListener{
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Выберите период")
                .setSelection(Pair(null, null))
                .build()

            picker.show(this.childFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                binding!!.labelDatePickedDetailedStatisticNum2.setText(convertTimeToDate(it.first) + " - " + convertTimeToDate(it.second))
                updateData(convertTimeToDate(it.first),convertTimeToDateSecond(it.second))

            }
            picker.addOnNegativeButtonClickListener{
                picker.dismiss()
            }
        }


        return binding!!.root
    }



    fun checkResults() {
        if (callCounter == 0) {
            if (errorOccurred) {
                return
            }

            recyclerView = binding!!.recycleListMarksDetailedStatisticNum2
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)

            datalist = arrayListOf<MarkDavlenie>()

            valuelist1 = arrayListOf<Float>()
            valuelist2 = arrayListOf<Float>()

            values = arrayListOf()

            val dInput  = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val dOutput = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

            for(i in list1.indices){
                val dataClass = MarkDavlenie(
                    list1[i].id,
                    list2[i].id,
                    list1[i].user_id,
                    list1[i].kind_of_mark_id,
                    list1[i].date,
                    list1[i].value_number,
                    list2[i].value_number,
                    list1[i].value_string,
                    list1[i].value_enum,
                    list1[i].value)
                datalist.add(dataClass)

                dates.add(dOutput.format(dInput.parse(list1[i].date)))
                valuelist1.add(list1[i].value_number!!.toFloat())
                valuelist2.add(list2[i].value_number!!.toFloat())

            }

            values.add(valuelist1)
            values.add(valuelist2)
            val lineView: LineView = requireActivity().findViewById(com.example.mydiplom.R.id.line_viewNum2)


            lineView.setBottomTextList(dates);
            lineView.setDrawDotLine(true);
            lineView.setShowPopup(LineView.SHOW_POPUPS_All);
            lineView.setBottomTextList(dates);
            lineView.setColorArray(intArrayOf(Color.BLUE))
            lineView.setFloatDataList(values);



            datalist.reverse()
            recyclerView.adapter = RecycleAdapterStatisticNum2(datalist,  viewModel)
        }
    }

    private fun updateData(date1: String, date2:String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        var kindOfMarkId = viewModel.kindOfMarkIdStatistic.value ?: 1

        Log.d("RetrofitClient","marksDataupdateeeee  date1 " + date1)
        Log.d("RetrofitClient","marksDataupdateeeee  date2 " + date2)

        callCounter = 2

//        val call1: Call<List<Mark>> = service.marksForUser(1, 1)
        val call1: Call<List<Mark>> = service.marksForUserByDate(1, 1,date1, date2)
        call1.enqueue(object : Callback<List<Mark>> {
            override fun onResponse(call: Call<List<Mark>>, response: Response<List<Mark>>) {
                val marksData = response.body() ?: emptyList()
                list1 = marksData
                callCounter -=1
                checkResults()

            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
                errorOccurred = true
            }
        })

        val call2: Call<List<Mark>> = service.marksForUserByDate(1, 2,date1, date2)
//        val call2: Call<List<Mark>> = service.marksForUser(1, 2)
        call2.enqueue(object : Callback<List<Mark>> {
            override fun onResponse(call: Call<List<Mark>>, response: Response<List<Mark>>) {
                val marksData = response.body() ?: emptyList()
                list2 = marksData
                callCounter -=1
                checkResults()

            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
                errorOccurred = true
            }
        })

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

    private fun convertTimeToDateSecond(time: Long):String{
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time + 1000*60*60*24
        val format =  SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(utc.time)
    }




}