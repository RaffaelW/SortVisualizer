package com.raffascript.sortvisualizer.visualization.presentation.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import com.raffascript.sortvisualizer.visualization.data.Highlight
import com.raffascript.sortvisualizer.visualization.data.HighlightOption
import com.raffascript.sortvisualizer.visualization.data.getHighlightWithHighestPriority
import com.raffascript.sortvisualizer.visualization.data.getHighlightsWithOption

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
    private var chartWidth = 0
    private var chartHeight = 0

    private var sortingList = intArrayOf()
    private var highlights = emptyList<Highlight>()

    private var isNewDataAvailable = true
    private val maxWaitingInterval = 1000 / 60L

    init {
        surfaceHolder.addCallback(this)
        setZOrderOnTop(false)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        chartWidth = width
        chartHeight = height
        startThread()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        chartWidth = width
        chartHeight = height
        isNewDataAvailable = true
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopThread()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        thread ?: return
        if (visibility == VISIBLE) {
            isNewDataAvailable = true
            if (thread?.isAlive == false) {
                startThread()
            }
        } else if (visibility == INVISIBLE) {
            stopThread()
        }
    }

    private fun startThread() {
        thread = Thread {
            draw()
        }
        threadRunning = true
        isNewDataAvailable = true
        thread!!.start()
    }

    private fun stopThread() {
        // The Thread will jump out of the while loop and complete
        threadRunning = false
        thread = null
    }

    fun updateData(newList: IntArray, highlights: List<Highlight>) {
        this.sortingList = newList
        this.highlights = highlights
        isNewDataAvailable = true
    }

    private fun draw() {
        var lastRedraw = 0L
        var deltaTime: Long
        while (threadRunning) {
            if (isNewDataAvailable) {
                deltaTime = System.currentTimeMillis() - lastRedraw
                if (deltaTime > maxWaitingInterval) {
                    isNewDataAvailable = false
                    lastRedraw = System.currentTimeMillis()

                    drawChart() ?: break
                } else {
                    try {
                        Thread.sleep(maxWaitingInterval - deltaTime)
                    } catch (e: InterruptedException) {
                        Log.e("ChartSurfaceView", "Thread was interrupted while sleeping. Message: ${e.message}")
                    }
                }
            }
        }
    }

    private fun drawChart(): Unit? {
        val barWidth = chartWidth.toFloat() / sortingList.size
        val unitHeight = chartHeight.toFloat() / sortingList.size

        drawWithLockedCanvasOrNull {
            it.drawBackground()

            sortingList.forEachIndexed { index, value ->
                it.drawBar(index, barWidth, unitHeight * value, highlights.getHighlightWithHighestPriority(index))
            }

            highlights.getHighlightsWithOption(HighlightOption.LINE).forEach { highlight ->
                it.drawHighlightLine(barWidth * (highlight.index + 1) - 1, Color.GREEN)
            }
        } ?: return null

        return Unit
    }

    private fun Canvas.drawBackground() {
        this.drawColor(paintBackground.color)
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

    private inline fun drawWithLockedCanvasOrNull(draw: (canvas: Canvas) -> Unit): Unit? {
        val canvas: Canvas
        if (!threadRunning || !surfaceHolder.surface.isValid) return null
        try {
            canvas = surfaceHolder.lockCanvas()
        } catch (e: IllegalStateException) {
            return null
        }

        draw(canvas)

        if (!threadRunning || !surfaceHolder.surface.isValid) return null
        try {
            surfaceHolder.unlockCanvasAndPost(canvas)
        } catch (e: IllegalStateException) {
            return null
        }

        return Unit
    }
}