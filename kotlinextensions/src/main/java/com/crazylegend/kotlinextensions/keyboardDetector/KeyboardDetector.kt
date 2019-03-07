package com.crazylegend.kotlinextensions.keyboardDetector

import android.app.Activity
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import io.reactivex.Observable

/**
 * USAGE
  KeyboardDetector(this@Activity).observe().subscribe({ status ->
 when(status) {
 KeyboardStatus.KEYBOARD_OPEN -> {}
 KeyboardStatus.KEYBOARD_CLOSED -> {}
}
})
 */

/**
 * Created by hristijan on 3/7/19 to long live and prosper !
 */
class KeyboardDetector(private val activity: Activity?) {

    companion object {
        const val TAG = "KeyboardDetector"
        const val MIN_HEIGHT_RATIO = 0.15
    }

    fun observe(): Observable<KeyboardStatus> {
        if (activity == null) {
            return Observable.just(KeyboardStatus.KEYBOARD_CLOSED)
        }

        val rootView = (activity.findViewById<View>(android.R.id.content) as ViewGroup?)

        val windowHeight = DisplayMetrics().let {
            activity.windowManager.defaultDisplay.getMetrics(it)
            it.heightPixels
        }

        return Observable.create<KeyboardStatus> { emitter ->
            val listener = ViewTreeObserver.OnGlobalLayoutListener {
                if (rootView == null) {
                    emitter.onNext(KeyboardStatus.KEYBOARD_CLOSED)
                    return@OnGlobalLayoutListener
                }

                val rect = Rect().apply { rootView.getWindowVisibleDisplayFrame(this) }
                val keyboardHeight = windowHeight - rect.height()

                if (keyboardHeight > windowHeight * MIN_HEIGHT_RATIO) {
                    emitter.onNext(KeyboardStatus.KEYBOARD_OPEN)
                } else {
                    emitter.onNext(KeyboardStatus.KEYBOARD_OPEN)
                }
            }

            rootView?.let {
                it.viewTreeObserver.addOnGlobalLayoutListener(listener)

                emitter.setCancellable {
                    it.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        }.distinctUntilChanged()

    }

}