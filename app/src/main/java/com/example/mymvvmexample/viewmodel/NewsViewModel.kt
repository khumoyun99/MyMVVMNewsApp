package com.example.mymvvmexample.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymvvmexample.database.NewsEntity
import com.example.mymvvmexample.repository.NewsRepository
import com.example.mymvvmexample.utils.NetworkHelper
import com.example.mymvvmexample.utils.NewsResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewsViewModel(
    private var newsRepository: NewsRepository,
    private val networkHelper: NetworkHelper
) :
    ViewModel(){

        fun fetchNews(keyword:String):StateFlow<NewsResource>{
            val stateFlow = MutableStateFlow<NewsResource>(NewsResource.Loading)

            viewModelScope.launch {
                if(networkHelper.isNetworkConnected()){
                    newsRepository.getAllNewsApi(keyword)
                        .catch {
                            stateFlow.emit(NewsResource.Error(it.message?: " "))
                        }.collect {
                            if(it.isSuccessful){
                                val newsApi = it.body()
                                val articleList = ArrayList(newsApi?.articles?: emptyList())
                                val newsList = ArrayList<NewsEntity>()
                                articleList.forEach {article->
                                    newsList.add(NewsEntity(
                                        author = article.author?:"",
                                        content = article.content,
                                        title = article.title,
                                        description = article.description,
                                        urlToImage = article.urlToImage,
                                        publishedAt = article.publishedAt,
                                        url = article.url
                                    ))
                                }

                                newsRepository.addNewsToDb(newsList)
                                stateFlow.emit(NewsResource.Success(newsList))
                            }else{
                                stateFlow.emit(NewsResource.Error("Server or Client error!!"))
                            }
                        }
                }else{
                    newsRepository.getDbNews()
                        .collect {
                            if(it.isEmpty()){
                                stateFlow.emit(NewsResource.Error("No internet connection"))
                            }else{
                                stateFlow.emit(NewsResource.Success(it))
//                                stateFlow.emit(NewsResource.Error("No internet connection"))
                            }
                        }
                }
            }
            return stateFlow
        }


        fun getNewsDatabase():List<NewsEntity>{
            var newsEntityList = ArrayList<NewsEntity>()
            viewModelScope.launch{
                newsRepository.getAllLikeNews(true)
                    .collect{
                        newsEntityList = ArrayList(it)
                    }
            }
            return newsEntityList
        }


}