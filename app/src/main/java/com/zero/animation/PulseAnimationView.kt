package com.zero.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator


class PulseAnimationView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var mPain: Paint = Paint()
    private var mRadius: Float = 0.toFloat()
    private var mX: Float = 0.toFloat()
    private var mY: Float = 0.toFloat()

    private var mPulseAnimationSet: AnimatorSet = AnimatorSet()

    companion object {
        const val ANIMATION_DURATION = 4000
        const val ANIMATION_DELAY = 1000
        const val COLOR_ADJUSTER = 5
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas.let {
            it?.drawCircle(mX, mY, mRadius, mPain)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                mX = event.x
                mY = event.y
                mRadius = 0f
                mPain.color = Color.RED
                mPulseAnimationSet.start()
            }
        }

        if (mPulseAnimationSet.isRunning) {
            mPulseAnimationSet.cancel()
        }

        mPulseAnimationSet.start()

        return super.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val growAnimator = ObjectAnimator.ofFloat(this, "radius", 0f, width.toFloat())
        growAnimator.duration = ANIMATION_DURATION.toLong()
        growAnimator.interpolator = LinearInterpolator()
        growAnimator.startDelay = ANIMATION_DELAY.toLong()

        val shrinkAnimator = ObjectAnimator.ofFloat(this, "radius", width.toFloat(), 0f)
        shrinkAnimator.duration = ANIMATION_DURATION.toLong()
        shrinkAnimator.interpolator = LinearOutSlowInInterpolator()
        shrinkAnimator.startDelay = ANIMATION_DELAY.toLong()

        val repeatAnimator = ObjectAnimator.ofFloat(this, "radius", 0f, width.toFloat())
        repeatAnimator.startDelay = ANIMATION_DELAY.toLong()
        repeatAnimator.duration = ANIMATION_DURATION.toLong()

        mPulseAnimationSet.play(growAnimator).before(shrinkAnimator)
        mPulseAnimationSet.play(repeatAnimator).after(shrinkAnimator)
    }

    private fun setRadius(radius: Float) {
        mRadius = radius
        mPain.color = Color.RED + (radius.toInt() / COLOR_ADJUSTER)
        invalidate()
    }

}