package net.firzen.android.learning.repositoriesapp.domain

import android.os.CountDownTimer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

private const val COUNTDOWN_TIME_LIMIT = 60000L
private const val COUNTDOWN_INTERVAL = 1000L

class CustomCountdown(private val onTick: ((currentValue: Int) -> Unit),
                      private val onFinish: (() -> Unit)) : DefaultLifecycleObserver {

    var timer: InternalTimer = InternalTimer(
        onTick = onTick,
        onFinish = onFinish,
        millisInFuture = COUNTDOWN_TIME_LIMIT,
        countDownInterval = COUNTDOWN_INTERVAL
    )

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        // unless the user already won the prize (that means time was 0), then if `onResume()`
        // is called, we cancel the current timer, take its `lastKnownTime` value, and use it
        // as a starting point of a new timer instance
        if (timer.lastKnownTime > 0) {
            timer.cancel()
            timer = InternalTimer(
                onTick = onTick,
                onFinish = onFinish,
                millisInFuture = timer.lastKnownTime,
                countDownInterval = COUNTDOWN_INTERVAL
            )
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        timer.cancel()
    }

    fun stop() {
        timer.cancel()
    }

    class InternalTimer(private val onTick: ((currentValue: Int) -> Unit),
                        private val onFinish: (() -> Unit),
                        millisInFuture: Long,
                        countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        // keeps last known countdown state, so that we can pause and continue the timer
        var lastKnownTime: Long = millisInFuture

        init {
            // starts the countdown immediately after new instance of this class is created
            this.start()
        }

        // called when countdown is finished
        override fun onFinish() {
            lastKnownTime = 0
            onFinish.invoke()
        }

        // count every time the remaining time has changed (once per second in our case)
        override fun onTick(millisUntilFinished: Long) {
            lastKnownTime = millisUntilFinished
            onTick(millisUntilFinished.toInt())
        }
    }
}
