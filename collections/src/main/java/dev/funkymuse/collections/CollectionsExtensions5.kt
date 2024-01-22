package dev.funkymuse.collections
import kotlin.collections.associateBy


fun <FIRST_LIST_TYPE, SECOND_LIST_TYPE, FIRST_LIST_TRANSFORMER, SECOND_LIST_TRANSFORMER, RETURN_TYPE>
        Iterable<FIRST_LIST_TYPE>.zipBy(
    other: Iterable<SECOND_LIST_TYPE>,
    firstListTransformer: (FIRST_LIST_TYPE) -> FIRST_LIST_TRANSFORMER,
    secondListTransformer: (SECOND_LIST_TYPE) -> SECOND_LIST_TRANSFORMER,
    pairPredicate: (first: FIRST_LIST_TYPE?, second: SECOND_LIST_TYPE?) -> RETURN_TYPE?
): List<RETURN_TYPE> {
    val firstByKey: Map<FIRST_LIST_TRANSFORMER, FIRST_LIST_TYPE> = associateBy {
        firstListTransformer(it)
    }
    val secondByKey: Map<SECOND_LIST_TRANSFORMER, SECOND_LIST_TYPE> = other.associateBy {
        secondListTransformer(it)
    }
    val resultList = (firstByKey.keys + secondByKey.keys).map { (firstByKey[it]) to (secondByKey[it]) }
    return resultList.asSequence().mapNotNull {
        val firstPair = it.first
        val secondPair = it.second
        pairPredicate(firstPair, secondPair)
    }.toList()
}