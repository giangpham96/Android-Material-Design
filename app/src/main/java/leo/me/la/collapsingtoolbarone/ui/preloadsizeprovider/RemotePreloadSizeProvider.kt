package leo.me.la.collapsingtoolbarone.ui.preloadsizeprovider

import com.bumptech.glide.ListPreloader
import android.graphics.BitmapFactory
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection


class RemotePreloadSizeProvider<T : ImageURLProvider>(
    private val baseSize: Int,
    private val baseOnWidth: Boolean = true
) : ListPreloader.PreloadSizeProvider<T> {
    override fun getPreloadSize(item: T, adapterPosition: Int, perItemPosition: Int): IntArray? {
        return item.provideUrl()?.let {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            var connection: URLConnection? = null
            return try {
                connection = URL(it).openConnection()
                BitmapFactory.decodeStream(connection.getInputStream(), null, options)
                IntArray(2) { position ->
                    val scale = if (baseOnWidth) {
                        baseSize.toFloat() / options.outWidth
                    } else {
                        baseSize.toFloat() / options.outHeight
                    }
                    return@IntArray (if (position == 0) options.outWidth * scale else options.outHeight * scale).toInt()
                }
            } catch (_: Exception) {
                null
            } finally {
                (connection as? HttpURLConnection)?.disconnect()
            }
        }
    }
}

interface ImageURLProvider {
    fun provideUrl(): String?
}
