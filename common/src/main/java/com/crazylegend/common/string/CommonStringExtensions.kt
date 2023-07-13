package com.crazylegend.common.string

import android.text.Spanned
import androidx.core.text.HtmlCompat

/**
 * Created by funkymuse on 5/26/21 to long live and prosper !
 */

fun String.toHtmlSpan(): Spanned = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)