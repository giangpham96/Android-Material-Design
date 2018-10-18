package leo.me.la.collapsingtoolbarone

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View

@SuppressWarnings("unused")
class StatsBehavior(context: Context, attributes: AttributeSet)
    : CoordinatorLayout.Behavior<CardView>(context, attributes) {

    private var maximumAppBarHeight: Int = 0
    private val minimumAppBarHeight = context.getActionBarSize()

    private val collapsedX: Float = 0F
    private var expandedX: Float = 0F
    private var expandedY: Float = 0F

    private var maximumWidth: Int = 0
    private var minimumWidth: Int = 0

    private var maximumHeight: Int = 0
    private var minimumHeight: Int = 0

    private val maxRadius: Float
    private val minRadius: Float

    init {
        val array = context.obtainStyledAttributes(attributes, R.styleable.StatsBehavior)
        maxRadius = array.getDimension(R.styleable.StatsBehavior_maxRadius, 0F)
        minRadius = array.getDimension(R.styleable.StatsBehavior_minRadius, 0F)
        array.recycle()
    }

    private var isInitialized: Boolean = false
    override fun layoutDependsOn(parent: CoordinatorLayout, child: CardView, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: CardView, dependency: View): Boolean {
        if (!isInitialized) {
            maximumAppBarHeight = dependency.height
            expandedX = (child.layoutParams as CoordinatorLayout.LayoutParams).leftMargin.toFloat()
            expandedY = maximumAppBarHeight - child.height / 2F
            maximumWidth = dependency.width
            minimumWidth = child.width
            maximumHeight = child.height
            minimumHeight = child.getChildAt(0).height + child.contentPaddingTop + child.contentPaddingBottom
            isInitialized = true
        }
        val ratio = (maximumAppBarHeight + dependency.y + (dependency.y / (maximumAppBarHeight - minimumAppBarHeight)) * minimumAppBarHeight) / maximumAppBarHeight
        child.apply {
            x = ratio * (expandedX - collapsedX) + collapsedX
            y = ratio * (expandedY - minimumAppBarHeight) + minimumAppBarHeight
        }

        val lp = (child.layoutParams as CoordinatorLayout.LayoutParams)
            .apply {
                width = ((minimumWidth - maximumWidth) * ratio + maximumWidth).toInt()
                height = ((maximumHeight - minimumHeight) * ratio + minimumHeight).toInt()
            }

        child.layoutParams = lp
        child.radius = (maxRadius - minRadius) * ratio + minRadius
        return true
    }
}
