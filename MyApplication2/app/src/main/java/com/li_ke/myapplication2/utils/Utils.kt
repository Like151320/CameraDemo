package com.li_ke.myapplication2.utils

import android.content.Context
import android.hardware.Camera
import android.view.WindowManager

/**
 * 作者: Li_ke
 * 日期: 2018/10/22 0022 10:14
 * 作用:
 */
/**获取摄像头信息*/
fun getMyCameraInfo(number: Int): Camera.CameraInfo {


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

/**不靠谱，并不一定正确。*/
fun testGetTrueAngln(context: Context, number: Int): Int {
    val cameraInfo = Camera.CameraInfo()
    Camera.getCameraInfo(number, cameraInfo)

//        TODO("可计算正确角度？解决Nexus 5X只能单独适配问题？") -> 是可以解决旋转问题,但断点推断时发现就和Nexus没倒装一样。
    val degrees = getDisplayRotation(context)
    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//前置
        var result = (cameraInfo.orientation + degrees) % 360
        result = (360 - result) % 360   // compensate the mirror
        println("正确角度: " + result)
        return result
    } else {
        // back-facing
        val result = (cameraInfo.orientation - degrees + 360) % 360
        println("正确角度: " + result)
        return result
    }
}

/**获取屏幕旋转信息*/
fun getDisplayRotation(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val rotation = windowManager.defaultDisplay.rotation
//        when (rotation) {
//            Surface.ROTATION_0 -> degrees = 0
//            Surface.ROTATION_90 -> degrees = 90
//            Surface.ROTATION_180 -> degrees = 180
//            Surface.ROTATION_270 -> degrees = 270
//        }
    return rotation
}