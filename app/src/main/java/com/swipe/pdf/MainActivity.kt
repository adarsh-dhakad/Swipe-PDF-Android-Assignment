package com.swipe.pdf

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pspdfkit.configuration.activity.PdfActivityConfiguration
import com.pspdfkit.ui.PdfActivity
import com.swipe.pdf.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {
    // declaring width and height
    // for our PDF file.
    var pageHeight = 1120
    var pagewidth = 792

    // creating a bitmap variable
    // for storing our images
    var scaledbmp: Bitmap? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // below code is used for
        // checking our permissions.
        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }

        binding.idBtnGeneratePDF.setOnClickListener(View.OnClickListener { // calling method to
            // generate our PDF file.
            generateBill()
        })

        binding.btnPdfRender.setOnClickListener {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "myBill.pdf"
            )
            val i = Intent(this, PdfViewerActivity::class.java)
                i.putExtra( "in", file.toString());
            startActivity(i)
        }

        binding.btnPspdfkit.setOnClickListener {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "myBill.pdf"
            )
            val config = PdfActivityConfiguration.Builder(this).build()
            PdfActivity.showDocument(this, Uri.fromFile(file), config)
        }

    }

    private fun generateBill() {
        val pdfDocument = PdfDocument()
        val paint = Paint()
        val title = Paint()
        val mypageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(mypageInfo)
        // creating a variable for canvas
        // from our page of PDF.
        val canvas = myPage.canvas
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        var margins = 30f
        canvas.drawRect(
            margins,
            margins,
            mypageInfo.pageWidth - margins,
            mypageInfo.pageHeight - margins,
            paint
        )
        canvas.drawLine(
            margins,
            mypageInfo.pageHeight / 2f,
            mypageInfo.pageWidth - margins,
            mypageInfo.pageHeight / 2f,
            paint
        )

        title.textSize = 20f
        title.color = ContextCompat.getColor(this, R.color.black)
        title.textAlign = Paint.Align.LEFT
        margins += 20f
        canvas.drawText("English", margins, margins + 20, title)

        //       title.color = Color.rgb(1,1,1)
        //   title.textAlign = Paint.Align.CENTER

        canvas.drawText(
            "We all know that health is wealth. With its intricate network of bones, muscles,",
            margins,
            margins + 80,
            title
        )
        canvas.drawText(
            "and organs, a well-functioning human body is much like an orchestrated",
            margins,
            margins + 110,
            title
        )
        canvas.drawText(
            "symphony. To keep this orchestra playing well, we need physical exercise. It ",
            margins,
            margins + 140,
            title
        )
        canvas.drawText(
            "may take the form of sports, yoga, or even regular walking. It is well-known that ",
            margins,
            margins + 170,
            title
        )
        canvas.drawText(
            "people who engage in physical exercise stay happier and live longer.",
            margins,
            margins + 200,
            title
        )

        canvas.drawText("हिन्दी", margins, mypageInfo.pageHeight / 2f + 30, title)


        canvas.drawText(
            "मनुष्य इस समाज में रहने वाला सामाजिक प्राणी है। समाज की प्रगति का दायित्व मनुष्य के ",
            margins,
            mypageInfo.pageHeight / 2f + 80,
            title
        )
        canvas.drawText(
            "व्यवहार पर निर्भर करता है। आज अराजकता के कारण ईर्ष्या और दुर्भावना की सोच है। इस ",
            margins,
            mypageInfo.pageHeight / 2f + 110,
            title
        )
        canvas.drawText(
            "कारण कुछ इंसान स्वभिमान और इंसानियत को भूल गए हैं। अराजकता के कई प्रकार हैं, ",
            margins,
            mypageInfo.pageHeight / 2f + 140,
            title
        )
        canvas.drawText(
            "जैसे भ्रष्टाचार, शोषण, अत्याचार, बेईमान, डकैती चोरी, हत्या रिश्वतखोरी इत्यादि अपराधिक ",
            margins,
            mypageInfo.pageHeight / 2f + 170,
            title
        )
        canvas.drawText(
            "काम हमारे समाज को खोखला कर रही है। असामाजिक तत्वों की शीघ्र पहचान करके उनके ",
            margins,
            mypageInfo.pageHeight / 2f + 200,
            title
        )
        canvas.drawText(
            "खिलाफ कानूनी कार्रवाई की जानी चाहिए। टेलीविजन, इंटरनेट, सोशल मीडिया और ",
            margins,
            mypageInfo.pageHeight / 2f + 230,
            title
        )
        canvas.drawText(
            "अखबारों के माध्यम से जागरूकता पैदा करनी चाहिए। अराजक बनकर समाज दूषित करने ",
            margins,
            mypageInfo.pageHeight / 2f + 260,
            title
        )
        canvas.drawText(
            "वाले के मन में डर तभी होगा जागरूकता और कानून के मोर्चे पर सरकार को सशक्त कदम ",
            margins,
            mypageInfo.pageHeight / 2f + 290,
            title
        )
        canvas.drawText(
            "उठाना चाहिए। तब हम एक उन्नत समाज की स्थापना कर पाएंगे।",
            margins,
            mypageInfo.pageHeight / 2f + 320,
            title
        )

        pdfDocument.finishPage(myPage)
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "myBill.pdf"
        )
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(
                this@MainActivity,
                "PDF file generated successfully.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        pdfDocument.close()
    }

    private fun generatePDF() {
        // creating an object variable
        // for our PDF document.
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.logo_sudoku)
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

        val pdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        val paint = Paint()
        val title = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        val mypageInfo = PageInfo.Builder(pagewidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        val myPage = pdfDocument.startPage(mypageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        val canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp!!, 56f, 40f, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15f

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.color = ContextCompat.getColor(this, R.color.purple_200)

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("A portal for IT professionals.", 209f, 100f, title)
        canvas.drawText("Geeks for Geeks", 209f, 80f, title)

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        title.color = ContextCompat.getColor(this, R.color.purple_200)
        title.textSize = 15f

        // below line is used for setting
        // our text to center of PDF.
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("This is sample document which we have created.", 396f, 560f, title)
        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val file = File(Environment.getExternalStorageDirectory(), "GFG.pdf")
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(FileOutputStream(file))

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(
                this@MainActivity,
                "PDF file generated successfully.",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            // below line is used
            // to handle error
            e.printStackTrace()
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close()
    }

    private fun checkPermission(): Boolean {
        // checking of permissions.
        val permission1 =
            ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ContextCompat.checkSelfPermission(applicationContext, permission.READ_EXTERNAL_STORAGE)
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                val writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    companion object {
        // constant code for runtime permissions
        private const val PERMISSION_REQUEST_CODE = 200
    }
}
