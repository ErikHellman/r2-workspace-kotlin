package org.readium.r2.streamer.Server

import fi.iki.elonen.router.RouterNanoHTTPD
import org.readium.r2.shared.Publication
import org.readium.r2.streamer.Containers.Container
import org.readium.r2.streamer.Fetcher.Fetcher
import org.readium.r2.streamer.Server.handler.*
import java.net.URL


class Server(port: Int) : AbstractServer(port) {

}

abstract class AbstractServer(port: Int) : RouterNanoHTTPD(port) {

    private val SEARCH_QUERY_HANDLE = "/search"
    private val MANIFEST_HANDLE = "/manifest"
    private val MANIFEST_ITEM_HANDLE = "/(.*)"
    private val MEDIA_OVERLAY_HANDLE = "/media-overlay"
    private val CSS_HANDLE = "/styles/(.*)"
    private val JS_HANDLE = "/scripts/(.*)"
    private var containsMediaOverlay = false

    private val ressources = Ressources()

    fun addResource(name: String, body: String) {
        ressources.add(name, body)
    }

    fun addEpub(publication: Publication, container: Container, fileName: String) {

        val fetcher = Fetcher(publication, container)

        addLinks(publication, fileName)

        publication.addSelfLink(fileName, URL("http://localhost:3333"))

        if (containsMediaOverlay) {
            addRoute(fileName + MEDIA_OVERLAY_HANDLE, MediaOverlayHandler::class.java, fetcher)
        }
        addRoute(fileName + MANIFEST_HANDLE, ManifestHandler::class.java, fetcher)
        addRoute(fileName + MANIFEST_ITEM_HANDLE, ResourceHandler::class.java, fetcher)
        addRoute( JS_HANDLE, JSHandler::class.java, ressources)
        addRoute( CSS_HANDLE, CSSHandler::class.java, ressources)
    }

    private fun addLinks(publication: Publication, filePath: String) {
        containsMediaOverlay = false
        for (link in publication.otherLinks) {
            if (link.rel.contains("media-overlay")) {
                containsMediaOverlay = true
                link.href = link.href?.replace("port", "localhost:" + listeningPort + filePath)
            }
        }
    }

}

