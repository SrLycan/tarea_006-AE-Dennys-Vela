package com.pucetec.students.controllers

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.services.SubjectService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subjects")
class SubjectController(
    private val subjectService: SubjectService
) {

    @PostMapping
    fun createSubject(@RequestBody request: SubjectRequest): ResponseEntity<SubjectResponse> {
        val response = subjectService.createSubject(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getAllSubjects(): ResponseEntity<List<SubjectResponse>> {
        val subjects = subjectService.getAllSubjects()
        return ResponseEntity.ok(subjects)
    }

    @GetMapping("/{id}")
    fun getSubjectById(@PathVariable id: Long): ResponseEntity<SubjectResponse> {
        val subject = subjectService.getSubjectById(id)
        return ResponseEntity.ok(subject)
    }

    @PutMapping("/{id}")
    fun updateSubject(
        @PathVariable id: Long,
        @RequestBody request: SubjectRequest
    ): ResponseEntity<SubjectResponse> {
        val response = subjectService.updateSubject(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteSubject(@PathVariable id: Long): ResponseEntity<Void> {
        subjectService.deleteSubject(id)
        return ResponseEntity.noContent().build()
    }
}
