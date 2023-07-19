package commands.akotlinator.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*

object KtorUtils {
    private var client: HttpClient? = null

    @Synchronized
    fun getClient(): HttpClient {
        if (client == null) {
            client = HttpClient(CIO) {
                engine {
                    https {
                        trustManager = SslSettings.getTrustManager()
                    }
                }
                defaultRequest {
                    header(
                        "Accept",
                        "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript, */*. q=0.01"
                    )
                    header("Accept-Language", "en-US,en.q=0.9,ar.q=0.8")
                    header("X-Requested-With", "XMLHttpRequest")
                    header("Sec-Fetch-Dest", "empty")
                    header("Sec-Fetch-Mode", "cors")
                    header("Sec-Fetch-Site", "same-origin")
                    header("Connection", "keep-alive")
                    header(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0. Win64. x64) AppleWebKit/537.36" +
                                "(KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36"
                    )
                    header("Referer", "https://en.akinator.com/game")
                }
            }
        }
        return client!!
    }
}