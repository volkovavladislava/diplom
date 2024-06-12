package com.example.mydiplom


//import com.jjoe64.graphview.GraphView
//import com.jjoe64.graphview.series.DataPoint
//import com.jjoe64.graphview.series.LineGraphSeries

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.adapters.RecycleAdapterStatisticNum1
import com.example.mydiplom.data.Advice
import com.example.mydiplom.data.File
import com.example.mydiplom.data.Mark
import com.example.mydiplom.data.MarkAverage
import com.example.mydiplom.databinding.FragmentDetailedStatisticNum1Binding
import com.example.mydiplom.viewmodel.SharedViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import com.squareup.picasso.Picasso
import im.dacer.androidcharts.LineView
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
import java.util.TimeZone


class FragmentDetailedStatisticNum1 : Fragment() {

    private var binding: FragmentDetailedStatisticNum1Binding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var  datalist: ArrayList<MarkAverage>
    private lateinit var  valuelist: ArrayList<Float>
    private lateinit var  valuelistAverage: ArrayList<Float>
    private var entries = mutableListOf<Entry>()


    var dates = ArrayList<String>()
//    val values = ArrayList<ArrayList<Float>>()
    private lateinit var values : ArrayList<ArrayList<Float>>

    private var date1 = "1900-01-01"
    private var date2 = "2030-01-01"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedStatisticNum1Binding.inflate(inflater, container, false)


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

        var kindOfMarkId = viewModel.kindOfMarkIdStatistic.value ?: 1

        val call: Call<List<MarkAverage>> = service.marksForUserAverage(viewModel.userLoginId.value!!, kindOfMarkId)
        call.enqueue(object : Callback<List<MarkAverage>> {
            override fun onResponse(call: Call<List<MarkAverage>>, response: Response<List<MarkAverage>>) {
                if (response.isSuccessful) {
                    var marksData = response.body()?: emptyList()
//                    Log.d("RetrofitClient","userData  " + marksData)
                    if(marksData.size == 0){
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.fragmentEmpty)
                    }else {

                        recyclerView = binding!!.recycleListMarks
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.setHasFixedSize(true)

                        datalist = arrayListOf<MarkAverage>()
                        valuelist = arrayListOf<Float>()
                        values = arrayListOf()
                        dates = arrayListOf<String>()
                        valuelistAverage = arrayListOf<Float>()

                        val dInput =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        val dOutput = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

                        for (i in marksData.indices) {
                            val dataClass = MarkAverage(
                                marksData[i].id,
                                marksData[i].user_id,
                                marksData[i].kind_of_mark_id,
                                marksData[i].date,
                                marksData[i].situation,
                                marksData[i].value_number,
                                marksData[i].value_string,
                                marksData[i].value_enum,
                                marksData[i].value,
                                marksData[i].moving_average
                            )
                            datalist.add(dataClass)

                            dates.add(dOutput.format(dInput.parse(marksData[i].date)))
                            valuelist.add(marksData[i].value_number!!.toFloat())
                            valuelistAverage.add(marksData[i].moving_average!!.toFloat())

                        }


                        values.add(valuelist)
                        values.add(valuelistAverage)

                        val lineView: LineView =
                            activity!!.findViewById(com.example.mydiplom.R.id.line_view)

//                    if(valuelist.size < 4){
//                        lineView.layout_gravity = "center_horizontal "
//                    }
                        lineView.setBottomTextList(dates);
                        lineView.setDrawDotLine(true);
                        lineView.setShowPopup(LineView.SHOW_POPUPS_All);
                        lineView.setBottomTextList(dates);
                        lineView.setColorArray(
                            intArrayOf(
                                Color.parseColor("#f0b54f"),
                                Color.parseColor("#5fc97b")
                            )
                        )
                        lineView.setFloatDataList(values);



                        datalist.reverse()
                        recyclerView.adapter = RecycleAdapterStatisticNum1(datalist, viewModel)

                    }
                }
                else{ }
            }
            override fun onFailure(call: Call<List<MarkAverage>>, t: Throwable) {
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
                date1 = convertTimeToDate(it.first)
                date2 = convertTimeToDate(it.second)
                updateData(convertTimeToDate(it.first),convertTimeToDateSecond(it.second))

            }
            picker.addOnNegativeButtonClickListener{

                binding!!.labelDatePicked.setText("Выберите период сортировки")
                date1 = "1900-01-01"
                date2 = "2030-01-01"
                updateData(date1,date2)
                picker.dismiss()
            }
        }

