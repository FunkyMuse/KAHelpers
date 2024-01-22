package dev.funkymuse.kotlinextensions.countUpTimer



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