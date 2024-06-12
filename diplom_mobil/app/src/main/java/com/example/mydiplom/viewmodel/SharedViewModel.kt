package com.example.mydiplom.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydiplom.ApiController
import com.example.mydiplom.data.AddFavoriteKindOfMark
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.data.User
import com.example.mydiplom.data.UserAuth
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
    var kindOfMarkNameStatistic = MutableLiveData<String>()

    var markId = MutableLiveData<Int>()


    var handMadeMarkId = MutableLiveData<Int>()
    var handMadeMarkRecordId = MutableLiveData<Int>()

    var userId = 1

    var userLogin = MutableLiveData<UserAuth>()
    val token = MutableLiveData<String?>()
    val userLoginId = MutableLiveData<Int?>()


    val updateEnumId= MutableLiveData<Int>()
    val updateEnumUserId= MutableLiveData<Int>()
    val updateEnumKindOfMarkId= MutableLiveData<Int>()
    val updateEnumDate= MutableLiveData<String>()
    val updateEnumSituation= MutableLiveData<Int>()
    val updateEnumValueEnum= MutableLiveData<Int>()
    val updateEnumValue= MutableLiveData<String>()


    val updateNum1Id= MutableLiveData<Int>()
    val updateNum1UserId= MutableLiveData<Int>()
    val updateNum1KindOfMarkId= MutableLiveData<Int>()
    val updateNum1Date= MutableLiveData<String>()
    val updateNum1Situation= MutableLiveData<Int>()
    val updateNum1ValueDouble= MutableLiveData<Double>()

    val updateNum2Id1= MutableLiveData<Int>()
    val updateNum2Id2= MutableLiveData<Int>()
    val updateNum2UserId= MutableLiveData<Int>()
    val updateNum2KindOfMarkId= MutableLiveData<Int>()
    val updateNum2Date= MutableLiveData<String>()
    val updateNum2Situation= MutableLiveData<Int>()
    val updateNum2Value1Double= MutableLiveData<Double>()
    val updateNum2Value2Double= MutableLiveData<Double>()

    fun notifyTokenExpired(){
        token.postValue(null)
        userLoginId.postValue(null)
    }


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