package com.pucetec.students.mappers

import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.entities.Enrollment

fun Enrollment.toResponse(): EnrollmentResponse {
    return EnrollmentResponse(
        id = this.id,
        createdAt = this.createdAt,
        status = this.status,
        student = this.student.toResponse(),
        subject = this.subject.toResponse()
    )
}
