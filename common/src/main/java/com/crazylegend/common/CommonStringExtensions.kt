package com.crazylegend.common

import android.os.Build
import android.text.Html
import android.text.Spanned

/**
 * Created by funkymuse on 5/26/21 to long live and prosper !
 */

@Suppress("DEPRECATION")
fun String.toHtmlSpan(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
} else {
    Html.fromHtml(this)
}