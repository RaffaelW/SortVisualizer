package com.raffascript.sortvisualizer.presentation.visualizer.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.raffascript.sortvisualizer.data.viszualization.Highlight
import com.raffascript.sortvisualizer.data.viszualization.HighlightOption
import com.raffascript.sortvisualizer.data.viszualization.getHighlightWithHighestPriority
import com.raffascript.sortvisualizer.data.viszualization.getHighlightsWithOption

class ChartSurfaceView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private val paintBackground = Paint().apply {
        color = Color.BLACK
    }
    private val paintBar = Paint().apply {
        color = Color.WHITE
    }
    private val paintHighlightPrimary = Paint().apply {
        color = Color.RED
    }
    private val paintHighlightSecondary = Paint().apply {
        color = Color.rgb(153, 255, 153)
    }
    private val paintHighlightLine = Paint().apply {
        strokeWidth = 0f
    }

    private var thread: Thread? = null
    private var threadRunning = false

    private val surfaceHolder = this.holder
    private var canvas: Canvas? = null
    private var chartWidth = 0
    private var chartHeight = 0

    private var sortingList = intArrayOf()
    private var highlights = emptyList<Highlight>()

    private var isNewDataAvailable = true
    private val minRedrawRate = 1000 / 60L

    init {
        surfaceHolder.addCallback(this)
        setZOrderOnTop(false)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startThread()
        chartWidth = width
        chartHeight = height
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        chartWidth = width
        chartHeight = height
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopThread()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        thread ?: return
        if (visibility == VISIBLE) {
            if (thread?.isAlive != true) {
                startThread()
            }
        } else if (visibility == INVISIBLE) {
            stopThread()
        }
    }

    private fun startThread() {
        thread = Thread {
            run()
        }
        threadRunning = true
        thread!!.start()
    }

    private fun stopThread() {
        // The Thread will jump out of the while loop and complete
        threadRunning = false
        thread = null
    }

    fun updateData(sortingList: IntArray, highlights: List<Highlight>) {
        this.sortingList = sortingList
        this.highlights = highlights
        isNewDataAvailable = true
    }

    private fun run() {
        var startTime = 0L
        while (threadRunning) {
            if (!isNewDataAvailable) {
                Thread.yield()
                continue
            }
            val deltaTime = System.currentTimeMillis() - startTime
            if (deltaTime < minRedrawRate) {
                try {
                    Thread.sleep(minRedrawRate - deltaTime)
                } catch (e: InterruptedException) {
                    Log.e("ChartSurfaceView", "Thread was interrupted while sleeping. Message: ${e.message}")
                }
            }
            startTime = System.currentTimeMillis()

            drawChart()
            isNewDataAvailable = false
        }
    }

    private fun drawChart() {
        val barWidth = chartWidth.toFloat() / sortingList.size
        val unitHeight = chartHeight.toFloat() / sortingList.size

        canvas = surfaceHolder.lockCanvas()
        canvas!!.drawBackground()
        sortingList.forEachIndexed { index, value ->
            canvas!!.drawBar(index, barWidth, unitHeight * value, highlights.getHighlightWithHighestPriority(index))
        }
        highlights.getHighlightsWithOption(HighlightOption.LINE).forEach {
            canvas!!.drawHighlightLine(barWidth * (it.index + 1) - 1, Color.GREEN)
        }
        surfaceHolder.unlockCanvasAndPost(canvas)
    }

    private fun Canvas.drawBackground() {
        this.drawRect(0F, 0F, chartWidth.toFloat(), chartHeight.toFloat(), paintBackground)
    }

    private fun Canvas.drawBar(position: Int, width: Float, height: Float, highlight: HighlightOption?) {
        val left = width * position
        val paint = when (highlight) {
            HighlightOption.COLOURED_PRIMARY -> paintHighlightPrimary
            HighlightOption.COLOURED_SECONDARY -> paintHighlightSecondary
            else -> paintBar
        }
        this.drawRect(left, chartHeight - height, left + width + 1, chartHeight.toFloat(), paint)
    }

    private fun Canvas.drawHighlightLine(xOffset: Float, color: Int) {
        val paint = paintHighlightLine.apply {
            this.color = color
        }
        this.drawLine(xOffset, 0F, xOffset, chartHeight.toFloat(), paint)
    }
}