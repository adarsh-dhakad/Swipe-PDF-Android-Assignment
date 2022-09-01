package com.swipe.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Insets
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowInsets
import android.view.WindowMetrics
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.github.barteksc.pdfviewer.listener.OnTapListener
import com.swipe.pdf.databinding.ActivityPdfViewerBinding
import java.io.File


class PdfViewerActivity : AppCompatActivity() {


    lateinit var mFileDescriptor: ParcelFileDescriptor

    /**
     * {@link android.graphics.pdf.PdfRenderer} to render the PDF.
     */
    private lateinit var mPdfRenderer: PdfRenderer
    private lateinit var file: File
    private lateinit var binding:ActivityPdfViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uri:Uri?

        if(intent.hasExtra("data")){
           uri = intent.getStringExtra("data")?.toUri()
            Log.d("wi233", "uri ${uri}")
        }else {
            uri = intent.data
        }
        Log.d("aer34 i","${uri}")
        binding.pdfView.fromUri(uri)
        //    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
            .enableSwipe(true) // allows to block changing pages using swipe
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
            .password(null)
            .scrollHandle(null)
            .enableAntialiasing(true) // improve rendering a little bit on low-res screens
            // spacing between pages in dp. To define spacing color, set view background
            .spacing(0)
            .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
//            .linkHandler(DefaultLinkHandler)
//            .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
            .fitEachPage(true) // fit each page to the view, else smaller pages are scaled relative to largest page.
            .pageSnap(false) // snap pages to screen boundaries
            .pageFling(false) // make a fling change only a single page like ViewPager
            .nightMode(false) // t
            .load()
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
            mFileDescriptor = contentResolver.openFileDescriptor(intent.data!!,"r")!!
         //   mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
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

