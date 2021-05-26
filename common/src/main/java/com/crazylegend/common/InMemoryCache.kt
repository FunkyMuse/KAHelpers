package com.crazylegend.common


/**
 * Created by hristijan on 3/1/19 to long live and prosper !
 */


/**
 * Class for an InMemory Cache to keep your variables globally in heap and get them wherever you want.
 */
object InMemoryCache {
    private val map = HashMap<String, Any?>()

    /**
     * put [key] & [value] where
     *
     * @param key is the String to get the value from anywhere, If you have the key then you can get the value. So keep it safe.
     *
     */
    fun put(key: String, value: Any?): InMemoryCache {
        map[key] = value
        return this
    }

    /**
     * get the saved value addressed by the key
     */
    fun get(key: String): Any? = map[key]

    /**
     * check if have the value on the Given Key
     */
    fun have(key: String) = map.containsKey(key)

    /**
     * check if have the value on the Given Key
     */
    fun contains(key: String) = have(key)

    /**
     * Clear all the InMemoryCache
     */
    fun clear() = map.clear()

    /**
     * get All The InMemoryCache
     */
    fun getAll() = map.toMap()

    /**
     * get All the InMemoryCache of an Specific Type.
     */
    fun getAllByType(clazz: Class<*>) = getAll().filter {
        val classValue = it.value
        classValue != null && classValue::class.java == clazz
    }
}


/**
 * get Saved Data from memory, null if it os not exists
 */
fun <T> getFromMemory(key: String): T? = InMemoryCache.get(key) as? T


/**
 * put Something In Memory to use it later
 */
fun putInMemory(key: String, any: Any?) = InMemoryCache.put(key, any)



