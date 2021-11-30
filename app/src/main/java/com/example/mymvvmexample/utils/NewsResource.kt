package com.example.mymvvmexample.utils

import com.example.mymvvmexample.database.NewsEntity

sealed class NewsResource {

    object Loading:NewsResource()

    data class Success(val list:List<NewsEntity>):NewsResource()

    data class Error(val message:String):NewsResource()
}