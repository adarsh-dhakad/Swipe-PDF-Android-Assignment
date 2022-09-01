package com.swipe.pdf

import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.swipe.pdf.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

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
            startActivity(Intent(this, PdfCreateActivity::class.java))
        })

        binding.btnPdfRender.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                val gallery = Intent()
                gallery.type = "application/*"
                gallery.action = Intent.ACTION_GET_CONTENT
                resultLauncherGallery.launch(gallery)

                //  showErrorSnackBar("You already have the storage permission.", false)
//                val galleryIntent = Intent(
//                    Intent.ACTION_PICK,
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                )
 //               resultLauncherGallery.launch(galleryIntent)
            } else {
                // Requests permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding.btnGkemonGenerator.setOnClickListener {
            startActivity(Intent(this, InputActivity::class.java))
        }

    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    private var resultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data

//                val thumbnail : Bitmap = data.extras!!.get("data") as Bitmap
                val intent = Intent(this,PdfViewerActivity::class.java)
            if (data != null) {
                intent.putExtra("data",data.data.toString())
                Log.d("aer34","${data.data}")
                startActivity(intent)
            }


//                Glide.with(this)
//                    .load(thumbnail)
//                    .centerCrop()
//                    .into(mBinding.ivDishImage)
//
//                mImagePath = saveImageToInternalStorage(thumbnail)
//                //   mBinding.ivDishImage.setImageBitmap(thumbnail)
//                mBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this@AddUpdateDishActivity , R.drawable.ic_vector_edit))
            }

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
        const val READ_CAMERA_PERMISSION_CODE = 32
        const val READ_STORAGE_PERMISSION_CODE = 12
        private const val IMAGE_DIRECTORY = "FavDishImages"

    }
}
