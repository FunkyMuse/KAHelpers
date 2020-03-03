package com.crazylegend.kotlinextensions.viewBinding

import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


inline fun <T : ViewBinding> AppCompatActivity.viewBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)
        }


fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
        FragmentViewBindingDelegate(this, viewBindingFactory)


fun <T : ViewBinding> globalViewBinding(viewBindingFactory: (View) -> T) =
        GlobalViewBindingDelegate(viewBindingFactory)

fun <T: ViewBinding>RecyclerView.ViewHolder.viewBinding(viewBindingFactory: (View) -> T) = RecyclerViewBindingDelegate(viewBindingFactory)