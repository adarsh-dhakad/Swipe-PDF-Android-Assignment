package com.swipe.pdf

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.swipe.pdf.databinding.ActivityPdfCreateBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PdfCreateActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPdfCreateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSave.setOnClickListener {
            Thread.sleep(1000)
            savePdf()
        }
    }

    private fun savePdf(){
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.pdf_layout, null)

        //Fetch the dimensions of the viewport
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        }
        else{
            this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        }
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )

        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        // 595  842
        Bitmap.createScaledBitmap(bitmap, displayMetrics.widthPixels, displayMetrics.heightPixels, true)

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(displayMetrics.widthPixels, displayMetrics.heightPixels, 1).create()

        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0F, 0F, null)
        pdfDocument.finishPage(page)

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "myBill2.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(
                this,
                "PDF file generated successfully.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }
}