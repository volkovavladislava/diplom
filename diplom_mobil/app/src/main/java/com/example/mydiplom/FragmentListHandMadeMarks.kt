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
import com.example.mydiplom.adapters.RecycleAdapterHandMadeMark
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.databinding.FragmentListHandMadeMarksBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
            .baseUrl("http://37.46.130.221:3000")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)


        val call: Call<List<KindOfMark>> = service.getListKindOfMarkOfHandMade(viewModel.userLoginId.value!!)
        val context = activity ?: return binding!!.root
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
                            handMadeMarks[i].enum_kind_of_mark_id,
                            handMadeMarks[i].has_reference)
                        datalist.add(dataClass)
                    }

                    Log.d("RetrofitClient","datalist " + datalist)

                    recyclerView.adapter = RecycleAdapterHandMadeMark(context, datalist, viewModel)

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