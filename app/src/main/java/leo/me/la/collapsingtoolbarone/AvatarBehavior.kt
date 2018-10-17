package leo.me.la.collapsingtoolbarone

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View
import de.hdodenhof.circleimageview.CircleImageView

@SuppressWarnings("unused")
class AvatarBehavior(context: Context, attributes: AttributeSet)
    : CoordinatorLayout.Behavior<CircleImageView>(context, attributes) {

    private var maximumAppBarHeight: Int = 0
    private val minimumAppBarHeight = context.getActionBarSize()

    private var maximumSize: Int = 0
    private val minimumSize = minimumAppBarHeight * 2 / 3

    private val collapsedX: Float = (minimumAppBarHeight - minimumSize) / 2F
    private val collapsedY: Float = (minimumAppBarHeight - minimumSize) / 2F
    private var expandedX: Float = 0F
    private var expandedY: Float = 0F

    private var isInitialized: Boolean = false
    override fun layoutDependsOn(parent: CoordinatorLayout, child: CircleImageView, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: CircleImageView, dependency: View): Boolean {
        if (!isInitialized) {
            maximumAppBarHeight = dependency.height
            maximumSize = child.height
            expandedX = (child.layoutParams as CoordinatorLayout.LayoutParams).leftMargin.toFloat()
            expandedY = maximumAppBarHeight - maximumSize.toFloat() - (child.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin
            isInitialized = true
        }
        val ratio = (maximumAppBarHeight + dependency.y + (dependency.y / (maximumAppBarHeight - minimumAppBarHeight)) * minimumAppBarHeight) / maximumAppBarHeight
        child.apply {
            x = ratio * (expandedX - collapsedX) + collapsedX
            y = ratio * (expandedY - collapsedY) + collapsedY
        }

        val size = ((maximumSize - minimumSize) * ratio + minimumSize).toInt()
        val lp = (child.layoutParams as CoordinatorLayout.LayoutParams)
            .apply {
                width = size
                height = size
            }

        child.layoutParams = lp
        return true
    }
}

fun Context.getActionBarSize(): Int {
    val styledAttributes = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
    val actionBarSize = styledAttributes.getDimension(0, 0f).toInt()
    styledAttributes.recycle()
    return actionBarSize
}
