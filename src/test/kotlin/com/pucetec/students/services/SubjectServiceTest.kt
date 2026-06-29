package com.pucetec.students.services

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.entities.Professor
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.repositories.ProfessorRepository
import com.pucetec.students.repositories.SubjectRepository
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
class SubjectServiceTest {

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @Mock
    private lateinit var professorRepository: ProfessorRepository

    @InjectMocks
    private lateinit var subjectService: SubjectService

    // Objetos reutilizables en los tests
    private val professor = Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu")
    private val subject = Subject(id = 1L, name = "Matematicas", code = "MAT101", professor = professor)

    // ──────────────────────────────────────────────────────────────
    // createSubject
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `createSubject retorna respuesta cuando los datos son validos`() {
        // Arrange
        val request = SubjectRequest(name = "Matematicas", code = "MAT101", professorId = 1L)
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any(Subject::class.java))).thenReturn(subject)

        // Act
        val response = subjectService.createSubject(request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Matematicas", response.name)
        assertEquals("MAT101", response.code)
        assertEquals("Dr. Perez", response.professor.name)
    }

    @Test
    fun `createSubject lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        // Arrange
        val request = SubjectRequest(name = "", code = "MAT101", professorId = 1L)

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            subjectService.createSubject(request)
        }
        verify(professorRepository, never()).findById(any())
    }

    @Test
    fun `createSubject lanza IllegalArgumentException cuando el codigo esta en blanco`() {
        // Arrange
        val request = SubjectRequest(name = "Matematicas", code = "", professorId = 1L)

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            subjectService.createSubject(request)
        }
        verify(professorRepository, never()).findById(any())
    }

    @Test
    fun `createSubject lanza ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        val request = SubjectRequest(name = "Matematicas", code = "MAT101", professorId = 99L)
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            subjectService.createSubject(request)
        }
        verify(subjectRepository, never()).save(any())
    }

    // ──────────────────────────────────────────────────────────────
    // getAllSubjects
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `getAllSubjects retorna lista de materias cuando existen registros`() {
        // Arrange
        val subjects = listOf(
            subject,
            Subject(id = 2L, name = "Fisica", code = "FIS101", professor = professor)
        )
        `when`(subjectRepository.findAll()).thenReturn(subjects)

        // Act
        val response = subjectService.getAllSubjects()

        // Assert
        assertEquals(2, response.size)
        assertEquals("Matematicas", response[0].name)
        assertEquals("Fisica", response[1].name)
    }

    @Test
    fun `getAllSubjects retorna lista vacia cuando no hay materias`() {
        // Arrange
        `when`(subjectRepository.findAll()).thenReturn(emptyList())

        // Act
        val response = subjectService.getAllSubjects()

        // Assert
        assertTrue(response.isEmpty())
    }

    // ──────────────────────────────────────────────────────────────
    // getSubjectById
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `getSubjectById retorna respuesta cuando la materia existe`() {
        // Arrange
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        // Act
        val response = subjectService.getSubjectById(1L)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Matematicas", response.name)
    }

    @Test
    fun `getSubjectById lanza SubjectNotFound cuando la materia no existe`() {
        // Arrange
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFound> {
            subjectService.getSubjectById(99L)
        }
    }

    // ──────────────────────────────────────────────────────────────
    // updateSubject
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `updateSubject retorna respuesta actualizada cuando los datos son validos`() {
        // Arrange
        val request = SubjectRequest(name = "Calculo", code = "CAL101", professorId = 1L)
        val updated = subject.copy(name = "Calculo", code = "CAL101")

        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))
        `when`(professorRepository.findById(1L)).thenReturn(Optional.of(professor))
        `when`(subjectRepository.save(any(Subject::class.java))).thenReturn(updated)

        // Act
        val response = subjectService.updateSubject(1L, request)

        // Assert
        assertEquals("Calculo", response.name)
        assertEquals("CAL101", response.code)
    }

    @Test
    fun `updateSubject lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        // Arrange
        val request = SubjectRequest(name = "", code = "MAT101", professorId = 1L)

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            subjectService.updateSubject(1L, request)
        }
        verify(subjectRepository, never()).findById(any())
    }

    @Test
    fun `updateSubject lanza IllegalArgumentException cuando el codigo esta en blanco`() {
        // Arrange
        val request = SubjectRequest(name = "Matematicas", code = "  ", professorId = 1L)

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            subjectService.updateSubject(1L, request)
        }
        verify(subjectRepository, never()).findById(any())
    }

    @Test
    fun `updateSubject lanza SubjectNotFound cuando la materia no existe`() {
        // Arrange
        val request = SubjectRequest(name = "Calculo", code = "CAL101", professorId = 1L)
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFound> {
            subjectService.updateSubject(99L, request)
        }
        verify(professorRepository, never()).findById(any())
    }

    @Test
    fun `updateSubject lanza ProfessorNotFound cuando el profesor no existe`() {
        // Arrange
        val request = SubjectRequest(name = "Calculo", code = "CAL101", professorId = 99L)
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))
        `when`(professorRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<ProfessorNotFound> {
            subjectService.updateSubject(1L, request)
        }
        verify(subjectRepository, never()).save(any())
    }

    // ──────────────────────────────────────────────────────────────
    // deleteSubject
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `deleteSubject elimina la materia cuando existe`() {
        // Arrange
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        // Act
        subjectService.deleteSubject(1L)

        // Assert
        verify(subjectRepository).delete(subject)
    }

    @Test
    fun `deleteSubject lanza SubjectNotFound cuando la materia no existe`() {
        // Arrange
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFound> {
            subjectService.deleteSubject(99L)
        }
        verify(subjectRepository, never()).delete(any())
    }

    // ──────────────────────────────────────────────────────────────
    // findEntityById
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `findEntityById retorna entidad cuando la materia existe`() {
        // Arrange
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))

        // Act
        val result = subjectService.findEntityById(1L)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("Matematicas", result.name)
    }

    @Test
    fun `findEntityById lanza SubjectNotFound cuando la materia no existe`() {
        // Arrange
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFound> {
            subjectService.findEntityById(99L)
        }
    }
}
