package com.example.mymvvmexample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mymvvmexample.R
import com.example.mymvvmexample.adapters.NewsRvAdapter
import com.example.mymvvmexample.database.AppDatabase
import com.example.mymvvmexample.database.NewsEntity
import com.example.mymvvmexample.databinding.FragmentFavouriteBinding
import com.example.mymvvmexample.databinding.ItemNewsRvBinding
import com.example.mymvvmexample.repository.NewsRepository
import com.example.mymvvmexample.retrofit.ApiClient
import com.example.mymvvmexample.utils.NetworkHelper
import com.example.mymvvmexample.utils.NewsResource
import com.example.mymvvmexample.viewmodel.NewsViewModel
import com.example.mymvvmexample.viewmodel.ViewModelFactory
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment() {

    private lateinit var binding:FragmentFavouriteBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsRvAdapter: NewsRvAdapter
    private lateinit var networkHelper: NetworkHelper
    private lateinit var newsRepository: NewsRepository
    private lateinit var appDatabase: AppDatabase
    private lateinit var newsEntityList:ArrayList<NewsEntity>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater,container,false)

        networkHelper = NetworkHelper(requireContext())
        appDatabase = AppDatabase.getInstance(requireContext())

        newsRepository = NewsRepository(ApiClient.apiService,appDatabase.newsDao())

        newsViewModel = ViewModelProvider(this, ViewModelFactory(
            newsRepository,
            networkHelper
        ))[NewsViewModel::class.java]


        newsRvAdapter = NewsRvAdapter(object :NewsRvAdapter.OnItemClickListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onImageClick(
                newsEntity: NewsEntity,
                itemNewsRvBinding: ItemNewsRvBinding
            ) {
                if(newsEntity.isLike){
                    itemNewsRvBinding.savedImg.setImageResource(R.drawable.heart_unselected)
                    newsEntity.isLike = false
                    lifecycleScope.launch {
                        appDatabase.newsDao().delete(newsEntity)
                    }
                    newsRvAdapter.notifyDataSetChanged()
                }
            }

            override fun onItemClick(newsEntity: NewsEntity, itemNewsRvBinding: ItemNewsRvBinding) {
                Toast.makeText(requireContext(), newsEntity.description, Toast.LENGTH_SHORT).show()
            }
        })

        newsEntityList = newsViewModel.getNewsDatabase() as ArrayList<NewsEntity>
            if(newsEntityList.isNotEmpty()){
                        binding.rv.visibility = View.VISIBLE
                        binding.messageTv.visibility = View.GONE
                        newsRvAdapter.submitList(newsEntityList)
                    }else{
                        binding.rv.visibility = View.GONE
                        binding.messageTv.visibility = View.VISIBLE
                    }

        binding.rv.adapter = newsRvAdapter


        return binding.root
    }
}
