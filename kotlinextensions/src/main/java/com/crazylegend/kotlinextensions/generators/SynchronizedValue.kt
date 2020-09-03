package com.crazylegend.kotlinextensions.generators


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


class SynchronizedValue<T> {

    private var value: T? = null
    private val lock = Any()

    fun set(data: T) {
        synchronized(lock) {
            value = data
        }
    }

    fun get(): T? {
        synchronized(lock) {
            return value
        }
    }

    override fun toString(): String {
        return "SynchronizedValue{" +
                "value=" + get() +
                '}'.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as SynchronizedValue<*>?
        if (if (value != null) value != that!!.value else that!!.value != null) return false
        return lock == that.lock

    }

    override fun hashCode(): Int {
        var result = value?.hashCode() ?: 0
        result = 31 * result + lock.hashCode()
        return result
    }


}
