package leo.me.la.collapsingtoolbarone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.activity_main.trends
import leo.me.la.collapsingtoolbarone.ui.adapter.TrendAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        trends.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, HORIZONTAL, false)
            adapter = TrendAdapter()
        }
    }
}
