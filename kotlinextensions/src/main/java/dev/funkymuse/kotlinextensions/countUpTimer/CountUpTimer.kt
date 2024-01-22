package dev.funkymuse.kotlinextensions.countUpTimer

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.os.Message




/**
 *  val countUpTimer = object : CountUpTimer(ALLOWED_MS_OF_INACTIVITY) {
override fun onTimerTick(timeRemaining: Long) {

}

override fun onTimerFinish() {

}
}
countUpTimer.startTimer()
 */
abstract class CountUpTimer : CountUpTimerCallback {

    /**
     * To maintain Timer start and stop status.
     */
    /**
     * Convenience method to check whether the timer is running or not
     *
     * @return: true if timer is running, else false.
     */
    @get:Synchronized
    var isRunning: Boolean = false
        private set
    /**
     * To maintain Timer resume and pause status.
     */
    /**
     * Method to check whether the timer is paused.
     *
     * @return: true if timer is paused else false.
     */
    /**
     * To pause the timer from Main thread.
     *
     * @param isPaused: true to pause the timer, false to resume.
     */
    @get:Synchronized
    var isPaused: Boolean = false
        private set

    /**
     * Timer time.
     */
    private var time: Long = 0
    private var localTime: Long = 0
    private var interval: Long = 0
    private var handler: Handler? = null

    /**
     * @return remaining time
     */
    val remainingTime: Long
        get() = if (isRunning) {
            this.time
        } else 0

    constructor() {
        init(0, INTERVAL.toLong())
    }

    constructor(timeInMillis: Long) {
        init(timeInMillis, INTERVAL.toLong())
    }

    constructor(timeInMillis: Long, intervalInMillis: Long) {
        init(timeInMillis, intervalInMillis)
    }

    /**
     *
     * @param time:     Time in milliseconds.
     * @param interval: in milliseconds.
     */
    private fun init(time: Long, interval: Long) {
        setTime(time)
        setInterval(interval)
        initListener()
    }

    @SuppressLint("HandlerLeak")
    private fun initListener() {

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == MSG) {
                    if (!isPaused) {
                        if (localTime <= time) {
                            onTimerTick(time - localTime)
                            localTime += interval
                            sendMessageDelayed(handler!!.obtainMessage(MSG), interval)
                        } else
                            stopTimer()

                    }
                }
            }
        }
    }

    /**
     * Method to start the timer.
     */
    @Synchronized
    fun startTimer() {
        if (isRunning)
            return


        isRunning = true
        isPaused = false
        localTime = 0
        handler?.obtainMessage()?.apply {
            handler?.sendMessage(this)
        }
    }

    /**
     * Method to stop the timer.
     */
    @Synchronized
    fun stopTimer(shouldCallTimerFinish: Boolean = true) {

        isRunning = false
        handler?.removeMessages(MSG)

        if (shouldCallTimerFinish)
            onTimerFinish()

    }

    /**
     * Convenience method to pause the timer.
     */
    @Synchronized
    fun pauseTimer() {
        isPaused = true
    }

    /**
     * Convenience method to resume the timer.
     */
    @Synchronized
    fun resumeTimer() {
        isPaused = false

        handler!!.sendMessage(handler!!.obtainMessage(MSG))
    }

    /**
     * Setter for Time.
     *
     * @param timeInMillis: in milliseconds
     */
    fun setTime(timeInMillis: Long) {
        var inMillis = timeInMillis
        if (isRunning)
            return

        if (this.time <= 0)
            if (inMillis < 0)
                inMillis *= -1
        this.time = inMillis
    }

    /**
     * Setter for interval.
     *
     * @param intervalInMillis: in milliseconds
     */
    fun setInterval(intervalInMillis: Long) {
        var inMillis = intervalInMillis
        if (isRunning)
            return

        if (this.interval <= 0)
            if (inMillis < 0)
                inMillis *= -1
        this.interval = inMillis
    }

    companion object {

        private val INTERVAL = 1000
        private val MSG = 1
    }
}