package com.crazylegend.setofusefulkotlinextensions.adapter


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "tests")
@JsonClass(generateAdapter = true)
data class TestModel(
    @ColumnInfo(name = "body")
    val body: String,
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "userId")
    val userId: Int
)