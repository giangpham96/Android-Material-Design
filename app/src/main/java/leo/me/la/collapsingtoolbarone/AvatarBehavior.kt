package leo.me.la.collapsingtoolbarone

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import de.hdodenhof.circleimageview.CircleImageView

@SuppressWarnings("unused")
class AvatarBehavior(context: Context, attributes: AttributeSet)
    : AppbarScrollBehavior<CircleImageView>(context, attributes) {
    private var maximumSize: Int = 0

    private val minimumSize = minimumAppBarHeight * 2 / 3
    private val collapsedX: Float = (minimumAppBarHeight - minimumSize) / 2F

    private val collapsedY: Float = (minimumAppBarHeight - minimumSize) / 2F
    private var expandedX: Float = 0F
    private var expandedY: Float = 0F
    private var isInitialized: Boolean = false

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: CircleImageView, dependency: View): Boolean {
        if (!isInitialized) {
            maximumSize = child.height
            expandedX = (child.layoutParams as CoordinatorLayout.LayoutParams).leftMargin.toFloat()
            expandedY = dependency.height - maximumSize.toFloat() - (child.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin
            isInitialized = true
        }

        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun animateBaseOnAppbarMovement(offset: Float, parent: CoordinatorLayout, child: CircleImageView, dependency: View) {
        child.apply {
            x = offset * (expandedX - collapsedX) + collapsedX
            y = offset * (expandedY - collapsedY) + collapsedY
        }

        val size = ((maximumSize - minimumSize) * offset + minimumSize).toInt()
        val lp = (child.layoutParams as CoordinatorLayout.LayoutParams)
            .apply {
                width = size
                height = size
            }

        child.layoutParams = lp
    }
}

