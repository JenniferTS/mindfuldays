package dev.mindfuldays.app.data.repository

import dev.mindfuldays.app.R
import dev.mindfuldays.app.data.model.MindfulnessAttitude
import java.util.Calendar

/** Las 9 actitudes de mindfulness de Jon Kabat-Zinn y la regla de rotación diaria. */
class AttitudeRepository {

    val attitudes: List<MindfulnessAttitude> = listOf(
        MindfulnessAttitude(1, "Non-Judging", "Observe your thoughts and feelings without labeling them as good or bad.", R.string.attitude_1_name, R.string.attitude_1_desc),
        MindfulnessAttitude(2, "Patience", "Let things unfold in their own time, without rushing the moment.", R.string.attitude_2_name, R.string.attitude_2_desc),
        MindfulnessAttitude(3, "Acceptance", "Recognize and embrace the present moment exactly as it is.", R.string.attitude_3_name, R.string.attitude_3_desc),
        MindfulnessAttitude(4, "Beginner's Mind", "Look at each experience as if it were the first time, with curiosity.", R.string.attitude_4_name, R.string.attitude_4_desc),
        MindfulnessAttitude(5, "Trust", "Trust yourself, your intuition and your own feelings.", R.string.attitude_5_name, R.string.attitude_5_desc),
        MindfulnessAttitude(6, "Non-Striving", "Let go of the constant need to reach an immediate result.", R.string.attitude_6_name, R.string.attitude_6_desc),
        MindfulnessAttitude(7, "Letting Go", "Release what you cling to and allow experiences to simply be.", R.string.attitude_7_name, R.string.attitude_7_desc),
        MindfulnessAttitude(8, "Gratitude", "Appreciate the simple things that sustain your life every day.", R.string.attitude_8_name, R.string.attitude_8_desc),
        MindfulnessAttitude(9, "Generosity", "Offer your presence, time and kindness without expecting anything in return.", R.string.attitude_9_name, R.string.attitude_9_desc)
    )

    fun getTodayAttitude(
        dayOfYear: Int = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    ): MindfulnessAttitude {
        val index = (dayOfYear - 1).mod(attitudes.size)
        return attitudes[index]
    }
}
