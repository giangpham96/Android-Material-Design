package leo.me.la.collapsingtoolbarone

import leo.me.la.collapsingtoolbarone.ui.preloadsizeprovider.ImageURLProvider

data class Trend(
    val name: String,
    val imageUrl: String
)

data class Post(
    val views: Int,
    val imageUrl: String,
    val tags: List<String>
) : ImageURLProvider {
    override fun provideUrl(): String? {
        return imageUrl
    }
}
