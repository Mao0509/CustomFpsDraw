package com.example.fps.test

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View

class CustomView1 : View {

    companion object {
        private const val TAG = "CustomView1"
        private const val DELAY = 16L
    }

    private var mFlag = false
    private var mSum = 0
    private val mRunnable = Runnable {
        invalidate()
    }

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


    fun start() {
        mRunnable.run()
        mFlag = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mFlag) {
            Log.d(TAG, "onDraw")
            mSum++
            if (mSum < 60) {
                postDelayed(
                    mRunnable,
                    DELAY
                )
            }
        }


    }

}