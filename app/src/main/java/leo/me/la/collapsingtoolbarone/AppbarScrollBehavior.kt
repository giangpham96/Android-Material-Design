package leo.me.la.collapsingtoolbarone

import android.content.Context
import android.support.annotation.CallSuper
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

/**
 * This class is designed to work with any direct child view of a [CoordinatorLayout], which has a
 * [Toolbar] nested inside a [CollapsingToolbarLayout] nested inside an [AppBarLayout]. The
 * [AppBarLayout] is a direct child of the [CoordinatorLayout] and the [Toolbar] is defined to
 * pin on top when the [AppBarLayout] collapse. If those conditions do not satisfy, this class
 * will not work as unexpected behaviors will arise
 */
abstract class AppbarScrollBehavior<T : View>(context: Context, attributes: AttributeSet)
    : CoordinatorLayout.Behavior<T>(context, attributes) {
    protected val minimumAppBarHeight = context.getActionBarSize()

    @CallSuper
    override fun layoutDependsOn(parent: CoordinatorLayout, child: T, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    @CallSuper
    override fun onDependentViewChanged(parent: CoordinatorLayout, child: T, dependency: View): Boolean {
        val offset = (dependency.height + dependency.y + (dependency.y / (dependency.height
            - minimumAppBarHeight)) * minimumAppBarHeight) / dependency.height
        animateBaseOnAppbarMovement(offset, parent, child, dependency)
        return true
    }

    /**
     * @param offset a value that runs from 1 to 0 base on the app bar movement.
     * 1 means that the app bar is fully opened
     * 0 means that the app bar is collapsed.
     */
    abstract fun animateBaseOnAppbarMovement(offset: Float, parent: CoordinatorLayout, child: T, dependency: View)
}

fun Context.getActionBarSize(): Int {
    val styledAttributes = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
    val actionBarSize = styledAttributes.getDimension(0, 0f).toInt()
    styledAttributes.recycle()
    return actionBarSize
}
