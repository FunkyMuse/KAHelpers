package com.crazylegend.kotlinextensions.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import com.crazylegend.kotlinextensions.context.notificationManager


/**
 * Created by hristijan on 3/7/19 to long live and prosper !
 */

/**
 * Removes notification
 * @param[id]: Id of the notification to remove
 */
fun Context.removeNotification(id: Int = 0) {
    notificationManager?.cancel(id)

}

inline fun Context.setNotification(id: Int = 0, channelName: String = "Default",
                                   builderMethod: NotificationCompat.Builder.() -> Any) {
    val builder = NotificationCompat.Builder(this, channelName)
    builder.apply {
        builderMethod()
    }
    notificationManager?.notify(id, builder.build())
}
