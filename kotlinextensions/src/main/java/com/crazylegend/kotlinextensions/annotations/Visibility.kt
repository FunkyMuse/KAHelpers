package com.crazylegend.kotlinextensions.annotations
import android.view.View.*
import androidx.annotation.IntDef

/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */

@IntDef(VISIBLE, INVISIBLE, GONE)
@Retention(AnnotationRetention.SOURCE)
annotation class Visibility