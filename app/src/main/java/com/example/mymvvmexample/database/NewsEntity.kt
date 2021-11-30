package com.example.mymvvmexample.database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val author:String,
    val content:String,
    val description:String,
    val publishedAt: String,
    val title:String,
    val urlToImage:String,
    var isLike:Boolean=false,
    var url:String
)
