package com.pucetec.students.handlers

import com.pucetec.students.exceptions.EnrollmentNotFound
import com.pucetec.students.exceptions.ErrorResponse
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.exceptions.StudentNotFoundException
import com.pucetec.students.exceptions.SubjectNotFound
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException::class)
    fun handleStudentNotFound(ex: StudentNotFoundException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.NOT_FOUND, ex.message)
    }

    @ExceptionHandler(ProfessorNotFound::class)
    fun handleProfessorNotFound(ex: ProfessorNotFound): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.NOT_FOUND, ex.message)
    }

    @ExceptionHandler(SubjectNotFound::class)
    fun handleSubjectNotFound(ex: SubjectNotFound): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.NOT_FOUND, ex.message)
    }

    @ExceptionHandler(EnrollmentNotFound::class)
    fun handleEnrollmentNotFound(ex: EnrollmentNotFound): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.NOT_FOUND, ex.message)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.message)
    }

    private fun buildResponse(status: HttpStatus, message: String?): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = message
        )
        return ResponseEntity.status(status).body(body)
    }
}
