package com.crazylegend.toaster

import androidx.annotation.StringRes

/**
 * Created by funkymuse on 3/8/21 to long live and prosper !
 */
interface ToasterContract {

    fun shortToast(@StringRes string: Int)

    fun shortToast(string: String)

    fun longToast(@StringRes string: Int)

    fun longToast(string: String)

}