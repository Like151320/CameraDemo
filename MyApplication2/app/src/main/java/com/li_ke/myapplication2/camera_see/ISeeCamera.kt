package com.li_ke.myapplication2.camera_see

import android.content.Context
import android.widget.Toast
import android.widget.VideoView

/**
 * 作者: Li_ke
 * 日期: 2018/10/17 0017 14:51
 * 作用:
 */
interface ISeeCamera {
    /**预览摄像头*/
    fun see(videoView: VideoView)

    /**停止录制*/
    fun finish(context: Context, videoView: VideoView?) {
        Toast.makeText(context, "未实现此功能", Toast.LENGTH_LONG).show()
    }
}