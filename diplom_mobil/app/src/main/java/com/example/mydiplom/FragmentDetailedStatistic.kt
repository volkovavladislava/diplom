package com.example.mydiplom

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mydiplom.data.Mark
import com.example.mydiplom.data.User
import com.example.mydiplom.databinding.FragmentDetailedStatisticBinding
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
//import com.jjoe64.graphview.GraphView
//import com.jjoe64.graphview.series.DataPoint
//import com.jjoe64.graphview.series.LineGraphSeries
import java.security.KeyStore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class FragmentDetailedStatistic : Fragment() {

    private var binding: FragmentDetailedStatisticBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedStatisticBinding.inflate(inflater, container, false)

//        // Получаем ссылку на GraphView из макета
//        val graphView: GraphView = binding!!.graph
//        // Создаем серию точек для графика
//        val series = LineGraphSeries(getDataPoints())
//        // Добавляем серию точек на график
//        graphView.addSeries(series)
//        1609459200000F
//        1640995200000F

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        var kindOfMarkId = viewModel.kindOfMarkIdStatistic.value ?: 1

        val call: Call<List<Mark>> = service.marksForUser(1, kindOfMarkId)
        call.enqueue(object : Callback<List<Mark>> {
            override fun onResponse(call: Call<List<Mark>>, response: Response<List<Mark>>) {
                if (response.isSuccessful) {
                    var userData = response.body()
                    Log.d("RetrofitClient","userData  " + userData)
                    userData?.let {

                    }
                }
                else{}
            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })





        val entries = mutableListOf<Entry>()
        entries.add(Entry(convertDateStringToMilliseconds( "2024-03-16T05:00:00.000Z"), 123f))
        entries.add(Entry(convertDateStringToMilliseconds( "2024-03-17T07:00:00.000Z") , 456f))
        entries.add(Entry(convertDateStringToMilliseconds( "2024-03-18T10:00:00.000Z") , 789f))
        entries.add(Entry(convertDateStringToMilliseconds( "2024-03-19T05:00:00.000Z"), 134f))
        entries.add(Entry(convertDateStringToMilliseconds( "2024-03-20T05:00:00.000Z") , 400f))
        entries.add(Entry(convertDateStringToMilliseconds( "2024-03-21T05:00:00.000Z") , 600f))

        val dataSet = LineDataSet(entries, "Dataset")

        val lineData = LineData(dataSet)

        val lineChart = binding!!.line
        lineChart.data = lineData

        val xAxis = lineChart.xAxis
        xAxis.isGranularityEnabled = true
        xAxis.granularity = 1f
        xAxis.valueFormatter = DateAxisValueFormatter()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setLabelRotationAngle(40f)


        return binding!!.root
    }

    class DateAxisValueFormatter : ValueFormatter() {
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        override fun getFormattedValue(value: Float): String {
            val t = simpleDateFormat.format(Date(value.toLong()))
            Log.d("RetrofitClient","simpleDateFormat " + t)
            return t

        }
    }

    fun convertDateStringToMilliseconds(inputDate: String): Float {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        val date = dateFormat.parse(inputDate)
        return date.time.toFloat()
    }





















//
//    private fun getDataPoints(): Array<DataPoint> {
//        // Пример данных с датами и значениями
//        val dates = arrayOf("2024-03-01", "2024-03-02", "2024-03-03", "2024-03-04")
//        val values = doubleArrayOf(10.0, 20.0, 15.0, 25.0)
//        val dataPoints = arrayOfNulls<DataPoint>(dates.size)
//
//        // Преобразуем строки с датами в объекты Date
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
//        for (i in dates.indices) {
//            try {
//                val date = dateFormat.parse(dates[i])
//                dataPoints[i] = DataPoint(date!!.time.toDouble(), values[i])
//            } catch (e: ParseException) {
//                e.printStackTrace()
//            }
//        }
//        return dataPoints.requireNoNulls()
//    }


}