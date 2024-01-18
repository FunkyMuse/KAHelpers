package com.funkymuse.kotlinextensions.location

data class ObtainedLocationModel(
    var address: String? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = null,
    var postalCode: String? = null,
    var knownName: String? = null
)