package com.crazylegend.kotlinextensions.cursor

import android.database.Cursor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


/**
 * Created by hristijan on 3/20/19 to long live and prosper !
 */

fun <T : Any> Cursor.mapToList(cursorPredicate: (Cursor) -> T): List<T> = generateSequence {
    if (moveToNext()) cursorPredicate(this) else null
}.toList()


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


/**
 * Create a [Iterable] that returns all the data from the [Cursor].
 *
 * Each element in the iterator represents one row from the cursor as a [Map]. The key is the column
 * name, and the value is the value of the column.
 */
fun Cursor.asIterable(): Iterable<Map<String, Any?>> = Iterable {
    object : Iterator<Map<String, Any?>> {
        override fun hasNext(): Boolean = position < count - 1

        override fun next(): Map<String, Any?> {
            moveToNext()
            return getRow()
        }
    }
}

/**
 * Create a [Sequence] that returns all the data from the [Cursor].
 *
 * Each element in the sequence represents one row from the cursor as a [Map]. The key is the column
 * name, and the value is the value of the column.
 */
fun Cursor.asSequence(): Sequence<Map<String, Any?>> = generateSequence {
    if (moveToNext()) getRow() else null
}

/**
 * Create a [Flow] that returns all the data from the [Cursor].
 *
 * Each element in the flow represents one row from the cursor as a [Map]. The key is the column
 * name, and the value is the value of the column.
 */
fun Cursor.asFlow(): Flow<Map<String, Any?>> = flow { while (moveToNext()) emit(getRow()) }

private fun Cursor.getRow(): Map<String, Any?> = hashMapOf<String, Any?>().apply {
    (0 until columnCount).forEach { index ->
        put(
                getColumnName(index),
                when (getType(index)) {
                    Cursor.FIELD_TYPE_BLOB -> getBlob(index)
                    Cursor.FIELD_TYPE_FLOAT -> getDouble(index)
                    Cursor.FIELD_TYPE_INTEGER -> getLong(index)
                    Cursor.FIELD_TYPE_NULL -> null
                    Cursor.FIELD_TYPE_STRING -> getString(index)
                    else -> null
                }
        )
    }
}