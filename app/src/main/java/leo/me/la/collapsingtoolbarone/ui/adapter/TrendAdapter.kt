package leo.me.la.collapsingtoolbarone.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.util.ViewPreloadSizeProvider
import configure
import leo.me.la.collapsingtoolbarone.R
import leo.me.la.collapsingtoolbarone.Trend
import leo.me.la.collapsingtoolbarone.ui.util.GlideApp
import leo.me.la.collapsingtoolbarone.ui.util.GlideRequest

class TrendAdapter(
    context: Context,
    private val preloadModelProvider: ViewPreloadSizeProvider<Trend>,
    private val trends: List<Trend> = listOf(
        Trend("Fitness", "https://hdqwalls.com/walls/thumb/gym-girl-8k-image.jpg"),
        Trend("Animal", "https://render.fineartamerica.com/images/rendered/search/print/images-medium-5/1-panda-king-wu.jpg"),
        Trend("Outdoor", "http://www.ablecanopies.co.uk/blog/wp-content/uploads/2015/11/outdoor-learning-for-secondary-schools-small-200x300.jpg"),
        Trend("Movie", "https://media3.mensxp.com/media/content/2017/Sep/this-fan-made-trailer-is-proof-pennywise-is-batman-rsquo-s-best-enemy600-1506513559_200x300.jpg"),
        Trend("Football", "https://d2x51gyc4ptf2q.cloudfront.net/content/uploads/2018/05/17105055/Lionel-Messi-1.jpg"),
        Trend("Music", "http://img.cdn2.vietnamnet.vn/Images/english/2017/01/04/14/20170104141533-sontung.jpg"),
        Trend("Art", "http://www.artscouncil.ie/uploadedImages/Main_Site/Pages/who_funded.jpg"),
        Trend("News", "https://cdn1.thr.com/sites/default/files/imagecache/landscape_928x523/2018/02/screen_shot_2018-02-07_at_9.57.26_am_1_-_h_2018.jpg"),
        Trend("Food", "http://www.restauranttristan.co.uk/wp-content/uploads/2016/10/tristan-dish-1111.jpg"),
        Trend("Style", "http://img.saobiz.net/d/2016/06/nang-tho-xu-hue.jpg")
    )
) : RecyclerView.Adapter<TrendAdapter.TrendVH>(), ListPreloader.PreloadModelProvider<Trend> {

    private val thumbnailRequest = GlideApp.with(context)
        .asDrawable()
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .transition(withCrossFade())

    private val fullRequest = GlideApp.with(context).asDrawable()

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): TrendVH {
        return TrendVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_trend, parent, false),
            fullRequest,
            thumbnailRequest
        ).also {
            preloadModelProvider.setView(it.itemView)
        }
    }

    override fun getItemCount(): Int {
        return trends.size
    }

    override fun onBindViewHolder(holder: TrendVH, position: Int) {
        holder.bind(trends[position])
    }

    override fun getPreloadItems(position: Int): List<Trend> {
        return listOf(trends[position])
    }

    override fun getPreloadRequestBuilder(item: Trend): RequestBuilder<Drawable>? {
        return fullRequest.load(item.imageUrl)
            .thumbnail(thumbnailRequest.load(item.imageUrl))
            .configure()
    }

    class TrendVH(
        itemView: View,
        private val fullRequest: GlideRequest<Drawable>,
        private val thumbnailRequest: GlideRequest<Drawable>
    ) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.image)
        private val name = itemView.findViewById<TextView>(R.id.name)

        fun bind(trend: Trend) {
            name.text = trend.name
            fullRequest.load(trend.imageUrl)
                .thumbnail(thumbnailRequest.load(trend.imageUrl))
                .configure()
                .into(image)
        }
    }
}
