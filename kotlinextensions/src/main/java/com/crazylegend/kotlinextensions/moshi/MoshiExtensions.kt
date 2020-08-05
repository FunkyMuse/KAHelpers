package com.crazylegend.kotlinextensions.moshi

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


/**
 * Created by crazy on 2/25/20 to long live and prosper !
 */


inline fun <reified T : Any> T.toJson(customBuilder: Moshi.Builder = Moshi.Builder()): String = customBuilder
        .add(KotlinJsonAdapterFactory()).build()
        .adapter<T>(T::class.java).toJson(this)


inline fun <reified T : Any> String.fromJson(customBuilder: Moshi.Builder = Moshi.Builder()): T? = customBuilder
        .add(KotlinJsonAdapterFactory()).build()
        .adapter(T::class.java).fromJson(this)


inline fun <reified T : Any> String.toJsonObjectList(customBuilder: Moshi.Builder = Moshi.Builder()): List<T>? {
    return Types.newParameterizedType(List::class.java, T::class.java).let { type ->
        customBuilder
                .add(KotlinJsonAdapterFactory()).build()
                .adapter<List<T>>(type).fromJson(this)
    }
}


inline fun <reified T : Any> String.toJsonObjectArrayList(customBuilder: Moshi.Builder = Moshi.Builder()): ArrayList<T>? {
    return Types.newParameterizedType(ArrayList::class.java, T::class.java).let { type ->
        customBuilder
                .add(KotlinJsonAdapterFactory()).build()
                .adapter<ArrayList<T>>(type).fromJson(this)
    }
}


inline fun <reified T : Any> String.toJsonObjectMutableList(customBuilder: Moshi.Builder = Moshi.Builder()): MutableList<T>? {
    return Types.newParameterizedType(MutableList::class.java, T::class.java).let { type ->
        customBuilder
                .add(KotlinJsonAdapterFactory()).build()
                .adapter<MutableList<T>>(type).fromJson(this)
    }
}


