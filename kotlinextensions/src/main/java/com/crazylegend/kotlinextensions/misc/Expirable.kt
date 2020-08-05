package com.crazylegend.kotlinextensions.misc

import java.util.*


/**
 * Created by crazy on 8/5/20 to long live and prosper !
 */
class Expirable<T>(private val variable: T, private val expirationDate: Date) {

    /**
     * Pass a variable with expiration time in milliseconds in the future
     * @param variable T
     * @param duration Long
     * @constructor
     */
    constructor(variable: T, duration: Long) : this(variable, Date(Date().time + duration))

    val value get() = if (hasExpired) null else variable

    private val hasExpired get() = expirationDate < Date()
}