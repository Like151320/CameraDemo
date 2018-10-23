package com.li_ke.myapplication2.camera_see

import android.content.Context
import android.hardware.Camera
import android.media.MediaRecorder
import android.view.SurfaceHolder
import android.widget.VideoView
import com.li_ke.myapplication2.savePath

/**
 * 作者: Li_ke
 * 日期: 2018/10/23 0023 11:12
 * 作用: 关于旋转摄像头 - 摄像头的旋转到底是什么个逻辑
 */
abstract class CameraOrientationBase : ISeeCamera {
    private val mediaRecorder = MediaRecorder()

    override fun see(videoView: VideoView) {

        //启动摄像头
        val cameraId = if (openCameraFaceElseBack()) 1 else 0
        val camera = Camera.open(cameraId)

        val previewOrientation = previewOrientation(cameraId)
        val outputOrientation = outputOrientation(cameraId)
        println("Li_ke: previewOrientation = $previewOrientation ;outputOrientation= $outputOrientation")

        camera.setPreviewDisplay(videoView.holder)
        //播放的旋转与录制旋转无关联，只是播放时转转罢了。
        //但是必须设置,如果不设置，预览的视频将旋转。
        //MediaRecorder没有设置预览方向的API，也是通过这里设置的。
        camera.setDisplayOrientation(previewOrientation)
//            camera.startPreview()//不预览

        //录制
        /**启动录制 [camera]:录制关联的摄像头，[holder]:录制预览 [orientation]:旋转角度*/
        fun startRecorder(mRecorder: MediaRecorder, camera: Camera, holder: SurfaceHolder, orientation: Int) {

            camera.unlock()//必须解锁摄像头

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
            mRecorder.setVideoEncodingBitRate(1024 * 1024);//设置视频编码比特率 - 传输频率?
            mRecorder.setOrientationHint(orientation);//录制输出的旋转角度
            //设置记录会话的最大持续时间（毫秒）
            mRecorder.setMaxDuration(30 * 1000);
            mRecorder.setPreviewDisplay(holder.surface);//视频预览

            //输出路径（不指定抛异常）
            mRecorder.setOutputFile(savePath)
            mRecorder.prepare();//预备、开始
            mRecorder.start();
        }
        startRecorder(mediaRecorder, camera, videoView.holder, outputOrientation)

    }

    override fun finish(context: Context, videoView: VideoView?) {

        val mRecorder = mediaRecorder
        mRecorder.setOnErrorListener(null)
        mRecorder.setOnInfoListener(null)
        mRecorder.setPreviewDisplay(null)
        try {
            mRecorder.stop()
            mRecorder.setOnInfoListener { mr, what, extra ->

            }
        } finally {
            mRecorder.reset()//可无
            mRecorder.release()
        }

        //立即关闭摄像头 - 使录制结束后没有短暂的卡顿。未发现副作用
        val camera = Camera.open(if (openCameraFaceElseBack()) 1 else 0)
        camera?.setPreviewCallback(null)
        camera?.stopPreview()
        camera?.setPreviewDisplay(null)//这句要在stopPreview后执行，不然会卡顿或者花屏
    }

    /**打开 前or后 摄像头*/
    abstract fun openCameraFaceElseBack(): Boolean

    /**设定预览旋转角度*/
    abstract fun previewOrientation(camera: Int): Int

    /**设定输出旋转角度*/
    abstract fun outputOrientation(camera: Int): Int
}