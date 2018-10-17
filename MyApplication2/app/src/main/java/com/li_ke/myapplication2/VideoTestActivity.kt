package com.li_ke.myapplication2

import android.content.Context
import android.hardware.Camera
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.li_ke.myapplication2.camera_see.*
import kotlinx.android.synthetic.main.activity_video_test.*

class VideoTestActivity : AppCompatActivity() {
    var perCallback: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_test)

        btn_getCameraInfo.setOnClickListener {
            getCameraInfo(0)
            getDisplayRotation()
        }

        btn_startSee.setOnClickListener {
            SeeCameraBackSave(this).see(videoView)
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

    ///////////////////////////////////////////////////////////////////////////
    // 信息获取
    ///////////////////////////////////////////////////////////////////////////

    /**获取摄像头信息*/
    fun getCameraInfo(number: Int): Camera.CameraInfo {


        //打印所有相机信息
        val cameraNumber = Camera.getNumberOfCameras()//获得设备上的相机数（>=0）
        for (i in 0 until cameraNumber) {
            val cameraInfo = Camera.CameraInfo()
            Camera.getCameraInfo(i, cameraInfo)

            println("摄像头编号： ${i}")
            println("是否可禁用快门声音： ${cameraInfo.canDisableShutterSound}")
            println("摄像头朝向自己： ${cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT}")
            println("摄像头旋转角度： ${cameraInfo.orientation}")
        }

        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(number, cameraInfo)
        return cameraInfo
    }

    /**获取屏幕旋转信息*/
    fun getDisplayRotation(): Int {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val rotation = windowManager.defaultDisplay.rotation
//        when (rotation) {
//            Surface.ROTATION_0 -> degrees = 0
//            Surface.ROTATION_90 -> degrees = 90
//            Surface.ROTATION_180 -> degrees = 180
//            Surface.ROTATION_270 -> degrees = 270
//        }
        return rotation
    }
}
