package com.example.mydiplom

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.adapters.RecycleAdapterDetailedHandMadeMark
import com.example.mydiplom.data.Mark
import com.example.mydiplom.databinding.FragmentDetailedHandMadeMarkBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentDetailedHandMadeMark : Fragment() {

    private var binding: FragmentDetailedHandMadeMarkBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var  datalist: ArrayList<Mark>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailedHandMadeMarkBinding.inflate(inflater, container, false)


        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        var handMadeMarkId = viewModel.handMadeMarkId.value ?: 1

        Log.d("RetrofitClient","handMadeMarkId  " + handMadeMarkId)
        val call: Call<List<Mark>> = service.marksForUser(1, handMadeMarkId)



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
                            marksData[i].userId,
                            marksData[i].kind_of_mark_id,
                            marksData[i].date,
                            marksData[i].value_number1,
                            marksData[i].value_number2,
                            marksData[i].value_bool,
                            marksData[i].value_string,
                            marksData[i].value_enum )
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

        return binding!!.root
    }


}