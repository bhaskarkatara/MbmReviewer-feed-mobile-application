package com.example.mbmkadhumdhadaka.dataModel

data class ReviewModel(
    val reviewText: String,
    val ratingStar: Int,
    val tagPlaces : String
)

object DummyData {
    // List of dummy reviews
    val reviews = listOf(
        ReviewModel(
            reviewText = "Excellent app! Really enjoyed using it.",
            ratingStar = 5,
            tagPlaces = "Canteen"
        ),
        ReviewModel(
            reviewText = "Needs improvement. Some features are missing.",
            ratingStar = 3,
            tagPlaces = "Library"
        ),
        ReviewModel(
            reviewText = "Not great. Had a lot of issues.",
            ratingStar = 2,
            tagPlaces = "Hostel"
        ),
        ReviewModel(
            reviewText = "Love the new update! Much better.",
            ratingStar = 4,
            tagPlaces = "Mess"
        )
    )
}