package com.pucetec.students.services

import com.pucetec.students.dto.ProfessorRequest
import com.pucetec.students.dto.ProfessorResponse
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.ProfessorRepository
import org.springframework.stereotype.Service

@Service
class ProfessorService(
    private val professorRepository: ProfessorRepository
) {

    fun createProfessor(request: ProfessorRequest): ProfessorResponse {
        require(request.name.isNotBlank()) { "name must not be blank" }
        val professor = request.toEntity()
        return professorRepository.save(professor).toResponse()
    }

    fun getAllProfessors(): List<ProfessorResponse> {
        return professorRepository.findAll().map { it.toResponse() }
    }

    fun getProfessorById(id: Long): ProfessorResponse {
        val professor = professorRepository.findById(id)
            .orElseThrow { ProfessorNotFound(id) }
        return professor.toResponse()
    }

    fun updateProfessor(id: Long, request: ProfessorRequest): ProfessorResponse {
        require(request.name.isNotBlank()) { "name must not be blank" }
        val existing = professorRepository.findById(id)
            .orElseThrow { ProfessorNotFound(id) }

        val updated = existing.copy(
            name = request.name,
            email = request.email
        )

        return professorRepository.save(updated).toResponse()
    }

    fun deleteProfessor(id: Long) {
        val existing = professorRepository.findById(id)
            .orElseThrow { ProfessorNotFound(id) }
        professorRepository.delete(existing)
    }

    fun findEntityById(id: Long) =
        professorRepository.findById(id).orElseThrow { ProfessorNotFound(id) }
}
