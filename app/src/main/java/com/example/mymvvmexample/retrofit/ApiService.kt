package com.example.mymvvmexample.retrofit

import com.example.mymvvmexample.models.NewsApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("?apiKey=38cc2d27abe94b76a273d0a6acb5a0f7")
    suspend fun getAllNews(@Query("q")keyword:String):Response<NewsApi>


}
