package dev.funkymuse.compose.core.utils

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule


internal fun generateRandomIntegerList(size: Int): MutableList<Int> {
    val resultList = ArrayList<Int>(size)
    for (i in 1..size) {
        resultList.add(kotlin.random.Random.nextInt(0, size))
    }
    return resultList
}

inline fun <reified A : ComponentActivity> createAndroidComposeRule(intent: Intent): AndroidComposeTestRule<ActivityScenarioRule<A>, A> {
    return AndroidComposeTestRule(
        activityRule = ActivityScenarioRule<A>(intent),
        activityProvider = { scenario -> scenario.getActivity() }
    )
}

fun <A : ComponentActivity> ActivityScenarioRule<A>.getActivity(): A {
    var activity: A? = null
    scenario.onActivity { activity = it }
    if (activity == null) {
        throw IllegalStateException("Activity was not set in the ActivityScenarioRule!")
    }
    return activity!!
}