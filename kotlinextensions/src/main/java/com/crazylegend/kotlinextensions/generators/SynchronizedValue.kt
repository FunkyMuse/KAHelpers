package com.crazylegend.kotlinextensions.generators


/**
 * Created by hristijan on 3/29/19 to long live and prosper !
 */


class SynchronizedValue<T> {

    private var t: T? = null
    private val lock = Any()

    fun set(t: T) {
        synchronized(lock) {
            this.t = t
        }
    }

    fun get(): T? {
        synchronized(lock) {
            return t
        }
    }

    override fun toString(): String {
        return "SynchronizedValue{" +
                "t=" + get() +
                '}'.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val that = other as SynchronizedValue<*>?

        if (if (t != null) t != that!!.t else that!!.t != null) return false
        return lock == that.lock

    }

    override fun hashCode(): Int {
        var result = if (t != null) t!!.hashCode() else 0
        result = 31 * result + lock.hashCode()
        return result
    }


}
