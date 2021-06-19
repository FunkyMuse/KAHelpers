package com.crazylegend.kotlinextensions.viewmodel

import android.content.Context
import androidx.lifecycle.AndroidViewModel

/**
 * Created by funkymuse on 6/19/21 to long live and prosper !
 */


/**
 * Gets the application context from within the android viewmodel
 */
val AndroidViewModel.context: Context
    get() = getApplication()