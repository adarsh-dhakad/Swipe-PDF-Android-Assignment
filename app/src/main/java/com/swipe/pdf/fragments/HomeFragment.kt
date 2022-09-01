package com.swipe.pdf.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.swipe.pdf.*
import com.swipe.pdf.activities.MainActivity
import com.swipe.pdf.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
   private lateinit var binding:FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.idBtnGeneratePDF.setOnClickListener(View.OnClickListener { // calling method to
            val fm: FragmentManager = requireActivity().supportFragmentManager
            val fragment = XmlToPdfFragment()
            fm.beginTransaction()
                .replace(R.id.main_contenier, fragment)
                .addToBackStack("Later Transaction").commit()
        })

        binding.btnPdfRender.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                val gallery = Intent()
                gallery.type = "application/*"
                gallery.action = Intent.ACTION_GET_CONTENT
                resultLauncherGallery.launch(gallery)
            } else {
                // Requests permission
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    MainActivity.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding.btnInputToXml.setOnClickListener {
            val fm: FragmentManager = requireActivity().supportFragmentManager
            val fragment = InputToPdfFragment()
            fm.beginTransaction()
                .replace(R.id.main_contenier, fragment)
                .addToBackStack("Later Transaction").commit()
        }
        return binding.root
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    private var resultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data
            if (data != null) {
//                intent.putExtra("data",data.data.toString())
//                Log.d("aer34","${data.data}")
//                startActivity(intent)
                val bundle = Bundle()
                bundle.putString("data", data.data.toString())
                val fm: FragmentManager = requireActivity().supportFragmentManager
                val fragobj = PdfViewerFragment()
                fragobj.arguments = bundle
                fm.beginTransaction()
                    .replace(R.id.main_contenier, fragobj)
                    .addToBackStack("Later Transaction").commit()
            }
        }

    }
}