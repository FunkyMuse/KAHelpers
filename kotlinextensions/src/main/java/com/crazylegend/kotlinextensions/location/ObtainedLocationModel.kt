package com.crazylegend.kotlinextensions.location

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by crazy on 3/30/19 to long live and prosper !
 */

@Parcelize
data class ObtainedLocationModel(var address :String = "",
                                 var city :String = "",
                                 var state:String = "",
                                 var country :String = "",
                                 var postalCode :String = "",
                                 var knownName: String = "") : Parcelable