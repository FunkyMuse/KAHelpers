package com.crazylegend.kotlinextensions

import android.widget.ArrayAdapter


/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */

operator fun <T> ArrayAdapter<T>.get(position: Int): T? = getItem(position)
