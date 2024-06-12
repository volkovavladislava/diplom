package com.example.mydiplom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mydiplom.data.LoginRequest
import com.example.mydiplom.data.RegistrationRequest
import com.example.mydiplom.data.UserAuth
import com.example.mydiplom.databinding.FragmentRegistrationBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentRegistration : Fragment() {

    private var binding: FragmentRegistrationBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentRegistrationBinding.inflate(inflater, container, false)



        binding!!.goLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentRegistration_to_fragmentLogin2)
        }


        binding!!.goLoginButton.setOnClickListener {
            if( !binding!!.registrationLogin.text.isNullOrEmpty()  && !binding!!.registrationPassword.text.isNullOrEmpty() &&
                !binding!!.registrationName.text.isNullOrEmpty()  && !binding!!.registrationHeight.text.isNullOrEmpty()
                && !binding!!.registrationWeight.text.isNullOrEmpty() &&
                !binding!!.registrationDate.text.isNullOrEmpty()  && !binding!!.registrationGender.text.isNullOrEmpty()) {

                //        http://192.168.0.32:3000
                //        http://10.0.2.2:3000

                val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.0.32:3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val service: ApiController = retrofit.create(ApiController::class.java)

                val login = binding!!.registrationLogin.text.toString()
                val pas = binding!!.registrationPassword.text.toString()
                val name = binding!!.registrationName.text.toString()
                val height = binding!!.registrationHeight.text.toString().toInt()
                val weight = binding!!.registrationWeight.text.toString().toInt()
                val date = binding!!.registrationDate.text.toString()
                val gender = binding!!.registrationGender.text.toString().toInt()

                val l = RegistrationRequest(login, pas, name, height, weight, date, gender)

                val call: Call<Void> = service.register(l)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {

                            findNavController().navigate(R.id.action_fragmentRegistration_to_fragmentLogin2)
                        } else {
                            Log.d("RetrofitClient", "response " + response)
                            Toast.makeText(context, "Неправильно заполнено", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("RetrofitClient", "Receive user from server problem " + t)
                    }
                })
            }
            else{
                Toast.makeText(context, "Сначала заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }


        return binding!!.root
    }

}