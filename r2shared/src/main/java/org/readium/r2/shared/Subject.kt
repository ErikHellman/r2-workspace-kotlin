package org.readium.r2.shared

import org.json.JSONObject

class Subject : JSONable{

    var name: String? = null
    //  The WebPubManifest elements
    var sortAs: String? = null
    //  Epub 3.1 "scheme" (opf:authority)
    var scheme: String? = null
    //  Epub 3.1 "code" (opf:term)
    var code: String? = null

    override fun getJSON(): JSONObject {
        val json = JSONObject()
        json.putOpt("name", name)
        json.putOpt("sortAs", sortAs)
        json.putOpt("scheme", scheme)
        json.putOpt("code", code)
        return json
    }

}