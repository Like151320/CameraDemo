package com.li_ke.myapplication2.camera_see

import android.hardware.Camera
import android.widget.VideoView
import com.li_ke.myapplication2.VideoTestActivity

/**
 * 作者: Li_ke
 * 日期: 2018/10/17 0017 14:44
 * 作用:
 * 仅仅是显示了摄像头内容,方向不对
 */
class SeeCameraBase(val activity: VideoTestActivity) : ISeeCamera {
    override fun see(videoView: VideoView) {
        activity.getPermission(arrayOf(android.Manifest.permission.CAMERA)) {
            val camera = Camera.open()
            camera.setPreviewDisplay(videoView.holder)
            camera.startPreview()
        }
    }
}