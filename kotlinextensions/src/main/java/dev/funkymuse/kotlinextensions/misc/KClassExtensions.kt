package dev.funkymuse.kotlinextensions.misc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlin.reflect.KClass





/**
 * Start an activity from the class
 * @param context Context to start the activity from
 * @param with Optional bundle
 */
fun <T : Any> KClass<T>.start(context: Context, with: ((bundle: Bundle) -> Unit)? = null) {
    val intent = Intent(context, this.java)
    if (with != null) {
        val bundle = Bundle()
        with(bundle)
        intent.putExtras(bundle)
    }
    context.startActivity(intent)
}

/**
 * Start an activity from the class for the result
 * @param context Context to start the activity from
 * @param requestCode Request code for the activity callback
 * @param with Optional bundle
 */
fun <T : Any> KClass<T>.startForResult(context: Activity, requestCode: Int, with: ((bundle: Bundle) -> Unit)? = null) {
    val intent = Intent(context, this.java)
    if (with != null) {
        val bundle = Bundle()
        with(bundle)
        intent.putExtras(bundle)
    }
    context.startActivityForResult(intent, requestCode)
}

/**
 * Start an activity from the class and clear the stack
 * @param context Context to start the activity from
 * @param with Optional bundle
 */
fun <T : Any> KClass<T>.startClearStack(context: Context, with: ((bundle: Bundle) -> Unit)? = null) {
    val intent = Intent(context, this.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    if (with != null) {
        val bundle = Bundle()
        with(bundle)
        intent.putExtras(bundle)
    }
    context.startActivity(intent)
}