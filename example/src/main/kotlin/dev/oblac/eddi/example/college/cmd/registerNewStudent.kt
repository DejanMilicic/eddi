package dev.oblac.eddi.example.college.cmd

import arrow.core.Either
import arrow.core.raise.either
import dev.oblac.eddi.example.college.RegisterStudent
import dev.oblac.eddi.example.college.StudentRegistered
import arrow.core.raise.ensure
import dev.oblac.eddi.CommandError
import dev.oblac.eddi.Seq

object RegisterNewStudentError

fun registerNewStudent(emailExists: (String) -> Boolean, command: RegisterStudent): Either<CommandError, StudentRegistered> =
    uniqueStudentEmail(emailExists, command.email)
        .mapLeft { it as CommandError }  // Convert RegisterNewStudentError to CommandError
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
): Either<RegisterNewStudentError, Unit> = either {
    ensure(!emailExists(email)) { RegisterNewStudentError }
}



