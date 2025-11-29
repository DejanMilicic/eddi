package dev.oblac.eddi.example.college.api

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

        Main.commands.launch(
            RegisterStudent(
                node.firstName,
                node.lastName,
                "${node.firstName.lowercase()}.${node.lastName.lowercase()}@college.edu"
            )
        )

        //call.respondText("Error", ContentType.Text.Plain, HttpStatusCode.BadRequest)
        call.respondText("Accepted", ContentType.Text.Plain, HttpStatusCode.Accepted)
    }
}
