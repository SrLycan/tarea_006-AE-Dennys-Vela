package com.pucetec.students.exceptions

class StudentNotFoundException(id: Long) : RuntimeException("Student with id $id not found")

class ProfessorNotFound(id: Long) : RuntimeException("Professor with id $id not found")

class SubjectNotFound(id: Long) : RuntimeException("Subject with id $id not found")

class EnrollmentNotFound(id: Long) : RuntimeException("Enrollment with id $id not found")
