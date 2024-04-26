package com.mel.debora_v11.utilities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

class WaveformView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var paint = Paint()
    private var amplitudes = ArrayList<Float>()
    private var spikes = ArrayList<RectF>()

    private val TAG = "deb11*"

    private var radius = 6f
    private var w = 9f

    private var sw = 0f
    private var sh = 400f

    private var d = 6f

    private var maxSpikes = 0

    init{
        paint.color = Color.rgb(244, 81, 30)
        sw = resources.displayMetrics.widthPixels.toFloat()

        maxSpikes = (sw / (w + d)).toInt()
    }

    fun addAmplitude(amp: Float){
//        var norm = Math.min(amp.toInt()/7, 400).toFloat()
        var norm = amp * 10
        amplitudes.add(norm)
        Log.d(TAG, "addAmplitude: $amp")

        spikes.clear()
        var amps = amplitudes.takeLast(maxSpikes)
        for (i in amps.indices){
            var left = sw - i*(w + d)
            var top = sh / 2 - amps[i] / 2
            var right = left + w
            var bottom = top + amps[i]

            spikes.add(RectF(left, top, right, bottom))

        }

        invalidate()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
//        canvas?.drawRoundRect(RectF(20f, 30f, 20+30f, 30f + 60f), 6f,6f, paint)

        spikes.forEach{
            canvas?.drawRoundRect(it, radius, radius, paint)
        }
    }

}