package com.crazylegend.setofusefulkotlinextensions.adapter


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestModel(
    val body: String,
    val id: Int,
    val title: String,
    val userId: Int
) : Parcelable