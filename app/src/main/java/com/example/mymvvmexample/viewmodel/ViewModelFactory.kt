package com.example.mymvvmexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mymvvmexample.repository.NewsRepository
import com.example.mymvvmexample.utils.NetworkHelper

class ViewModelFactory(
    private val newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper
) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NewsViewModel::class.java)){
            return NewsViewModel(newsRepository,networkHelper) as T
        }
        throw Exception("Error")
    }
}