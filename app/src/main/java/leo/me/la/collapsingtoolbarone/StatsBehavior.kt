package leo.me.la.collapsingtoolbarone

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View

@SuppressWarnings("unused")
class StatsBehavior(context: Context, attributes: AttributeSet)
    : AppbarScrollBehavior<CardView>(context, attributes) {

    private val collapsedX: Float = 0F

    private var expandedX: Float = 0F
    private var expandedY: Float = 0F
    private var maxWidth: Int = 0

    private var minWidth: Int = 0
    private var maxHeight: Int = 0

    private var minHeight: Int = 0

    private val maxRadius: Float
    private val minRadius: Float
    private val maxContentPadding: Float
    private val minContentPadding: Float
    init {
        val array = context.obtainStyledAttributes(attributes, R.styleable.StatsBehavior)
        maxRadius = array.getDimension(R.styleable.StatsBehavior_maxRadius, 0F)
        minRadius = array.getDimension(R.styleable.StatsBehavior_minRadius, 0F)
        maxContentPadding = array.getDimension(R.styleable.StatsBehavior_maxContentPadding, 0F)
        minContentPadding = array.getDimension(R.styleable.StatsBehavior_minContentPadding, 0F)
        array.recycle()
    }

    private var isInitialized: Boolean = false

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: CardView, dependency: View): Boolean {
        if (!isInitialized) {
            expandedX = (child.layoutParams as CoordinatorLayout.LayoutParams).leftMargin.toFloat()
            expandedY = dependency.height - child.height / 2F
            maxWidth = dependency.width
            minWidth = child.width
            maxHeight = child.height
            minHeight = child.getChildAt(0).height + 2 * minContentPadding.toInt()
            isInitialized = true
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun animateBaseOnAppbarMovement(offset: Float, parent: CoordinatorLayout, child: CardView, dependency: View) {
        child.apply {
            x = offset * (expandedX - collapsedX) + collapsedX
            y = offset * (expandedY - minimumAppBarHeight) + minimumAppBarHeight
            layoutParams = (layoutParams as CoordinatorLayout.LayoutParams)
                .apply {
                    width = ((minWidth - maxWidth) * offset + maxWidth).toInt()
                    height = ((maxHeight - minHeight) * offset + minHeight).toInt()
                }
            radius = (maxRadius - minRadius) * offset + minRadius
            with(((maxContentPadding - minContentPadding) * offset + minContentPadding).toInt()) {
                setContentPadding(this, this, this, this)
            }
        }
    }
}
