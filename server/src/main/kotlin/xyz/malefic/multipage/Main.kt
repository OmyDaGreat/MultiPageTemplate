package xyz.malefic.multipage

import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.then
import org.http4k.filter.ServerFilters.CatchLensFailure
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Undertow
import org.http4k.server.asServer

val app: HttpHandler =
    CatchLensFailure.then(
        routes(
            "/ping" bind GET to { _: Request ->
                Response(OK)
            },
            "/hello" bind GET to { request: Request ->
                Response(OK).body("Hello, ${request.query("name")}!")
            },
            "/add" bind GET to { request: Request ->
                Response(OK).body(request.query("first") + request.query("second"))
            },
        ),
    )

fun main() {
    println("Hello World!")
    app.asServer(Undertow(9000)).start()
}
