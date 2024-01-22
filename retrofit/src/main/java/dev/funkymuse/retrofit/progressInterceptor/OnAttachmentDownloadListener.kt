package dev.funkymuse.retrofit.progressInterceptor



interface OnAttachmentDownloadListener {
    fun onAttachmentDownloadedStarted()
    fun onAttachmentDownloadedFinished()
    fun onAttachmentDownloadUpdate(percent: Int) // if the content is known it'll return the correct size otherwise you'll see some weird sizes
}

inline fun onAttachmentDownloadListenerDSL(
        crossinline downloadStarted: () -> Unit = {},
        crossinline downloadFinished: () -> Unit = {},
        crossinline progress: (percentage: Int) -> Unit = {}) = object : OnAttachmentDownloadListener {
    override fun onAttachmentDownloadedStarted() {
        downloadStarted()
    }

    override fun onAttachmentDownloadedFinished() {
        downloadFinished()
    }

    override fun onAttachmentDownloadUpdate(percent: Int) {
        progress(percent)
    }
}