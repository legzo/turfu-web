package org.jtelabs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val logger: Logger = LoggerFactory.getLogger("Main")

fun main() {

    simulate(
        filePath = "src/org.jtelabs.main/resources/pronos/pronos-real.txt",
        occurences = 2,
        topPlaceSynthese = 2,
        nonPartants = listOf(7)
    )

}

private fun simulate(
    filePath: String,
    occurences: Int,
    topPlaceSynthese: Int,
    nonPartants: List<Int> = listOf()
) {
    val pronostics = Loader(filePath).getPronostics()
    logger.info("Chargement des pronostics : \n${pronostics.prettyPrint()}")

    val synthese = pronostics.toSynthese()
    logger.info("\norg.jtelabs.Synthese : \n${synthese.lignes.prettyPrint()}\n")

    val allCombinaisons = pronostics.flatMap {
        val combinaisons = it.toCombinaisons()
        logger.debug("${combinaisons.size} combinaisons pour $it, combinaisons -> $combinaisons")
        combinaisons
    }

    logger.info("${allCombinaisons.size} combinaisons en tout")

    val combinationsFilteredByPopularity = allCombinaisons.limitOccurencesTo(occurences)
    logger.info("\n${combinationsFilteredByPopularity.size} combinaisons données au plus $occurences fois")

    val combinationsFilteredByPopularityAndSynthese =
        combinationsFilteredByPopularity.filterWithTopPlaceFromSynthese(topPlaceSynthese, synthese)

    logger.info("\n${combinationsFilteredByPopularityAndSynthese.size} combinaisons après filtre par la synthèse (top $topPlaceSynthese)")

    val combinaisonsFinales = combinationsFilteredByPopularityAndSynthese
        .excludeNonPartants(nonPartants)

    logger.info("\nCombinaisons finales: \n${combinaisonsFinales.prettyPrint()}")
}

fun List<Any>.prettyPrint() = this.joinToString(separator = "\n") { "  $it" }
