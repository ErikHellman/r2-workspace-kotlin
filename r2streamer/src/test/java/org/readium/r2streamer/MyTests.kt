package org.readium.r2streamer

import android.os.Environment
import org.junit.Test
import org.junit.Assert.*
import org.readium.r2streamer.Parser.EpubParser

class MyTests{

    lateinit var epubParser: EpubParser

    @Test
    fun test(){
        epubParser = EpubParser()
        //val pubBox = epubParser.parse(
          //      "/sdcard/Documents/book.epub")
        //val publication = pubBox.publication
        //assertTrue(publication.metadata.title == "Burn-out")
    }
}