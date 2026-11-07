package dev.mindfuldays.app.data.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AttitudeRepositoryTest {

    private lateinit var repository: AttitudeRepository

    @Before
    fun setUp() {
        repository = AttitudeRepository()
    }

    @Test
    fun `garantizar que la lista contiene exactamente 9 actitudes de mindfulness`() {
        val attitudes = repository.attitudes
        assertEquals(9, attitudes.size)
    }

    @Test
    fun `garantizar que todas las actitudes poseen IDs unicos y recursos de string validos`() {
        val attitudes = repository.attitudes
        val ids = attitudes.map { it.id }.toSet()
        assertEquals(9, ids.size)

        attitudes.forEach { attitude ->
            assertTrue("El ID de recurso del nombre no debe ser cero", attitude.nameRes != 0)
            assertTrue("El ID de recurso de la descripción no debe ser cero", attitude.descriptionRes != 0)
            assertTrue("El nombre por defecto no debe estar vacío", attitude.name.isNotBlank())
        }
    }

    @Test
    fun `garantizar rotacion ciclica correcta de las 9 actitudes por dia del ano`() {
        // Día 1 (1 de Enero): Primera actitud (id = 1)
        assertEquals(1, repository.getTodayAttitude(dayOfYear = 1).id)

        // Día 9: Novena actitud (id = 9)
        assertEquals(9, repository.getTodayAttitude(dayOfYear = 9).id)

        // Día 10: Reinicia el ciclo en la primera actitud (id = 1)
        assertEquals(1, repository.getTodayAttitude(dayOfYear = 10).id)

        // Día 365 (año común): (365 - 1) % 9 = 364 % 9 = 4 -> índice 4 -> id = 5 ("Confianza")
        assertEquals(5, repository.getTodayAttitude(dayOfYear = 365).id)
    }

    @Test
    fun `garantizar que la llamada por defecto de hoy retorna una actitud valida`() {
        val today = repository.getTodayAttitude()
        assertNotNull(today)
        assertTrue(today.id in 1..9)
    }
}
