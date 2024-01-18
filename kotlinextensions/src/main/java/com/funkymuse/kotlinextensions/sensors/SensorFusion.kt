package com.funkymuse.kotlinextensions.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.util.Timer
import java.util.TimerTask
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

open class SensorFusion(context: Context) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // angular speeds from gyro
    private val gyro = FloatArray(3)

    // rotation matrix from gyro data
    private var gyroMatrix = FloatArray(9)

    // orientation angles from gyro matrix
    private val gyroOrientation = FloatArray(3)

    // magnetic field vector
    private val magnet = FloatArray(3)

    // accelerometer vector
    private val accel = FloatArray(3)

    // orientation angles from accel and magnet
    private val accMagOrientation: FloatArray = FloatArray(3)

    // final orientation angles from sensor fusion
    val fusedOrientation = FloatArray(3)

    // accelerometer and magnetometer based rotation matrix
    private val rotationMatrix = FloatArray(9)
    private var timestamp: Long = 0
    private var initState = true
    private val fuseTimer = Timer()

    init {
        gyroOrientation[0] = 0.0f
        gyroOrientation[1] = 0.0f
        gyroOrientation[2] = 0.0f

        // initialise gyroMatrix with identity matrix
        gyroMatrix[0] = 1.0f
        gyroMatrix[1] = 0.0f
        gyroMatrix[2] = 0.0f
        gyroMatrix[3] = 0.0f
        gyroMatrix[4] = 1.0f
        gyroMatrix[5] = 0.0f
        gyroMatrix[6] = 0.0f
        gyroMatrix[7] = 0.0f
        gyroMatrix[8] = 1.0f

        // get sensorManager and initialise sensor listeners
        initListeners()

        // wait for one second until gyroscope and magnetometer/accelerometer
        // data is initialised then scedule the complementary filter task
        fuseTimer.scheduleAtFixedRate(CalculateFusedOrientationTask(),
                1000, TIME_CONSTANT.toLong())
    }

    // This function registers sensor listeners for the accelerometer, magnetometer and gyroscope.
    private fun initListeners() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                // copy new accelerometer data into accel array and calculate orientation
                System.arraycopy(event.values, 0, accel, 0, 3)
                calculateAccMagOrientation()
            }
            Sensor.TYPE_GYROSCOPE ->                 // process gyro data
                gyroFunction(event)
            Sensor.TYPE_MAGNETIC_FIELD ->                 // copy new magnetometer data into magnet array
                System.arraycopy(event.values, 0, magnet, 0, 3)
        }
    }

    // calculates orientation angles from accelerometer and magnetometer output
    private fun calculateAccMagOrientation() {
        if (SensorManager.getRotationMatrix(rotationMatrix, null, accel, magnet)) {
            SensorManager.getOrientation(rotationMatrix, accMagOrientation)
        }
    }

    // This function is borrowed from the Android reference
    // at http://developer.android.com/reference/android/hardware/SensorEvent.html#values
    // It calculates a rotation vector from the gyroscope angular speed values.
    private fun getRotationVectorFromGyro(gyroValues: FloatArray,
                                          deltaRotationVector: FloatArray,
                                          timeFactor: Float) {
        val normValues = FloatArray(3)

        // Calculate the angular speed of the sample
        val omegaMagnitude = sqrt(gyroValues[0] * gyroValues[0] + gyroValues[1] * gyroValues[1] + (
                gyroValues[2] * gyroValues[2]).toDouble()).toFloat()

        // Normalize the rotation vector if it's big enough to get the axis
        if (omegaMagnitude > EPSILON) {
            normValues[0] = gyroValues[0] / omegaMagnitude
            normValues[1] = gyroValues[1] / omegaMagnitude
            normValues[2] = gyroValues[2] / omegaMagnitude
        }

        // Integrate around this axis with the angular speed by the timestep
        // in order to get a delta rotation from this sample over the timestep
        // We will convert this axis-angle representation of the delta rotation
        // into a quaternion before turning it into the rotation matrix.
        val thetaOverTwo = omegaMagnitude * timeFactor
        val sinThetaOverTwo = sin(thetaOverTwo.toDouble()).toFloat()
        val cosThetaOverTwo = cos(thetaOverTwo.toDouble()).toFloat()
        deltaRotationVector[0] = sinThetaOverTwo * normValues[0]
        deltaRotationVector[1] = sinThetaOverTwo * normValues[1]
        deltaRotationVector[2] = sinThetaOverTwo * normValues[2]
        deltaRotationVector[3] = cosThetaOverTwo
    }

    // This function performs the integration of the gyroscope data.
    // It writes the gyroscope based orientation into gyroOrientation.
    private fun gyroFunction(event: SensorEvent) {
        // don't start until first accelerometer/magnetometer orientation has been acquired

        // initialisation of the gyroscope based rotation matrix
        if (initState) {
            val initMatrix: FloatArray = getRotationMatrixFromOrientation(accMagOrientation)
            val test = FloatArray(3)
            SensorManager.getOrientation(initMatrix, test)
            gyroMatrix = matrixMultiplication(gyroMatrix, initMatrix)
            initState = false
        }

        // copy the new gyro values into the gyro array
        // convert the raw gyro data into a rotation vector
        val deltaVector = FloatArray(4)
        if (timestamp != 0L) {
            val dT = (event.timestamp - timestamp) * NS2S
            System.arraycopy(event.values, 0, gyro, 0, 3)
            getRotationVectorFromGyro(gyro, deltaVector, dT / 2.0f)
        }

        // measurement done, save current time for next interval
        timestamp = event.timestamp

        // convert rotation vector into rotation matrix
        val deltaMatrix = FloatArray(9)
        SensorManager.getRotationMatrixFromVector(deltaMatrix, deltaVector)

        // apply the new rotation interval on the gyroscope based rotation matrix
        gyroMatrix = matrixMultiplication(gyroMatrix, deltaMatrix)

        // get the gyroscope based orientation from the rotation matrix
        SensorManager.getOrientation(gyroMatrix, gyroOrientation)
    }

    private fun getRotationMatrixFromOrientation(o: FloatArray): FloatArray {
        val xM = FloatArray(9)
        val yM = FloatArray(9)
        val zM = FloatArray(9)
        val sinX = sin(o[1].toDouble()).toFloat()
        val cosX = cos(o[1].toDouble()).toFloat()
        val sinY = sin(o[2].toDouble()).toFloat()
        val cosY = cos(o[2].toDouble()).toFloat()
        val sinZ = sin(o[0].toDouble()).toFloat()
        val cosZ = cos(o[0].toDouble()).toFloat()

        // rotation about x-axis (pitch)
        xM[0] = 1.0f
        xM[1] = 0.0f
        xM[2] = 0.0f
        xM[3] = 0.0f
        xM[4] = cosX
        xM[5] = sinX
        xM[6] = 0.0f
        xM[7] = -sinX
        xM[8] = cosX

        // rotation about y-axis (roll)
        yM[0] = cosY
        yM[1] = 0.0f
        yM[2] = sinY
        yM[3] = 0.0f
        yM[4] = 1.0f
        yM[5] = 0.0f
        yM[6] = -sinY
        yM[7] = 0.0f
        yM[8] = cosY

        // rotation about z-axis (azimuth)
        zM[0] = cosZ
        zM[1] = sinZ
        zM[2] = 0.0f
        zM[3] = -sinZ
        zM[4] = cosZ
        zM[5] = 0.0f
        zM[6] = 0.0f
        zM[7] = 0.0f
        zM[8] = 1.0f

        // rotation order is y, x, z (roll, pitch, azimuth)
        var resultMatrix = matrixMultiplication(xM, yM)
        resultMatrix = matrixMultiplication(zM, resultMatrix)
        return resultMatrix
    }

    private fun matrixMultiplication(A: FloatArray, B: FloatArray): FloatArray {
        val result = FloatArray(9)
        result[0] = A[0] * B[0] + A[1] * B[3] + A[2] * B[6]
        result[1] = A[0] * B[1] + A[1] * B[4] + A[2] * B[7]
        result[2] = A[0] * B[2] + A[1] * B[5] + A[2] * B[8]
        result[3] = A[3] * B[0] + A[4] * B[3] + A[5] * B[6]
        result[4] = A[3] * B[1] + A[4] * B[4] + A[5] * B[7]
        result[5] = A[3] * B[2] + A[4] * B[5] + A[5] * B[8]
        result[6] = A[6] * B[0] + A[7] * B[3] + A[8] * B[6]
        result[7] = A[6] * B[1] + A[7] * B[4] + A[8] * B[7]
        result[8] = A[6] * B[2] + A[7] * B[5] + A[8] * B[8]
        return result
    }

    internal inner class CalculateFusedOrientationTask : TimerTask() {
        override fun run() {
            val oneMinusCoeff = 1.0f - FILTER_COEFFICIENT

            /*
             * Fix for 179� <--> -179� transition problem:
             * Check whether one of the two orientation angles (gyro or accMag) is negative while the other one is positive.
             * If so, add 360� (2 * math.PI) to the negative value, perform the sensor fusion, and remove the 360� from the result
             * if it is greater than 180�. This stabilizes the output in positive-to-negative-transition cases.
             */

            // azimuth
            if (gyroOrientation[0] < -0.5 * Math.PI && accMagOrientation[0] > 0.0) {
                fusedOrientation[0] = (FILTER_COEFFICIENT * (gyroOrientation[0] + 2.0 * Math.PI) + oneMinusCoeff * accMagOrientation[0]).toFloat()
                fusedOrientation[0].minus(if (fusedOrientation[0] > Math.PI) 2.0 * Math.PI else 0.toDouble())
            } else if (accMagOrientation[0] < -0.5 * Math.PI && gyroOrientation[0] > 0.0) {
                fusedOrientation[0] = (FILTER_COEFFICIENT * gyroOrientation[0] + oneMinusCoeff * (accMagOrientation[0] + 2.0 * Math.PI)).toFloat()
                fusedOrientation[0].minus(if (fusedOrientation[0] > Math.PI) 2.0 * Math.PI else 0.toDouble())
            } else {
                fusedOrientation[0] = FILTER_COEFFICIENT * gyroOrientation[0] + oneMinusCoeff * accMagOrientation[0]
            }

            // pitch
            if (gyroOrientation[1] < -0.5 * Math.PI && accMagOrientation[1] > 0.0) {
                fusedOrientation[1] = (FILTER_COEFFICIENT * (gyroOrientation[1] + 2.0 * Math.PI) + oneMinusCoeff * accMagOrientation[1]).toFloat()
                fusedOrientation[1].minus(if (fusedOrientation[1] > Math.PI) 2.0 * Math.PI else 0.toDouble())
            } else if (accMagOrientation[1] < -0.5 * Math.PI && gyroOrientation[1] > 0.0) {
                fusedOrientation[1] = (FILTER_COEFFICIENT * gyroOrientation[1] + oneMinusCoeff * (accMagOrientation[1] + 2.0 * Math.PI)).toFloat()
                fusedOrientation[1].minus(if (fusedOrientation[1] > Math.PI) 2.0 * Math.PI else 0.toDouble())
            } else {
                fusedOrientation[1] = FILTER_COEFFICIENT * gyroOrientation[1] + oneMinusCoeff * accMagOrientation[1]
            }

            // roll
            if (gyroOrientation[2] < -0.5 * Math.PI && accMagOrientation[2] > 0.0) {
                fusedOrientation[2] = (FILTER_COEFFICIENT * (gyroOrientation[2] + 2.0 * Math.PI) + oneMinusCoeff * accMagOrientation[2]).toFloat()

                fusedOrientation[2].minus(if (fusedOrientation[2] > Math.PI) 2.0 * Math.PI else 0.toDouble())
            } else if (accMagOrientation[2] < -0.5 * Math.PI && gyroOrientation[2] > 0.0) {
                fusedOrientation[2] = (FILTER_COEFFICIENT * gyroOrientation[2] + oneMinusCoeff * (accMagOrientation[2] + 2.0 * Math.PI)).toFloat()
                fusedOrientation[2].minus(if (fusedOrientation[2] > Math.PI) 2.0 * Math.PI else 0.toDouble())
            } else {
                fusedOrientation[2] = FILTER_COEFFICIENT * gyroOrientation[2] + oneMinusCoeff * accMagOrientation[2]
            }

            // overwrite gyro matrix and orientation with fused orientation
            // to comensate gyro drift
            gyroMatrix = getRotationMatrixFromOrientation(fusedOrientation)
            System.arraycopy(fusedOrientation, 0, gyroOrientation, 0, 3)
        }
    }

    companion object {
        private const val EPSILON = 0.000000001f
        private const val NS2S = 1.0f / 1000000000.0f
        private const val TIME_CONSTANT = 30
        private const val FILTER_COEFFICIENT = 0.98f
    }

}