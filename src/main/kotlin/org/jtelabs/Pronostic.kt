package org.jtelabs

import org.paukov.combinatorics3.Generator

data class Pronostic(
    val id: String,
    val chevaux: List<Int>
) {
    fun toCombinaisons() = Generator
        .combination(chevaux)
        .simple(4)
        .toSet()
        .map { Combinaison(it.toSet()) }
}
