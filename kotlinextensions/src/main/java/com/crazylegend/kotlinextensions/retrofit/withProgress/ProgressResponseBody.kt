package com.crazylegend.kotlinextensions.retrofit.withProgress

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException


/**
 * Created by hristijan on 3/20/19 to long live and prosper !
 */

class ProgressResponseBody(
    private  val responseBody: ResponseBody,
    private val progressListener: OnAttachmentDownloadListener?
) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        progressListener?.onAttachmentDownloadedStarted()


        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)

                totalBytesRead += if (bytesRead != -1L) bytesRead else 0


                val percent =
                    if (bytesRead == -1L) 100f
                    else
                        totalBytesRead.toFloat() / responseBody.contentLength().toFloat() * 100


                progressListener?.onAttachmentDownloadUpdate(percent.toInt())

                if (percent.toInt() == 100) {
                    progressListener?.onAttachmentDownloadedFinished()
                }

                return bytesRead
            }
        }
    }
}