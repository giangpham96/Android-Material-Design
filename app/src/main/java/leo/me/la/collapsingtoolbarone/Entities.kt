package leo.me.la.collapsingtoolbarone

data class Trend(
    val name: String,
    val imageUrl: String
)

data class Post(
    val views: Int,
    val imageUrl: String,
    val tags: List<String>
)
