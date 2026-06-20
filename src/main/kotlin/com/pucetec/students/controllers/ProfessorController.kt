package com.pucetec.students.controllers

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.services.ProfessorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/professors")
class ProfessorController(
    private val professorService: ProfessorService
) {

    @PostMapping
    fun createProfessor(@RequestBody request: ProfessorRequest): ResponseEntity<ProfessorResponse> {
        val response = professorService.createProfessor(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    fun getAllProfessors(): ResponseEntity<List<ProfessorResponse>> {
        val professors = professorService.getAllProfessors()
        return ResponseEntity.ok(professors)
    }

    @GetMapping("/{id}")
    fun getProfessorById(@PathVariable id: Long): ResponseEntity<ProfessorResponse> {
        val professor = professorService.getProfessorById(id)
        return ResponseEntity.ok(professor)
    }

    @PutMapping("/{id}")
    fun updateProfessor(
        @PathVariable id: Long,
        @RequestBody request: ProfessorRequest
    ): ResponseEntity<ProfessorResponse> {
        val response = professorService.updateProfessor(id, request)
        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/{id}")
    fun deleteProfessor(@PathVariable id: Long): ResponseEntity<Void> {
        professorService.deleteProfessor(id)
        return ResponseEntity.noContent().build()
    }
}
