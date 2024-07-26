package com.example.mbmkadhumdhadaka.dataModel

data class PostModel(
    val postId: String,
    val postContent: String,
    val postImage: String,
    val postOwnerPhoto: String,
    val postOwnerName: String = "Your Name"
)

object DummyData {
    val posts = listOf(
        PostModel(
            postId = "1",
            postContent = "this is first post",
            postImage = "https://via.placeholder.com/150", // Dummy image URL
            postOwnerPhoto = "https://via.placeholder.com/100" // Dummy owner photo URL
        ),
        PostModel(
            postId = "1",
            postContent = "this is second post",
            postImage = "https://via.placeholder.com/150", // Dummy image URL
            postOwnerPhoto = "https://via.placeholder.com/100" // Dummy owner photo URL
        ),
        PostModel(
            postId = "1",
            postContent = "this is third post",
            postImage = "https://via.placeholder.com/150", // Dummy image URL
            postOwnerPhoto = "https://via.placeholder.com/100" // Dummy owner photo URL
        ),
        PostModel(
            postId = "1",
            postContent = "this is fourth post",
            postImage = "https://via.placeholder.com/150", // Dummy image URL
            postOwnerPhoto = "https://via.placeholder.com/100" // Dummy owner photo URL
        )
        ,
        PostModel(
            postId = "1",
            postContent = "this is fifth post",
            postImage = "https://via.placeholder.com/150", // Dummy image URL
            postOwnerPhoto = "https://via.placeholder.com/100" // Dummy owner photo URL
        )


    )
}
