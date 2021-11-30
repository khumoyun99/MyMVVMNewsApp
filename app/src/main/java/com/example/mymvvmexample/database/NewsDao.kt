package com.example.mymvvmexample.database

import androidx.room.*
import androidx.room.OnConflictStrategy.*

@Dao
interface NewsDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(newsEntity: NewsEntity)

    @Insert(onConflict = REPLACE)
    suspend fun insert(newsEntityList: List<NewsEntity>)

    @Delete
    suspend fun delete(newsEntity: NewsEntity)

    @Delete
    fun delete(newsEntityList: List<NewsEntity>)

    @Query("select * from newsentity")
    fun getAllNews():List<NewsEntity>

    @Query("select * from newsentity where isLike=:boo")
    suspend fun getAllLikeNews(boo:Boolean):List<NewsEntity>


}