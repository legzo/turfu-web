package org.jtelabs

import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {

    val logger = LoggerFactory.getLogger("TurfuApp")

    val port = if (args.isNotEmpty()) args[0].toInt() else 5000

    val app = routes(
        "/api" bind routes(
            "generate" bind POST to { request: Request ->

                val pronos = request.query("pronos").orEmpty()
                val occurences = request.query("occurences").orEmpty()
                val topXSynthese = request.query("topXSynthese").orEmpty()
                val nonPartants = request.query("nonPartants").orEmpty()

                val message = "pronos: $pronos, occurences: $occurences, " +
                        "topXSynthese: $topXSynthese, nonPartants: $nonPartants"

                logger.info(message)

                val pronostics = StringLoader(pronos).getPronostics()
                val synthese = pronostics.toSynthese()

                val allCombinaisons = pronostics.flatMap { it.toCombinaisons() }

                val combinationsFilteredByPopularity = allCombinaisons.limitOccurencesTo(occurences.toInt())

                val combinationsFilteredByPopularityAndSynthese =
                    combinationsFilteredByPopularity.filterWithTopPlaceFromSynthese(topXSynthese.toInt(), synthese)

                val nonPartantsAsList = nonPartants.parseAsList()

                val combinaisonsFinales =
                    combinationsFilteredByPopularityAndSynthese.excludeNonPartants(nonPartantsAsList)

                val logs = """${pronostics.prettyPrint()}
                    |
                    |Synthèse :
                    |${synthese.lignes.prettyPrint()}
                    |
                    |${allCombinaisons.size} combinaisons en tout
                    |▽
                    |${combinationsFilteredByPopularity.size} combinaisons données au plus $occurences fois
                    |▽
                    |${combinationsFilteredByPopularityAndSynthese.size} combinaisons après filtre par la synthèse (max 1 dans top $topXSynthese)
                    |▽
                    |${combinaisonsFinales.size} combinaisons après exclusion des non partants ($nonPartantsAsList)
                    |
                    |✨ Combinaisons finales: 
                    |${combinaisonsFinales.prettyPrint()}
                """.trimMargin()
                Response(OK).body(logs)
            }
        ),
        "/" bind static(Classpath("/web"))
    )


    app.asServer(Jetty(port)).start().block()
}

private fun String.parseAsList() = if (!isBlank()) trim().split(' ').map { it.toInt() } else listOf()