package com.crazylegend.kotlinextensions.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */

class NotificationsUtil(private val context: Context,
                        private val channelName:String, private val channelID:String,
                        private val channelDescription:String,
                        private val enableCustomSound:Boolean,
                        rawNotificationSound:Int) {

    private var notificationCompatBuilder: NotificationCompat.Builder? = null
    private var notificationManagerCompat: NotificationManagerCompat? = null
    private var soundUri:Uri ? = null

    init {
        if (enableCustomSound){
              soundUri = Uri.parse("android.resource://" + context.packageName + "/" + rawNotificationSound)
        }
    }


    fun createNotification(id: String, message: String, title: String, notificationDrawable:Int) {
        notificationCompatBuilder = NotificationCompat.Builder(context, createNotificationChannel())
        notificationManagerCompat = NotificationManagerCompat.from(context)

       // val pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationCompatBuilder
            ?.setDefaults(Notification.DEFAULT_LIGHTS)
            ?.setSmallIcon(notificationDrawable)
            ?.setContentTitle(title)?.setContentText(message)
            ?.setPriority(NotificationCompat.PRIORITY_HIGH)
            ?.setAutoCancel(true)
            ?.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            ?.setColorized(true)
            ?.setOnlyAlertOnce(true)

         //   ?.setContentIntent(pendingIntent)
        if (enableCustomSound){
            notificationCompatBuilder?.setSound(soundUri)
        }

        notificationCompatBuilder?.build()?.let { notificationManagerCompat?.notify(id.toInt(), it) }


    }

    fun createNotificationWithPendingIntent(id: String, message: String, title: String, notificationDrawable:Int, pendingIntent: PendingIntent) {
        notificationCompatBuilder = NotificationCompat.Builder(context, createNotificationChannel())
        notificationManagerCompat = NotificationManagerCompat.from(context)

       /* val pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT)*/

        notificationCompatBuilder
            ?.setDefaults(Notification.DEFAULT_LIGHTS)
            ?.setSmallIcon(notificationDrawable)
            ?.setContentTitle(title)?.setContentText(message)
            ?.setPriority(NotificationCompat.PRIORITY_HIGH)
            ?.setAutoCancel(true)
            ?.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            ?.setColorized(true)
            ?.setOnlyAlertOnce(true)
            ?.setContentIntent(pendingIntent)

        if (enableCustomSound){
            notificationCompatBuilder?.setSound(soundUri)
        }

        notificationCompatBuilder?.build()?.let { notificationManagerCompat?.notify(id.toInt(), it) }


    }

    fun createExpandableNotification(id: String, message: String, title: String,notificationDrawable:Int) {
        notificationCompatBuilder = NotificationCompat.Builder(context, createNotificationChannel())
        notificationManagerCompat = NotificationManagerCompat.from(context)

        notificationCompatBuilder?.setSmallIcon(notificationDrawable)
            ?.setContentTitle(title)
            ?.setContentText(message)
            ?.setDefaults(Notification.DEFAULT_LIGHTS)
            ?.setPriority(NotificationCompat.PRIORITY_HIGH)
            ?.setAutoCancel(true)
            ?.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            ?.setColorized(true)
            ?.setStyle(NotificationCompat.BigTextStyle().bigText(message))
            ?.setOnlyAlertOnce(true)

        if (enableCustomSound){
            notificationCompatBuilder?.setSound(soundUri)
        }

        notificationCompatBuilder?.build()?.let { notificationManagerCompat?.notify(id.toInt(), it) }

    }

    private fun createNotificationChannel(): String {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = channelName
            val description = channelDescription
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val notificationChannel = NotificationChannel(channelID, name, NotificationManager.IMPORTANCE_HIGH)
            if (enableCustomSound){
                notificationChannel.setSound(soundUri, audioAttributes)
            }
            notificationChannel.description = description
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

        }

        return channelID

    }

}