        binding!!.bthGetAdviceDetailedStatisticNum1.setOnClickListener {

            val call1: Call<Advice> = service.getAdvice(viewModel.userLoginId.value!!, kindOfMarkId, date1, date2)

            call1.enqueue(object : Callback<Advice> {
                override fun onResponse(call: Call<Advice>, response: Response<Advice>) {
                    if (response.isSuccessful) {
                        var adviceData = response.body()

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setTitle("Рекомендация")
                        builder.setMessage(adviceData!!.comment)
                        builder.setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        builder.setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                    else{}
                }
                override fun onFailure(call: Call<Advice>, t: Throwable) {
                    Log.d("RetrofitClient","Receive user from server problem " + t)
                }
            })


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

    private fun convertTimeToDateSecond(time: Long):String{
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time + 1000*60*60*24
        val format =  SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(utc.time)
    }





    private fun updateData(date1: String, date2:String){
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

        var kindOfMarkId = viewModel.kindOfMarkIdStatistic.value ?: 1

//        Log.d("RetrofitClient","marksDataupdateeeee  date1 " + date1)
//        Log.d("RetrofitClient","marksDataupdateeeee  date2 " + date2)

        val call: Call<List<MarkAverage>> = service.marksForUserAverageByDate(viewModel.userLoginId.value!!, kindOfMarkId,date1, date2)
        call.enqueue(object : Callback<List<MarkAverage>> {
            override fun onResponse(call: Call<List<MarkAverage>>, response: Response<List<MarkAverage>>) {
                if (response.isSuccessful) {
                    var marksData = response.body() ?: emptyList()
//                    Log.d("RetrofitClient", "marksDataupdateeeee  " + marksData.size)

                    if (marksData.size == 0) {
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.fragmentEmpty)
                    } else {
                        recyclerView = binding!!.recycleListMarks
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.setHasFixedSize(true)

                        datalist = arrayListOf<MarkAverage>()
                        valuelist = arrayListOf<Float>()
                        values = arrayListOf()
                        dates = arrayListOf<String>()
                        valuelistAverage = arrayListOf<Float>()

                        val dInput =
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        val dOutput = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())


                        for (i in marksData.indices) {
                            val dataClass = MarkAverage(
                                marksData[i].id,
                                marksData[i].user_id,
                                marksData[i].kind_of_mark_id,
                                marksData[i].date,
                                marksData[i].situation,
                                marksData[i].value_number,
                                marksData[i].value_string,
                                marksData[i].value_enum,
                                marksData[i].value,
                                marksData[i].moving_average
                            )
                            datalist.add(dataClass)

                            dates.add(dOutput.format(dInput.parse(marksData[i].date)))
                            valuelist.add(marksData[i].value_number!!.toFloat())
                            valuelistAverage.add(marksData[i].moving_average!!.toFloat())

                        }


                        values.add(valuelist)
                        values.add(valuelistAverage)

                        val lineView: LineView =
                            activity!!.findViewById(com.example.mydiplom.R.id.line_view)


                        lineView.setBottomTextList(dates);
                        lineView.setDrawDotLine(true);
                        lineView.setShowPopup(LineView.SHOW_POPUPS_All);
                        lineView.setBottomTextList(dates);
                        lineView.setColorArray(
                            intArrayOf(
                                Color.parseColor("#f0b54f"),
                                Color.parseColor("#5fc97b")
                            )
                        )
                        lineView.setFloatDataList(values);


                        datalist.reverse()
                        recyclerView.adapter = RecycleAdapterStatisticNum1(datalist, viewModel)

                    }
                }
                else{}
            }
            override fun onFailure(call: Call<List<MarkAverage>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })
    }

























//    entries=mutableListOf<Entry>()
//                    Log.d("RetrofitClient","entries  " + entries)
//                    for(i in marksData.indices){
//                        val dataClass = Mark(
//                            marksData[i].id,
//                            marksData[i].user_id,
//                            marksData[i].kind_of_mark_id,
//                            marksData[i].date,
//                            marksData[i].value_number,
//                            marksData[i].value_string,
//                            marksData[i].value_enum,
//                            marksData[i].value)
//                        datalist.add(dataClass)
//                      val e = Entry(convertDateStringToMilliseconds(dataClass.date), marksData[i].value_number?.toFloat() ?: 0f)
//                        entries.add(e)
//                    }
//                    val dataSet = LineDataSet(entries, "").apply {
//                        setDrawCircles(true) // Разрешить отображение точек
//                        setCircleColor(Color.RED) // Установить цвет точек
//                        setCircleRadius(4f) // Установить размер точек
//                        setValueTextSize(12f)
//                    }
//
//                    val lineData = LineData(dataSet)
//
//                    val lineChart = binding!!.line
//
//                    val xAxis = lineChart.xAxis
//                    xAxis.isGranularityEnabled = true
//                    xAxis.granularity = 1f
//                    xAxis.valueFormatter = DateAxisValueFormatter()
//                    xAxis.position = XAxis.XAxisPosition.BOTTOM
//                    xAxis.setLabelRotationAngle(30f)
//
//                    lineChart.data = lineData
//                    lineChart.invalidate()





    //        // Получаем ссылку на GraphView из макета
//        val graphView: GraphView = binding!!.graph
//        // Создаем серию точек для графика
//        val series = LineGraphSeries(getDataPoints())
//        // Добавляем серию точек на график
//        graphView.addSeries(series)
//        1609459200000F
//        1640995200000F


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