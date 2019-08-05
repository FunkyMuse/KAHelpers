package com.crazylegend.kotlinextensions.misc

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.JELLY_BEAN
import android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
import android.webkit.WebSettings
import android.webkit.WebView
/**
 * Created by hristijan on 8/5/19 to long live and prosper !
 */


object DefaultUserAgent {

    fun getDefaultUserAgent(context: Context): String {
        var ua: String
        when {
            SDK_INT >= JELLY_BEAN_MR1 -> ua = getWebSettingsDefaultUserAgent(context)
            SDK_INT >= JELLY_BEAN -> ua = getUserAgent(context)
            else -> try {
                val constructor = WebSettings::class.java.getDeclaredConstructor(
                        Context::class.java, WebView::class.java
                )
                constructor.isAccessible = true
                try {
                    val settings = constructor.newInstance(context, null)
                    ua = settings.userAgentString
                } finally {
                    constructor.isAccessible = false
                }
            } catch (e: Exception) {
                ua = WebView(context).settings.userAgentString
            }
        }
        return ua
    }

    @SuppressLint("NewApi")
    private fun getWebSettingsDefaultUserAgent(context: Context): String {
        return WebSettings.getDefaultUserAgent(context)
    }

    @SuppressLint("PrivateApi")
    private fun getUserAgent(context: Context): String {
        var userAgent: String
        try {
            @Suppress("UNCHECKED_CAST")
            val clz = Class.forName("android.webkit.WebSettingsClassic") as Class<out WebSettings>
            val constructor = clz.getDeclaredConstructor(
                    Context::class.java, Class.forName("android.webkit.WebViewClassic")
            )
            constructor.isAccessible = true
            try {
                val settings = constructor.newInstance(context, null)
                userAgent = settings.userAgentString
            } finally {
                constructor.isAccessible = false
            }
        } catch (e: Exception) {
            userAgent = WebView(context).settings.userAgentString
        }

        return userAgent
    }
}