package com.covid19.genarateqrcode

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.covid19.genarateqrcode.databinding.ActivityQrcodeReaderBinding
import java.util.jar.Manifest

class QRCodeReaderActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var activityQrcodeReaderBinding:ActivityQrcodeReaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_qrcode_reader)
        activityQrcodeReaderBinding = ActivityQrcodeReaderBinding.inflate(layoutInflater)
        setContentView(activityQrcodeReaderBinding.root)

        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA),123)
        }else{
            startScanning()
        }

    }

    private fun startScanning() {
        codeScanner = CodeScanner(this,activityQrcodeReaderBinding.scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS

        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this,"Scan Result:${it.text}",Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this,"Camera initialization error:${it.message}",Toast.LENGTH_LONG).show()
            }
        }
        activityQrcodeReaderBinding.scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123){
            if (grantResults[0]==PackageManager.PERMISSION_DENIED){
                startScanning()
                Toast.makeText(this,"Camera permission granted",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Camera permission denied",Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized){
            codeScanner?.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::codeScanner.isInitialized){
            codeScanner?.releaseResources()
        }
    }

}