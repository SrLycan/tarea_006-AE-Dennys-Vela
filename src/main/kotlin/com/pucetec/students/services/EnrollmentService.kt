package com.pucetec.students.services

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.dto.EnrollmentStatusUpdateRequest
import com.pucetec.students.entities.Enrollment
import com.pucetec.students.exceptions.EnrollmentNotFound
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.EnrollmentRepository
import com.pucetec.students.repositories.StudentRepository
import com.pucetec.students.repositories.SubjectRepository
import org.springframework.stereotype.Service

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val studentRepository: StudentRepository,
    private val subjectRepository: SubjectRepository
) {

    fun createEnrollment(request: EnrollmentRequest): EnrollmentResponse {
        val student = studentRepository.findById(request.studentId)
            .orElseThrow { StudentNotFoundException(request.studentId) }

        val subject = subjectRepository.findById(request.subjectId)
            .orElseThrow { SubjectNotFound(request.subjectId) }

        val enrollment = Enrollment(
            status = "INSCRITO",
            student = student,
            subject = subject
        )

        return enrollmentRepository.save(enrollment).toResponse()
    }

    fun getAllEnrollments(): List<EnrollmentResponse> {
        return enrollmentRepository.findAll().map { it.toResponse() }
    }

    fun getEnrollmentById(id: Long): EnrollmentResponse {
        val enrollment = enrollmentRepository.findById(id)
            .orElseThrow { EnrollmentNotFound(id) }
        return enrollment.toResponse()
    }

    fun updateEnrollmentStatus(id: Long, request: EnrollmentStatusUpdateRequest): EnrollmentResponse {
        require(request.status.isNotBlank()) { "status must not be blank" }

        val existing = enrollmentRepository.findById(id)
            .orElseThrow { EnrollmentNotFound(id) }

        val updated = existing.copy(status = request.status)

        return enrollmentRepository.save(updated).toResponse()
    }

    fun deleteEnrollment(id: Long) {
        val existing = enrollmentRepository.findById(id)
            .orElseThrow { EnrollmentNotFound(id) }
        enrollmentRepository.delete(existing)
    }
}
