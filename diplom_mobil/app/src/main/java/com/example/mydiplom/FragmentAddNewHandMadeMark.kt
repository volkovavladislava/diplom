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
import com.example.mydiplom.data.AddHandMadeKindOfMark
import com.example.mydiplom.databinding.FragmentAddNewHandMadeMarkBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentAddNewHandMadeMark : Fragment() {


    private var binding: FragmentAddNewHandMadeMarkBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewHandMadeMarkBinding.inflate(inflater, container, false)

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



        binding!!.bthaddNewHandMadeMark.setOnClickListener {

            if( !binding!!.addNewHandMadeMarkName.text.isNullOrEmpty() ){
                val name = binding!!.addNewHandMadeMarkName.text.toString()

                val kindOfMark = AddHandMadeKindOfMark(name = name, user_id=viewModel.userLoginId.value!!, enum_kind_of_mark_id=2 )


                val call: Call<Void> = service.addHandMadeKindOfMark(kindOfMark.user_id, kindOfMark)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Данные успешно добавлены", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("RetrofitClient","Receive user from server problem " + t)
                        Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else{
                Toast.makeText(context, "Сначала добавьте название", Toast.LENGTH_SHORT).show()
            }

        }


        return binding!!.root
    }


}