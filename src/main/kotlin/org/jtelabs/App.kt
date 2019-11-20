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
                val chevauxSpecifies = request.query("chevauxSpecifies").orEmpty()

                val pronostics = StringLoader(pronos).getPronostics()
                val synthese = pronostics.toSynthese()

                val allCombinaisons = pronostics.flatMap {
                    val combinaisons = it.toCombinaisons()
                    if (combinaisons.any { combi -> combi.chevaux.size != 4 }) {
                        logger.warn("Attention doublon potentiel pour le prono : $it")
                    }
                    combinaisons
                }

                class FilterResult(val combinaisons: List<Combinaison>, val logLines: List<String> = listOf())

                val combinaisonsFinalesResult =
                    listOf(
                        FiltreOccurences(occurences.toInt()),
                        FiltreTopSynthese(topXSynthese.toInt(), synthese),
                        ExcludeNonPartants(nonPartants.parseAsList()),
                        // ExcludeTousSimilaires(),
                        FiltreUnParmi(chevauxSpecifies.parseAsList())
                    ).fold(initial = FilterResult(allCombinaisons)) { acc, current ->
                        val filteredCombinaisons = current.filter(acc.combinaisons)
                        val logLine = "Application du ${current.name} -> ${filteredCombinaisons.size} combinaisons"
                        FilterResult(filteredCombinaisons, acc.logLines + logLine)
                    }

                val combinaisonsFinales = combinaisonsFinalesResult.combinaisons

                val logs = """${pronostics.prettyPrint()}
                    |
                    |Synthèse :
                    |${synthese.lignes.prettyPrint()}
                    |
                    |${allCombinaisons.size} combinaisons en tout
                    |▽
                    |${combinaisonsFinalesResult.logLines.prettyPrintForFilters()}
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

private fun String.parseAsList() = if (!isBlank()) trim().split(" ", ", ").map { it.toInt() } else listOf()
