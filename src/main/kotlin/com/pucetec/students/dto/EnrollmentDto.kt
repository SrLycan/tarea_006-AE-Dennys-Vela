package com.pucetec.students.dto

import java.time.LocalDateTime

data class EnrollmentRequest(
    val studentId: Long,
    val subjectId: Long
)

data class EnrollmentStatusUpdateRequest(
    val status: String
)

data class EnrollmentResponse(
    val id: Long,
    val createdAt: LocalDateTime,
    val status: String,
    val student: StudentResponse,
    val subject: SubjectResponse
)
