package com.crazylegend.kotlinextensions.retrofit.progressInterceptor


/**
 * Created by hristijan on 3/20/19 to long live and prosper !
 */
interface OnAttachmentDownloadListener {
    fun onAttachmentDownloadedStarted()
    fun onAttachmentDownloadedFinished()
    fun onAttachmentDownloadUpdate(percent: Int) // if the content is known it'll return the correct size otherwise you'll see some weird sizes
}