package com.example.mydiplom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mydiplom.data.LoginRequest
import com.example.mydiplom.data.UserAuth
import com.example.mydiplom.databinding.FragmentLoginBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentLogin : Fragment() {

    private var binding: FragmentLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        val loginTV = binding!!.loginTV



        binding!!.goRegistButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentLogin2_to_fragmentRegistration)
        }




        binding!!.loginButton.setOnClickListener {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://37.46.130.221:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val service: ApiController = retrofit.create(ApiController::class.java)

            val login = binding!!.loginLogin.text.toString()
            val pas = binding!!.loginPassword.text.toString()

            val l = LoginRequest(login,pas)

            val call: Call<UserAuth> = service.login(l)
            call.enqueue(object : Callback<UserAuth> {
                override fun onResponse(call: Call<UserAuth>, response: Response<UserAuth>) {
                    if (response.isSuccessful) {
                        var user = response.body()
//                        loginTV.setText(user!!.accessToken)

                        val intent = Intent(requireContext(), MainActivity::class.java).apply {
                            putExtra("token", user!!.accessToken)
                            putExtra("id", user.id.toInt())
                        }
                        startActivity(intent)

                        requireActivity().finish()
//                        startActivity(Intent(requireContext(), MainActivity::class.java))
//                        findNavController().navigate(R.id.action_fragmentLogin2_to_app_navigation)

                    }
                    else{
                        loginTV.setText("Неверный логин или пароль")
                    }
                }
                override fun onFailure(call: Call<UserAuth>, t: Throwable) {
                    Log.d("RetrofitClient","Receive user from server problem " + t)
                }
            })
        }



        return binding!!.root
    }


}