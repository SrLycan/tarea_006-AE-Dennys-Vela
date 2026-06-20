package com.pucetec.students.controllers

import com.pucetec.students.dto.StudentRequest
import com.pucetec.students.dto.StudentResponse
import com.pucetec.students.services.StudentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/students")
class StudentController(
    private val studentService: StudentService
) {

    @PostMapping
    fun createStudent(@RequestBody request: StudentRequest): ResponseEntity<StudentResponse> {
        val response = studentService.createStudent(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getAllStudents(): ResponseEntity<List<StudentResponse>> {
        val students = studentService.getAllStudents()
        return ResponseEntity.ok(students)
    }

    @GetMapping("/{id}")
    fun getStudentById(@PathVariable id: Long): ResponseEntity<StudentResponse> {
        val student = studentService.getStudentById(id)
        return ResponseEntity.ok(student)
    }

    @PutMapping("/{id}")
    fun updateStudent(
        @PathVariable id: Long,
        @RequestBody request: StudentRequest
    ): ResponseEntity<StudentResponse> {
        val response = studentService.updateStudent(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteStudent(@PathVariable id: Long): ResponseEntity<Void> {
        studentService.deleteStudent(id)
        return ResponseEntity.noContent().build()
    }
}
