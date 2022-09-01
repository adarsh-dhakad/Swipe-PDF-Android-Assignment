package com.swipe.pdf.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.swipe.pdf.databinding.FragmentXmlToPdfBinding
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.io.FileOutputStream


class XmlToPdfFragment : Fragment() {
    private lateinit var binding: FragmentXmlToPdfBinding
    private lateinit var document: PdfDocument
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentXmlToPdfBinding.inflate(inflater, container, false)
        binding.btnSave.setOnClickListener {

            lifecycleScope.launch {
                generatePdfFromView(binding.idPdfLayout.root)
            }

        }
        return binding.root
    }

    private fun generatePdfFromView(root: ConstraintLayout) {
        val bitmap: Bitmap = getBitmapFromView(root)
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
                                requireActivity().contentResolver.openFileDescriptor(uri, "w")
                            val fileOutputStream = FileOutputStream(pfd!!.fileDescriptor)
                            document.writeTo(fileOutputStream)
                            document.close()

                            Toast.makeText(requireContext(), "pdf Saved Successfully", Toast.LENGTH_SHORT).show()

                        }
                    }catch (e: FileNotFoundException){
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "File Not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    private fun getBitmapFromView(view: ConstraintLayout): Bitmap {
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


}