package com.crazylegend.fragment

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener


/**
 * Created by crazy on 9/15/20 to long live and prosper !
 */

inline fun Fragment.fragmentBooleanResult(
        requestKey: String,
        bundleKey: String,
        crossinline onDenied: () -> Unit = {},
        crossinline onGranted: () -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        if (bundle.getBoolean(bundleKey, false)) onGranted() else onDenied()
    }
}

inline fun Fragment.fragmentStringResult(
        requestKey: String,
        bundleKey: String,
        defaultValue: String? = null,
        crossinline action: (String?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getString(bundleKey, defaultValue))
    }
}

inline fun Fragment.fragmentBooleanResult(
        requestKey: String,
        bundleKey: String,
        defaultValue: Boolean = false,
        crossinline action: (predicate: Boolean) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getBoolean(bundleKey, defaultValue))
    }
}

inline fun Fragment.fragmentStringArrayListResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (list: ArrayList<String>?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getStringArrayList(bundleKey))
    }
}

inline fun Fragment.fragmentByteArrayResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (array: ByteArray?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getByteArray(bundleKey))
    }
}

inline fun Fragment.fragmentByteResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (byte: Byte?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getByte(bundleKey))
    }
}

inline fun Fragment.fragmentCharResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (char: Char?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getChar(bundleKey))
    }
}

inline fun Fragment.fragmentCharArrayResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (charArray: CharArray?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getCharArray(bundleKey))
    }
}

inline fun <T : Parcelable> Fragment.fragmentParcelableResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (T?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getParcelable(bundleKey))
    }
}

inline fun <T : Parcelable> Fragment.fragmentParcelableListResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (ArrayList<T>?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getParcelableArrayList(bundleKey))
    }
}

inline fun Fragment.fragmentDoubleResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (double: Double?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getDouble(bundleKey))
    }
}

inline fun Fragment.fragmentDoubleArrayResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (DoubleArray?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getDoubleArray(bundleKey))
    }
}


inline fun Fragment.fragmentFloatResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (Float?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getFloat(bundleKey))
    }
}

inline fun Fragment.fragmentFloatArrayResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (FloatArray?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getFloatArray(bundleKey))
    }
}


inline fun Fragment.fragmentIntResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (Int?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getInt(bundleKey))
    }
}

inline fun Fragment.fragmentIntArrayResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (IntArray?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle.getIntArray(bundleKey))
    }
}

inline fun <T : Any?> Fragment.fragmentResult(
        requestKey: String,
        bundleKey: String,
        crossinline action: (T?) -> Unit
) {
    setFragmentResultListener(requestKey) { _, bundle ->
        action(bundle[bundleKey] as? T)
    }
}