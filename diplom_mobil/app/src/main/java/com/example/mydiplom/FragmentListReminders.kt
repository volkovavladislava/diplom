package com.example.mydiplom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.adapters.RecycleAdapterPrompt
import com.example.mydiplom.data.AddPrompt
import com.example.mydiplom.data.Prompt
import com.example.mydiplom.data.User

import com.example.mydiplom.databinding.FragmentListRemindersBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentListReminders : Fragment(),  RecycleAdapterPrompt.Listener{

    private var binding: FragmentListRemindersBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var  datalist: ArrayList<Prompt>
    lateinit var nameList:Array<String>
    lateinit var dateList: Array<String>

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListRemindersBinding.inflate(inflater, container, false)


        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)



        val call: Call<List<Prompt>> = service.getPrompts(1)
        call.enqueue(object : Callback<List<Prompt>> {
            override fun onResponse(call: Call<List<Prompt>>, response: Response<List<Prompt>>) {
                if (response.isSuccessful) {
                    var prompts = response.body() ?: emptyList()

                    recyclerView = binding!!.recycleListPrompt
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    datalist = arrayListOf<Prompt>()

                    for(i in prompts.indices){
                        val dataClass = Prompt(prompts[i].id,prompts[i].userId,prompts[i].name,prompts[i].description, prompts[i].date)
                        datalist.add(dataClass)
                    }

                    recyclerView.adapter = RecycleAdapterPrompt(datalist, this@FragmentListReminders, viewModel)

                }
                else{}
            }
            override fun onFailure(call: Call<List<Prompt>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })


        binding!!.bthAddPrompt.setOnClickListener {

                view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentAddPrompt)
        }

        return binding!!.root
    }

    override fun onItemClick(prompt: Prompt) {
        Log.d("RetrofitClient","onItemClick")
    }


}