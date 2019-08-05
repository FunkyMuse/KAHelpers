package com.crazylegend.kotlinextensions.annotations

import androidx.annotation.IntDef


import androidx.fragment.app.FragmentTransaction.*

/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */


@IntDef(TRANSIT_NONE, TRANSIT_FRAGMENT_OPEN, TRANSIT_FRAGMENT_CLOSE, TRANSIT_FRAGMENT_FADE)
@Retention(AnnotationRetention.SOURCE)
annotation class FragmentTransit