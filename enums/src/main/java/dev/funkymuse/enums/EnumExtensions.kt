package dev.funkymuse.enums




inline fun <reified T : Enum<T>> find(predicate: (item: T) -> Boolean): T? = enumValues<T>().find { predicate(it) }

inline fun <reified T : Enum<T>> first(predicate: (item: T) -> Boolean): T = enumValues<T>().first { predicate(it) }

inline fun <reified T : Enum<T>> convert(ord: Int): T = enumValues<T>()[ord]

/**
 * Returns an enum entry with specified name.
 */
inline fun <reified E : Enum<E>> enumValueOfOrNull(name: String): E? {
    return try {
        enumValueOf<E>(name)
    } catch (e: Throwable) {
        null
    }
}