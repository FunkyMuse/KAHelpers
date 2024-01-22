package dev.funkymuse.setofusefulkotlinextensions.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.funkymuse.setofusefulkotlinextensions.adapter.TestModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDao {

    @Query("select * from tests")
    fun getAllFlow(): Flow<List<TestModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: List<TestModel>)

    @Query("select  * from tests")
    suspend fun getAll(): List<TestModel>
}