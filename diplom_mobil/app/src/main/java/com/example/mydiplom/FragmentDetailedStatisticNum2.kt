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


class FragmentDetailedStatisticNum2 : Fragment() {

    private var binding: FragmentDetailedStatisticNum2Binding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var  datalist: ArrayList<MarkDavlenie>
    private var entries = mutableListOf<Entry>()


    private var callCounter = 2
    private var errorOccurred = false
//    val results = mutableListOf<List<Mark>>()
    private lateinit var  list1: List<Mark>
    private lateinit var  list2: List<Mark>

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

        return binding!!.root
    }



    fun checkResults() {
        if (callCounter == 0) {
            if (errorOccurred) {
                return
            }
            Log.d("RetrofitClient","list1 " + list1)
            Log.d("RetrofitClient","list2 " + list2)

            recyclerView = binding!!.recycleListMarksDetailedStatisticNum2
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.setHasFixedSize(true)

            datalist = arrayListOf<MarkDavlenie>()
            entries=mutableListOf<Entry>()

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

                val e = Entry(convertDateStringToMilliseconds(dataClass.date), list1[i].value_number?.toFloat() ?: 0f)
                entries.add(e)

            }


            Log.d("RetrofitClient","entries  " + entries)

            val dataSet = LineDataSet(entries, "").apply {
                setDrawCircles(true) // Разрешить отображение точек
                setCircleColor(Color.RED) // Установить цвет точек
                setCircleRadius(4f) // Установить размер точек
                setValueTextSize(12f)
            }

            val lineData = LineData(dataSet)

            val lineChart = binding!!.line2

            val xAxis = lineChart.xAxis
            xAxis.isGranularityEnabled = true
            xAxis.granularity = 1f
            xAxis.valueFormatter = FragmentDetailedStatisticNum1.DateAxisValueFormatter()
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setLabelRotationAngle(30f)

            lineChart.data = lineData



            lineChart.invalidate()
            datalist.reverse()
            recyclerView.adapter = RecycleAdapterStatisticNum2(datalist,  viewModel)
        }
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




}