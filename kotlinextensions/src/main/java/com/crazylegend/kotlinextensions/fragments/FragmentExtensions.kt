package com.crazylegend.kotlinextensions.fragments

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.crazylegend.kotlinextensions.context.notification


/**
 * Created by hristijan on 2/22/19 to long live and prosper !
 */


fun Fragment.shortToast(text: String) {
    Toast.makeText(this.requireActivity(), text, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(text: String) {
    Toast.makeText(this.requireActivity(), text, Toast.LENGTH_LONG).show()
}

fun Fragment.finish() {
    this.requireActivity().finish()
}

inline fun <reified T> Fragment.launch() {
    this.requireActivity().startActivity(Intent(this.requireActivity(), T::class.java))
}


fun Fragment.getAppCompatActivity(): AppCompatActivity {
    return this.requireActivity() as AppCompatActivity
}

inline fun Fragment.notification(body: NotificationCompat.Builder.() -> Unit, channelID:String) = requireActivity().notification(body, channelID)
