package me.nspain.cubtracking.badgework

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class BadgeworkApplication

fun main(args: Array<String>) {
    runApplication<BadgeworkApplication>(*args)
}
