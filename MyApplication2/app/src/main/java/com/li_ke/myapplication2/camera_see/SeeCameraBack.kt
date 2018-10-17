package com.li_ke.myapplication2.camera_see

import android.hardware.Camera
import android.widget.VideoView
import com.li_ke.myapplication2.VideoTestActivity

/**
 * 作者: Li_ke
 * 日期: 2018/10/17 0017 14:44
 * 作用:
 * 正确显示了摄像头方向
 */
class SeeCameraBack(val activity: VideoTestActivity) : ISeeCamera {
    override fun see(videoView: VideoView) {

        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(0, cameraInfo)
        val orientation = cameraInfo.orientation

        activity.getPermission(arrayOf(android.Manifest.permission.CAMERA)) {
            val camera = Camera.open()
            camera.setPreviewDisplay(videoView.holder)
            camera.setDisplayOrientation(orientation)
            camera.startPreview()
        }
    }
}