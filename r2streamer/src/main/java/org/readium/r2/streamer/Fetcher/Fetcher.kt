package org.readium.r2.streamer.Fetcher

import org.readium.r2.shared.*;
import org.readium.r2.streamer.Containers.Container
import java.io.InputStream
import java.net.URL

class Fetcher(publication: Publication, container: Container){
    lateinit var publication: Publication
    lateinit var container: Container
    var rootFileDirectory: String = ""
    var contentFilters: ContentFilters?

    init {
        this.container = container
        this.publication = publication

        val rootFilePath = publication.internalData["rootfile"] ?: throw Exception("Missing root file")
        if (rootFilePath.isNotEmpty() && rootFilePath.contains('/'))
            rootFileDirectory = URL(rootFilePath).removeLastComponent().toString()
        else
            rootFileDirectory = ""
        contentFilters = getContentFilters(container.rootFile.mimetype)
    }

    fun data(path: String) : ByteArray? {
        publication.resource(path) ?: throw Exception("Missing file")
        var data: ByteArray? = container.data(path)
        if (data != null)
            data = contentFilters?.apply(data, publication, path)
        return data
    }

    fun dataStream(path: String) : InputStream {
        publication.resource(path) ?: throw Exception("Missing file")
        val inputStream = container.dataInputStream(path)
        contentFilters?.apply(inputStream, publication, path)
        return inputStream
    }

    fun dataLength(path: String) : Long {
        val relativePath = rootFileDirectory.plus(path)

        publication.resource(path) ?: throw Exception("Missing file")
        return container.dataLength(relativePath)
    }

    fun getContentFilters(mimeType: String?) : ContentFilters {
        when (mimeType){
            "application/epub+zip", "application/oebps-package+xml" -> return ContentFiltersEpub()
            "application/x-cbr" -> return ContentFiltersCbz()
            else -> throw Exception("Missing container  else MIMEtype")
        }
    }
}