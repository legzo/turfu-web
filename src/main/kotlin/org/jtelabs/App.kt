package org.jtelabs

import org.http4k.core.Method
import org.http4k.core.Method.GET
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

                val pronos = request.query("pronos")
                val occurences = request.query("occurences")
                val topXSynthese = request.query("topXSynthese")
                val nonPartants = request.query("nonPartants")

                val message = "pronos: $pronos, occurences: $occurences, " +
                    "topXSynthese: $topXSynthese, nonPartants: $nonPartants"

                logger.info(message)

                val parsedPronos = StringLoader(pronos.orEmpty()).getPronostics()

                Response(OK).body(parsedPronos.prettyPrint())
            }
        ),
        "/" bind static(Classpath("/web"))
    )


    app.asServer(Jetty(port)).start().block()
}
