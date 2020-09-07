package com.crazylegend.setofusefulkotlinextensions.adapter


import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class TestModel(
        val body: String,
        val id: Int,
        val title: String,
        val userId: Int
) : Parcelable