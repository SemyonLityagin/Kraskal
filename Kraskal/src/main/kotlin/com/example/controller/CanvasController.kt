package com.example.controller

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.Group
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import tornadofx.*
import java.lang.Math.pow

class CanvasController: Controller() {
    val mController: MenuController by inject()
    val names = mutableListOf<String>()
    val nodes = mutableListOf<Group>()
    val edges = mutableListOf<Line>()
    val tempEdges = mutableListOf<Line>()
    val tempTexts = mutableListOf<Text>()
    var tempNode: Group? = null
    lateinit var pane: Pane

    val black = c(0,0,0,1.0)
    val gray = c(50,143,156,1.0)
    val red = c(139,0,0,1.0)
    val violent = c(114,0,163,1.0)
    var canvasWidth = SimpleDoubleProperty()
    var canvasHeight = SimpleDoubleProperty()
    var canvasMaxWidth = SimpleDoubleProperty()
    var canvasMaxHeight = SimpleDoubleProperty()
    var dragFlag = false

    fun setThisPane(pane: Pane){
        this.pane = pane
    }
    fun checkEdge(id1: String, id2: String): Boolean{
        val name1 = id1+"_"+id2
        val name2 = id2+"_"+id1
        edges.forEach{
            if(it.id == name1 || it.id == name2) return false
        }
        return true
    }

    fun addCircle(x: Double, y: Double, nodeRadius: Double, currText: String): Boolean{
        if((x-nodeRadius) <=0 || (x+nodeRadius) >= canvasMaxWidth.value || (y - nodeRadius) <= 0 || (y+nodeRadius) >= canvasMaxHeight.value) return false
        nodes.forEach {
            if(Math.sqrt(pow(it.layoutX-x,2.0) + pow(it.layoutY-y,2.0))<=2*nodeRadius) return false
        }
        names.add(currText)
        mController.pane.group {
            id = currText
            layoutX = x
            layoutY = y
            nodes.add(this@group)
            circle {
                id = currText
                centerX = 0.0
                centerY = 0.0
                radius = nodeRadius
                fill = black
            }
            text{
                this.x = 0.0 - currText.length*3
                this.y = 0.0 + 3
                text = currText
                fill = Color.YELLOW
                style{
                    fontWeight = FontWeight.BOLD
                }
            }
            mController.btGraphStart(mController.actBt)

            setOnMousePressed {
                if(mController.actBt == NameButton.NODE){
                    for(edge in edges){
                        val ids = edge.id.split("_")
                        if(this@group.id in ids){
                            tempEdges.add(edge)
                            tempTexts.add(pane.children.find{ it is Text && it.id == edge.id } as Text)
                        }
                    }
                    tempEdges.forEach{ it.isVisible = false }
                    tempTexts.forEach{ it.isVisible = false }
                }
            }
            setOnMouseDragged {
                if(mController.actBt == NameButton.NODE){
                    if(this@group.layoutX+it.x+nodeRadius < canvasMaxWidth.value && this@group.layoutX+it.x-nodeRadius > 0 &&
                        this@group.layoutY+it.y+nodeRadius < canvasMaxHeight.value && this@group.layoutY+it.y-nodeRadius > 0){
                        var flagCollision = true
                        for(n in nodes){
                            if(n != this@group){
                                if(pow(this.layoutX+it.x-n.layoutX,2.0)+pow(this.layoutY+it.y-n.layoutY,2.0) <= pow(2*nodeRadius,2.0)+1){
                                    flagCollision = false
                                }
                            }
                        }
                        if(flagCollision){
                            this@group.layoutX += it.x - 0.7
                            this@group.layoutY += it.y - 0.7
                        }
                    }
                    dragFlag = true
                }
            }
            setOnMouseReleased {
                if(mController.actBt == NameButton.DELETE){
                    val tempEdges = mutableListOf<Line>()
                    for(edge in edges){
                        if(this@group.id in edge.id.split("_")){
                            tempEdges.add(edge)
                        }
                    }
                    tempEdges.forEach{ l ->
                        pane.children.remove(pane.children.find { it is Text && it.id == l.id })
                    }
                    edges.removeAll(tempEdges)
                    pane.children.removeAll(tempEdges)
                    pane.children.remove(this@group)
                    names.remove(this@group.id)
                    nodes.remove(this@group)
                    tempEdges.clear()
                    mController.btGraphStart(mController.actBt)
                }
                if(mController.actBt == NameButton.EDGE){
                    if(tempNode == null) tempNode = this@group
                    else if(tempNode != this@group){
                        val gr1 = tempNode!!
                        val gr2 = this@group
                        if(gr1.id != gr2.id && checkEdge(gr1.id, gr2.id)) {
                            pane.line {
                                id = gr1.id + "_" + gr2.id
                                startX = gr1.layoutX
                                startY = gr1.layoutY
                                endX = gr2.layoutX
                                endY = gr2.layoutY
                                strokeWidth = mController.lineThick
                                stroke = black
                                edges.add(this@line)
                                setOnMouseReleased {
                                    if(mController.actBt == NameButton.EDGE){
                                        val weight = mController.weightTextField.text
                                        (parent.getChildList()?.find { it is Text && it.id == this.id } as Text).text = if (weight == "") "0" else weight
                                        mController.btGraphStart(mController.actBt)
                                    }
                                    if(mController.actBt == NameButton.DELETE){
                                        parent.getChildList()?.remove(
                                            parent.getChildList()?.find { it is Text && it.id == this.id })
                                        edges.remove(this@line)
                                        parent.getChildList()?.remove(this@line)
                                        mController.btGraphStart(mController.actBt)
                                    }
                                    mController.btGraphStart(mController.actBt)
                                }
                            }
                            val weight = mController.weightTextField.text
                            pane.text {
                                id = gr1.id + "_" + gr2.id
                                layoutX = (gr1.layoutX + gr2.layoutX) / 2
                                layoutY = (gr1.layoutY + gr2.layoutY) / 2
                                if(mController.actBt == NameButton.EDGE) {
                                    text = if (weight == "") "0" else weight
                                    mController.btGraphStart(mController.actBt)
                                }
                                fill = Color.YELLOW

                                style {
                                    fontSize = 14.px
                                    fontWeight = FontWeight.EXTRA_BOLD
                                }
                            }
                        }
                        tempNode = null
                        mController.underLine()
                    }
                }
                if(mController.actBt == NameButton.NODE){
                    tempEdges.forEach{
                        val ids = it.id.split("_")
                        if(this@group.id == ids[0]){
                            it.startX = layoutX
                            it.startY = layoutY
                        }
                        else{
                            it.endX = layoutX
                            it.endY = layoutY
                        }
                        val textEdge = tempTexts.find{t -> t.id == it.id }!!
                        textEdge.layoutX = (it.startX + it.endX)/2
                        textEdge.layoutY = (it.startY + it.endY)/2
                        textEdge.isVisible = true
                        it.isVisible = true
                    }
                    tempEdges.clear()
                    tempTexts.clear()
                }
            }
        }
        return true
    }

