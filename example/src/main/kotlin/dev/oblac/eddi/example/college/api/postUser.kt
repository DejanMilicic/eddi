package dev.oblac.eddi.example.college.api

import arrow.core.Either
import dev.oblac.eddi.CommandError
import dev.oblac.eddi.db.DbEventStore
import dev.oblac.eddi.example.college.Main
import dev.oblac.eddi.example.college.RegisterStudent
import dev.oblac.eddi.json.Json
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

data class StudentRequest(
    val firstName: String,
    val lastName: String
)

fun Routing.apiStudents() {
    post("/api/students") {
        val body = call.receiveText()
        val node = Json.fromJson(body, StudentRequest::class)

        val command = RegisterStudent(
            node.firstName,
            node.lastName,
            "${node.firstName.lowercase()}.${node.lastName.lowercase()}@college.edu"
        )

        println("About to invoke command: $command")

        try {
            val result: Either<CommandError, Unit> = Main.commands(command)

            println("Result: $result")

            result.fold(
                ifLeft = { error ->
                    println("Error occurred: $error")
                    call.respondText("$error", ContentType.Text.Plain, HttpStatusCode.BadRequest)
                },
                ifRight = {
                    println("Success!")
                    call.respondText("Accepted", ContentType.Text.Plain, HttpStatusCode.Accepted)
                }
            )
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            e.printStackTrace()
            call.respondText("Exception: ${e.message}", ContentType.Text.Plain, HttpStatusCode.InternalServerError)
        }

//        val result = Main.commands.invoke<Unit>(command)
//
//        println("Result: $result")
//
//        result.fold(
//            ifLeft = { error ->
//                call.respondText("Error: $error", ContentType.Text.Plain, HttpStatusCode.BadRequest)
//            },
//            ifRight = {
//                call.respondText("Accepted", ContentType.Text.Plain, HttpStatusCode.Accepted)
//            }
//        )
    }
}
