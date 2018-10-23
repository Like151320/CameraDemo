package com.li_ke.myapplication2.camera_see

import android.hardware.Camera


/**
 * 作者: Li_ke
 * 日期: 2018/10/23 0023 11:25
 * 作用:
 * 遍历记录：
 * 小米6
 * 前摄像头。重心在-- 270   后摄像头。重心在—— 90
 * 角度：0  90 180 270 |  0  90 180 270
 * 预览：右 下  左  上  | 右 下  左  上
 * 录制：上 上  下  下  | 下 下  上  上
 *
 * 后摄像头捕获一帧图片方向为90（已旋转90），
 *
 * Camera.CameraInfo.orientation 的API是这么解释的：
 * 摄像头是横装的，当手机普通的拿着，你正在看屏幕。若后摄像机的顶边是屏幕的右边，则值为90。
 * 若前摄像头顶边是屏幕右边，则值为270
 *
 * 最终：不知道啥原理，反正找规律后公式正确。
 * */
class CameraOrientationDemo : CameraOrientationBase() {
    override fun openCameraFaceElseBack(): Boolean {
        return false
    }

    override fun previewOrientation(camera: Int): Int {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(camera, info)
        return if (camera == 0)//背面正常 90
            info.orientation
        else
            (Math.abs(180 - info.orientation)) % 360 //正面反转 270 -> 90
    }

    override fun outputOrientation(camera: Int): Int {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(camera, info)
        return if (camera == 0)//背面正常 90
            info.orientation
        else
            info.orientation //正面反转 270
    }
}