package leo.me.la.collapsingtoolbarone

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.activity_main.trends
import leo.me.la.collapsingtoolbarone.ui.adapter.TrendAdapter
import android.support.v7.widget.LinearSnapHelper
import android.widget.LinearLayout.VERTICAL
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider
import kotlinx.android.synthetic.main.activity_main.posts
import leo.me.la.collapsingtoolbarone.ui.adapter.PostAdapter
import leo.me.la.collapsingtoolbarone.ui.preloadsizeprovider.RemotePreloadSizeProvider
import android.app.Activity
import android.graphics.Point
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Interpolator
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.actions
import kotlinx.android.synthetic.main.activity_main.add_friend
import kotlinx.android.synthetic.main.activity_main.add_note
import kotlinx.android.synthetic.main.activity_main.add_post
import kotlinx.android.synthetic.main.activity_main.close_actions
import kotlinx.android.synthetic.main.activity_main.content_scorller
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.name

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpTrendRecyclerView()
        setUpPostRecyclerView()
        setUpScrollViewBehavior()
        setUpFab()
        setUpActions()
        name.isSelected = true
    }

    private fun setUpTrendRecyclerView() {
        val trendPreloadSizeProvider = ViewPreloadSizeProvider<Trend>()
        val trendAdapter = TrendAdapter(this@MainActivity, trendPreloadSizeProvider)
        val trendPreloader = RecyclerViewPreloader<Trend>(this, trendAdapter, trendPreloadSizeProvider, 5)
        trends.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, HORIZONTAL, false)
            adapter = trendAdapter
            addOnScrollListener(trendPreloader)
        }
        LinearSnapHelper().attachToRecyclerView(trends)
    }

    private fun setUpPostRecyclerView() {
        val postAdapter = PostAdapter(this@MainActivity)
        val postPreloadSizeProvider = RemotePreloadSizeProvider<Post>(getScreenWidth())
        val postPreloader = RecyclerViewPreloader<Post>(this, postAdapter, postPreloadSizeProvider, 5)
        posts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, VERTICAL, false)
            adapter = postAdapter
            addOnScrollListener(postPreloader)
        }
    }

    private fun setUpScrollViewBehavior() {
        content_scorller.setOnScrollChangeListener { _: NestedScrollView?, _: Int, y: Int, _: Int, oldY: Int ->
            val hideActionsAnimatorSet = fab.getTag(R.string.animator_hide) as? AnimatorSet
            val revealActionsAnimator = fab.getTag(R.string.animator_reveal) as? AnimatorSet
            if (hideActionsAnimatorSet?.isRunning == true || revealActionsAnimator?.isRunning == true) {
                return@setOnScrollChangeListener
            }
            if (actions.visibility == View.VISIBLE) {
                hideActions()
            } else {
                if (y > oldY) {
                    fab.hide()
                } else {
                    fab.show()
                }
            }
        }
    }

    private fun hideActions() {
        val hideActionsAnimatorSet = fab.getTag(R.string.animator_hide) as? AnimatorSet
        val revealActionsAnimator = fab.getTag(R.string.animator_reveal) as? AnimatorSet
        hideActionsAnimatorSet?.cancel()
        revealActionsAnimator?.cancel()
        val xAnimator = ObjectAnimator.ofFloat(
            fab,
            "x",
            fab.getTag(R.string.initial_x) as Float
        )
        val yAnimator = ObjectAnimator.ofFloat(
            fab,
            "y",
            fab.getTag(R.string.initial_y) as Float
        ).apply {
            interpolator = Interpolator { input ->
                1 - (Math.pow(1.2, -30 * (input - 0.2)) * Math.sin(2 * Math.PI * (input - 0.2) + Math.PI / 2)).toFloat()
            }
        }
        val hideActionsAnimator = ViewAnimationUtils.createCircularReveal(
            actions,
            (actions.width / 2),
            (actions.height / 2),
            actions.width / 2f * 1.2f,
            0f
        ).apply {
            duration = 300
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    fab.show()
                    actions.visibility = View.INVISIBLE
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}

            })
        }
        AnimatorSet().apply {
            playSequentially(
                hideActionsAnimator,
                AnimatorSet().apply {
                    playTogether(xAnimator, yAnimator)
                    duration = 300
                },
                // Ensure that the fab is shown at least half a second before it is
                // hidden by scrolling down behavior
                ValueAnimator.ofInt(0, 1).apply {
                    duration = 500
                }
            )
        }.also {
            fab.setTag(R.string.animator_hide, it)
        }.start()
    }

    private fun setUpActions() {
        add_friend.setOnClickListener {
            hideActions()
            Toast.makeText(this, "Find friends", Toast.LENGTH_SHORT).show()
        }
        add_post.setOnClickListener {
            hideActions()
            Toast.makeText(this, "New post", Toast.LENGTH_SHORT).show()
        }
        add_note.setOnClickListener {
            hideActions()
            Toast.makeText(this, "New note", Toast.LENGTH_SHORT).show()
        }
        close_actions.setOnClickListener {
            hideActions()
        }
    }

    private fun setUpFab() {
        fab.apply {
            post {
                setTag(R.string.initial_x, fab.x)
                setTag(R.string.initial_y, fab.y)
            }
            setOnClickListener {
                val hideActionsAnimatorSet = fab.getTag(R.string.animator_hide) as? AnimatorSet
                val revealActionsAnimatorSet = fab.getTag(R.string.animator_reveal) as? AnimatorSet
                if (hideActionsAnimatorSet?.isRunning == true || revealActionsAnimatorSet?.isRunning == true) {
                    return@setOnClickListener
                }
                val xAnimator = ObjectAnimator.ofFloat(
                    it,
                    "x",
                    actions.width / 2F - it.width / 2F
                )
                val yAnimator = ObjectAnimator.ofFloat(
                    it,
                    "y",
                    actions.y + actions.height / 2 - it.height / 2
                ).apply {
                    interpolator = Interpolator { input ->
                        1 - (Math.pow(1.2, -30 * (input - 0.2)) * Math.sin(2 * Math.PI * (input - 0.2) + Math.PI / 2)).toFloat()
                    }
                }
                val revealActionsAnimator = ViewAnimationUtils.createCircularReveal(
                    actions,
                    (actions.width / 2),
                    (actions.height / 2),
                    0f,
                    actions.width / 2f * 1.2f
                ).apply {
                    duration = 300
                }
                AnimatorSet().apply {
                    playSequentially(
                        AnimatorSet().apply {
                            duration = 300
                            playTogether(xAnimator, yAnimator)
                            addListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(animation: Animator?) {}

                                override fun onAnimationEnd(animation: Animator?) {
                                    fab.hide()
                                    actions.visibility = View.VISIBLE
                                }

                                override fun onAnimationCancel(animation: Animator?) {}

                                override fun onAnimationStart(animation: Animator?) {}

                            })
                        },
                        revealActionsAnimator,
                        // Ensure that the actions list is shown at least half a second before it
                        // is hidden by scrolling behavior
                        ValueAnimator.ofInt(0, 1).apply {
                            duration = 500
                        }
                    )
                }.also { animator ->
                    it.setTag(R.string.animator_reveal, animator)
                }.start()
            }
        }
    }
}

fun Activity.getScreenWidth(): Int {
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}
