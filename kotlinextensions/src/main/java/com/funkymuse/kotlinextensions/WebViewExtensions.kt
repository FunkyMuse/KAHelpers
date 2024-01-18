package com.funkymuse.kotlinextensions

import android.webkit.WebView




/**
 * Loads html into a webview styled by a stylesheet from assets. Example
 * AMUtil.loadStyledHTML(webview, "style.css", "&lt;p&gt;Example&lt;/p&gt;");
 * @param stylesheetName Name of the stylesheet
 * @param htmlBody The html string to put in the &lt;body&gt;
 */
fun WebView.loadHTMLWithStylesheet(stylesheetName: String, htmlBody: String) {
    val body = "<html><head><link rel=\"stylesheet\" href=\"file:///android_asset/$stylesheetName\" type=\"text/css\"><meta name='viewport'></head><body>$htmlBody</body></html>"

    loadDataWithBaseURL("x-data://base", body, "text/html", "UTF-8", null)
}

/**
 * Loads html into a webview with custom css styling. Example
 * AMUtil.loadStyledHTML(webview, "{p {text-align: center;color: red;}}", "&lt;p&gt;Example&lt;/p&gt;");
 * @param style Css contents
 * @param htmlBody The html string to put in the &lt;body&gt;
 */
fun WebView.loadStyledHTML(style: String, htmlBody: String) {
    val body = "<html><head><style>$style</style><meta name='viewport'></head><body>$htmlBody</body></html>"

    loadDataWithBaseURL("x-data://base", body, "text/html", "UTF-8", null)
}

fun WebView.loadHtml(text: String) {
    loadData(text, "text/html", "UTF-8")
}