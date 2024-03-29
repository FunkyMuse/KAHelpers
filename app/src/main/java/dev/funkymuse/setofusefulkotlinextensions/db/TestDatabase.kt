package dev.funkymuse.setofusefulkotlinextensions.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.funkymuse.kotlinextensions.singleton.ParameterizedSingleton
import dev.funkymuse.setofusefulkotlinextensions.adapter.TestModel

@Database(entities = [TestModel::class], version = 1, exportSchema = false)
abstract class TestDatabase : RoomDatabase() {

    abstract fun dao(): TestDao

    companion object : ParameterizedSingleton<TestDatabase, Context>({
        Room.databaseBuilder(it, TestDatabase::class.java, "test-db")
                .fallbackToDestructiveMigration()
                .build()
    })

}