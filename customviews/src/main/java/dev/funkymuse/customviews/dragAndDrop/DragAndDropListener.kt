package dev.funkymuse.customviews.dragAndDrop

import android.content.ClipDescription
import android.view.DragEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class DragAndDropListener(@DrawableRes private val solidOutline: Int, @DrawableRes private val dashOutline: Int) : View.OnDragListener {
    override fun onDrag(view: View, event: DragEvent): Boolean {
        return when (event.action) {
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.setDashedOutline(); true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                view.setSolidOutline(); true
            }
            DragEvent.ACTION_DROP -> {
                val draggingView = event.localState as View
                val draggingViewParent = draggingView.parent as DragAndDropContainer
                draggingViewParent.removeContent(draggingView)

                val landingContainer = view as DragAndDropContainer
                landingContainer.setContent(draggingView)
                landingContainer.setSolidOutline()
                true
            }
            DragEvent.ACTION_DRAG_STARTED -> event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            else -> true
        }
    }

    private fun View.setSolidOutline() {
        background = ContextCompat.getDrawable(context, solidOutline)
    }

    private fun View.setDashedOutline() {
        background = ContextCompat.getDrawable(context, dashOutline)
    }
}