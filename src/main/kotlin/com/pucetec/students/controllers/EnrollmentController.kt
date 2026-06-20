package com.pucetec.students.controllers

import com.pucetec.students.dto.EnrollmentRequest
import com.pucetec.students.dto.EnrollmentResponse
import com.pucetec.students.dto.EnrollmentStatusUpdateRequest
import com.pucetec.students.services.EnrollmentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/enrollments")
class EnrollmentController(
    private val enrollmentService: EnrollmentService
) {

    @PostMapping
    fun createEnrollment(@RequestBody request: EnrollmentRequest): ResponseEntity<EnrollmentResponse> {
        val response = enrollmentService.createEnrollment(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getAllEnrollments(): ResponseEntity<List<EnrollmentResponse>> {
        val enrollments = enrollmentService.getAllEnrollments()
        return ResponseEntity.ok(enrollments)
    }

    @GetMapping("/{id}")
    fun getEnrollmentById(@PathVariable id: Long): ResponseEntity<EnrollmentResponse> {
        val enrollment = enrollmentService.getEnrollmentById(id)
        return ResponseEntity.ok(enrollment)
    }

    @PutMapping("/{id}")
    fun updateEnrollmentStatus(
        @PathVariable id: Long,
        @RequestBody request: EnrollmentStatusUpdateRequest
    ): ResponseEntity<EnrollmentResponse> {
        val response = enrollmentService.updateEnrollmentStatus(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteEnrollment(@PathVariable id: Long): ResponseEntity<Void> {
        enrollmentService.deleteEnrollment(id)
        return ResponseEntity.noContent().build()
    }
}
