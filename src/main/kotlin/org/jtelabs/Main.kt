package org.jtelabs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger("Main")

fun main() {

    simulate(
        filePath = "src/main/resources/pronos/pronos-real.txt",
        occurences = 2,
        topPlaceSynthese = 2,
        nonPartants = listOf(7)
    )
/*
    simulate(
        fileContent = "P1 2 15 3 6 12 1 8 5;P2 1 2 8 9 3 5 7 6;P3 2 1 5 9 6 8 7 15;P4 1 7 8 6 4 2 3 5;P5 1 2 15 5 6 4 12 9;P6 15 3 11 1 2 6 8 12;P7 15 6 1 2 3 8 12 5;P8 12 5 1 2 15 7 6 11;P9 7 12 6 1 2 15 3 10;P10 1 2 8 6 15 3 12 5;P11 1 3 2 8 6 15 12 9;P12 2 6 1 15 3 8 12 5;P13 5 8 2 3 1 12 6 5;P14 1 2 3 6 15 8 12 5;P15 15 8 1 2 6 3 12 5;P16 1 8 2 12 6 15 5 3;P17 1 2 6 15 3 8 12 5;P18 1 2 6 15 3 8 12 7;P19 5 2 15 1 3 6 8 12;P20 2 6 1 15 3 8 12 5;P21 12 7 2 8 5 1 6 15;",
        occurences = 1,
        topPlaceSynthese = 4,
        nonPartants = listOf()
    )
*/
}

private fun simulate(
    filePath: String? = null,
    fileContent: String? = null,
    occurences: Int,
    topPlaceSynthese: Int,
    nonPartants: List<Int> = listOf()
) {
    val pronostics = if (filePath != null) {
        Loader(filePath).getPronostics()
    } else StringLoader(fileContent!!).getPronostics()

    logger.info("Chargement des pronostics : \n${pronostics.prettyPrint()}")

    val synthese = pronostics.toSynthese()
    logger.info("\nSynthese : \n${synthese.lignes.prettyPrint()}\n")

    val allCombinaisons = pronostics.flatMap {
        val combinaisons = it.toCombinaisons()
        logger.debug("${combinaisons.size} combinaisons pour $it, combinaisons -> $combinaisons")
        combinaisons
    }

    logger.info("${allCombinaisons.size} combinaisons en tout")

    val combinaisonsFinales = listOf(
        FiltreDeFrequence(occurences),
        FiltreTopSynthese(topPlaceSynthese, synthese),
        FiltreNonPartants(nonPartants)
    ).fold(allCombinaisons) { acc, filter ->
        val result = filter.filter(acc)
        logger.info("Application du filtre ${filter.name} >> ${acc.size} combinaisons")
        result
    }

    logger.info("\nCombinaisons finales: \n${combinaisonsFinales.prettyPrint()}")
}

fun List<Any>.prettyPrint() = this.joinToString(separator = "\n") { "  $it" }
fun List<Any>.prettyPrintForFilters() = this.joinToString(separator = "\n▽\n") { "$it" }

