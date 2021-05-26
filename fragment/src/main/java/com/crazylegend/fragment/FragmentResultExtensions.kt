package com.crazylegend.fragment

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener


/**
 * Created by crazy on 9/15/20 to long live and prosper !
 */

inline fun Fragment.fragmentBooleanResult(requestKey: String, bundleKey: String, crossinline onDenied: () -> Unit = {}, crossinline onGranted: () -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        if (bundle.getBoolean(bundleKey, false)) onGranted() else onDenied()
    }
}

inline fun Fragment.fragmentStringResult(requestKey: String, bundleKey: String, defaultValue: String? = null, crossinline action: String?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getString(bundleKey, defaultValue).action()
    }
}

inline fun Fragment.fragmentBooleanResult(requestKey: String, bundleKey: String, defaultValue: Boolean = false, crossinline action: Boolean.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getBoolean(bundleKey, defaultValue).action()
    }
}

inline fun Fragment.fragmentStringArrayListResult(requestKey: String, bundleKey: String, crossinline action: ArrayList<String>?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getStringArrayList(bundleKey).action()
    }
}

inline fun Fragment.fragmentByteArrayResult(requestKey: String, bundleKey: String, crossinline action: ByteArray?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getByteArray(bundleKey).action()
    }
}

inline fun Fragment.fragmentByteResult(requestKey: String, bundleKey: String, crossinline action: Byte?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getByte(bundleKey).action()
    }
}

inline fun Fragment.fragmentCharResult(requestKey: String, bundleKey: String, crossinline action: Char?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getChar(bundleKey).action()
    }
}

inline fun Fragment.fragmentCharArrayResult(requestKey: String, bundleKey: String, crossinline action: CharArray?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getCharArray(bundleKey).action()
    }
}

inline fun <T : Parcelable> Fragment.fragmentParcelableResult(requestKey: String, bundleKey: String, crossinline action: T?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getParcelable<T>(bundleKey).action()
    }
}

inline fun <T : Parcelable> Fragment.fragmentParcelableListResult(requestKey: String, bundleKey: String, crossinline action: ArrayList<T>?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getParcelableArrayList<T>(bundleKey).action()
    }
}

inline fun Fragment.fragmentDoubleResult(requestKey: String, bundleKey: String, crossinline action: Double?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getDouble(bundleKey).action()
    }
}

inline fun Fragment.fragmentDoubleArrayResult(requestKey: String, bundleKey: String, crossinline action: DoubleArray?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getDoubleArray(bundleKey).action()
    }
}


inline fun Fragment.fragmentFloatResult(requestKey: String, bundleKey: String, crossinline action: Float?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getFloat(bundleKey).action()
    }
}

inline fun Fragment.fragmentFloatArrayResult(requestKey: String, bundleKey: String, crossinline action: FloatArray?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getFloatArray(bundleKey).action()
    }
}


inline fun Fragment.fragmentIntResult(requestKey: String, bundleKey: String, crossinline action: Int?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getInt(bundleKey).action()
    }
}

inline fun Fragment.fragmentIntArrayResult(requestKey: String, bundleKey: String, crossinline action: IntArray?.() -> Unit) {
    setFragmentResultListener(requestKey) { _, bundle ->
        bundle.getIntArray(bundleKey).action()
    }
}