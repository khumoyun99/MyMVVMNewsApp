package com.example.mymvvmexample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mymvvmexample.R
import com.example.mymvvmexample.adapters.NewsRvAdapter
import com.example.mymvvmexample.database.AppDatabase
import com.example.mymvvmexample.database.NewsEntity
import com.example.mymvvmexample.databinding.FragmentSearchBinding
import com.example.mymvvmexample.databinding.ItemNewsRvBinding
import com.example.mymvvmexample.repository.NewsRepository
import com.example.mymvvmexample.retrofit.ApiClient
import com.example.mymvvmexample.utils.NetworkHelper
import com.example.mymvvmexample.utils.NewsResource
import com.example.mymvvmexample.viewmodel.NewsViewModel
import com.example.mymvvmexample.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding:FragmentSearchBinding
    private lateinit var newsViewMode: NewsViewModel
    private lateinit var newsRvAdapter: NewsRvAdapter
    private lateinit var networkHelper: NetworkHelper
    private lateinit var newsRepository: NewsRepository
    private lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        networkHelper = NetworkHelper(requireContext())
        appDatabase = AppDatabase.getInstance(requireContext())

        newsRepository = NewsRepository(ApiClient.apiService,appDatabase.newsDao())

        newsViewMode = ViewModelProvider(this, ViewModelFactory(
            newsRepository,
            networkHelper
        ))[NewsViewModel::class.java]


        newsRvAdapter = NewsRvAdapter(object :NewsRvAdapter.OnItemClickListener{
            override fun onImageClick(
                newsEntity: NewsEntity,
                itemNewsRvBinding: ItemNewsRvBinding
            ) {
                if(newsEntity.isLike){
                    itemNewsRvBinding.savedImg.setImageResource(R.drawable.heart_unselected)
                    newsEntity.isLike = false
                    lifecycleScope.launch {
                        newsRepository.addNewsRoomDb(newsEntity)
                    }
                }else{
                    itemNewsRvBinding.savedImg.setImageResource(R.drawable.heart_selected)
                    newsEntity.isLike = true

                    lifecycleScope.launch {
                        newsRepository.addNewsRoomDb(newsEntity)
                    }
                }
            }

            override fun onItemClick(newsEntity: NewsEntity, itemNewsRvBinding: ItemNewsRvBinding) {
                Toast.makeText(requireContext(), newsEntity.description, Toast.LENGTH_SHORT).show()
            }
        })

        binding.searchRv.adapter = newsRvAdapter

        binding.searchView.setOnClickListener {
            val search = binding.searchEt.text
            if(search.isNotEmpty()){
                lifecycleScope.launch {
                    newsViewMode.fetchNews(search.toString())
                        .collect {
                            when(it){
                                is NewsResource.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                    binding.searchInfoTv.visibility = View.GONE
                                }
                                is NewsResource.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.searchInfoTv.visibility = View.VISIBLE
                                    binding.searchInfoTv.text = it.message
                                }
                                is NewsResource.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    binding.searchInfoTv.visibility = View.GONE
                                    binding.searchRv.visibility = View.VISIBLE
                                    newsRvAdapter.submitList(it.list)

                                }
                            }
                        }
                }


            }
        }


        return binding.root

    }

}