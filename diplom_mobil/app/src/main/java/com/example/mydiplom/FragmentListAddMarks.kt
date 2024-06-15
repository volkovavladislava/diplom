package com.example.mydiplom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mydiplom.adapters.ListAdapterAddMarks
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.databinding.FragmentListAddMarksBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentListAddMarks : Fragment() {


    private var binding: FragmentListAddMarksBinding? = null

    private lateinit var listAdapterAddMarks: ListAdapterAddMarks
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
        binding = FragmentListAddMarksBinding.inflate(inflater, container, false)


//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:3000")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val service: ApiController = retrofit.create(ApiController::class.java)

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

//        37.46.130.221
//        192.168.0.32
        val retrofit = Retrofit.Builder()
            .baseUrl("http://37.46.130.221:3000")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)
        val call: Call<List<KindOfMark>> = service.getListKindOfMarkOfSystem()

        call.enqueue(object : Callback<List<KindOfMark>> {
            override fun onResponse(call: Call<List<KindOfMark>>, response: Response<List<KindOfMark>>) {
                if (response.isSuccessful) {

                    var kindOfMarks = response.body() ?: emptyList()

                    dataArrayList.clear()

                    for(i in kindOfMarks.indices){
                        if(kindOfMarks[i].id == 1){
                            dataArrayList.add(KindOfMark(1, "Давление",kindOfMarks[i].user_id, kindOfMarks[i].enum_kind_of_mark_id, kindOfMarks[i].has_reference))
                            continue
                        }
                        if(kindOfMarks[i].id == 2){
                            continue
                        }
                        listData = KindOfMark(kindOfMarks[i].id, kindOfMarks[i].name,kindOfMarks[i].user_id, kindOfMarks[i].enum_kind_of_mark_id, kindOfMarks[i].has_reference)
                        dataArrayList.add(listData)
                    }

                    val a = binding!!.listviewMarks
                    a.apply {
                        adapter =  ListAdapterAddMarks(this.context, dataArrayList,viewModel)
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