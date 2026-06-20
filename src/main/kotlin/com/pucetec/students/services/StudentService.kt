package com.pucetec.students.services

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentRepository: StudentRepository
) {

    fun createStudent(request: StudentRequest): StudentResponse {
        require(request.name.isNotBlank()) { "name must not be blank" }
        val student = request.toEntity()
        return studentRepository.save(student).toResponse()
    }

    fun getAllStudents(): List<StudentResponse> {
        return studentRepository.findAll().map { it.toResponse() }
    }

    fun getStudentById(id: Long): StudentResponse {
        val student = studentRepository.findById(id)
            .orElseThrow { StudentNotFoundException(id) }
        return student.toResponse()
    }

    fun updateStudent(id: Long, request: StudentRequest): StudentResponse {
        require(request.name.isNotBlank()) { "name must not be blank" }
        val existing = studentRepository.findById(id)
            .orElseThrow { StudentNotFoundException(id) }

        val updated = existing.copy(
            name = request.name,
            email = request.email
        )

        return studentRepository.save(updated).toResponse()
    }

    fun deleteStudent(id: Long) {
        val existing = studentRepository.findById(id)
            .orElseThrow { StudentNotFoundException(id) }
        studentRepository.delete(existing)
    }
}
