package com.example.mydiplom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mydiplom.data.AddHandMadeKindOfMark
import com.example.mydiplom.data.AddMark
import com.example.mydiplom.databinding.FragmentAddNewHandMadeMarkBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FragmentAddNewHandMadeMark : Fragment() {


    private var binding: FragmentAddNewHandMadeMarkBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewHandMadeMarkBinding.inflate(inflater, container, false)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)



        binding!!.bthaddNewHandMadeMark.setOnClickListener {
            val name = binding!!.addNewHandMadeMarkName.text.toString()

            val kindOfMark = AddHandMadeKindOfMark(name = name, user_id=1, enum_kind_of_mark_id=2 )


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


        return binding!!.root
    }


}