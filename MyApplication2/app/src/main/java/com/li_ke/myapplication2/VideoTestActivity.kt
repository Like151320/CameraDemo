package com.li_ke.myapplication2

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import com.li_ke.myapplication2.camera_see.CameraOrientationDemo
import com.li_ke.myapplication2.camera_see.ISeeCamera
import com.li_ke.myapplication2.utils.getDisplayRotation
import com.li_ke.myapplication2.utils.getMyCameraInfo
import kotlinx.android.synthetic.main.activity_video_test.*
import java.io.File

/**
 * 视频Demo
 */

/**录制地址*/
val savePath = File(
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
    "test.mp4"
).path

class VideoTestActivity : AppCompatActivity() {
    var perCallback: (() -> Unit)? = null
    private val iSeeCamera: ISeeCamera = CameraOrientationDemo()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_test)

        btn_getCameraInfo.setOnClickListener {
            getMyCameraInfo(0)
            getDisplayRotation(this)
        }

        btn_startSee.setOnClickListener {
            getPermission(
                arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) { iSeeCamera.see(videoView) }
        }

        btn_finishSee.setOnClickListener {
            iSeeCamera.finish(this, videoView)
        }

        btn_seeRecorder.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(savePath), "video/mp4"))
        }
    }

    fun getPermission(permissions: Array<String>, callback: () -> Unit) {
        this.perCallback = callback
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 100)
        }
    }

    /**得到摄像头启动权限*/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100)
            perCallback?.invoke()
    }
}
