package com.funkymuse.common.string

import android.text.Spanned
import androidx.core.text.HtmlCompat

fun String.toHtmlSpan(): Spanned = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)