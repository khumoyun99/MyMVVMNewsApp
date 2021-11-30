package com.example.mymvvmexample.repository

import com.example.mymvvmexample.database.NewsDao
import com.example.mymvvmexample.database.NewsEntity
import com.example.mymvvmexample.retrofit.ApiService
import kotlinx.coroutines.flow.flow

class NewsRepository(private var apiService: ApiService,private val newsDao: NewsDao) {

    suspend fun getAllNewsApi(keyword:String) = flow { emit(apiService.getAllNews(keyword))}

    suspend fun getDbNews() = flow {emit(newsDao.getAllNews())}

    suspend fun addNewsToDb(list:List<NewsEntity>) = newsDao.insert(list)

    suspend fun addNewsRoomDb(newsEntity: NewsEntity) = newsDao.insert(newsEntity)

    suspend fun deleteNewsRoomDb(newsEntity: NewsEntity) = newsDao.delete(newsEntity)

    suspend fun getAllLikeNews(boolean: Boolean) = flow { emit(newsDao.getAllLikeNews(boolean)) }


}