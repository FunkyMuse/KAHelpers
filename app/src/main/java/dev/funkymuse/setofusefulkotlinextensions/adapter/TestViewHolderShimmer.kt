package dev.funkymuse.setofusefulkotlinextensions.adapter

import android.animation.ObjectAnimator
import android.os.SystemClock
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import dev.funkymuse.kotlinextensions.views.setPrecomputedText
import dev.funkymuse.setofusefulkotlinextensions.R
import dev.funkymuse.setofusefulkotlinextensions.customviews.databinding.CustomizableCardViewBinding

class TestViewHolderShimmer(private val binding: CustomizableCardViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val FADE_DURATION = 1000L
    }

    private val animation = ObjectAnimator.ofFloat(itemView, View.ALPHA, 1f, 0f, 1f).apply {
        repeatCount = ObjectAnimator.INFINITE
        duration = FADE_DURATION
        // Reset the alpha on animation end.
        doOnEnd { itemView.alpha = 1f }
    }

    init {
        with(binding.card) {
            setContentPadding(10, 10, 10, 10)
        }
    }

    fun bind(item: TestModel) {
        animation.end()
        binding.content.setBackgroundResource(0)
        binding.title.setBackgroundResource(0)
        binding.image.setImageResource(dev.funkymuse.setofusefulkotlinextensions.customviews.R.drawable.pin_code_highlight_state)
        binding.content.setPrecomputedText(item.body)
        binding.title.setPrecomputedText(item.title)
    }

    fun showPlaceHolder() {
        // Shift the timing of fade-in/out for each item by its adapter position. We use the
        // elapsed real time to make this independent from the timing of method call.
        animation.currentPlayTime =
            (SystemClock.elapsedRealtime() - bindingAdapterPosition * 30L) % FADE_DURATION
        animation.start()
        // Show the placeholder UI.
        binding.image.setImageResource(R.drawable.image_placeholder)
        binding.title.text = null
        binding.content.text = null
        binding.content.setBackgroundResource(R.drawable.text_placeholder)
        binding.title.setBackgroundResource(R.drawable.text_placeholder)
    }
}