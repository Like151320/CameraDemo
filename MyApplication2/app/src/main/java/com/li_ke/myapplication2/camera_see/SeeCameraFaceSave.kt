package com.li_ke.myapplication2.camera_see

import android.hardware.Camera
import android.media.MediaRecorder
import android.os.Environment
import android.view.SurfaceHolder
import android.widget.VideoView
import com.li_ke.myapplication2.VideoTestActivity
import java.io.File

/**
 * 作者: Li_ke
 * 日期: 2018/10/17 0017 14:44
 * 作用:
 * 转为录制并展示录制的内容
 */
class SeeCameraFaceSave(val activity: VideoTestActivity) : ISeeCamera {

    override fun see(videoView: VideoView) {

        val cameraInfo = Camera.CameraInfo()
        Camera.getCameraInfo(1, cameraInfo)
        var orientation = cameraInfo.orientation

        //Nexus正面倒立
        if (android.os.Build.MANUFACTURER == "LGE" && android.os.Build.MODEL == "Nexus 5X")
            orientation -= 180

        //相机权限+录制权限
        activity.getPermission(
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {

            //启动摄像头
            val camera = Camera.open(1)
            //不预览
//            camera.setPreviewDisplay(videoView.holder)
            camera.setDisplayOrientation(orientation)//播放的旋转与录制旋转无关，只是播放时转转罢了。
            camera.startPreview()

            //录制
            startRecorder(camera, videoView.holder, orientation)
        }
    }

    /**启动录制 [camera]:录制关联的摄像头，[holder]:录制预览 [orientation]:旋转角度*/
    private fun startRecorder(camera: Camera, holder: SurfaceHolder, orientation: Int) {

        camera.unlock()//必须解锁摄像头

        val mRecorder = MediaRecorder()
        mRecorder.setCamera(camera)

        // 这两项需要放在setOutputFormat之前
        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);//设置音频来源
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//设置视频来源

        // Set output file format
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//设置保存格式

        // 这两项需要放在setOutputFormat之后
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//设置编码格式
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

        mRecorder.setVideoSize(640, 480);//视频宽高
        mRecorder.setVideoFrameRate(30);//帧率
        mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);//设置视频编码比特率 - 传输频率?
        mRecorder.setOrientationHint(orientation);//录制输出的旋转角度
        //设置记录会话的最大持续时间（毫秒）
        mRecorder.setMaxDuration(30 * 1000);
        mRecorder.setPreviewDisplay(holder.surface);//视频预览

        //输出路径（不指定抛异常）
        mRecorder.setOutputFile(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "test.mp4"
            ).path
        )
        mRecorder.prepare();//预备、开始
        mRecorder.start();
    }
}