package com.crazylegend.kotlinextensions.fragments

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener


/**
 * Created by crazy on 9/15/20 to long live and prosper !
 */

inline fun Fragment.fragmentBooleanResult(resultKey: String, bundleKey: String, crossinline onDenied: () -> Unit = {}, crossinline onGranted: () -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        if (bundle.getBoolean(bundleKey, false)) onGranted() else onDenied()
    }
}

inline fun Fragment.fragmentStringResult(resultKey: String, bundleKey: String, defaultValue: String? = null, crossinline action: String?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getString(bundleKey, defaultValue).action()
    }
}

inline fun Fragment.fragmentBooleanResult(resultKey: String, bundleKey: String, defaultValue: Boolean = false, crossinline action: Boolean.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getBoolean(bundleKey, defaultValue).action()
    }
}

inline fun Fragment.fragmentStringArrayListResult(resultKey: String, bundleKey: String, crossinline action: ArrayList<String>?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getStringArrayList(bundleKey).action()
    }
}

inline fun Fragment.fragmentByteArrayResult(resultKey: String, bundleKey: String, crossinline action: ByteArray?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getByteArray(bundleKey).action()
    }
}

inline fun Fragment.fragmentByteResult(resultKey: String, bundleKey: String, crossinline action: Byte?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getByte(bundleKey).action()
    }
}

inline fun Fragment.fragmentCharResult(resultKey: String, bundleKey: String, crossinline action: Char?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getChar(bundleKey).action()
    }
}

inline fun Fragment.fragmentCharArrayResult(resultKey: String, bundleKey: String, crossinline action: CharArray?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getCharArray(bundleKey).action()
    }
}

inline fun <T : Parcelable> Fragment.fragmentParcelableResult(resultKey: String, bundleKey: String, crossinline action: T?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getParcelable<T>(bundleKey).action()
    }
}

inline fun <T : Parcelable> Fragment.fragmentParcelableListResult(resultKey: String, bundleKey: String, crossinline action: ArrayList<T>?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getParcelableArrayList<T>(bundleKey).action()
    }
}

inline fun Fragment.fragmentDoubleResult(resultKey: String, bundleKey: String, crossinline action: Double?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getDouble(bundleKey).action()
    }
}

inline fun Fragment.fragmentDoubleArrayResult(resultKey: String, bundleKey: String, crossinline action: DoubleArray?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getDoubleArray(bundleKey).action()
    }
}


inline fun Fragment.fragmentFloatResult(resultKey: String, bundleKey: String, crossinline action: Float?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getFloat(bundleKey).action()
    }
}

inline fun Fragment.fragmentFloatArrayResult(resultKey: String, bundleKey: String, crossinline action: FloatArray?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getFloatArray(bundleKey).action()
    }
}


inline fun Fragment.fragmentIntResult(resultKey: String, bundleKey: String, crossinline action: Int?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getInt(bundleKey).action()
    }
}

inline fun Fragment.fragmentIntArrayResult(resultKey: String, bundleKey: String, crossinline action: IntArray?.() -> Unit) {
    setFragmentResultListener(resultKey) { _, bundle ->
        bundle.getIntArray(bundleKey).action()
    }
}