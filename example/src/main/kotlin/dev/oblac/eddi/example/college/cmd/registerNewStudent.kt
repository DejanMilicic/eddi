package dev.oblac.eddi.example.college.cmd

import arrow.core.raise.either
import dev.oblac.eddi.example.college.RegisterStudent
import dev.oblac.eddi.example.college.StudentRegistered
import arrow.core.raise.ensure

object RegisterNewStudentError

fun registerNewStudent(emailExists: (String) -> Boolean, command: RegisterStudent) =
    uniqueStudentEmail(emailExists, command.email)
        .map {
            StudentRegistered(
                firstName = command.firstName,
                lastName = command.lastName,
                email = command.email
            )
        }

private fun uniqueStudentEmail(
    emailExists: (String) -> Boolean,
    email: String
) = either {
    ensure(!emailExists(email)) { RegisterNewStudentError }
}
