package com.example.mydiplom

import android.content.Intent
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
import com.example.mydiplom.adapters.RecycleAdapterPrompt
import com.example.mydiplom.data.Prompt
import com.example.mydiplom.databinding.FragmentListRemindersBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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



        val call: Call<List<Prompt>> = service.getPrompts(viewModel.userLoginId.value!!)
        call.enqueue(object : Callback<List<Prompt>> {
            override fun onResponse(call: Call<List<Prompt>>, response: Response<List<Prompt>>) {
                if (response.isSuccessful) {
                    var prompts = response.body() ?: emptyList()

                    recyclerView = binding!!.recycleListPrompt
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    datalist = arrayListOf<Prompt>()

                    for(i in prompts.indices){
                        val dataClass = Prompt(prompts[i].id,prompts[i].userId,prompts[i].name,prompts[i].description, prompts[i].date,prompts[i].calendar_id)
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