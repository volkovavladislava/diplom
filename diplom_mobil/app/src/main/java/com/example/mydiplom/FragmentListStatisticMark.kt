package com.example.mydiplom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mydiplom.adapters.ListAdapterStatistic
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.databinding.FragmentListStatisticMarkBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentListStatisticMark : Fragment() {


    private var binding: FragmentListStatisticMarkBinding? = null
    private lateinit var listAdapterStatistic: ListAdapterStatistic
    private lateinit var listData: KindOfMark
    var dataArrayList = ArrayList<KindOfMark?>()
    private val viewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListStatisticMarkBinding.inflate(inflater, container, false)


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
        val call: Call<List<KindOfMark>> = service.getListKindOfMarkOfSystem()

        call.enqueue(object : Callback<List<KindOfMark>> {
            override fun onResponse(call: Call<List<KindOfMark>>, response: Response<List<KindOfMark>>) {
                if (response.isSuccessful) {
                    Log.d("RetrofitClient","response " + response.body() )
                    var kindOfMarks = response.body() ?: emptyList()

                    dataArrayList.clear()
                    for(i in kindOfMarks.indices){
                        if(kindOfMarks[i].id == 1){
                            dataArrayList.add(KindOfMark(1, "Давление",kindOfMarks[i].user_id, kindOfMarks[i].enum_kind_of_mark_id, kindOfMarks[i].has_reference))

                        }else{
                            if(kindOfMarks[i].id == 2){

                            }
                            else {
                                listData = KindOfMark(
                                    kindOfMarks[i].id,
                                    kindOfMarks[i].name,
                                    kindOfMarks[i].user_id,
                                    kindOfMarks[i].enum_kind_of_mark_id,
                                    kindOfMarks[i].has_reference
                                )
                                dataArrayList.add(listData)
                            }
                        }

                    }
//                    Log.d("RetrofitClient","dataArrayList " + dataArrayList)
                    val a = binding!!.listviewMarksStatistic
                    a.apply {
                        adapter =  ListAdapterStatistic(this.context, dataArrayList,viewModel)
                        isClickable = true}

                }
            }
            override fun onFailure(call: Call<List<KindOfMark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive kindOfMarks from server problem " + t)
            }
        })

        return binding!!.root
    }


}