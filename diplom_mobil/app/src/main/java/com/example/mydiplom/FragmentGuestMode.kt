package com.example.mydiplom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.mydiplom.data.User
import com.example.mydiplom.data.UserAuth
import com.example.mydiplom.data.UserGuestMode
import com.example.mydiplom.databinding.FragmentGuestModeBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentGuestMode : Fragment() {

    private var binding: FragmentGuestModeBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentGuestModeBinding.inflate(inflater, container, false)


        binding!!.guestModeGenerateBth.setOnClickListener {
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

            val call: Call<UserGuestMode> = service.getUserGuestPassword(viewModel.userLoginId.value!!)
            call.enqueue(object : Callback<UserGuestMode> {
                override fun onResponse(call: Call<UserGuestMode>, response: Response<UserGuestMode>) {
                    if (response.isSuccessful) {
                        var passwordData = response.body()
                        passwordData?.let {
                            binding!!.guestModePassword.setText(it.guest_password.toString())
                        }
                    }
                    else {
                        Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UserGuestMode>, t: Throwable) {
                    Log.d("RetrofitClient", "Receive user from server problem " + t)
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return binding!!.root
    }


}