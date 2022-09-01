package com.swipe.pdf

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.swipe.pdf.databinding.ActivityPdfCreateBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class PdfCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfCreateBinding
    private lateinit var document: PdfDocument
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnSave.setOnClickListener {

            lifecycleScope.launch {
                //   savePdf(this@PdfCreateActivity.binding.idPdfLayout.root)
              //  binding.idPdfLayout.etEnglishText.text = " this is my english"
                generatePdfFromView(this@PdfCreateActivity.binding.idPdfLayout.root)
            }
        }
    }

    private fun generatePdfFromView(view: View) {
        val bitmap: Bitmap = getBitmapFromView(view)
        document = PdfDocument()
        val myPageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(
            bitmap.width,
            bitmap.height,
            1
        ).create()

        val myPage: PdfDocument.Page = document.startPage(myPageInfo)
        val canvas = myPage.canvas
        canvas.drawBitmap(bitmap, 0F, 0F, null)
        document.finishPage(myPage)

        createFile()
    }


    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("application/pdf")
        intent.putExtra(Intent.EXTRA_TIME, "invoice.pdf")
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                try {
                    val uri: Uri? = data.data
                    Log.d("ass122", "# ${data.data} # ${data.data!!.path}")

                    if (uri != null) {
                        val pfd: ParcelFileDescriptor? =
                            contentResolver.openFileDescriptor(uri, "w")
                        val fileOutputStream = FileOutputStream(pfd!!.fileDescriptor)
                        document.writeTo(fileOutputStream)
                        document.close()

                        Toast.makeText(this, "pdf Saved Successfully", Toast.LENGTH_SHORT).show()

                    }
                }catch (e:FileNotFoundException){
                    e.printStackTrace()
                    Toast.makeText(this, "File Not found", Toast.LENGTH_SHORT).show()
                }
                }
            }
        }

    private fun getBitmapFromView(view: View): Bitmap {
        val returnBitmap = Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(returnBitmap)

        if (view.background != null) {
            val bgDrawable: Drawable = view.background
            bgDrawable.draw(canvas)
        } else {
            // draw white on canvas
            canvas.drawColor(Color.WHITE)
        }

        view.draw(canvas)

        return returnBitmap
    }

//    private suspend fun savePdf(view: View) {
//
//        //Fetch the dimensions of the viewport
//        val displayMetrics = DisplayMetrics()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            this@PdfCreateActivity.display?.getRealMetrics(displayMetrics)
//            displayMetrics.densityDpi
//        } else {
//            this@PdfCreateActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
//        }
//        view.measure(
//            View.MeasureSpec.makeMeasureSpec(
//                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
//            ),
//            View.MeasureSpec.makeMeasureSpec(
//                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
//            )
//        )
//
//        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
//
//        withContext(Dispatchers.IO) {
//
//            val bitmap = Bitmap.createBitmap(
//                view.measuredWidth,
//                view.measuredHeight,
//                Bitmap.Config.ARGB_8888
//            )
//            val canvas = Canvas(bitmap)
//            view.draw(canvas)
//
//            // 595  842
//            Bitmap.createScaledBitmap(
//                bitmap,
//                displayMetrics.widthPixels,
//                displayMetrics.heightPixels,
//                true
//            )
//
//            val pdfDocument = PdfDocument()
//            val pageInfo = PdfDocument.PageInfo.Builder(
//                displayMetrics.widthPixels,
//                displayMetrics.heightPixels,
//                1
//            ).create()
//
//            val page = pdfDocument.startPage(pageInfo)
//            page.canvas.drawBitmap(bitmap, 0F, 0F, null)
//            pdfDocument.finishPage(page)
//
//            val file = File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//                "myBill.pdf"
//            )
//            try {
//                pdfDocument.writeTo(FileOutputStream(file))
//                runOnUiThread {
//                    Toast.makeText(
//                        this@PdfCreateActivity,
//                        "PDF file generated successfully.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//            pdfDocument.close()
//        }
//    }
}