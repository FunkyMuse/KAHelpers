package com.crazylegend.kotlinextensions.cursor

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */


/**
 * Slightly more concise version of [SQLiteDatabase.query] that allows omission
 * of most fields by use of named parameters.
 *
 * See [SQLiteDatabase.query] for more information.
 *
 * @param table Table referenced in the query.
 * @param columns List of columns to return (null to return all - not recommended).
 * @param selection Selection query - contents of WHERE clause - excluding WHERE itself.
 * @param selArgs A string to replace for each '?' found in [selection].
 * @param groupBy GROUP BY clause - excluding the 'GROUP BY'. May be null
 * to disable grouping.
 * @param having HAVING clause, excluding the 'HAVING'. May be null.
 * @param orderBy ORDER BY clause, excluding the 'ORDER BY'. May be null to
 * return in default order.
 * @param limit LIMIT clause, excluding 'LIMIT' itself. Pass null to disable
 * limit.
 *
 * @return [Cursor] object, positioned before the first query.
 */
fun SQLiteDatabase.optQuery(table: String, columns: Array<String>? = null,
                            selection: String? = null, selArgs: Array<String>? = null,
                            groupBy: String? = null, having: String? = null,
                            orderBy: String? = null, limit: String? = null): Cursor {
    return query(table, columns, selection, selArgs, groupBy, having, orderBy, limit)
}

/**
 * Queries for a single column value in a single row. Automatically sets a
 * LIMIT of 1 for any query submitted
 *
 * @param table Table referenced in the query.
 * @param column Name of column to query.
 * @param selection Selection query - contents of WHERE clause - excluding WHERE itself.
 * @param selArgs A string to replace for each '?' found in [selection].
 * @param groupBy GROUP BY clause - excluding the 'GROUP BY'. May be null
 * to disable grouping.
 * @param having HAVING clause, excluding the 'HAVING'. May be null.
 * @param orderBy ORDER BY clause, excluding the 'ORDER BY'. May be null to
 * return in default order.
 *
 * @return Value of the column in the first row of the resulting query.
 */
inline fun <reified T> SQLiteDatabase.queryValue(table: String, column: String,
                                                 selection: String? = null,
                                                 selArgs: Array<String>? = null,
                                                 groupBy: String? = null,
                                                 having: String? = null,
                                                 orderBy: String? = null,
                                                 defaultValue: T? = null): T? =
        optQuery(table, arrayOf(column), selection, selArgs, groupBy, having, orderBy, "1").use {
            if (it.moveToNext()) it.get<T>(0) else null
        } ?: defaultValue