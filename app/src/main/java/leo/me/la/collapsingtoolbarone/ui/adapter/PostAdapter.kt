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
            "https://us.123rf.com/450wm/photodeti/photodeti1711/photodeti171100520/90583841-funny-fitness-cat-with-dumbbell-above-white-banner-isolated-on-white-background-.jpg?ver=6",
            listOf("Fitness", "Animal", "Funny", "Cat", "Dumbbell", "Workout")
        ),
        Post(
            441,
            "https://www.diabetesprevention.pitt.edu/wps/wp-content/uploads/2015/07/feature-July2015a.jpg",
            listOf("Lifestyle", "Sport", "Food", "Fitness", "Running", "Healthy")
        ),
        Post(
            12039,
            "https://scontent-arn2-1.xx.fbcdn.net/v/t31.0-8/15972427_262180554195307_8376375207276903176_o.jpg?_nc_cat=108&_nc_ht=scontent-arn2-1.xx&oh=79e9cd50bbaacdc5761d81c63a312007&oe=5C5C1548",
            listOf("Love", "Civil War", "Funny", "Photoshop", "Dab", "Ba vi")
        ),
        Post(
            9021,
            "https://scontent-arn2-1.xx.fbcdn.net/v/t1.0-0/p206x206/25660120_1848015098605167_8097854011213076199_n.jpg?_nc_cat=109&_nc_ht=scontent-arn2-1.xx&oh=e2290726497790ed76f5cf390c40fa4d&oe=5C3F31F7",
            listOf("Self", "Beauty", "Pretty", "Yearbook")
        ),
        Post(
            2311,
            "https://scontent-arn2-1.xx.fbcdn.net/v/t1.0-9/10689984_373320009488245_7199137391190762517_n.jpg?_nc_cat=107&_nc_ht=scontent-arn2-1.xx&oh=442045d060e5f2c1aef465b709b68d73&oe=5C5C326B",
            listOf("Freshman")
        ),
        Post(
            102,
            "https://scontent-arn2-1.xx.fbcdn.net/v/t1.0-9/10406975_482948431842940_4537894200487913963_n.png?_nc_cat=100&_nc_ht=scontent-arn2-1.xx&oh=9061b3256fae862bff0fb6d73f02c387&oe=5C4768BF",
            listOf("K-pop", "Idol", "Red Velvet", "Quote", "Singer")
        ),
        Post(
            7249,
            "https://scontent-arn2-1.xx.fbcdn.net/v/t1.0-9/43470085_1908189912822137_5102037478219448320_o.jpg?_nc_cat=101&_nc_ht=scontent-arn2-1.xx&oh=11be085ebcbc3181fa5ee71fe2ba7445&oe=5C40B524",
            listOf("Vintage", "Ancient", "Countryside", "1980s")
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
