package com.swipe.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Insets
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import android.view.WindowMetrics
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class PdfViewerActivity : AppCompatActivity() {


    lateinit var mFileDescriptor: ParcelFileDescriptor

    /**
     * {@link android.graphics.pdf.PdfRenderer} to render the PDF.
     */
    private lateinit var mPdfRenderer: PdfRenderer
    private lateinit var file: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

//        val intent = intent
//        val pdfUrl = "https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf"
//
//        val pdfView = findViewById<ImageView>(R.id.pdfView)
//        val file = File(Environment.getExternalStorageDirectory(), "myBill.pdf")

        //   RetrievePDFFromURL(pdfView).execute(pdfUrl)
        if(intent.hasExtra("in")){
            val uri: String? = intent.getStringExtra("in")
            val index = uri
            file = File("${uri}")
            Log.d("wi233", "uri ${uri}")
        }else {
            val uri: Uri? = intent.data
            val index = uri.toString().indexOf("storage/emulated/0/")
            file = File("${uri.toString().substring(index)}")
            Log.d("wi233", "uri ${uri.toString().substring(index)}")
        }
     //  Log.d("wi233", "url ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}")
    }

    override fun onResume() {
        super.onResume()
        val imageView = findViewById<ImageView>(R.id.pdfView)
        imageView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            //
            if (imageView.getWidth() > 0 && imageView.getHeight() > 0) {
                openRenderer(this)
            }
        }
    }

    var widthOfScreen = 0
    var heightOfScreen = 0
    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            widthOfScreen = windowMetrics.bounds.width() - insets.left - insets.right
            heightOfScreen = windowMetrics.bounds.height() - insets.left - insets.right

            Log.e("widthPixels", "${windowMetrics.bounds.width() - insets.left - insets.right}")
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            widthOfScreen = displayMetrics.widthPixels
            heightOfScreen = displayMetrics.heightPixels

        }
    }

    private fun openRenderer(context: Context) {
        //   val file = File(Environment.getDownloadCacheDirectory(), "myBill.pdf")
        //      val file = File(Environment.getExternalStorageDirectory(), "myBill.pdf")
        val image = findViewById<ImageView>(R.id.pdfView)
//        val file = File(
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//            "myBill.pdf"
//        )
        try {
            val reqWidth = image.width
            val reqHeight: Int = image.height
            Log.d("wi233", "${image.height} ${image.width}")

            mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val renderer = PdfRenderer(mFileDescriptor)

            val pagecount = renderer.pageCount;
            Toast.makeText(this, "PDF pages $pagecount", Toast.LENGTH_LONG).show()

            //
            val renderPage = renderer.openPage(0);
            val renderPageWigth = renderPage.width
            val rendererPageHeigth = renderPage.height
//            image.layoutParams = MarginLayoutParams(
//                renderPageWigth,
//                rendererPageHeigth
//            )
            ZoomInZoomOut(image)
            val bitmap = Bitmap.createBitmap(renderPageWigth, rendererPageHeigth, Bitmap.Config.ARGB_8888)
            val m = image.imageMatrix
            //   val rect = Rect(0,0,reqWidth,reqHeight)
            //    renderer.openPage(0).render(bitmap,rect,m,PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            renderPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            image.setImageBitmap(bitmap)
            image.invalidate()
            renderPage.close()
            renderer.close()
            mFileDescriptor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}