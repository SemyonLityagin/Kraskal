package com.example.controller

import com.example.view.ExplainView
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.paint.Paint
import javafx.scene.shape.Line
import tornadofx.*

enum class NameButton{
    NODE,
    EDGE,
    GRAPH,
    GRAPH_START,
    CLEAR,
    DELETE,
    FULL,
    ONE_STEP_IN,
    ONE_STEP_OUT,
    SHOW
}

class LeftMenuController: Controller() {
    val canvasController: CanvasController by inject()
    lateinit var pane: Pane
    var actBt: NameButton = NameButton.NODE
    val btState = Array<SimpleBooleanProperty>(10,{SimpleBooleanProperty()})
    var group1: Group? = null
    var group2: Group? = null
    var nodeCounter = 0
    var textShiftX = 3
    var textShiftY = 3
    var explainText = "start"
    var flagExpClose = true

    fun btClicked(nameButton: NameButton){
        btState.forEach {
            it.value = false
        }
        btState[nameButton.ordinal].value = true
        actBt = nameButton
        group1 = null
        group2 = null
        printState()
    }
    fun btClear(){
        nodeCounter=0
        textShiftX=3
        btClicked(NameButton.CLEAR)
        pane.getChildList()?.removeAll(pane.children)
    }
    fun btNode(x: Double, y: Double, nodeRadius: Double, Color: Paint){
        pane.group {
            layoutX = x
            layoutY = y

            setOnMouseClicked {
                if(actBt == NameButton.DELETE){
                    canvasController.delCircle(x,y)
                    canvasController.delEdge1Side(x,y)
                    val linesToDelete = mutableListOf<Line>()
                    for(i in parent.getChildList()!!){
                        if(i is Line){
                            if((Math.abs(i.startX - x) <= 0.1 && Math.abs(i.startY - y) <= 0.1) ||
                                (Math.abs(i.endX - x) <= 0.1 && Math.abs(i.endY - y) <= 0.1)) {
                                linesToDelete.add(i)
                            }
                        }
                    }
                    for(i in linesToDelete){
                        parent.getChildList()?.remove(i)
                    }
                    linesToDelete.clear()
                    parent.getChildList()?.remove(this)
                }
                if(actBt == NameButton.EDGE){
                    if(group1==null) group1 = this
                    else if(group1!=this){
                        group2 = this
                        pane.line {
                            startX = group1!!.layoutX
                            startY = group1!!.layoutY
                            endX = group2!!.layoutX
                            endY = group2!!.layoutY
                            strokeWidth = 2.0
                            canvasController.addEdge(startX,endX,startY,endY,CircleState.BLACK)

                            setOnMouseClicked {
                                if(actBt == NameButton.DELETE){
                                    canvasController.delEdge(startX,endX,startY,endY)
                                    parent.getChildList()?.remove(this)
                                }
                            }
                            group1 = null
                            group2 = null

                            val tempOther = mutableListOf<Node>()
                            for(i in parent.getChildList()!!){
                                if(!(i is Line)) tempOther.add(i)
                            }
                            val par = parent
                            parent.getChildList()?.removeAll(tempOther)
                            for(i in tempOther){
                                par.getChildList()?.add(i)
                            }
                            tempOther.clear()
                        }
                    }
                }
            }

            circle{
                nodeCounter++
                centerX = 0.0
                centerY = 0.0
                radius = nodeRadius
            }
            if(nodeCounter>=10 && textShiftX!=6) textShiftX=6
            text {
                fill = javafx.scene.paint.Color.WHITESMOKE
                this.x = 0.0 - textShiftX
                this.y = 0.0 + textShiftY
                text = "${nodeCounter}"
            }
        }
        /*
        pane.circle{
            nodeCounter++
            centerX = x
            centerY = y
            println("///"+x.toString() + " , " + y.toString())
            radius = nodeRadius
            setOnMouseClicked {
                println("Clicked")
                if(actBt == NameButton.DELETE) {
                    canvasController.delCircle(centerX, centerY)
                    for(i in (parent.getChildList()!!)){
                        if(i is Text){
                            if(abs(i.x+textShiftX - this.centerX)<= 0.01 && abs(i.y-textShiftY - this.centerY)<= 0.01){
                                parent.getChildList()?.remove(i)
                                break
                            }
                        }
                    }
                    parent.getChildList()?.remove(this)
                }
                if(actBt == NameButton.EDGE){
                    if(fCircle == null) fCircle = this
                    else if(this!=fCircle){
                        sCircle = this
                        pane.line {
                            this.startX = fCircle!!.centerX
                            this.endX = sCircle!!.centerX
                            this.startY = fCircle!!.centerY
                            this.endY = sCircle!!.centerY
                            strokeWidth = 2.0
                            setOnMouseClicked {
                                if(actBt == NameButton.DELETE){
                                    canvasController.delEdge(startX, endX, startY, endY)
                                    parent.getChildList()?.remove(this)
                                }
                            }
                        }
                        fCircle = null
                        sCircle = null
                    }
                }
            }
        }
        if(nodeCounter>=10 && textShiftX!=6) textShiftX = 6
        pane.text {
            fill = javafx.scene.paint.Color.WHITESMOKE
            this.x = x-textShiftX
            this.y = y+textShiftY
            text = "${nodeCounter}"
            setOnMouseClicked {
                if(actBt == NameButton.DELETE) {
                    canvasController.delCircle(this.x+textShiftX, this.y-textShiftY)
                    for(i in (parent.getChildList()!!)){
                        if(i is Circle){
                            if(abs(i.centerX - this.x-textShiftX)<= 0.01 && abs(i.centerY - this.y+textShiftY)<= 0.01){
                                parent.getChildList()?.remove(i)
                                break
                            }
                        }
                    }
                    parent.getChildList()?.remove(this)
                }
                if(actBt == NameButton.EDGE){
                    for(i in (parent.getChildList()!!)){
                        if(i is Circle){
                            if(abs(i.centerX - this.x-textShiftX)<= 0.01 && abs(i.centerY - this.y+textShiftY)<= 0.01){
                                i.onMouseClicked
                                break
                            }
                        }
                    }
                }
            }

        }*/
    }
    fun btShow(){
        btClicked(NameButton.SHOW)
        if(flagExpClose){
            flagExpClose = false
            ExplainView().openWindow()
        }
    }

    fun printState(){
        println(btState.joinToString())
    }
}