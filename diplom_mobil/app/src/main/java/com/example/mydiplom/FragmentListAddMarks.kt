package com.example.mydiplom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.mydiplom.adapters.ListAdapterAddMarks
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.databinding.FragmentListAddMarksBinding
import com.example.mydiplom.viewmodel.SharedViewModel
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


        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
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
                        listData = KindOfMark(kindOfMarks[i].id, kindOfMarks[i].name,kindOfMarks[i].user_id, kindOfMarks[i].enum_kind_of_mark_id)
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