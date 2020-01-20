package com.example.canvasexperiments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.util.AttributeSet
import android.view.SoundEffectConstants
import android.view.View
import com.example.canvasexperiments.utils.getXCoOrdinateOfCircle
import com.example.canvasexperiments.utils.getYCoOrdinateOfCircle
import java.util.*

class ClockView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var hour = 0.0f
    private var minute = 0.0f
    private var second = 0.0f

    private val secondHandler = Handler()

    private val secondRunnable = object : Runnable {
        override fun run() {
            second++
            minute++
            hour++
            if (hour == 12.0f * 60.0f * 60.0f) {
                hour = 0.0f
            }
            if (minute == 60.0f * 60.0f) {
                minute = 0.0f
            }
            if (second == 60.0f) {
                second = 0.0f
            }
            playSoundEffect(SoundEffectConstants.CLICK)
            secondHandler.postDelayed(this, 1000L)
            invalidate()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        second = calendar.get(Calendar.SECOND).toFloat()
        minute = calendar.get(Calendar.MINUTE) * 60.0f + second
        hour = calendar.get(Calendar.HOUR) * 60.0f * 60.0f + minute
        isSoundEffectsEnabled = true
        secondHandler.postDelayed(secondRunnable, 1000L)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        secondHandler.removeCallbacks(secondRunnable)
    }

    private val bgCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#5d5c5c")
    }

    private val hourPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = bgCirclePaint.color
        style = Paint.Style.STROKE
        strokeWidth = 6.0f
    }

    private val minutePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = bgCirclePaint.color
        style = Paint.Style.STROKE
        strokeWidth = 3.0f
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
    }

    private val hourHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = bgCirclePaint.color
        strokeWidth = 12.0f
    }

    private val minuteHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = bgCirclePaint.color
        strokeWidth = 6.0f
    }

    private val secondHandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#E62447")
        strokeWidth = 2.0f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawCircle(
                width * 0.5f,
                height * 0.5f,
                HOUR_OUTER_RADIUS + 50.0f,
                bgCirclePaint
            )
            it.drawCircle(
                width * 0.5f,
                height * 0.5f,
                HOUR_OUTER_RADIUS,
                dotPaint
            )
            if (width > 0 && height > 0) {

                for (i in 0 until 60) {

                    val xx = getXCoOrdinateOfCircle(
                        (width / 2).toFloat(),
                        HOUR_OUTER_RADIUS,
                        6.0f * i
                    )
                    val yy = getYCoOrdinateOfCircle(
                        (height / 2).toFloat(),
                        HOUR_OUTER_RADIUS,
                        6.0f * i
                    )
                    val sX = getXCoOrdinateOfCircle(
                        (width / 2).toFloat(),
                        if (i % 5 == 0) HOUR_INNER_RADIUS else MINUTE_INNER_RADIUS,
                        6.0f * i
                    )
                    val sY = getYCoOrdinateOfCircle(
                        (height / 2).toFloat(),
                        if (i % 5 == 0) HOUR_INNER_RADIUS else MINUTE_INNER_RADIUS,
                        6.0f * i
                    )
                    it.drawLine(sX, sY, xx, yy, if (i % 5 == 0) hourPaint else minutePaint)

                }
                val hourX =
                    getXCoOrdinateOfCircle(
                        width * 0.5f,
                        HOUR_OUTER_RADIUS * 0.60f,
                        (360.0f / 43200.0f) * (hour - (3 * 60 * 60))
                    )
                val hourY = getYCoOrdinateOfCircle(
                    (height / 2).toFloat(),
                    HOUR_OUTER_RADIUS * 0.60f,
                    (360.0f / 43200.0f) * (hour - (3 * 60 * 60))
                )
                canvas.drawLine(width * 0.5f, height * 0.5f, hourX, hourY, hourHandPaint)
                val minuteX =
                    getXCoOrdinateOfCircle(
                        width * 0.5f,
                        HOUR_OUTER_RADIUS * 0.80f,
                        0.1f * (minute - 15 * 60)
                    )
                val minuteY = getYCoOrdinateOfCircle(
                    (height / 2).toFloat(),
                    HOUR_OUTER_RADIUS * 0.80f,
                    0.1f * (minute - 15 * 60)
                )
                canvas.drawLine(width * 0.5f, height * 0.5f, minuteX, minuteY, minuteHandPaint)
                val secondX =
                    getXCoOrdinateOfCircle(
                        width * 0.5f,
                        HOUR_OUTER_RADIUS * 0.80f,
                        6.0f * (second - 15)
                    )
                val secondY = getYCoOrdinateOfCircle(
                    (height / 2).toFloat(),
                    HOUR_OUTER_RADIUS * 0.80f,
                    6.0f * (second - 15)
                )
                canvas.drawLine(width * 0.5f, height * 0.5f, secondX, secondY, secondHandPaint)
            }
            it.drawCircle(
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                15.0f,
                bgCirclePaint
            )
        }
    }

    companion object {
        private const val HOUR_OUTER_RADIUS = 250.0f
        private const val HOUR_INNER_RADIUS = 220.0f
        private const val MINUTE_INNER_RADIUS = 230.0f
    }

}