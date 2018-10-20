package leo.me.la.collapsingtoolbarone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.activity_main.trends
import leo.me.la.collapsingtoolbarone.ui.adapter.TrendAdapter
import android.support.v7.widget.LinearSnapHelper
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.util.ViewPreloadSizeProvider



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val preloadSizeProvider = ViewPreloadSizeProvider<Trend>()
        val trendAdapter = TrendAdapter(this@MainActivity, preloadSizeProvider)
        val preloader = RecyclerViewPreloader<Trend>(this, trendAdapter, preloadSizeProvider, 5)
        trends.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, HORIZONTAL, false)
            adapter = trendAdapter
            addOnScrollListener(preloader)
        }
        LinearSnapHelper().attachToRecyclerView(trends)
    }
}
