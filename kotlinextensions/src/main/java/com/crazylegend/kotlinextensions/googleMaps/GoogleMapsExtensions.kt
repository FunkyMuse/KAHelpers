package com.crazylegend.kotlinextensions.googleMaps

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import com.crazylegend.intent.canBeHandled


/**
 * Created by crazy on 1/24/20 to long live and prosper !
 */


const val MAPS_URL = "com.google.android.apps.maps"
const val LOC_MARKER_IN_MAPS_BY_LATLONG = "geo:0,0?q=%f,%f(%s)"
const val LOC_MARKER_IN_MAPS_BY_ADDRESS = "geo:0,0?q=%s"
const val SEARCH_PLACES = "geo:0,0?q=%s"
const val SEARCH_PLACES_BY_LOCATION = "geo:%f,%f?q=%s"

/**
 * Show provided location maker in google maps
 * pass lat,long
 *
 * @param name name which is shown for that marker
 * @return true if successfully opened google maps, or false if google maps not found
 */
fun Context.showMakerInGoogleMap(
        latitude: Double?,
        longitude: Double?,
        name: String
): Boolean {
    val link = String.format(LOC_MARKER_IN_MAPS_BY_LATLONG, latitude, longitude, name)
    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    mapIntent.setPackage(MAPS_URL)
    return if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
        true
    } else
        false

}

/**
 * Show provided location maker in google maps
 * pass lat,long
 *
 * @param address address on the basis of which search is performed, to find the address
 * @return true if successfully opened google maps, or false if google maps not found
 */
fun Context.showMakerInGoogleMap(address: String): Boolean {
    val link = String.format(LOC_MARKER_IN_MAPS_BY_ADDRESS, address)
    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    mapIntent.setPackage(MAPS_URL)
    return if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
        true
    } else
        false

}

/**
 * Show provided location maker in google maps
 * pass lat,long
 *
 * @param format  string format of which you will send parameters
 * @param objects objects seperated by comma, on the basis of format you defined
 *
 *
 * example : format="geo:0,0?q=%s"
 * objects will be, one string object which will be placed in place of **%s**
 * result will be "geo:0,0?q=<your provided string here>"
 * @return true if successfully opened google maps, or false if google maps not found*/

fun Context.showMakerInGoogleMap(format: String, vararg objects: Any): Boolean {
    val link = String.format(format, *objects)
    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    mapIntent.setPackage(MAPS_URL)
    return if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
        true
    } else
        false
}

/**
 * Show provided location maker in google maps
 * pass lat,long
 *
 * @param typeOfPlace like restruant,hotel..etc
 * @return true if successfully opened google maps, or false if google maps not found
 */
fun Context.searchPlaces(typeOfPlace: String): Boolean {
    val link = String.format(SEARCH_PLACES, typeOfPlace)
    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    mapIntent.setPackage(MAPS_URL)
    return if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
        true
    } else
        false

}

/**
 * Show provided location maker in google maps
 * pass lat,long
 *
 * @param typeOfPlace like restaurant,hotel..etc
 * @param latitude    latitude
 * @param longitude   longitude
 * @return true if successfully opened google maps, or false if google maps not found
 */
fun Context.searchPlacesByLatLong(
        latitude: Double?,
        longitude: Double?,
        typeOfPlace: String
): Boolean {
    val link = String.format(SEARCH_PLACES_BY_LOCATION, latitude, longitude, typeOfPlace)
    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    mapIntent.setPackage(MAPS_URL)
    return if (mapIntent.resolveActivity(packageManager) != null) {
        startActivity(mapIntent)
        true
    } else
        false

}

/**
 * Get Map Coordinates
 *
 * @param latitude
 * @param longitude
 * @param markerTitle title/name of route or name
 * @return Intent
 */
private fun getMapCoordinatesIntent(latitude: Double, longitude: Double, markerTitle: String?): Intent {
    var uri = "geo:$latitude,$longitude?q=$latitude,$longitude"
    if (markerTitle != null && markerTitle.isNotEmpty()) {
        uri += "($markerTitle)"
    }
    return Intent(Intent.ACTION_VIEW, Uri.parse(uri))
}

