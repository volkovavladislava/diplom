package com.example.mydiplom


import com.example.mydiplom.data.KindOfMark
import retrofit2.Call
import retrofit2.http.GET

interface ApiController {

    @GET("/api/listKindOfMark")
    fun getListKindOfMark(): Call<List<KindOfMark>>
}