package com.crazylegend.kotlinextensions.arguments

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment


/**
 * Created by hristijan on 3/5/19 to long live and prosper !
 */

@Suppress("UNCHECKED_CAST")
fun <T> Activity.argument(key: String) = intent?.extras?.get(key) as T?

fun <T> Activity.lazyArgument(key: String) = lazy { argument<T>(key) }

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.argument(key: String) = arguments?.get(key) as T?

fun <T> Fragment.lazyArgument(key: String) = lazy { argument<T>(key) }

inline fun <reified T : Parcelable> Bundle.putParcelableCollection(key: String, value: Collection<T>) = putParcelableArray(key, value.toTypedArray())

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Parcelable> Bundle.getParcelableMutableList(key: String): MutableList<T> = (getParcelableArray(key) as Array<T>).toMutableList()

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Parcelable> Bundle.getParcelableMutableSet(key: String): MutableSet<T> = (getParcelableArray(key) as Array<T>).toMutableSet()
