package org.readium.r2.streamer.Parser.EpubParserSubClasses

import org.readium.r2.shared.Link
import org.readium.r2.streamer.AEXML.AEXML
import org.readium.r2.streamer.AEXML.Node

class NCXParser{

    var ncxDocumentPath: String? = null

    fun tableOfContents(document: AEXML) : List<Link> {
        val navMapElement = document.root()!!.getFirst("navMap")!!
        return nodeArray(navMapElement, "navPoint")
    }

    fun pageList(document: AEXML) : List<Link> {
        val pageListElement = document.root()!!.getFirst("pageList")
        return nodeArray(pageListElement, "pageTarget")
    }

    private fun nodeArray(element: Node?, type: String) : List<Link>
    {
        // The "to be returned" node array.
        val newNodeArray: MutableList<Link> = mutableListOf()

        // Find the elements of `type` in the XML element.
        val elements = element?.get(type) ?: return emptyList()
        // For each element create a new node of type `type`.
        for (elem in elements) {
            val newNode = node(elem, type)
            newNodeArray.plusAssign(newNode)
        }
        return newNodeArray
    }

    private fun node(element: Node, type: String) : Link {
        val newNode = Link()
        newNode.href = ncxDocumentPath + element.getFirst("content")!!.properties["src"]
        newNode.title = element.getFirst("navLabel")!!.getFirst("text")!!.name
        element.get("type")?.let {
            for (childNode in it){
                newNode.children.plusAssign(node(childNode, type))
            }
        }
        return newNode
    }

}