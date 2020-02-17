package com.crazylegend.kotlinextensions.misc

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */


/**
 * Writes a boolean value to the [Parcel] (as a byte).
 */
fun Parcel.writeBoolean(value: Boolean) {
    writeByte(if (value) 1.toByte() else 0)
}

/**
 * Reads a boolean value from the [Parcel] (converted from a byte).
 */
fun Parcel.readBoolean() = readByte().toInt() != 0

/**
 * Reified version of [Parcel.readParcelable] that allows classloader
 * argument to be omitted.
 */
inline fun <reified T : Parcelable> Parcel.readTypedParcelable(): T? =
        readParcelable(T::class.java.classLoader)