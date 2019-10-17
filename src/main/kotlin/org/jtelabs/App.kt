package org.jtelabs

import org.http4k.core.Method.GET
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty()) args[0].toInt() else 5000

    val app = routes(
        "/api" bind GET to { Response(OK).body("Hello World!") },
        "/" bind static(Classpath("/web"))
    )


    app.asServer(Jetty(port)).start().block()
}
