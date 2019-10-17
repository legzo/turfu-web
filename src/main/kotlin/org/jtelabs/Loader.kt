package org.jtelabs

import java.io.File

class Loader(private val filePath: String) {

    fun getPronostics() = File(filePath)
        .readLines()
        .parseLines()
}

fun List<String>.parseLines() =
    this.map { line ->
        val tokens = line.split(' ')
        Pronostic(tokens[0], tokens.drop(1).map { it.toInt() })
    }

class StringLoader(private val content: String) {
    fun getPronostics() = content
        .split(';')
        .parseLines()
}
