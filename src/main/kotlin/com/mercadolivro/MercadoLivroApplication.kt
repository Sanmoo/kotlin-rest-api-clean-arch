package com.mercadolivro

import com.mercadolivro.configuration.beans
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MercadoLivroApplication

fun main(args: Array<String>) {
	runApplication<MercadoLivroApplication>(*args) {
		addInitializers(beans)
	}
}
