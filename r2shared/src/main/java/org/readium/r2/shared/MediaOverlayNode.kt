package org.readium.r2.shared

import java.net.URL

class Clip{
    var relativeUrl: URL? = null
    var fragmentId: String? = null
    var start: Double? = null
    var end: Double? = null
    var duration: Double? = null
}

class MediaOverlayNode (var text: String? = null, private var audio: String? = null) {

    var role: MutableList<String> = mutableListOf()
    var children: MutableList<MediaOverlayNode> = mutableListOf()

    private fun fragmentId() : String? {
        val text = this.text ?: return null
        return text.split('#').last()
    }

    fun clip() : Clip {
        var newClip = Clip()

        val audioString = this.audio ?: throw Exception("audio")
        val audioFileString = audioString.split('#').first()
        val audioFileUrl = URL(audioFileString)

        newClip.relativeUrl = audioFileUrl
        val times = audioString.split('#').last()
        newClip = parseTimer(times, newClip)
        newClip.fragmentId = fragmentId()
        return newClip
    }

    private fun parseTimer(times: String, clip: Clip) : Clip {
        //  Remove "t=" prefix
        val netTimes = times.removeRange(0, 2)
        val start = try {netTimes.split(',').first()} catch (e: Exception) { null }
        val end = try {netTimes.split(',').last()} catch (e: Exception) { null }
        val startTimer = start?.toDoubleOrNull() ?: throw Exception("timersParsing")
        val endTimer = end?.toDoubleOrNull() ?: throw Exception("timerParsing")
        clip.start = startTimer
        clip.end = endTimer
        clip.duration = endTimer - startTimer
        return clip
    }

}