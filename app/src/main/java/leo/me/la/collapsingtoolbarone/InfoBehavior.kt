package leo.me.la.collapsingtoolbarone

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Color
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

@SuppressWarnings("unused")
class InfoBehavior(context: Context, attributes: AttributeSet)
    : AppbarScrollBehavior<LinearLayout>(context, attributes) {
    private var maxHeight: Int = 0

    private val minHeight = minimumAppBarHeight * 2 / 3

    private val collapsedY: Float = (minimumAppBarHeight - minHeight) / 2F
    private val collapsedX: Float = minimumAppBarHeight.toFloat()
    private var collapsedWidth: Int = 0
    private var expandedY: Float = 0F
    private var expandedX: Float = 0F
    private var expandedWidth: Int = 0
    private var isInitialized: Boolean = false

    private val colorPicker = ArgbEvaluator()

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: LinearLayout, dependency: View): Boolean {
        if (!isInitialized) {
            maxHeight = child.height
            expandedX = child.x
            expandedWidth = child.width
            collapsedWidth = (dependency.width - collapsedX).toInt()
            expandedY = dependency.height - maxHeight.toFloat() - (child.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin
            isInitialized = true
            for (i in 0..child.childCount) {
                (child.getChildAt(i) as? TextView)?.run {
                    setTag(R.string.initial_text_color, currentTextColor)
                }
            }
        }

        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun animateBaseOnAppbarMovement(offset: Float, parent: CoordinatorLayout, child: LinearLayout, dependency: View) {
        child.apply {
            x = offset * (expandedX - collapsedX) + collapsedX
            y = offset * (expandedY - collapsedY) + collapsedY
        }

        val lp = (child.layoutParams as CoordinatorLayout.LayoutParams)
            .apply {
                height = ((maxHeight - minHeight) * offset + minHeight).toInt()
                width = ((expandedWidth - collapsedWidth) * offset + collapsedWidth).toInt()
            }

        for (i in 0..child.childCount) {
            (child.getChildAt(i) as? TextView)?.run {
                setTextColor(colorPicker.evaluate(offset, Color.WHITE, getTag(R.string.initial_text_color)) as Int)
            }
        }
        child.layoutParams = lp
    }
}

