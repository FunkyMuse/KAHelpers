package com.crazylegend.kotlinextensions.cursor

import android.database.Cursor


/**
 * Created by hristijan on 3/20/19 to long live and prosper !
 */


inline fun Cursor.getInt(columnName: String): Int {
    return this.getInt(this.getColumnIndex(columnName))
}

inline fun Cursor.getLong(columnName: String): Long {
    return this.getLong(this.getColumnIndex(columnName))
}

inline fun Cursor.getString(columnName: String): String {
    return this.getString(this.getColumnIndex(columnName))
}

inline fun Cursor.getIntOrNull(columnName: String): Int? {
    return this.getInt(this.getColumnIndex(columnName))
}

inline fun Cursor.getLongOrNull(columnName: String): Long? {
    return this.getLong(this.getColumnIndex(columnName))
}

inline fun Cursor.getStringOrNull(columnName: String): String? {
    return this.getString(this.getColumnIndex(columnName))
}

