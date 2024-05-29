package com.example.mydiplom

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
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000")
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