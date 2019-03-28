package com.crazylegend.kotlinextensions.pdf

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresPermission
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * Created by hristijan on 3/28/19 to long live and prosper !
 */


@RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
fun View.createPDF(mediaDir:String, uriAuthority:String): Uri? {

    val file: File?
    val uuid = UUID.randomUUID().toString()
    val directory = File(Environment.getExternalStorageDirectory(), mediaDir)

    file = if (directory.exists()) {
        File(directory, uuid.plus(".pdf"))
    } else {
        directory.mkdirs()

        File(directory, uuid.plus(".pdf"))

    }
    file.createNewFile()

    val fos = FileOutputStream(file)


    this.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    this.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

    this.layout(0, 0, this.measuredWidth, this.measuredHeight)

    val bitmap = Bitmap.createBitmap(this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    paint.style = Paint.Style.FILL
    paint.color = Color.WHITE
    canvas.drawPaint(paint)

    this.draw(canvas)

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 0).create()
    val page = pdfDocument.startPage(pageInfo)

    val pageCanvas = page.canvas
    val paintPage = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    paintPage.style = Paint.Style.FILL
    paintPage.color = Color.WHITE
    pageCanvas.drawPaint(paintPage)
    pageCanvas.drawBitmap(bitmap, 0f, 0f, paintPage)

    pdfDocument.finishPage(page)
    pdfDocument.writeTo(fos)
    pdfDocument.close()
    fos.flush()
    fos.close()

    return FileProvider.getUriForFile(this.context, uriAuthority, file)

}

@RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
fun View.createPDF(pdfNameWithoutDotPDF:String, mediaDir:String, uriAuthority:String): Uri? {

    val file: File?
    val directory = File(Environment.getExternalStorageDirectory(), mediaDir)

    file = if (directory.exists()) {
        File(directory, pdfNameWithoutDotPDF.plus(".pdf"))
    } else {
        directory.mkdirs()

        File(directory, pdfNameWithoutDotPDF.plus(".pdf"))

    }
    file.createNewFile()

    val fos = FileOutputStream(file)


    this.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    this.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

    this.layout(0, 0, this.measuredWidth, this.measuredHeight)

    val bitmap = Bitmap.createBitmap(this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    paint.style = Paint.Style.FILL
    paint.color = Color.WHITE
    canvas.drawPaint(paint)

    this.draw(canvas)

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 0).create()
    val page = pdfDocument.startPage(pageInfo)

    val pageCanvas = page.canvas
    val paintPage = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    paintPage.style = Paint.Style.FILL
    paintPage.color = Color.WHITE
    pageCanvas.drawPaint(paintPage)
    pageCanvas.drawBitmap(bitmap, 0f, 0f, paintPage)

    pdfDocument.finishPage(page)
    pdfDocument.writeTo(fos)
    pdfDocument.close()
    fos.flush()
    fos.close()

    return FileProvider.getUriForFile(this.context, uriAuthority, file)
}

@RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
fun View.createPDF(pdfNameWithoutDotPDF:String, pageNumber:Int, mediaDir:String, uriAuthority:String): Uri? {

    val file: File?
    val directory = File(Environment.getExternalStorageDirectory(), mediaDir)

    file = if (directory.exists()) {
        File(directory, pdfNameWithoutDotPDF.plus(".pdf"))
    } else {
        directory.mkdirs()

        File(directory, pdfNameWithoutDotPDF.plus(".pdf"))

    }
    file.createNewFile()

    val fos = FileOutputStream(file)


    this.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    this.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

    this.layout(0, 0, this.measuredWidth, this.measuredHeight)

    val bitmap = Bitmap.createBitmap(this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    paint.style = Paint.Style.FILL
    paint.color = Color.WHITE
    canvas.drawPaint(paint)

    this.draw(canvas)

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, pageNumber).create()
    val page = pdfDocument.startPage(pageInfo)

    val pageCanvas = page.canvas
    val paintPage = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    paintPage.style = Paint.Style.FILL
    paintPage.color = Color.WHITE
    pageCanvas.drawPaint(paintPage)
    pageCanvas.drawBitmap(bitmap, 0f, 0f, paintPage)

    pdfDocument.finishPage(page)
    pdfDocument.writeTo(fos)
    pdfDocument.close()
    fos.flush()
    fos.close()

    return FileProvider.getUriForFile(this.context, uriAuthority, file)
}

@RequiresPermission(allOf = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
fun View.createPDF(pdfNameWithoutDotPDF:String, pageNumber:Int, mediaDir:String, uriAuthority:String, background :Int = Color.WHITE): Uri? {

    val file: File?
    val directory = File(Environment.getExternalStorageDirectory(), mediaDir)

    file = if (directory.exists()) {
        File(directory, pdfNameWithoutDotPDF.plus(".pdf"))
    } else {
        directory.mkdirs()

        File(directory, pdfNameWithoutDotPDF.plus(".pdf"))

    }
    file.createNewFile()

    val fos = FileOutputStream(file)


    this.layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    this.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))

    this.layout(0, 0, this.measuredWidth, this.measuredHeight)

    val bitmap = Bitmap.createBitmap(this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    paint.style = Paint.Style.FILL
    paint.color = background
    canvas.drawPaint(paint)

    this.draw(canvas)

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, pageNumber).create()
    val page = pdfDocument.startPage(pageInfo)

    val pageCanvas = page.canvas
    val paintPage = Paint(Paint.ANTI_ALIAS_FLAG or Paint.LINEAR_TEXT_FLAG)
    paintPage.style = Paint.Style.FILL
    paintPage.color = Color.WHITE
    pageCanvas.drawPaint(paintPage)
    pageCanvas.drawBitmap(bitmap, 0f, 0f, paintPage)

    pdfDocument.finishPage(page)
    pdfDocument.writeTo(fos)
    pdfDocument.close()
    fos.flush()
    fos.close()

    return FileProvider.getUriForFile(this.context, uriAuthority, file)
}
