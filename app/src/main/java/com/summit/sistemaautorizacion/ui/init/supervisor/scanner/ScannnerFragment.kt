package com.summit.sistemaautorizacion.ui.init.supervisor.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.impl.PreviewConfig
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.summit.sistemaautorizacion.R
import com.summit.sistemaautorizacion.base.BaseFragment
import com.summit.sistemaautorizacion.ui.auth.AuthViewModel
import com.summit.sistemaautorizacion.ui.auth.AuthViewModelFactory
import com.summit.sistemaautorizacion.ui.init.GlobalViewModel
import com.summit.sistemaautorizacion.ui.init.GlobalViewModelFactory
import kotlinx.android.synthetic.main.fragment_scannner.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class ScannnerFragment : BaseFragment(),KodeinAware , QRCodeReaderView.OnQRCodeReadListener {

    override val kodein: Kodein by kodein()
    lateinit var viewModel: GlobalViewModel
    private val factory: GlobalViewModelFactory by instance()
    lateinit var authVM: AuthViewModel
    private val authFactory: AuthViewModelFactory by instance()
    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }
    override fun getLayout()=R.layout.fragment_scannner
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = requireActivity().run {
            ViewModelProvider(this,factory).get(GlobalViewModel::class.java)
        }
        authVM = requireActivity().run {
            ViewModelProvider(this,authFactory).get(AuthViewModel::class.java)
        }


        lbl_scan.setOnClickListener {
            iniciarScan()
        }
    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (isCameraPermissionGranted()) {
                iniciarScan()
            } else {
                Toast.makeText(requireContext(), "Camera permission is required.", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun iniciarScan(){
        if (isCameraPermissionGranted()) {
            qrdecoderview.setOnQRCodeReadListener(this)
            //Use this function to enable/disable decoding
            qrdecoderview.setQRDecodingEnabled(true);

            // Use this function to change the autofocus interval (default is 5 secs)
            qrdecoderview.setAutofocusInterval(2000L);

            // Use this function to enable/disable Torch
            qrdecoderview.setTorchEnabled(true);

            // Use this function to set front camera preview
            qrdecoderview.setFrontCamera();

            // Use this function to set back camera preview
            qrdecoderview.setBackCamera();
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        Log.e("camera","datos")
    }

    override fun onResume() {
        super.onResume()
        qrdecoderview.startCamera();
    }

    override fun onDestroy() {
        super.onDestroy()
        qrdecoderview.stopCamera();
    }
}