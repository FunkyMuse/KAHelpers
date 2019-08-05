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


/**
 * Same as [Cursor.getShort], but returns [defaultValue] when the column
 * value is <code>NULL</code>.
 *
 * @param columnIndex zero-based index of the column.
 *
 * @return Contents of the column, or [defaultValue] if [Cursor.isNull]
 * returns true for the specified column.
 */
fun Cursor.getNullableShort(columnIndex: Int,
                            defaultValue: Short? = null): Short? =
        if (!isNull(columnIndex)) getShort(columnIndex) else defaultValue

/**
 * Same as [Cursor.getInt], but returns [defaultValue] when the column
 * value is <code>NULL</code>.
 *
 * @param columnIndex zero-based index of the column.
 *
 * @return Contents of the column, or [defaultValue] if [Cursor.isNull]
 * returns true for the specified column.
 */
fun Cursor.getNullableInt(columnIndex: Int,
                          defaultValue: Int? = null): Int? =
        if (!isNull(columnIndex)) getInt(columnIndex) else defaultValue

/**
 * Same as [Cursor.getLong], but returns [defaultValue] when the column
 * value is <code>NULL</code>.
 *
 * @param columnIndex zero-based index of the column.
 *
 * @return Contents of the column, or [defaultValue] if [Cursor.isNull]
 * returns true for the specified column.
 */
fun Cursor.getNullableLong(columnIndex: Int,
                           defaultValue: Long? = null): Long? =
        if (!isNull(columnIndex)) getLong(columnIndex) else defaultValue

/**
 * Same as [Cursor.getFloat], but returns [defaultValue] when the column
 * value is <code>NULL</code>.
 *
 * @param columnIndex zero-based index of the column.
 *
 * @return Contents of the column, or [defaultValue] if [Cursor.isNull]
 * returns true for the specified column.
 */
fun Cursor.getNullableFloat(columnIndex: Int,
                            defaultValue: Float? = null): Float? =
        if (!isNull(columnIndex)) getFloat(columnIndex) else defaultValue

/**
 * Same as [Cursor.getDouble], but returns [defaultValue] when the column
 * value is <code>NULL</code>.
 *
 * @param columnIndex zero-based index of the column.
 *
 * @return Contents of the column, or [defaultValue] if [Cursor.isNull]
 * returns true for the specified column.
 */
fun Cursor.getNullableDouble(columnIndex: Int,
                             defaultValue: Double? = null): Double? =
        if (!isNull(columnIndex)) getDouble(columnIndex) else defaultValue

/**
 * Generic version of [Cursor.getString], [Cursor.getNullableShort],
 * [Cursor.getNullableInt], [Cursor.getNullableLong], [Cursor.getNullableFloat],
 * [Cursor.getNullableDouble], and [Cursor.getBlob].
 *
 * [T] must be one of [String], [Short], [Int], [Long], [Float], [Double], or
 * [ByteArray]) - method will throw an [IllegalArgumentException] for any other
 * type.
 *
 * @param columnIndex zero-based index of the column.
 *
 * @return Contents of the column, or null if [Cursor.isNull] returns true
 * for the specified column.
 */
inline operator fun <reified T> Cursor.get(columnIndex: Int): T? {
    val klass = T::class.java
    return when (klass) {
        java.lang.String::class.java -> getString(columnIndex)
        java.lang.Short::class.java -> getNullableShort(columnIndex)
        java.lang.Integer::class.java -> getNullableInt(columnIndex)
        java.lang.Long::class.java -> getNullableLong(columnIndex)
        java.lang.Float::class.java -> getNullableFloat(columnIndex)
        java.lang.Double::class.java -> getNullableDouble(columnIndex)
        else -> when (klass.isArray) {
            klass.componentType == java.lang.Byte::class.java -> getBlob(columnIndex)
            else -> throw IllegalArgumentException("Unrecognized type: ${T::class.java.canonicalName}")
        }
    } as T?
}
