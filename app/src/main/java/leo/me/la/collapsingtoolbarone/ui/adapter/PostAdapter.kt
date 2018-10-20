package leo.me.la.collapsingtoolbarone.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import configure
import leo.me.la.collapsingtoolbarone.Post
import leo.me.la.collapsingtoolbarone.R
import leo.me.la.collapsingtoolbarone.ui.util.GlideApp
import leo.me.la.collapsingtoolbarone.ui.util.GlideRequest

class PostAdapter(
    context: Context,
    private val posts: List<Post> = listOf(
        Post(
            1122,
            "https://www.idfootballdesk.com/media/fc-barcelona/12/barca-girl-messi-10-the-trim-inside.jpg",
            listOf("Style", "FC Barcelona", "Football", "Girl", "Beauty", "Model", "Cules", "FCB")
        ),
        Post(
            3231,
            "http://dollars-bbs.org/sports/src/1433367290411.jpg",
            listOf("Fitness", "Animal", "Funny", "Cat", "Dumbbell", "Workout")
        )
    )
) : RecyclerView.Adapter<PostAdapter.PostVH>(), ListPreloader.PreloadModelProvider<Post> {
    override fun getPreloadItems(position: Int): List<Post> {
        return listOf(posts[position])
    }

    override fun getPreloadRequestBuilder(item: Post): RequestBuilder<*>? {
        return fullRequest.load(item.imageUrl)
            .thumbnail(thumbnailRequest.load(item.imageUrl))
            .configure()
    }

    private val thumbnailRequest = GlideApp.with(context)
        .asDrawable()
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .transition(DrawableTransitionOptions.withCrossFade())

    private val fullRequest = GlideApp.with(context).asDrawable()
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): PostVH {
        return PostVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false),
            fullRequest,
            thumbnailRequest
        )
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(viewholder: PostVH, position: Int) {
        viewholder.bind(posts[position])
    }

    class PostVH(
        itemView: View,
        private val fullRequest: GlideRequest<Drawable>,
        private val thumbnailRequest: GlideRequest<Drawable>
    ) : RecyclerView.ViewHolder(itemView) {
        private val views = itemView.findViewById<TextView>(R.id.views)
        private val content = itemView.findViewById<ImageView>(R.id.content)
        private val tags = itemView.findViewById<ChipGroup>(R.id.tags)
        @SuppressLint("SetTextI18n")
        fun bind(post: Post) {
            views.text = "${post.views} views"
            fullRequest.load(post.imageUrl)
                .thumbnail(thumbnailRequest.load(post.imageUrl))
                .configure()
                .into(content)
            tags.apply {
                removeAllViews()
                post.tags.forEach { tag ->
                    addView(
                        Chip(itemView.context).also {
                            it.text = tag
                            it.chipBackgroundColor = ColorStateList.valueOf(
                                ContextCompat.getColor(context, R.color.colorChip)
                            )
                            it.setTextColor(Color.WHITE)
                        }
                    )
                }
            }
        }
    }
}
