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
import com.example.mydiplom.adapters.RecycleAdapterFile
import com.example.mydiplom.data.File
import com.example.mydiplom.databinding.FragmentListFilesBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentListFiles : Fragment() {

    private var binding: FragmentListFilesBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var  datalist: ArrayList<File>
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
        binding = FragmentListFilesBinding.inflate(inflater, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)


        val call: Call<List<File>> = service.getFiles(1)
        call.enqueue(object : Callback<List<File>> {
            override fun onResponse(call: Call<List<File>>, response: Response<List<File>>) {
                if (response.isSuccessful) {
                    var files = response.body() ?: emptyList()

                    recyclerView = binding!!.recycleListFile
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    datalist = arrayListOf<File>()

                    for(i in files.indices){
                        val dataClass = File(
                            files[i].id,
                            files[i].userId,
                            files[i].link,
                            files[i].mime_type,
                            files[i].date,
                            files[i].name,
                            files[i].comment)
                        datalist.add(dataClass)
                    }

                    recyclerView.adapter = RecycleAdapterFile(datalist, viewModel)

                }
                else{}
            }
            override fun onFailure(call: Call<List<File>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })




        binding!!.bthAddFile.setOnClickListener {
                view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentAddFile)
        }

        return binding!!.root
    }


}