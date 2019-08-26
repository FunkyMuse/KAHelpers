package com.crazylegend.setofusefulkotlinextensions


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestModel(
    var body: String,
    var id: Int,
    var title: String,
    var userId: Int
) : Parcelable