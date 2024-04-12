package com.example.mydiplom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mydiplom.data.User
import com.example.mydiplom.data.UserUpdate
import com.example.mydiplom.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentProfile : Fragment() {

    private var binding: FragmentProfileBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)


        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.32:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        val call: Call<User> = service.getUser(1)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    var userData = response.body()
                    userData?.let {
                        binding!!.profileWeight.setText(it.weight.toString())
                        binding!!.profileHeight.setText(it.height.toString())
                        binding!!.profiletDate.setText(it.date_birth)
                    }
                }
                else{

                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })



        binding!!.bthUpdateProfile.setOnClickListener {
            val weight = binding!!.profileWeight.text.toString().toInt()
            val height = binding!!.profileHeight.text.toString().toInt()
            val date = binding!!.profiletDate.text.toString()

            val userUpdate = UserUpdate(userId = 1, name = "3", height = height, weight = weight)

            val call: Call<Void> = service.updateUser(userUpdate. userId, userUpdate)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Данные успешно обновлены", Toast.LENGTH_SHORT).show()
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



        return binding!!.root
    }


}