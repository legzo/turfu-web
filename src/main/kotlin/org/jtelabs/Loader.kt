package org.jtelabs

import java.io.File

class Loader(private val filePath: String) {

    fun getPronostics() = File(filePath)
        .readLines()
        .parseLines()
}

fun List<String>.parseLines() =
    this.mapNotNull { line ->
        try {
            val trimed = line.trim()

            if (trimed.isNotBlank()) {
                val tokens = trimed.split(' ', '\t')
                Pronostic(tokens[0], tokens.drop(1).map { it.toInt() })
            } else null

        } catch (t: Throwable) { null }
    }

class StringLoader(private val content: String?) {

    fun getPronostics(): List<Pronostic> {
        if (content == null) return listOf()

        return content
            .split(';')
            .parseLines()
    }
}
