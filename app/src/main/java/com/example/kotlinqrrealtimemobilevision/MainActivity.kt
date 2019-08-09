package com.example.kotlinqrrealtimemobilevision

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import github.nisrulz.qreader.QRDataListener
import github.nisrulz.qreader.QREader
import kotlinx.android.synthetic.main.activity_main.*
import java.security.Permission

class MainActivity : AppCompatActivity() {

    private  var qrEader: QREader?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dexter.withActivity(this@MainActivity).withPermission(android.Manifest.permission.CAMERA)
            .withListener(object :PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    setupCamera()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(this@MainActivity,"You Must Enable this permission",Toast.LENGTH_SHORT)
                        .show()
                }

            }).check()
    }

    private fun setupCamera() {
        btn_enable_disable.setOnClickListener{
            if(qrEader!!.isCameraRunning)
            {
                btn_enable_disable.text="Start"
                qrEader!!.stop()
            }
            else{
                btn_enable_disable.text="Stop"
                qrEader!!.start()
            }
        }

        setupQREader()
    }

    private fun setupQREader() {
        qrEader = QREader.Builder(this,camera_view, QRDataListener { data -> code_info.post { code_info.text=data } })
            .facing(QREader.BACK_CAM).enableAutofocus(true)
            .height(camera_view.height)
            .width(camera_view.width)
            .build()
    }

    override fun onResume() {
        super.onResume()

        if (qrEader !=null)
            qrEader!!.initAndStart(camera_view)
    }

    override fun onPause() {
        super.onPause()
        if (qrEader!=null)
            qrEader!!.releaseAndCleanup()
    }
}
