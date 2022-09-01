package com.swipe.pdf

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.swipe.pdf.databinding.ActivityInputBinding
import java.io.FileNotFoundException
import java.io.FileOutputStream

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            if (checkInput()) {
                generatePdf()
            } else {
                Toast.makeText(this, "Please Enter Some Text", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private lateinit var document: PdfDocument
    var pageHeight = 1120
    var pagewidth = 792
    private fun generatePdf() {
        //     val bitmap: Bitmap = getBitmapFromView(view)
        document = PdfDocument()
        val myPageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(
            pagewidth,
            pageHeight,
            1
        ).create()

        val myPage: PdfDocument.Page = document.startPage(myPageInfo)
        val canvas = myPage.canvas
        val paint = Paint()
        val title = Paint()

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        var margins = 30f
        // draw border rectangle
        canvas.drawRect(
            margins,
            margins,
            myPageInfo.pageWidth - margins,
            myPageInfo.pageHeight - margins,
            paint
        )
        // draw center line
        canvas.drawLine(
            margins,
            myPageInfo.pageHeight / 2f,
            myPageInfo.pageWidth - margins,
            myPageInfo.pageHeight / 2f,
            paint
        )

        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        title.textSize = 30f
        title.color = ContextCompat.getColor(this, R.color.black)
        title.textAlign = Paint.Align.LEFT
        margins += 20f

        // draw title
        canvas.drawText(binding.etEnglish.text.toString(), margins, margins + 20, title)


        var mTextLayout: StaticLayout =
            createTextLayout(binding.etEnglishText.text.toString(), canvas.width - 90)
        canvas.save()
// calculate x and y position where your text will be placed
        canvas.translate(margins, margins + 80);
        mTextLayout.draw(canvas);
        canvas.restore()
        // draw title hindi
        canvas.drawText(
            binding.etHindi.text.toString(),
            margins,
            myPageInfo.pageHeight / 2f + 35,
            title
        )

        mTextLayout = createTextLayout(binding.etHindiText.text.toString(), canvas.width - 90)
        canvas.save()
        canvas.translate(margins, myPageInfo.pageHeight / 2f + 70);
        mTextLayout.draw(canvas);
        canvas.restore()
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

                            Toast.makeText(this, "pdf Saved Successfully", Toast.LENGTH_SHORT)
                                .show()

                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        Toast.makeText(this, "File Not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    private fun createTextLayout(text: String, width: Int): StaticLayout {
        val textPaint = TextPaint()
        textPaint.textSize = 30f
        textPaint.color = ContextCompat.getColor(this, R.color.black)
        val mTextLayout: StaticLayout
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            val sb = StaticLayout.Builder.obtain(
                text,
                0,
                text.length,
                textPaint,
                width
            )
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setMaxLines(10)
//                .setLineSpacing(0.0f, 1.0f)
//                .setIncludePad(false)
            mTextLayout = sb.build()
        } else {
            mTextLayout = StaticLayout(
                text,
                textPaint,
                width,
                Layout.Alignment.ALIGN_NORMAL,
                1.0f,
                0.0f,
                false
            )
        }
        return mTextLayout
    }

    private fun checkInput(): Boolean {
        if (binding.etEnglish.text.toString().trim().length < 2) {
            binding.etEnglish.error = "Text Should be > 2"
            return false
        }
        if (binding.etEnglishText.text.toString().trim().length < 10) {
            binding.etEnglishText.error = "Text Should be > 10"
            return false
        }

        if (binding.etHindi.text.toString().trim().length < 2) {
            binding.etHindi.error = "Text Should be > 2"
            return false
        }

        if (binding.etHindiText.text.toString().trim().length < 10) {
            binding.etHindiText.error = "Text Should be > 10"
            return false
        }

        return true
    }
}