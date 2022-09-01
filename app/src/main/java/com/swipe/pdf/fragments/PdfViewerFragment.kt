package com.swipe.pdf.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.swipe.pdf.databinding.FragmentPdfViewerBinding


class PdfViewerFragment : Fragment() {
   private lateinit var binding: FragmentPdfViewerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentPdfViewerBinding.inflate(inflater,container,false)
        val uri: Uri?

        val bundle = this.arguments
        if(bundle != null) {
            uri = bundle.getString("data")?.toUri()
        }else{
            uri = null
        }
//        if(intent.hasExtra("data")){
//            uri = intent.getStringExtra("data")?.toUri()
//            Log.d("wi233", "uri ${uri}")
//        }else {
//            uri = intent.data
//        }
//        Log.d("aer34 i","${uri}")
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
        return binding.root
    }

}