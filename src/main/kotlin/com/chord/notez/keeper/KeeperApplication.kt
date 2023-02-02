package com.chord.notez.keeper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class KeeperApplication

fun main(args: Array<String>) {
	runApplication<KeeperApplication>(*args)
}

@Table("MESSAGES")
data class Message(@Id val id: String?, val text: String)

interface MessageRepository : CrudRepository<Message, String>{
	@Query("select * from messages")
	fun findMessages(): List<Message>
}

@Service
class MessageService(val db: MessageRepository) {

	fun findMessages(): List<Message> = db.findMessages()

	fun post(message: Message) {
		db.save(message)
	}
}

@RestController
class MessageResource(val service: MessageService) {
	@GetMapping("/message")
	fun index(): List<Message> = service.findMessages()

	@PostMapping("/message")
	fun post(@RequestBody message: Message) {
		service.post(message)
	}
}