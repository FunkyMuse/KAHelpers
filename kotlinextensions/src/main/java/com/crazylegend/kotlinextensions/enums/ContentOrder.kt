package com.crazylegend.kotlinextensions.enums


/**
 * Created by hristijan on 3/1/19 to long live and prosper !
 */

/**
 * Enum to choose between Ascending or Descending order
 */
enum class ContentOrder(internal val s: String) {
    /**
     * sort by DESC Order
     */
    DESCENDING("DESC"),
    /**
     * Sort by ASC Order
     */
    ASCENDING("ASC")

}