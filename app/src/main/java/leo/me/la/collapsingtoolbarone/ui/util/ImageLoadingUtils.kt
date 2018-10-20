import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import leo.me.la.collapsingtoolbarone.ui.util.GlideApp
import com.bumptech.glide.request.target.Target
import leo.me.la.collapsingtoolbarone.ui.util.GlideRequest

fun ImageView.loadUri(
    uri: Uri?,
    cache: Boolean = true,
    noFade: Boolean = false,
    noPlaceholder: Boolean = false,
    thumbnail: Uri? = null,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null
) {
    GlideApp.with(context)
        .load(uri)
        .thumbnail(GlideApp.with(context).load(thumbnail))
        .configure(
            cache,
            noFade,
            noPlaceholder,
            onSuccess,
            onError
        )
        .into(this)
}

fun ImageView.loadUri(
    uri: String?,
    cache: Boolean = true,
    noFade: Boolean = false,
    noPlaceholder: Boolean = false,
    thumbnail: String? = null,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null
) = loadUri(
    uri?.let(Uri::parse),
    cache,
    noFade,
    noPlaceholder,
    thumbnail?.let(Uri::parse),
    onSuccess,
    onError
)

fun GlideRequest<Drawable>.configure(
    cache: Boolean = true,
    noFade: Boolean = false,
    noPlaceholder: Boolean = false,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null
) = this.diskCacheStrategy(if (cache) DiskCacheStrategy.AUTOMATIC else DiskCacheStrategy.NONE)
    .skipMemoryCache(!cache)
    .listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onError?.invoke()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onSuccess?.invoke()
            return false
        }
    })
    .centerCrop()
    .apply {
        if (!noPlaceholder) placeholder(android.R.color.darker_gray)
        if (!noFade) transition(DrawableTransitionOptions().crossFade())
    }
