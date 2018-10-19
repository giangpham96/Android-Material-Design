package leo.me.la.collapsingtoolbarone.ui.coordinatorbehavior

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View

@SuppressWarnings("unused")
class WrappedScrollViewBehavior(context: Context, attributes: AttributeSet)
    : AppBarLayout.ScrollingViewBehavior(context, attributes) {
    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return super.layoutDependsOn(parent, child, dependency) || dependency is CardView
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (dependency is AppBarLayout)
            return super.onDependentViewChanged(parent, child, dependency)
        if (dependency is CardView)
            child.y = dependency.y + dependency.height + (dependency.layoutParams as
                CoordinatorLayout.LayoutParams).bottomMargin + (child.layoutParams as
                CoordinatorLayout.LayoutParams).topMargin
        return false
    }
}
