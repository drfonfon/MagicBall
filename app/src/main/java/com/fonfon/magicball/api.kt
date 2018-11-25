package com.fonfon.magicball

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//Data классы - специальные классы для POJO объектов, с генерируемыми функицями toString, hash, copy
data class Answer(val text: String)

//Описание api
interface Api {
    @GET("/answers")
    fun answers(): Call<List<Answer>>
}

val api = Retrofit.Builder()
    .baseUrl("https://private-db490c-magicball.apiary-mock.com")
    .client(OkHttpClient())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(Api::class.java)