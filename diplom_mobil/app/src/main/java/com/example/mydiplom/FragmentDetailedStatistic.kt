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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.adapters.RecycleAdapterStatisticNum1
import com.example.mydiplom.data.Mark
import com.example.mydiplom.databinding.FragmentDetailedStatisticBinding
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
//import com.jjoe64.graphview.GraphView
//import com.jjoe64.graphview.series.DataPoint
//import com.jjoe64.graphview.series.LineGraphSeries
import java.security.KeyStore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class FragmentDetailedStatistic : Fragment() {

    private var binding: FragmentDetailedStatisticBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var  datalist: ArrayList<Mark>
    private var entries = mutableListOf<Entry>()

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
                    var marksData = response.body()?: emptyList()
                    Log.d("RetrofitClient","userData  " + marksData)


                    recyclerView = binding!!.recycleListMarks
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    datalist = arrayListOf<Mark>()
                    entries=mutableListOf<Entry>()

                    for(i in marksData.indices){
                        val dataClass = Mark(
                            marksData[i].id,
                            marksData[i].userId,
                            marksData[i].kind_of_mark_id,
                            marksData[i].date,
                            marksData[i].value_number1,
                            marksData[i].value_number2,
                            marksData[i].value_bool,
                            marksData[i].value_string,
                            marksData[i].value_enum )
                        datalist.add(dataClass)

                        val e = Entry(convertDateStringToMilliseconds(dataClass.date), marksData[i].value_number1?.toFloat() ?: 0f)
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

                    val lineChart = binding!!.line

                    val xAxis = lineChart.xAxis
                    xAxis.isGranularityEnabled = true
                    xAxis.granularity = 1f
                    xAxis.valueFormatter = DateAxisValueFormatter()
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setLabelRotationAngle(30f)

                    lineChart.data = lineData



                    lineChart.invalidate()
                    datalist.reverse()
                    recyclerView.adapter = RecycleAdapterStatisticNum1(datalist,  viewModel)



                }
                else{}
            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })




        binding!!.bthDatePicked.setOnClickListener{
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Выберите период")
                .setSelection(Pair(null, null))
                .build()

            picker.show(this.childFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                binding!!.labelDatePicked.setText(convertTimeToDate(it.first) + " - " + convertTimeToDate(it.second))
                updateData(convertTimeToDate(it.first),convertTimeToDate(it.second))

            }
            picker.addOnNegativeButtonClickListener{
                picker.dismiss()
            }
        }










        return binding!!.root
    }

    class DateAxisValueFormatter : ValueFormatter() {
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        override fun getFormattedValue(value: Float): String {
            val t = simpleDateFormat.format(Date(value.toLong()))
//            Log.d("RetrofitClient","simpleDateFormat " + t)
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

        Log.d("RetrofitClient","marksDataupdateeeee  date1 " + date1)
        Log.d("RetrofitClient","marksDataupdateeeee  date2 " + date2)
        val call: Call<List<Mark>> = service.marksForUserByDate(1, kindOfMarkId,date1, date2)
        call.enqueue(object : Callback<List<Mark>> {
            override fun onResponse(call: Call<List<Mark>>, response: Response<List<Mark>>) {
                if (response.isSuccessful) {
                    var marksData = response.body()?: emptyList()
                    Log.d("RetrofitClient","marksDataupdateeeee  " + marksData.size)


                    recyclerView = binding!!.recycleListMarks
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    datalist = arrayListOf<Mark>()

                    entries=mutableListOf<Entry>()

                    for(i in marksData.indices){
                        val dataClass = Mark(
                            marksData[i].id,
                            marksData[i].userId,
                            marksData[i].kind_of_mark_id,
                            marksData[i].date,
                            marksData[i].value_number1,
                            marksData[i].value_number2,
                            marksData[i].value_bool,
                            marksData[i].value_string,
                            marksData[i].value_enum )
                        datalist.add(dataClass)
                        val e = Entry(convertDateStringToMilliseconds(dataClass.date), marksData[i].value_number1?.toFloat() ?: 0f)
                        entries.add(e)
                    }

                    val dataSet = LineDataSet(entries, "").apply {
                        setDrawCircles(true) // Разрешить отображение точек
                        setCircleColor(Color.RED) // Установить цвет точек
                        setCircleRadius(4f) // Установить размер точек

                    }
                    val lineData = LineData(dataSet)

                    val lineChart = binding!!.line

                    val xAxis = lineChart.xAxis
                    xAxis.isGranularityEnabled = true
                    xAxis.granularity = 1f
                    xAxis.valueFormatter = DateAxisValueFormatter()
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setLabelRotationAngle(30f)

                    lineChart.data = lineData



                    lineChart.invalidate()
                    datalist.reverse()
                    recyclerView.adapter = RecycleAdapterStatisticNum1(datalist,  viewModel)

                }
                else{}
            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })
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