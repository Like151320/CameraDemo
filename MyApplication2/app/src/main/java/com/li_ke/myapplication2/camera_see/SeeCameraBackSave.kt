package com.li_ke.myapplication2.camera_see

import android.content.Context
import android.hardware.Camera
import android.media.MediaRecorder
import android.view.SurfaceHolder
import android.widget.VideoView
import com.li_ke.myapplication2.VideoTestActivity
import com.li_ke.myapplication2.savePath
import com.li_ke.myapplication2.utils.testGetTrueAngln

/**
 * 作者: Li_ke
 * 日期: 2018/10/17 0017 14:44
 * 作用:
 * 转为录制并展示录制的内容
 */
class SeeCameraBackSave(val activity: VideoTestActivity) : ISeeCamera {
    private val mRecorder = MediaRecorder()

    override fun see(videoView: VideoView) {

        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(0, cameraInfo)
        val orientation = testGetTrueAngln(activity, 0)//cameraInfo.orientation

        //相机权限+录制权限
        activity.getPermission(
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {

            //启动摄像头
            val camera = Camera.open()
            //焦点
            if (camera.parameters.supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                val parameters = camera.parameters
                parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
                camera.parameters = parameters
            }
//            camera.setPreviewDisplay(videoView.holder)//不预览
            camera.setDisplayOrientation(orientation)//播放的旋转与录制旋转无关，只是播放时转转罢了。
            camera.startPreview()

            //录制
            startRecorder(camera, videoView.holder, orientation)
        }
    }

    /**启动录制 [camera]:录制关联的摄像头，[holder]:录制预览 [orientation]:旋转角度*/
    private fun startRecorder(camera: Camera, holder: SurfaceHolder, orientation: Int) {

        //坑：获得Preview宽高、但摄像头可能不支持这宽高。 -> camera.parameters.supportedVideoSizes//支持的宽高
        val size = camera.parameters.supportedPreviewSizes[0]//w:1920,h:1080

        camera.unlock()//必须解锁摄像头才能绑定mRecorder

        mRecorder.reset()
        mRecorder.setCamera(camera)

        // 这两项需要放在setOutputFormat之前
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//设置视频来源
        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);//设置音频来源

        // Set output file format
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//设置保存格式

        // 这两项需要放在setOutputFormat之后
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置编码格式
//        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//设置编码格式

        mRecorder.setVideoSize(size.width, size.height);//视频宽高
//        mRecorder.setVideoFrameRate(30);//帧率
        mRecorder.setVideoEncodingBitRate(4 * 100000);//设置视频编码比特率 - 传输频率?
        mRecorder.setOrientationHint(orientation);//录制输出的旋转角度
        //设置记录会话的最大持续时间（毫秒）
//        mRecorder.setMaxDuration(30 * 1000);
        mRecorder.setPreviewDisplay(holder.surface);//视频预览

        //输出路径（不指定抛异常）
        mRecorder.setOutputFile(savePath)
        mRecorder.prepare();//预备、开始
        mRecorder.start();
    }

    /**如此结束可以正常重播,只是在视频最后图像会固定一段时间,而奇鹭的不会*///TODO("未解决")
    override fun finish(context: Context, videoView: VideoView?) {
        Camera.open().release()

        mRecorder.setOnErrorListener(null)
        mRecorder.setOnInfoListener(null)
        mRecorder.setPreviewDisplay(null)
        try {
            mRecorder.stop()
        } finally {
            mRecorder.reset()//可无
            mRecorder.release()
        }
    }
}