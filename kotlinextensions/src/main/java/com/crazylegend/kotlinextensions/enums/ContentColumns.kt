package com.crazylegend.kotlinextensions.enums


/**
 * Created by hristijan on 3/1/19 to long live and prosper !
 */


/**
 * Enum to choose between multiple column type
 */
enum class ContentColumns(internal val s: String) {
    /**
     * the size Column
     */
    SIZE ("_size"),
    /**
     * the displayName Column
     */
    DISPLAY_NAME ("_display_name"),
    /**
     * the title Column
     */
    TITLE ("title"),
    /**
     * the date Added Column
     */
    DATE_ADDED ("date_added"),
    /**
     * the date Modified Column
     */
    DATE_MODIFIED ("date_modified"),
    /**
     * the width Column
     */
    WIDTH ("width"),
    /**
     * the Height Column
     */
    HEIGHT ("height")
}