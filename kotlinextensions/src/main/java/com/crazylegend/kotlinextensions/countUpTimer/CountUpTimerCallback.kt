package com.crazylegend.kotlinextensions.countUpTimer


/**
 * Created by crazy on 2/25/20 to long live and prosper !
 */
interface CountUpTimerCallback {

    /**
     * Method to be called every second by the [CountUpTimer]
     *
     * @param timeRemaining: Time remaining in milliseconds.
     */
    fun onTimerTick(timeRemaining: Long)

    /**
     * Method to be called by [CountUpTimer] when the thread is getting  IsFinished
     */
    fun onTimerFinish()

}