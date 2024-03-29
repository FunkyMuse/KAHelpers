package dev.funkymuse.kotlinextensions.notifications

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService

/**
 * Removes notification
 * @param[id]: Id of the notification to remove
 */
fun Context.removeNotification(id: Int = 0) {
    getSystemService<NotificationManagerCompat>()?.cancel(id)

}

inline fun Context.setNotification(id: Int = 0, channelName: String = "Default",
                                   builderMethod: NotificationCompat.Builder.() -> Any) {
    val builder = NotificationCompat.Builder(this, channelName)
    builder.apply {
        builderMethod()
    }
    getSystemService<NotificationManagerCompat>()?.notify(id, builder.build())
}
