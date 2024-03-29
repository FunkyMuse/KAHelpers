package dev.funkymuse.common.device

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * https://code.google.com/p/codenameone/issues/detail?id=294
 */
object DefaultUserAgent {

    fun getDefaultUserAgent(context: Context): String {
        var ua: String
        try {
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
        return ua
    }

    @SuppressLint("NewApi")
    fun getWebSettingsDefaultUserAgent(context: Context): String =
        WebSettings.getDefaultUserAgent(context)

    @SuppressLint("PrivateApi")
    fun getUserAgent(context: Context): String {
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
