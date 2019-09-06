package com.crazylegend.kotlinextensions.jobscheduler

import android.Manifest.permission.RECEIVE_BOOT_COMPLETED
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import androidx.annotation.RequiresPermission
import com.crazylegend.kotlinextensions.context.activityManager
import com.crazylegend.kotlinextensions.context.jobScheduler


/**
 * Created by hristijan on 4/30/19 to long live and prosper !
 */

@RequiresPermission(allOf = [RECEIVE_BOOT_COMPLETED])
inline fun <reified T> JobScheduler?.scheduleJob(context: Context, id: Int) {
    this?.schedule(
            JobInfo.Builder(
                    id,
                    ComponentName(context, T::class.java)
            )
                    .setPersisted(true)
                    .setOverrideDeadline(500)
                    .setRequiresDeviceIdle(false)
                    .build()
    )
}


@RequiresPermission(allOf = [RECEIVE_BOOT_COMPLETED])
inline fun <reified T> JobScheduler?.scheduleJob(context: Context, id: Int, overrideDeadline: Long = 500, requiresDeviceIdle: Boolean = false, isPersisted: Boolean = true) {
    this?.schedule(
            JobInfo.Builder(
                    id,
                    ComponentName(context, T::class.java)
            )
                    .setPersisted(isPersisted)
                    .setOverrideDeadline(overrideDeadline)
                    .setRequiresDeviceIdle(requiresDeviceIdle)
                    .build()
    )
}


@RequiresPermission(allOf = [RECEIVE_BOOT_COMPLETED])
inline fun <reified T> JobScheduler?.scheduleJob(context: Context,
                                                 id: Int, overrideDeadline: Long = 500,
                                                 requiresDeviceIdle: Boolean = false,
                                                 isPersisted: Boolean = true, jobInfo: JobInfo.() -> Unit) {

    val jInfo = JobInfo.Builder(
            id,
            ComponentName(context, T::class.java)
    )
            .setPersisted(isPersisted)
            .setOverrideDeadline(overrideDeadline)
            .setRequiresDeviceIdle(requiresDeviceIdle).build()

    jobInfo.invoke(jInfo)
    this?.schedule(
            jInfo
    )
}


@RequiresPermission(allOf = [RECEIVE_BOOT_COMPLETED])
inline fun <reified T> JobScheduler?.scheduleJob(context: Context,
                                                 jobInfoBuilder: JobInfo.Builder.() -> Unit,
                                                 id: Int, overrideDeadline: Long = 500,
                                                 requiresDeviceIdle: Boolean = false,
                                                 isPersisted: Boolean = true) {

    val jInfo = JobInfo.Builder(
            id,
            ComponentName(context, T::class.java)
    )
            .setPersisted(isPersisted)
            .setOverrideDeadline(overrideDeadline)
            .setRequiresDeviceIdle(requiresDeviceIdle)

    jobInfoBuilder.invoke(jInfo)
    this?.schedule(
            jInfo.build()
    )
}


@RequiresPermission(allOf = [RECEIVE_BOOT_COMPLETED])
inline fun <reified T> JobScheduler?.scheduleJob(context: Context,
                                                 jobInfoBuilder: JobInfo.Builder.() -> Unit,
                                                 id: Int) {

    val jInfo = JobInfo.Builder(
            id,
            ComponentName(context, T::class.java)
    )

    jobInfoBuilder.invoke(jInfo)
    this?.schedule(
            jInfo.build()
    )
}


@RequiresPermission(allOf = [RECEIVE_BOOT_COMPLETED])
inline fun <reified T> JobScheduler?.scheduleJob(context: Context, id: Int,
                                                 jobInfoBuilder: JobInfo.Builder.() -> Unit
): JobInfo? {

    val jInfo = JobInfo.Builder(
            id,
            ComponentName(context, T::class.java)
    )

    jobInfoBuilder.invoke(jInfo)

    val jobInfo = jInfo.build()

    this?.schedule(
            jobInfo
    )

    return jobInfo
}

inline fun <reified T> Context.isJobRunning(): Boolean {
    val manager = activityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (T::class.java.name == service.service.className) {
            return true
        }
    }
    return false
}


fun Context.cancelSchedulerJob(id:Int){
    this.jobScheduler?.cancel(id)
}