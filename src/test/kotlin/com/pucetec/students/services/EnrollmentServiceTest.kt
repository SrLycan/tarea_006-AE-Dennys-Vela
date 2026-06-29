package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentStatusUpdateRequest
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.entities.Professor
import com.pucetec.students.entities.Student
import com.pucetec.students.entities.Subject
import com.pucetec.students.exceptions.EnrollmentNotFound
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentRepository
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
class EnrollmentServiceTest {

    @Mock
    private lateinit var enrollmentRepository: EnrollmentRepository

    @Mock
    private lateinit var studentRepository: StudentRepository

    @Mock
    private lateinit var subjectRepository: SubjectRepository

    @InjectMocks
    private lateinit var enrollmentService: EnrollmentService

    // Objetos reutilizables en los tests
    private val professor = Professor(id = 1L, name = "Dr. Perez", email = "perez@puce.edu")
    private val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
    private val subject = Subject(id = 1L, name = "Matematicas", code = "MAT101", professor = professor)
    private val enrollment = Enrollment(id = 1L, status = "INSCRITO", student = student, subject = subject)

    // ──────────────────────────────────────────────────────────────
    // createEnrollment
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `createEnrollment retorna respuesta cuando el estudiante y la materia existen`() {
        // Arrange
        val request = EnrollmentRequest(studentId = 1L, subjectId = 1L)
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(1L)).thenReturn(Optional.of(subject))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(enrollment)

        // Act
        val response = enrollmentService.createEnrollment(request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("INSCRITO", response.status)
        assertEquals("Ana Lopez", response.student.name)
        assertEquals("Matematicas", response.subject.name)
    }

    @Test
    fun `createEnrollment lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        val request = EnrollmentRequest(studentId = 99L, subjectId = 1L)
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            enrollmentService.createEnrollment(request)
        }
        verify(subjectRepository, never()).findById(any())
        verify(enrollmentRepository, never()).save(any())
    }

    @Test
    fun `createEnrollment lanza SubjectNotFound cuando la materia no existe`() {
        // Arrange
        val request = EnrollmentRequest(studentId = 1L, subjectId = 99L)
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))
        `when`(subjectRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<SubjectNotFound> {
            enrollmentService.createEnrollment(request)
        }
        verify(enrollmentRepository, never()).save(any())
    }

    // ──────────────────────────────────────────────────────────────
    // getAllEnrollments
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `getAllEnrollments retorna lista de inscripciones cuando existen registros`() {
        // Arrange
        val enrollments = listOf(
            enrollment,
            Enrollment(id = 2L, status = "APROBADO", student = student, subject = subject)
        )
        `when`(enrollmentRepository.findAll()).thenReturn(enrollments)

        // Act
        val response = enrollmentService.getAllEnrollments()

        // Assert
        assertEquals(2, response.size)
        assertEquals("INSCRITO", response[0].status)
        assertEquals("APROBADO", response[1].status)
    }

    @Test
    fun `getAllEnrollments retorna lista vacia cuando no hay inscripciones`() {
        // Arrange
        `when`(enrollmentRepository.findAll()).thenReturn(emptyList())

        // Act
        val response = enrollmentService.getAllEnrollments()

        // Assert
        assertTrue(response.isEmpty())
    }

    // ──────────────────────────────────────────────────────────────
    // getEnrollmentById
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `getEnrollmentById retorna respuesta cuando la inscripcion existe`() {
        // Arrange
        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))

        // Act
        val response = enrollmentService.getEnrollmentById(1L)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("INSCRITO", response.status)
    }

    @Test
    fun `getEnrollmentById lanza EnrollmentNotFound cuando la inscripcion no existe`() {
        // Arrange
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFound> {
            enrollmentService.getEnrollmentById(99L)
        }
    }

    // ──────────────────────────────────────────────────────────────
    // updateEnrollmentStatus
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `updateEnrollmentStatus retorna respuesta actualizada cuando los datos son validos`() {
        // Arrange
        val request = EnrollmentStatusUpdateRequest(status = "APROBADO")
        val updated = enrollment.copy(status = "APROBADO")

        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))
        `when`(enrollmentRepository.save(any(Enrollment::class.java))).thenReturn(updated)

        // Act
        val response = enrollmentService.updateEnrollmentStatus(1L, request)

        // Assert
        assertEquals("APROBADO", response.status)
    }

    @Test
    fun `updateEnrollmentStatus lanza IllegalArgumentException cuando el status esta en blanco`() {
        // Arrange
        val request = EnrollmentStatusUpdateRequest(status = "")

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            enrollmentService.updateEnrollmentStatus(1L, request)
        }
        verify(enrollmentRepository, never()).findById(any())
    }

    @Test
    fun `updateEnrollmentStatus lanza EnrollmentNotFound cuando la inscripcion no existe`() {
        // Arrange
        val request = EnrollmentStatusUpdateRequest(status = "APROBADO")
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFound> {
            enrollmentService.updateEnrollmentStatus(99L, request)
        }
        verify(enrollmentRepository, never()).save(any())
    }

    // ──────────────────────────────────────────────────────────────
    // deleteEnrollment
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `deleteEnrollment elimina la inscripcion cuando existe`() {
        // Arrange
        `when`(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment))

        // Act
        enrollmentService.deleteEnrollment(1L)

        // Assert
        verify(enrollmentRepository).delete(enrollment)
    }

    @Test
    fun `deleteEnrollment lanza EnrollmentNotFound cuando la inscripcion no existe`() {
        // Arrange
        `when`(enrollmentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<EnrollmentNotFound> {
            enrollmentService.deleteEnrollment(99L)
        }
        verify(enrollmentRepository, never()).delete(any())
    }
}
