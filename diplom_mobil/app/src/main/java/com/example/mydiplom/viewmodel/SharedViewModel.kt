package com.example.mydiplom.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydiplom.ApiController
import com.example.mydiplom.data.AddFavoriteKindOfMark
import com.example.mydiplom.data.KindOfMark
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SharedViewModel : ViewModel() {
    var articleId = MutableLiveData<Int>()
    var promptId = MutableLiveData<Int>()
    var fileId = MutableLiveData<Int>()

    var kindOfMarkIdAddMark = MutableLiveData<Int>()
    var kindOfMarkNameAddMark = MutableLiveData<String>()

    var kindOfMarkIdStatistic = MutableLiveData<Int>()

    var markId = MutableLiveData<Int>()


    var handMadeMarkId = MutableLiveData<Int>()
    var handMadeMarkRecordId = MutableLiveData<Int>()

    var userId = 1

    fun addToFavorite(context: Context, userId: Int, kindOfMarkId: Int){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        val call: Call<Void> = service.addFavoriteKindOfMark(AddFavoriteKindOfMark(userId,kindOfMarkId))

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
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

    fun deleteFavorite(context: Context, userId: Int, kindOfMarkId: Int){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)

        val call: Call<Void> = service.deleteFavoriteKindOfMark(AddFavoriteKindOfMark(userId,kindOfMarkId))

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Удалено из избранного", Toast.LENGTH_SHORT).show()
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


}