package com.example.mydiplom

import android.Manifest
import android.app.Instrumentation.ActivityResult
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.lifecycle.lifecycleScope
import com.example.mydiplom.camera.CameraRecognitionCenter
import com.example.mydiplom.databinding.FragmentCameraBinding
import com.example.mydiplom.databinding.FragmentDetailedFileBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch


class FragmentCamera : Fragment() {

    private var binding: FragmentCameraBinding? = null


    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        permissionGranted ->
        if(permissionGranted){
            startCamera()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    private fun startCamera() {
        val context = requireActivity()
        val center = CameraRecognitionCenter(context)
        center.setupCamera(this)
        lifecycleScope.launch {
            center.cameraProvider
                .filterNotNull()
                .collectLatest {
                    val preview = Preview.Builder().build()
                    it.bindToLifecycle(
                        this@FragmentCamera,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview
                    )
                    preview.setSurfaceProvider(binding!!.previewCamera.surfaceProvider)
                }
        }
    }

}