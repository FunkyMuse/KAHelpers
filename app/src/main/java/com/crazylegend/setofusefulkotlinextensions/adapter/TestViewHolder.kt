package com.crazylegend.setofusefulkotlinextensions.adapter

import android.animation.ObjectAnimator
import android.os.SystemClock
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.crazylegend.kotlinextensions.viewBinding.viewBinding
import com.crazylegend.kotlinextensions.views.setPrecomputedText
import com.crazylegend.setofusefulkotlinextensions.R
import com.crazylegend.setofusefulkotlinextensions.databinding.RecyclerViewItemBinding

class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val FADE_DURATION = 1000L
    private val card by viewBinding(RecyclerViewItemBinding::bind)

    private val animation = ObjectAnimator.ofFloat(itemView, View.ALPHA, 1f, 0f, 1f).apply {
        repeatCount = ObjectAnimator.INFINITE
        duration = FADE_DURATION
        // Reset the alpha on animation end.
        doOnEnd { itemView.alpha = 1f }
    }

    init {
        with(card.card) {
            setContentPadding(10, 10, 10, 10)
        }
    }

    fun bind(item: TestModel) {
        animation.end()
        card.content.setBackgroundResource(0)
        card.title.setBackgroundResource(0)
        card.image.setImageResource(R.drawable.pin_code_highlight_state)
        card.content.setPrecomputedText(item.body)
        card.title.setPrecomputedText(item.title)
    }

    fun showPlaceHolder() {
        // Shift the timing of fade-in/out for each item by its adapter position. We use the
        // elapsed real time to make this independent from the timing of method call.
        animation.currentPlayTime =
                (SystemClock.elapsedRealtime() - adapterPosition * 30L) % FADE_DURATION
        animation.start()
        // Show the placeholder UI.
        card.image.setImageResource(R.drawable.image_placeholder)
        card.title.text = null
        card.content.text = null
        card.content.setBackgroundResource(R.drawable.text_placeholder)
        card.title.setBackgroundResource(R.drawable.text_placeholder)
    }
}