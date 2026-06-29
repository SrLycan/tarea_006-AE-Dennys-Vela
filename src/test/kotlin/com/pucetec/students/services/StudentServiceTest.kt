package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.entities.Student
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.repositories.StudentRepository
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
class StudentServiceTest {

    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentService: StudentService

    // ──────────────────────────────────────────────────────────────
    // createStudent
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `createStudent retorna respuesta cuando el nombre es valido`() {
        // Arrange
        val request = StudentRequest(name = "Ana Lopez", email = "ana@puce.edu")
        val saved = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        `when`(studentRepository.save(any(Student::class.java))).thenReturn(saved)

        // Act
        val response = studentService.createStudent(request)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Ana Lopez", response.name)
        assertEquals("ana@puce.edu", response.email)
    }

    @Test
    fun `createStudent lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        // Arrange
        val request = StudentRequest(name = "", email = "vacio@puce.edu")

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            studentService.createStudent(request)
        }
        verify(studentRepository, never()).save(any())
    }

    // ──────────────────────────────────────────────────────────────
    // getAllStudents
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `getAllStudents retorna lista de estudiantes cuando existen registros`() {
        // Arrange
        val students = listOf(
            Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu"),
            Student(id = 2L, name = "Carlos Ruiz", email = "carlos@puce.edu")
        )
        `when`(studentRepository.findAll()).thenReturn(students)

        // Act
        val response = studentService.getAllStudents()

        // Assert
        assertEquals(2, response.size)
        assertEquals("Ana Lopez", response[0].name)
        assertEquals("Carlos Ruiz", response[1].name)
    }

    @Test
    fun `getAllStudents retorna lista vacia cuando no hay estudiantes`() {
        // Arrange
        `when`(studentRepository.findAll()).thenReturn(emptyList())

        // Act
        val response = studentService.getAllStudents()

        // Assert
        assertTrue(response.isEmpty())
    }

    // ──────────────────────────────────────────────────────────────
    // getStudentById
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `getStudentById retorna respuesta cuando el estudiante existe`() {
        // Arrange
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        // Act
        val response = studentService.getStudentById(1L)

        // Assert
        assertEquals(1L, response.id)
        assertEquals("Ana Lopez", response.name)
    }

    @Test
    fun `getStudentById lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.getStudentById(99L)
        }
    }

    // ──────────────────────────────────────────────────────────────
    // updateStudent
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `updateStudent retorna respuesta actualizada cuando los datos son validos`() {
        // Arrange
        val existing = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        val request = StudentRequest(name = "Ana Martinez", email = "ana.m@puce.edu")
        val updated = existing.copy(name = "Ana Martinez", email = "ana.m@puce.edu")

        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(existing))
        `when`(studentRepository.save(any(Student::class.java))).thenReturn(updated)

        // Act
        val response = studentService.updateStudent(1L, request)

        // Assert
        assertEquals("Ana Martinez", response.name)
        assertEquals("ana.m@puce.edu", response.email)
    }

    @Test
    fun `updateStudent lanza IllegalArgumentException cuando el nombre esta en blanco`() {
        // Arrange
        val request = StudentRequest(name = "  ", email = "ana@puce.edu")

        // Act & Assert
        assertThrows<IllegalArgumentException> {
            studentService.updateStudent(1L, request)
        }
        verify(studentRepository, never()).findById(any())
    }

    @Test
    fun `updateStudent lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        val request = StudentRequest(name = "Nuevo Nombre", email = "nuevo@puce.edu")
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.updateStudent(99L, request)
        }
    }

    // ──────────────────────────────────────────────────────────────
    // deleteStudent
    // ──────────────────────────────────────────────────────────────

    @Test
    fun `deleteStudent elimina el estudiante cuando existe`() {
        // Arrange
        val student = Student(id = 1L, name = "Ana Lopez", email = "ana@puce.edu")
        `when`(studentRepository.findById(1L)).thenReturn(Optional.of(student))

        // Act
        studentService.deleteStudent(1L)

        // Assert
        verify(studentRepository).delete(student)
    }

    @Test
    fun `deleteStudent lanza StudentNotFoundException cuando el estudiante no existe`() {
        // Arrange
        `when`(studentRepository.findById(99L)).thenReturn(Optional.empty())

        // Act & Assert
        assertThrows<StudentNotFoundException> {
            studentService.deleteStudent(99L)
        }
        verify(studentRepository, never()).delete(any())
    }
}
