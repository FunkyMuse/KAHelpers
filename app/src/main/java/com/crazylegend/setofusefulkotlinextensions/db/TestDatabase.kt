package com.crazylegend.setofusefulkotlinextensions.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.crazylegend.kotlinextensions.singleton.ParameterizedSingleton
import com.crazylegend.setofusefulkotlinextensions.adapter.TestModel

/**
 * Created by crazy on 1/14/21 to long live and prosper !
 */
@Database(entities = [TestModel::class], version = 1, exportSchema = false)
abstract class TestDatabase : RoomDatabase() {

    abstract fun dao(): TestDao

    companion object : ParameterizedSingleton<TestDatabase, Context>({
        Room.databaseBuilder(it, TestDatabase::class.java, "test-db")
                .fallbackToDestructiveMigration()
                .build()
    })

}