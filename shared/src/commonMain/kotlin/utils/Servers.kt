package commands.akotlinator.utils

import ServerNotFoundException
import GuessType
import Language
import Server
import ServerList
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*


object Servers {
    private const val LIST_URL =
        "https://global3.akinator.com/ws/instances_v2.php?media_id=14&mode=https&footprint=cd8e6509f3420878e18d75b9831b317f"

    suspend fun findServers(client: HttpClient, localization: Language, guessType: GuessType): ServerList {
        val servers = getServers(client)
            .filter { it.guessType == guessType && it.language == localization }

        if (servers.isEmpty())
            throw ServerNotFoundException()

        return ServerList(servers)
    }

    suspend fun getServers(client: HttpClient): List<Server> {
        val listXml = fetchListXml(client)
        // You would need to use an XML parser here to parse the listXml into a List<Server>.
        // This is just a placeholder.
        return listOf()
    }

    private suspend fun fetchListXml(client: HttpClient): HttpResponse {
        return client.get(LIST_URL)
    }
}
