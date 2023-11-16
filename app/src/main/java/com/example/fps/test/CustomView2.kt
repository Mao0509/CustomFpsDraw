package com.example.fps.test

import android.content.Context
import android.graphics.Canvas
import android.os.SystemClock
import android.util.AttributeSet
import android.util.Log
import android.view.Choreographer
import android.view.View

class CustomView2 : View, Choreographer.FrameCallback {

    companion object {
        private const val TAG = "CustomView2"
        private const val FRAME_NUM = 30
        private const val DRAW_INTERVAL = 1000L / FRAME_NUM
    }

    private val mRunnable = Runnable {
        Log.d(TAG, "run invalidate")
        invalidate()
        Choreographer.getInstance().postFrameCallback(this)
    }
    private var mSum = 0
    private var mLastVsyncTime = 0L
    private var mDoFrameInterval = 16L
    private var mFlag = false

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        context.display?.refreshRate?.let {
            mDoFrameInterval = (1000 / it).toLong()
            Log.d(TAG, "onAttachedToWindow mFrameInterval:$mDoFrameInterval")
        }
    }

    fun start() {
        mRunnable.run()
        mFlag = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mFlag) {
            Log.d(TAG, "onDraw")
            mSum++
            if (mSum < FRAME_NUM) {
                val expectDrawTime = mLastVsyncTime + DRAW_INTERVAL //期望绘制的时间
                var targetVsyncTime = mLastVsyncTime + mDoFrameInterval
                while (targetVsyncTime + mDoFrameInterval <= expectDrawTime) { //得出对应的Vsync时间
                    targetVsyncTime += mDoFrameInterval
                }
                val curTime = SystemClock.uptimeMillis()
                var delayTime = targetVsyncTime - curTime
                if (delayTime > mDoFrameInterval) {
                    delayTime -= mDoFrameInterval / 2 // 不能将delay时间设置为刚好Vsync时间 不然会错过
                    Log.d(TAG, "postDelayed targetVsyncTime:$targetVsyncTime curTime:$curTime delayTime:$delayTime")
                    postDelayed(
                        mRunnable,
                        delayTime
                    )
                } else { // 下一次Vsync时间马上到来直接触发
                    Log.d(TAG, "direct invalidate")
                    mRunnable.run()
                }
            }
        }

    }


    override fun doFrame(frameTimeNanos: Long) {
        mLastVsyncTime = frameTimeNanos.div(1000000)
        Log.d(TAG, "doFrame mLastVsyncTime:$mLastVsyncTime")
    }


}