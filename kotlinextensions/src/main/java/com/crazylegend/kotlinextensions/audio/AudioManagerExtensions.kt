package com.crazylegend.kotlinextensions.audio

import android.media.AudioManager
import android.view.KeyEvent


/**
 * Created by hristijan on 3/20/19 to long live and prosper !
 */

fun AudioManager.dispatchEvent(keycode: Int){
    dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keycode))
    dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keycode))
}