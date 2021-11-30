package com.example.mymvvmexample.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymvvmexample.R
import com.example.mymvvmexample.database.NewsEntity
import com.example.mymvvmexample.databinding.ItemNewsRvBinding
import com.squareup.picasso.Picasso

class NewsRvAdapter(val listener:OnItemClickListener):ListAdapter<NewsEntity,NewsRvAdapter.Vh>(MyDiffUtil()) {


    inner class Vh(val itemNewsRvBinding: ItemNewsRvBinding):RecyclerView.ViewHolder(itemNewsRvBinding.root){
        fun onBind(newsEntity: NewsEntity){
            itemNewsRvBinding.apply {
                Picasso.get().load(newsEntity.urlToImage).into(itemNewsImg)
                itemNewsTitle.text = newsEntity.title
                itemNewsAuther.text = newsEntity.author
                itemNewsDate.text = newsEntity.publishedAt

                if(newsEntity.isLike){
                    savedImg.setImageResource(R.drawable.heart_selected)
                }


                savedImg.setOnClickListener {
                    listener.onImageClick(newsEntity,itemNewsRvBinding)
                }

                itemView.setOnClickListener {
                    listener.onItemClick(newsEntity,itemNewsRvBinding)
                }
            }
        }
    }

    class MyDiffUtil:DiffUtil.ItemCallback<NewsEntity>(){
        override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemNewsRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnItemClickListener{
        fun onImageClick(newsEntity: NewsEntity,itemNewsRvBinding: ItemNewsRvBinding)
        fun onItemClick(newsEntity: NewsEntity,itemNewsRvBinding: ItemNewsRvBinding)
    }
}