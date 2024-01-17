package com.crazylegend.kotlinextensions.location


/**
 * Created by crazy on 3/30/19 to long live and prosper !
 */

data class ObtainedLocationModel(
    var address: String? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = null,
    var postalCode: String? = null,
    var knownName: String? = null
)