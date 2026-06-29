package com.pucetec.students.services

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.entities.Professor
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.repositories.ProfessorRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProfessorServiceTest {

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var professorService: ProfessorService

    // Objeto reutilizable en los tests
    private val professor = Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu")

    // ──────────────────────────────────────────────────────────────
    // createProfessor
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `createProfessor retorna respuesta cuando el nombre es valido`() {
        // Arrange
        val request = ProfessorRequest(name = "Dr. Perez", email = "perez@puce.edu")
        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(professor)

        // Act
        val response = professorService.createProfessor(request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Dr. Perez", response.name)
        assertEquals("perez@puce.edu", response.email)
    }

    @Test
    fun `createProfessor lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        // Arrange
        val request = ProfessorRequest(name = "", email = "perez@puce.edu")

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            professorService.createProfessor(request)
        }
        verify(professorRepository, never()).save(any())
    }

    // ──────────────────────────────────────────────────────────────
    // getAllProfessors
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `getAllProfessors retorna lista de profesores cuando existen registros`() {
        // Arrange
        val professors = listOf(
            professor,
            Professor(id = 2L, name = "Dra. Torres", email = "torres@puce.edu")
        )
        `when`(professorRepository.findAll()).thenReturn(professors)

        // Act
        val response = professorService.getAllProfessors()

        // Assert
        assertEquals(2, response.size)
        assertEquals("Dr. Perez", response[0].name)
        assertEquals("Dra. Torres", response[1].name)
    }

    @Test
    fun `getAllProfessors retorna lista vacia cuando no hay profesores`() {
        // Arrange
        `when`(professorRepository.findAll()).thenReturn(emptyList())

        // Act
        val response = professorService.getAllProfessors()

        // Assert
        assertTrue(response.isEmpty())
    }

    // ──────────────────────────────────────────────────────────────
    // getProfessorById
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `getProfessorById retorna respuesta cuando el profesor existe`() {
        // Arrange
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))

        // Act
        val response = professorService.getProfessorById(1L)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Dr. Perez", response.name)
    }

    @Test
    fun `getProfessorById lanza ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            professorService.getProfessorById(99L)
        }
    }

    // ──────────────────────────────────────────────────────────────
    // updateProfessor
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `updateProfessor retorna respuesta actualizada cuando los datos son validos`() {
        // Arrange
        val request = ProfessorRequest(name = "Dr. Perez Actualizado", email = "perez.new@puce.edu")
        val updated = professor.copy(name = "Dr. Perez Actualizado", email = "perez.new@puce.edu")

        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(professorRepository.save(any(Professor::class.java))).thenReturn(updated)

        // Act
        val response = professorService.updateProfessor(1L, request)

        // Assert
        assertEquals("Dr. Perez Actualizado", response.name)
        assertEquals("perez.new@puce.edu", response.email)
    }

    @Test
    fun `updateProfessor lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        // Arrange
        val request = ProfessorRequest(name = "   ", email = "perez@puce.edu")

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            professorService.updateProfessor(1L, request)
        }
        verify(professorRepository, never()).findById(any())
    }

    @Test
    fun `updateProfessor lanza ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        val request = ProfessorRequest(name = "Dr. Nuevo", email = "nuevo@puce.edu")
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            professorService.updateProfessor(99L, request)
        }
        verify(professorRepository, never()).save(any())
    }

    // ──────────────────────────────────────────────────────────────
    // deleteProfessor
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `deleteProfessor elimina el profesor cuando existe`() {
        // Arrange
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))

        // Act
        professorService.deleteProfessor(1L)

        // Assert
        verify(professorRepository).delete(professor)
    }

    @Test
    fun `deleteProfessor lanza ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            professorService.deleteProfessor(99L)
        }
        verify(professorRepository, never()).delete(any())
    }

    // ──────────────────────────────────────────────────────────────
    // findEntityById
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `findEntityById retorna entidad cuando el profesor existe`() {
        // Arrange
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))

        // Act
        val result = professorService.findEntityById(1L)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("Dr. Perez", result.name)
    }

    @Test
    fun `findEntityById lanza ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            professorService.findEntityById(99L)
        }
    }
}