/**
 * Get Map Coordinates
 *
 * @param latitude
 * @param longitude
 * @param markerTitle              title/name of route or name
 * @param selectionTitle           title while choosing from multiple maps
 * @param exceptionMessageIfOccurs msg if exception occurs,
 * pass **null** if you don't want to show the toast msg
 */
fun Context.showMapCoordinates(latitude: Double, longitude: Double,
                               markerTitle: String, selectionTitle: String, exceptionMessageIfOccurs: String?) {
    val intent = getMapCoordinatesIntent(
            latitude,
            longitude,
            markerTitle
    )
    try {
        val chooser = Intent.createChooser(intent, selectionTitle)
        if (chooser.canBeHandled(this)) {
            startActivity(chooser)
        }
    } catch (ex: ActivityNotFoundException) {
        if (exceptionMessageIfOccurs != null)
            ex.printStackTrace()
    }

}

/**
 * Get Map Coordinates
 *
 * @param latitudeFrom
 * @param longitudeFrom
 * @param latitudeTo
 * @param longitudeTo
 */
private fun getMapRouteIntent(latitudeFrom: Double, longitudeFrom: Double, latitudeTo: Double,
                              longitudeTo: Double): Intent {
    val uri = Uri.parse("http://maps.google.com/maps?saddr=$latitudeFrom,$longitudeFrom&daddr=$latitudeTo,$longitudeTo")
    return Intent(Intent.ACTION_VIEW, uri)
}


/**
 * Get Map Coordinates
 *
 * @param latitudeFrom
 * @param longitudeFrom
 * @param latitudeTo
 * @param longitudeTo
 * @param selectionTitle           title while choosing from multiple maps
 * @param exceptionMessageIfOccurs msg if exception occurs,
 * pass **null** if you don't want to show the toast msg
 */
fun Context.showMapRoute(latitudeFrom: Double, longitudeFrom: Double, latitudeTo: Double,
                         longitudeTo: Double, selectionTitle: String,
                         exceptionMessageIfOccurs: String?) {
    val intent = getMapRouteIntent(
            latitudeFrom,
            longitudeFrom,
            latitudeTo,
            longitudeTo
    )
    try {
        val chooser = Intent.createChooser(intent, selectionTitle)
        if (chooser.canBeHandled(this)) {
            startActivity(chooser)
        }
    } catch (ex: ActivityNotFoundException) {
        if (exceptionMessageIfOccurs != null)
            ex.printStackTrace()
    }


}

/**Returns true if Google Maps is present in your device.*/
fun Context.isGoogleMapsInstalled(): Boolean {
    return try {
        packageManager.getApplicationInfo("com.google.android.apps.maps", 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.openMapAppsChooser(latitude: Double, longitude: Double) {
    val uri = Uri.parse("geo:${latitude},${longitude}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}


fun List<Location>.googleMapDirectionsIntent(): Intent {
    val intentList = this.mapIndexed { index, it ->
        "${it.latitude}:${it.longitude}" + if (index == lastIndex) "" else "/"
    }.toString().removePrefix("[").removeSuffix("]")
            .replace(" ", "")
            .replace(",", "")
            .replace(":", ",")
    val url = "https://www.google.com/maps/dir/$intentList"

    val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse(url))
    intent.setPackage("com.google.android.apps.maps")
    return intent
}

fun googleMapDirectionsIntentForList(list: List<Location>): Intent {
    val intentList = list.mapIndexed { index, it ->
        "${it.latitude}:${it.longitude}" + if (index == list.lastIndex) "" else "/"
    }.toString().removePrefix("[").removeSuffix("]")
            .replace(" ", "")
            .replace(",", "")
            .replace(":", ",")
    val url = "https://www.google.com/maps/dir/$intentList"

    val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse(url))
    intent.setPackage("com.google.android.apps.maps")
    return intent
}