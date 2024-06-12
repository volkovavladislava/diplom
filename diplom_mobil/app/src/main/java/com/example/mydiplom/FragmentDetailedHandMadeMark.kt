package com.example.mydiplom

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.adapters.RecycleAdapterDetailedHandMadeMark
import com.example.mydiplom.data.DeleteHandMadeMark
import com.example.mydiplom.data.Mark
import com.example.mydiplom.databinding.FragmentDetailedHandMadeMarkBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import com.google.android.material.datepicker.MaterialDatePicker
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

class FragmentDetailedHandMadeMark : Fragment() {

    private var binding: FragmentDetailedHandMadeMarkBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var  datalist: ArrayList<Mark>

    private var date1 = "1900-01-01"
    private var date2 = "2030-01-01"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedHandMadeMarkBinding.inflate(inflater, container, false)


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

        var handMadeMarkId = viewModel.handMadeMarkId.value ?: 1


        val call: Call<List<Mark>> = service.marksForUser(viewModel.userLoginId.value!!, handMadeMarkId)



        call.enqueue(object : Callback<List<Mark>> {
            override fun onResponse(call: Call<List<Mark>>, response: Response<List<Mark>>) {
                if (response.isSuccessful) {
                    var marksData = response.body()?: emptyList()


                    recyclerView = binding!!.recycleListDetailedHandMadeMark
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    datalist = arrayListOf<Mark>()

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
                    recyclerView.adapter = RecycleAdapterDetailedHandMadeMark(datalist,  viewModel)



                }
                else{}
            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })

        binding!!.bthAddValueDetailedHandMadeMark.setOnClickListener{
                view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentAddRecordHandMadeMark)
            this.viewModel.handMadeMarkId.value = handMadeMarkId
        }



        binding!!.bthDeletePersonalMark.setOnClickListener {

            val deletePersonalMark = DeleteHandMadeMark(viewModel.userLoginId.value!!,handMadeMarkId)
            val call1: Call<Void> = service.deletePersonalMark(deletePersonalMark)

            call1.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Показатель удален", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
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




        binding!!.bthDatePickedPersonalMark.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Выберите период")
                .setSelection(Pair(null, null))
                .build()

            picker.show(this.childFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                binding!!.labelDatePickedPersonalMark.setText(convertTimeToDate(it.first) + " - " + convertTimeToDate(it.second))
                date1 = convertTimeToDate(it.first)
                date2 = convertTimeToDate(it.second)
                updateData(convertTimeToDate(it.first),convertTimeToDateSecond(it.second))

            }
            picker.addOnNegativeButtonClickListener{
                binding!!.labelDatePickedPersonalMark.setText("Выберите период сортировки")
                date1 = "1900-01-01"
                date2 = "2030-01-01"
                updateData(date1,date2)
                picker.dismiss()
            }
        }



        return binding!!.root
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


        var handMadeMarkId = viewModel.handMadeMarkId.value ?: 1


        val call: Call<List<Mark>> = service.marksForUserByDate(viewModel.userLoginId.value!!, handMadeMarkId, date1, date2)



        call.enqueue(object : Callback<List<Mark>> {
            override fun onResponse(call: Call<List<Mark>>, response: Response<List<Mark>>) {
                if (response.isSuccessful) {
                    var marksData = response.body()?: emptyList()


                    recyclerView = binding!!.recycleListDetailedHandMadeMark
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    datalist = arrayListOf<Mark>()

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
                    recyclerView.adapter = RecycleAdapterDetailedHandMadeMark(datalist,  viewModel)



                }
                else{}
            }
            override fun onFailure(call: Call<List<Mark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })


    }

}