    fun clearAllObject(){
        nodes.clear()
        edges.clear()
        names.clear()
    }

    fun addGraphEdge(id1: String, id2: String, weight: Int){
        val gr1 = nodes.find{it.id == id1}
        val gr2 = nodes.find{it.id == id2}
        if(gr1 == null || gr2 == null) return
        pane.line {
            id = gr1.id + "_" + gr2.id
            startX = gr1.layoutX
            startY = gr1.layoutY
            endX = gr2.layoutX
            endY = gr2.layoutY
            strokeWidth = mController.lineThick
            stroke = black
            edges.add(this@line)
            setOnMouseReleased {
                if(mController.actBt == NameButton.EDGE){
                    val weight2 = mController.weightTextField.text
                    (parent.getChildList()?.find { it is Text && it.id == this.id } as Text).text = if (weight2 == "") "0" else weight2
                }
                if(mController.actBt == NameButton.DELETE){
                    parent.getChildList()?.remove(
                        parent.getChildList()?.find { it is Text && it.id == this.id })
                    edges.remove(this@line)
                    parent.getChildList()?.remove(this@line)
                }
                mController.btGraphStart(mController.actBt)
            }
        }
        pane.text {
            id = gr1.id + "_" + gr2.id
            layoutX = (gr1.layoutX + gr2.layoutX) / 2
            layoutY = (gr1.layoutY + gr2.layoutY) / 2
            if(mController.actBt == NameButton.NODE){
                text = tempTexts.find { it.id.contains(gr1.id) && it.id.contains(gr2.id)}!!.text
            }
            else if(mController.actBt == NameButton.EDGE || mController.actBt == NameButton.GRAPH) {
                text = weight.toString()
            }
            mController.btGraphStart(mController.actBt)
            fill = Color.YELLOW
            style {
                fontWeight = FontWeight.BOLD
            }
        }
        mController.underLine()
    }

    fun aloneNode(): Boolean{
        for(n in nodes){
            if(edges.find { n.id in it.id.split("_") }==null) return true
        }
        return false
    }
}