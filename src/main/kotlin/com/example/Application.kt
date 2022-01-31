package com.example

import Connection
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(WebSockets)
    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        val whisperConnections = Collections.synchronizedMap<List<String>, MutableList<Connection>>(LinkedHashMap())

        webSocket("/chat") {
            println("Adding user!")
            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    connections.forEach {
                        it.session.send(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection!")
                connections -= thisConnection
            }
        }

        // whisper
//        webSocket("/whisper") {
//            println("whisper socket in")
//            val name = call.request.queryParameters["name"].toString()
//            val target = call.request.queryParameters["target"].toString()
//            val thisConnection = Connection(this, name)
//            val idList: List<String> = listOf(
//                name, target
//            ).sortedBy { it }
//
//            val connections = if (whisperConnections[idList].isNullOrEmpty()) {
//                whisperConnections[idList] = mutableListOf(thisConnection)
//                whisperConnections[idList]
//            } else {
//                whisperConnections[idList]!!.add(thisConnection)
//                whisperConnections[idList]
//            }
//
//            try {
//                send("You are connected for whisper! There are ${connections!!.size} here. AGN! Spend Happy Time!")
//                for (frame in incoming) {
//                    frame as? Frame.Text ?: continue
//                    val receivedText = frame.readText()
//                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
//                    connections.forEach {
//                        it.session.send(textWithUsername)
//                    }
//                }
//            } catch (e: Exception) {
//                println(e.localizedMessage)
//            } finally {
//                println("Removing $thisConnection!")
//                whisperConnections.remove(idList)
//            }
//        }
    }
}

