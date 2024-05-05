package com.example.mydiplom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mydiplom.data.UpdateOperatingValue
import com.example.mydiplom.data.User
import com.example.mydiplom.data.UserOperatingValue
import com.example.mydiplom.data.UserUpdate
import com.example.mydiplom.databinding.FragmentProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date


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

//        baseUrl("http://192.168.0.32:3000")
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        val call: Call<User> = service.getUser(1)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    var userData = response.body()
                    userData?.let {
                        binding!!.profileName.setText(it.name.toString())
                        binding!!.profileWeight.setText(it.weight.toString())
                        binding!!.profileHeight.setText(it.height.toString())
                        binding!!.profileDate.setText(it.date_birth)
                    }
                }
                else{}
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })


        val call1: Call<List<UserOperatingValue>> = service.getUserOpertingValue(1)

        call1.enqueue(object : Callback<List<UserOperatingValue>> {
            override fun onResponse(call: Call<List<UserOperatingValue>>, response: Response<List<UserOperatingValue>>) {
                if (response.isSuccessful) {
                    var userOperatingValueData = response.body()
                    for(i in userOperatingValueData!!.indices){
                        when (userOperatingValueData[i].kind_of_mark_id){
                            1 -> binding!!.profilePressureSystolic.setText(userOperatingValueData[i].value.toString())
                            2 -> binding!!.profilePressureDiastolic.setText(userOperatingValueData[i].value.toString())
                            3 -> binding!!.profilePulse.setText(userOperatingValueData[i].value.toString())
                            4 -> binding!!.profileSugar.setText(userOperatingValueData[i].value.toString())
                            5 -> binding!!.profileCholesterol.setText(userOperatingValueData[i].value.toString())
                        }
                    }
                }
                else{}
            }
            override fun onFailure(call: Call<List<UserOperatingValue>>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })

        binding!!.bthUpdateUserMarks.setOnClickListener {

            val pressureSystolic = binding!!.profilePressureSystolic.text.toString().toDouble()
            val pressureDiastolic = binding!!.profilePressureDiastolic.text.toString().toDouble()
            val pulse = binding!!.profilePulse.text.toString().toDouble()
            val sugar = binding!!.profileSugar.text.toString().toDouble()
            val cholesterol = binding!!.profileCholesterol.text.toString().toDouble()

            val valueUpdate = UpdateOperatingValue(1, pressureSystolic, pressureDiastolic, pulse, sugar, cholesterol, SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date()))
            val call2: Call<Void> = service.updateUserOperatingValue(valueUpdate.user_id, valueUpdate)

            call2.enqueue(object : Callback<Void> {
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


        binding!!.bthUpdateProfile.setOnClickListener {
            if( !binding!!.profileName.text.isNullOrEmpty()  && !binding!!.profileWeight.text.isNullOrEmpty() &&
                !binding!!.profileHeight.text.isNullOrEmpty()  && !binding!!.profileDate.text.isNullOrEmpty()) {
                val name = binding!!.profileName.text.toString()
                val weight = binding!!.profileWeight.text.toString().toInt()
                val height = binding!!.profileHeight.text.toString().toInt()
                val date = binding!!.profileDate.text.toString()

                val userUpdate = UserUpdate(
                    userId = 1,
                    name = name,
                    height = height,
                    weight = weight,
                    date_birth = date
                )

                val call: Call<Void> = service.updateUser(userUpdate.userId, userUpdate)

                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Данные успешно обновлены", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.d("RetrofitClient", "Receive user from server problem " + t)
                        Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else{
                Toast.makeText(context, "Сначала заполните имя, вес, рост и дату рождения", Toast.LENGTH_SHORT).show()
            }
        }



        return binding!!.root
    }


}