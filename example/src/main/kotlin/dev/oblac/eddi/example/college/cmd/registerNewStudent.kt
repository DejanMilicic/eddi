package dev.oblac.eddi.example.college.cmd

import arrow.core.raise.either
import dev.oblac.eddi.EventStore
import dev.oblac.eddi.example.college.RegisterStudent
import dev.oblac.eddi.example.college.StudentRegistered
import dev.oblac.eddi.example.college.StudentRegisteredEvent
import arrow.core.raise.ensure

object RegisterNewStudentError

fun registerNewStudent(es: EventStore, command: RegisterStudent) =
    uniqueStudentEmail(es, command.email)
        .map {
            StudentRegistered(
                firstName = command.firstName,
                lastName = command.lastName,
                email = command.email
            )
        }

private fun uniqueStudentEmail(es: EventStore, email: String) = either {
    ensure(es.findEvents<StudentRegistered>(StudentRegisteredEvent.NAME, mapOf("email" to email)).isEmpty())
        { RegisterNewStudentError }
}
