package dev.mindfuldays.app.data.model

import androidx.annotation.StringRes

data class MindfulnessAttitude(
    val id: Int,
    val name: String = "",
    val description: String = "",
    @get:StringRes val nameRes: Int = 0,
    @get:StringRes val descriptionRes: Int = 0
)
