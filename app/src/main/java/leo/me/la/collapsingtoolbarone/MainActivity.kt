package leo.me.la.collapsingtoolbarone

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
import kotlinx.android.synthetic.main.activity_main.name

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val trendPreloadSizeProvider = ViewPreloadSizeProvider<Trend>()
        val trendAdapter = TrendAdapter(this@MainActivity, trendPreloadSizeProvider)
        val trendPreloader = RecyclerViewPreloader<Trend>(this, trendAdapter, trendPreloadSizeProvider, 5)
        trends.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, HORIZONTAL, false)
            adapter = trendAdapter
            addOnScrollListener(trendPreloader)
        }
        LinearSnapHelper().attachToRecyclerView(trends)
        val postAdapter = PostAdapter(this@MainActivity)
        val postPreloadSizeProvider = RemotePreloadSizeProvider<Post>(getScreenWidth())
        val postPreloader = RecyclerViewPreloader<Post>(this, postAdapter, postPreloadSizeProvider, 5)
        posts.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, VERTICAL, false)
            adapter = postAdapter
            addOnScrollListener(postPreloader)
        }
        name.isSelected = true
    }
}

fun Activity.getScreenWidth() : Int {
    val display = windowManager.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x
}
