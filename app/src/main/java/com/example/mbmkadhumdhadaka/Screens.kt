package com.example.mbmkadhumdhadaka

import android.accessibilityservice.AccessibilityService.ScreenshotResult

sealed class Screens (val route: String){
    data object LgSpScreen :Screens(route = "lg_sp_screen")
    data object  FeedScreen :Screens(route = "feed_screen")
    data object ProfileScreen : Screens(route = "profile_screen")
    data object ReviewScreen :Screens(route = "review_screen")
    data object CreatePostScreen :Screens("create_post_screen")

}