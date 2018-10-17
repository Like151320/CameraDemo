package com.li_ke.myapplication2.camera_see

import android.hardware.Camera
import android.widget.VideoView
import com.li_ke.myapplication2.VideoTestActivity

/**
 * 作者: Li_ke
 * 日期: 2018/10/17 0017 14:44
 * 作用:
 * 正确显示了摄像头方向（正面）
 */
class SeeCameraFace(val activity: VideoTestActivity) : ISeeCamera {
    override fun see(videoView: VideoView) {

        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(1, cameraInfo)
        var orientation = cameraInfo.orientation

        //Nexus正面倒立
        if (android.os.Build.MANUFACTURER == "LGE" && android.os.Build.MODEL == "Nexus 5X")
            orientation -= 180

        activity.getPermission(arrayOf(android.Manifest.permission.CAMERA)) {
            val camera = Camera.open(1)
            camera.setPreviewDisplay(videoView.holder)
            camera.setDisplayOrientation(orientation)
            camera.startPreview()
        }
    }
}