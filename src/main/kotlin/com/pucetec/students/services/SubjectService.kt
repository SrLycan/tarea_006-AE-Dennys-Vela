package com.pucetec.students.services

import com.pucetec.students.dto.SubjectRequest
import com.pucetec.students.dto.SubjectResponse
import com.pucetec.students.exceptions.ProfessorNotFound
import com.pucetec.students.exceptions.SubjectNotFound
import com.pucetec.students.mappers.toEntity
import com.pucetec.students.mappers.toResponse
import com.pucetec.students.repositories.ProfessorRepository
import com.pucetec.students.repositories.SubjectRepository
import org.springframework.stereotype.Service

@Service
class SubjectService(
    private val subjectRepository: SubjectRepository,
    private val professorRepository: ProfessorRepository
) {

    fun createSubject(request: SubjectRequest): SubjectResponse {
        require(request.name.isNotBlank()) { "name must not be blank" }
        require(request.code.isNotBlank()) { "code must not be blank" }

        val professor = professorRepository.findById(request.professorId)
            .orElseThrow { ProfessorNotFound(request.professorId) }

        val subject = request.toEntity(professor)
        return subjectRepository.save(subject).toResponse()
    }

    fun getAllSubjects(): List<SubjectResponse> {
        return subjectRepository.findAll().map { it.toResponse() }
    }

    fun getSubjectById(id: Long): SubjectResponse {
        val subject = subjectRepository.findById(id)
            .orElseThrow { SubjectNotFound(id) }
        return subject.toResponse()
    }

    fun updateSubject(id: Long, request: SubjectRequest): SubjectResponse {
        require(request.name.isNotBlank()) { "name must not be blank" }
        require(request.code.isNotBlank()) { "code must not be blank" }

        val existing = subjectRepository.findById(id)
            .orElseThrow { SubjectNotFound(id) }

        val professor = professorRepository.findById(request.professorId)
            .orElseThrow { ProfessorNotFound(request.professorId) }

        val updated = existing.copy(
            name = request.name,
            code = request.code,
            professor = professor
        )

        return subjectRepository.save(updated).toResponse()
    }

    fun deleteSubject(id: Long) {
        val existing = subjectRepository.findById(id)
            .orElseThrow { SubjectNotFound(id) }
        subjectRepository.delete(existing)
    }

    fun findEntityById(id: Long) =
        subjectRepository.findById(id).orElseThrow { SubjectNotFound(id) }
}
