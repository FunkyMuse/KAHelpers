package com.crazylegend.setofusefulkotlinextensions.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel
import kotlinx.coroutines.flow.Flow

/**
 * Created by crazy on 1/14/21 to long live and prosper !
 */
@Dao
interface TestDao {

    @Query("select * from tests")
    fun getAllFlow(): Flow<List<TestModel>>

    @Insert
    suspend fun insertList(list: List<TestModel>)

    @Query("select  * from tests")
    suspend fun getAll(): List<TestModel>
}