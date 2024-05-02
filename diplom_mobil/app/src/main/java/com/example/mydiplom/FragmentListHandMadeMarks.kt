package com.example.mydiplom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.adapters.RecycleAdapterHandMadeMark
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.databinding.FragmentListHandMadeMarksBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentListHandMadeMarks : Fragment() {

    private var binding: FragmentListHandMadeMarksBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var  datalist: ArrayList<KindOfMark>
    private val viewModel: SharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListHandMadeMarksBinding.inflate(inflater, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)


        val call: Call<List<KindOfMark>> = service.getListKindOfMarkOfHandMade(1)

        call.enqueue(object : Callback<List<KindOfMark>> {
            override fun onResponse(call: Call<List<KindOfMark>>, response: Response<List<KindOfMark>>) {
                if (response.isSuccessful) {
                    var handMadeMarks = response.body() ?: emptyList()

                    recyclerView = binding!!.recycleListHandMadeMark
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    datalist = arrayListOf<KindOfMark>()

                    for(i in handMadeMarks.indices){
                        val dataClass = KindOfMark(
                            handMadeMarks[i].id,
                            handMadeMarks[i].name,
                            handMadeMarks[i].user_id,
                            handMadeMarks[i].enum_kind_of_mark_id)
                        datalist.add(dataClass)
                    }

                    Log.d("RetrofitClient","datalist " + datalist)

                    recyclerView.adapter = RecycleAdapterHandMadeMark(datalist, viewModel)

                }
                else{}
            }
            override fun onFailure(call: Call<List<KindOfMark>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })


        binding!!.bthAddNewHandMadeMark.setOnClickListener {
                view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentAddNewHandMadeMark)
        }

        return binding!!.root
    }

}