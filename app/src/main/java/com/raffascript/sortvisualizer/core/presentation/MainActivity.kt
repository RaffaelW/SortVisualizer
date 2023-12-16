package com.raffascript.sortvisualizer.core.presentation

import android.animation.ObjectAnimator
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.raffascript.sortvisualizer.core.presentation.theme.AlgorithmsVisualizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setOnExitAnimationListener { screen ->
                try {
                    val zoomX = ObjectAnimator.ofFloat(screen.iconView, View.SCALE_X, .5f, 0f).apply {
                        interpolator = OvershootInterpolator()
                        duration = 500L
                        doOnEnd { screen.remove() }
                    }
                    val zoomY = ObjectAnimator.ofFloat(screen.iconView, View.SCALE_Y, .5f, 0f).apply {
                        interpolator = OvershootInterpolator()
                        duration = 500L
                        doOnEnd { screen.remove() }
                    }
                    zoomX.start()
                    zoomY.start()
                } catch (e: NullPointerException) {
                    screen.remove()
                }
            }
        }
        super.onCreate(savedInstanceState)
        setContent {
            AlgorithmsVisualizerTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // hide/show window insets based on screen orientation
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.statusBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            isOrientationLandscape = true
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.statusBars())
            isOrientationLandscape = false
        }
    }

    companion object {
        var isOrientationLandscape = false
    }
}