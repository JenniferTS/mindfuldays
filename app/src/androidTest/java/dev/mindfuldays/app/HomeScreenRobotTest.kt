package dev.mindfuldays.app

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.mindfuldays.app.data.model.MindfulnessAttitude
import dev.mindfuldays.app.robot.homeRobot
import dev.mindfuldays.app.ui.home.HomeScreenContent
import dev.mindfuldays.app.ui.theme.MindfulDaysTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenRobotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_muestraActitudYReflexion_yEjecutaAcciones() {
        var clickedAi = false
        var clickedTimer = false
        val attitude = MindfulnessAttitude(
            id = 3,
            name = "Aceptación",
            description = "Reconoce y acoge el momento presente exactamente como es.",
            nameRes = R.string.attitude_3_name,
            descriptionRes = R.string.attitude_3_desc
        )

        composeTestRule.setContent {
            MindfulDaysTheme {
                HomeScreenContent(
                    attitude = attitude,
                    aiReflection = "Reflexión de prueba para el workshop",
                    isLoadingAi = false,
                    onGenerateAiReflection = { clickedAi = true },
                    onNavigateToSettings = {},
                    onNavigateToTimer = { clickedTimer = true }
                )
            }
        }

        // Obtiene el contexto para recuperar cadenas internacionalizadas
        val context = androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext

        // Ejecución expresiva y limpia con el Robot Pattern
        homeRobot(composeTestRule) {
            assertReflectionText("Reflexión de prueba para el workshop")
            clickGenerateReflection(context.getString(R.string.btn_new_reflection))
            clickTimerButton(context.getString(R.string.btn_start_timer))
        }

        // Valida que las acciones se hayan ejecutado
        assertTrue("El botón de IA debió haberse presionado", clickedAi)
        assertTrue("El botón de navegación al Temporizador debió haberse presionado", clickedTimer)
    }
}
