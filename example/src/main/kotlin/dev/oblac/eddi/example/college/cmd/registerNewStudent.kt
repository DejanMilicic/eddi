package dev.oblac.eddi.example.college.cmd

import arrow.core.Either
import arrow.core.raise.either
import dev.oblac.eddi.EventStore
import dev.oblac.eddi.example.college.RegisterStudent
import dev.oblac.eddi.example.college.StudentRegistered
import dev.oblac.eddi.example.college.StudentRegisteredEvent
import arrow.core.raise.ensure

object RegisterNewStudentError

fun registerNewStudent(es: EventStore, command: RegisterStudent): Either<RegisterNewStudentError, Unit> = either {
    uniqueEmail(es, command.email).bind()

    es.storeEvent(
        StudentRegistered(
            firstName = command.firstName,
            lastName = command.lastName,
            email = command.email
        )
    )
}

private fun uniqueEmail(es: EventStore, email: String): Either<RegisterNewStudentError, Unit> = either {
    ensure(es.findEvents<StudentRegistered>(StudentRegisteredEvent.NAME, mapOf("email" to email)).isEmpty()) {
        RegisterNewStudentError
    }
}
