package com.covid19.genarateqrcode

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.covid19.genarateqrcode.databinding.ActivityMainBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //setContentView(R.layout.activity_main)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.btnGoNextPage.setOnClickListener {
            val intent = Intent(this,QRCodeReaderActivity::class.java)
            startActivity(intent)
            finish()
        }

        viewBinding.btnGenerateQRCode.setOnClickListener {
            val data = viewBinding.etData.text.toString().trim()
            if(data.isEmpty()){

            }else{
                val writer = QRCodeWriter()
                try {
                    val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE,512,512)
                    val width = bitMatrix.width
                    val height = bitMatrix.height
                    val bmp = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
                    for(x in 0 until width){
                        for (y in 0 until height){
                            bmp.setPixel(x,y,if(bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                        }
                    }
                    viewBinding.ivQRCode.setImageBitmap(bmp)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}