package com.example.mbmkadhumdhadaka.dataModel


data class PostModel<T>(
    var postId: String = "",
    val postContent: String = "",
    val postImage: String = "",
    val postOwnerPhoto: String = "",
    val postOwnerName: String = "Your Name",
    val userId: String = "", // UID of the user who created the post
    val timestamp: Long = System.currentTimeMillis()
)


object DummyData {
    val posts = listOf(
        PostModel<Any>(
            postId = "1",
            postContent = "this is first post",
            postImage = "https://via.placeholder.com/150", // Dummy image URL
            postOwnerPhoto = "https://via.placeholder.com/100" // Dummy owner photo URL
        ),
        PostModel<Any>(
            postId = "1",
            postContent = "this is second post",
            postImage = "https://via.placeholder.com/150", // Dummy image URL
            postOwnerPhoto = "https://via.placeholder.com/100" // Dummy owner photo URL
        ),
        PostModel<Any>(
            postId = "1",
            postContent = "this is third post",
            postImage = "https://via.placeholder.com/150", // Dummy image URL
            postOwnerPhoto = "https://via.placeholder.com/100" // Dummy owner photo URL
        ),
        PostModel<Any>(
            postId = "1",
            postContent = "this is fourth post",
            postImage = "https://via.placeholder.com/150", // Dummy image URL
            postOwnerPhoto = "https://via.placeholder.com/100" // Dummy owner photo URL
        )
        ,
        PostModel<Any>(
            postId = "1",
            postContent = "this is fifth post",
            postImage = "https://via.placeholder.com/150", // Dummy image URL
            postOwnerPhoto = "https://via.placeholder.com/100" // Dummy owner photo URL
        )


    )
}
