package com.example.controller

import com.example.view.ExplainView
import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import kraskal.Edge
import kraskal.Kruskal
import tornadofx.*
import java.io.File
import java.lang.Math.abs
import java.util.ArrayList

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
    lateinit var nodeTextField: TextField
    lateinit var weightTextField: TextField
    var algKruskal: Kruskal? = null
    var actBt: NameButton = NameButton.NODE
    val btState = Array<SimpleBooleanProperty>(10,{SimpleBooleanProperty()})
    var group1: Group? = null
    var group2: Group? = null
    var nodeCounter = 0
    var explainText = "start"
    var flagExpClose = true
    var flagOstov = false
    var lineThick = 4.0

    data class Point(val x:Double,val y:Double, val weight: String, val id: String, val state: Paint)
    var isDragged = false
    var linesToRestore:MutableList<Point> = mutableListOf()
    var wasDragged = false
    var lateDelete = false
    var moveEdge = false

    fun setThisPane(pane: Pane){
        this.pane = pane
    }
    fun setNodeText(textField: TextField){
        nodeTextField = textField
    }
    fun setWeightText(textField: TextField){
        weightTextField = textField
    }
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

    fun addGraphEdge(x1: Double, x2: Double, y1:Double, y2:Double, state: Paint, weight:Int, _id: String){
        pane.line {
            startX = x1
            startY = y1
            endX = x2
            endY = y2
            stroke = canvasController.black
            id = _id
            strokeWidth = lineThick

            setOnMouseClicked {
                if(actBt == NameButton.DELETE){
                    parent.getChildList()?.remove(
                        parent.getChildList()?.find { it is Text && it.id == this.id })
                    canvasController.delEdge(startX,endX,startY,endY)
                    parent.getChildList()?.remove(this)
                }
            }

            val tempOther = mutableListOf<Node>()
            for(i in parent.getChildList()!!){
                if(!(i is Line)) tempOther.add(i)
            }
            parent.getChildList()?.removeAll(tempOther)
            parent.getChildList()?.addAll(tempOther)
            tempOther.clear()
        }
        pane.text{
            id = _id
            layoutX = (x1+x2)/2
            layoutY = (y1+y2)/2
            text = weight.toString()
            fill = javafx.scene.paint.Color.YELLOW
            style{
                fontWeight = FontWeight.BOLD
            }
        }
    }

    fun btGraph(){
        btClicked(NameButton.GRAPH)
        val nodeNames = mutableListOf<String>()
        val listString = mutableListOf<String>()
        try {
            File("C:\\Users\\cemen\\OneDrive\\Рабочий стол\\учеба\\android\\Kraskal\\src\\main\\resources\\graph.txt").readLines().forEach{
                btClear()
                btClicked(NameButton.GRAPH)
                listString.add(it)
                if (it.contains(" "))
                {
                    val tmplist = it.split(" ")
                    for (i in 0..1){
                        if (!(tmplist[i] in nodeNames))
                            nodeNames.add(tmplist[i])
                    }
                }
            }
        } catch(e: Exception){
            println(e)
        }

        val radius = 200.0
        val centerX = canvasController.canvasWidth/2
        val centerY = canvasController.canvasHeight/2
        for (i in 1..nodeNames.size){
            val angle = 2*Math.PI*i/nodeNames.size
            canvasController.addCircle(centerX+radius*Math.cos(angle),centerY+radius*Math.sin(angle),20.0,
                javafx.scene.paint.Color.BLACK,nodeNames[i-1])
        }
        for (str in listString){
            if (str.contains(" "))
            {
                val tmplist = str.split(" ")
                canvasController.addGraphEdge(tmplist[0],tmplist[1],tmplist[2].toInt())
            }
        }
    }

    fun btClear(){
        nodeCounter=0
        btClicked(NameButton.CLEAR)
        canvasController.clearAllObject()
        algKruskal = null
        flagOstov = false
        pane.getChildList()?.removeAll(pane.children)
    }
    fun btNode(x: Double, y: Double, nodeRadius: Double, Color: Paint, name: String){
        if (wasDragged==false)
            isDragged = false
        pane.group {
            id = name
            layoutX = x
            layoutY = y
            setOnMousePressed {
                if (actBt == NameButton.NODE)
                {
                    val linesToDelete = mutableListOf<Line>()
                    for(i in parent.getChildList()!!){
                        if(i is Line){
                            if((abs(i.startX - this.layoutX) <= 0.1 && abs(i.startY - this.layoutY) <= 0.1) ||
                                (abs(i.endX - this.layoutX) <= 0.1 && abs(i.endY - this.layoutY) <= 0.1)) {
                                var edgeText = parent.getChildList()!!.find { it is Text && it.id == i.id }
                                var weight = "0"
                                if(edgeText is Text) {
                                    weight = edgeText.text
                                }
                                if (abs(i.startX-this.layoutX)<=0.1 && abs(i.startY - this.layoutY) <= 0.1)
                                    linesToRestore.add(Point(i.endX,i.endY,weight,i.id,i.stroke))
                                else
                                    linesToRestore.add(Point(i.startX,i.startY,weight,i.id,i.stroke))
                                linesToDelete.add(i)
                            }
                        }
                    }
                    for(i in linesToDelete){
                        parent.getChildList()?.remove(
                            parent.getChildList()?.find { it is Text && it.id == i.id })
                        parent.getChildList()?.remove(i)
                    }
                    linesToDelete.clear()
                    canvasController.delCircle(this@group.layoutX,this@group.layoutY)
                    canvasController.delEdge1Side(this@group.layoutX,this@group.layoutY)
                }
            }
            setOnMouseDragged {
                if (actBt == NameButton.NODE) {
                    isDragged = true
                    canvasController.delEdge1Side(x,y)
                    this.layoutX += it.x
                    this.layoutY += it.y
                    isDragged = false
                    moveEdge = true
                }
            }
            setOnMouseClicked {
                if(actBt == NameButton.DELETE && isDragged) {
                    lateDelete = true
                }
                if(actBt == NameButton.DELETE && !isDragged){
                    val linesToDelete = mutableListOf<Line>()
                    for(i in parent.getChildList()!!){
                        if(i is Line){
                            if((abs(i.startX - x) <= 0.1 && abs(i.startY - y) <= 0.1) ||
                                (abs(i.endX - x) <= 0.1 && abs(i.endY - y) <= 0.1)) {
                                linesToDelete.add(i)
                            }
                        }
                    }
                    for(i in linesToDelete){
                        parent.getChildList()?.remove(
                            parent.getChildList()?.find { it is Text && it.id == i.id })
                        parent.getChildList()?.remove(i)
                    }
                    linesToDelete.clear()
                    parent.getChildList()?.remove(this)
                    canvasController.delCircle(x,y)
                    canvasController.delEdge1Side(x,y)
                }
                if(actBt == NameButton.EDGE && !isDragged){
                    if(group1==null) group1 = this
                    else if(group1!=this){
                        group2 = this
                        var nameEdge = group1!!.id+"_"+group2!!.id
                        var weight = if(weightTextField.text.isEmpty()) 0 else weightTextField.text.toInt()
                        if(canvasController.addEdge(group1!!.layoutX,group2!!.layoutX,group1!!.layoutY,group2!!.layoutY,canvasController.black,weight, nameEdge)){
                            pane.line {
                                startX = group1!!.layoutX
                                startY = group1!!.layoutY
                                endX = group2!!.layoutX
                                endY = group2!!.layoutY
                                stroke = canvasController.black
                                id = nameEdge
                                strokeWidth = lineThick

                                setOnMouseClicked {
                                    if(actBt == NameButton.DELETE){
                                        parent.getChildList()?.remove(
                                            parent.getChildList()?.find { it is Text && it.id == this.id })
                                        canvasController.delEdge(startX,endX,startY,endY)
                                        parent.getChildList()?.remove(this)
                                    }
                                }

                                val tempOther = mutableListOf<Node>()
                                for(i in parent.getChildList()!!){
                                    if(!(i is Line)) tempOther.add(i)
                                }
                                parent.getChildList()?.removeAll(tempOther)
                                parent.getChildList()?.addAll(tempOther)
                                tempOther.clear()
                            }
                            pane.text{
                                id = group1!!.id+"_"+group2!!.id
                                layoutX = (group1!!.layoutX + group2!!.layoutX)/2
                                layoutY = (group1!!.layoutY + group2!!.layoutY)/2
                                text = weight.toString()
                                fill = javafx.scene.paint.Color.YELLOW
                                style{
                                    fontWeight = FontWeight.BOLD
                                }
                            }
                        }
                        group1 = null
                        group2 = null

                    }
                }
                if(moveEdge) {
                    wasDragged = true
                    moveEdge = false
                    for (point in linesToRestore) {
                        pane.line {
                            startX = point.x
                            startY = point.y
                            endX = this@group.layoutX
                            endY = this@group.layoutY
                            id = point.id
                            strokeWidth = lineThick
                            stroke = point.state
                            canvasController.addEdge(point.x,layoutX,point.y,layoutY,point.state,point.weight.toInt(),point.id)

                            setOnMouseClicked {
                                if(actBt == NameButton.DELETE){
                                    parent.getChildList()?.remove(
                                        parent.getChildList()?.find { it is Text && it.id == this.id })
                                    canvasController.delEdge(startX,endX,startY,endY)
                                    parent.getChildList()?.remove(this)
                                }
                            }

                            val tempOther = mutableListOf<Node>()
                            for(i in parent.getChildList()!!){
                                if(!(i is Line)) tempOther.add(i)
                            }
                            parent.getChildList()?.removeAll(tempOther)
                            parent.getChildList()?.addAll(tempOther)
                            tempOther.clear()
                        }
                        pane.text{
                            id = point.id
                            layoutX = (point.x + this@group.layoutX)/2
                            layoutY = (point.y + this@group.layoutY)/2
                            text = point.weight
                            fill = javafx.scene.paint.Color.YELLOW
                            style{
                                fontWeight = FontWeight.BOLD
                            }
                        }
                    }
                    canvasController.restoreCircle(this@group.layoutX,this@group.layoutY,(this@group.getChildList()!![0] as Circle).radius,(this@group.getChildList()!![0] as Circle).fill,(this@group.getChildList()!![1] as Text).text)
                    linesToRestore.clear()
                }
                if (lateDelete){
                    val linesToDelete = mutableListOf<Line>()
                    for(i in pane.getChildList()!!){
                        if(i is Line){
                            if((abs(i.startX - this.layoutX) <= 0.1 && abs(i.startY - this.layoutY) <= 0.1) ||
                                (abs(i.endX - this.layoutX) <= 0.1 && abs(i.endY - this.layoutY) <= 0.1)) {
                                linesToDelete.add(i)
                            }
                        }
                    }
                    for(i in linesToDelete){
                        parent.getChildList()?.remove(
                            parent.getChildList()?.find { it is Text && it.id == i.id })
                        parent.getChildList()?.remove(i)
                    }
                    linesToDelete.clear()
                    parent.getChildList()?.remove(this)
                    canvasController.delCircle(x,y)
                    canvasController.delEdge1Side(x,y)
                    lateDelete = false
                }
            }
            if(!isDragged){
                circle{
                    nodeCounter++
                    centerX = 0.0
                    centerY = 0.0
                    radius = nodeRadius
                    fill = Color
                }
                text {
                    this.x = 0.0 - name.length*3
                    this.y = 0.0 + 3
                    text = name
                    fill = javafx.scene.paint.Color.YELLOW
                    style{
                        fontWeight = FontWeight.BOLD
                    }
                }
            }
            isDragged = false
        }
    }
    fun btShow(){
        btClicked(NameButton.SHOW)
        if(flagExpClose){
            flagExpClose = false
            ExplainView().openWindow()
        }
    }
    fun initKruskal(){
        val canvEdges = canvasController.getEdge()
        val edges = mutableListOf<Edge>()
        if(canvEdges.size == 0){
            notConnectedGraph()
            return
        }
        canvEdges.forEach{
            val ver = it.id.split("_")
            edges.add(Edge(ver[0], ver[1],it.weight))
        }
        algKruskal = Kruskal(edges)
        if(!algKruskal!!.isConnected || canvasController.aloneNode()){
            notConnectedGraph()
            algKruskal = null
        }
    }

    class FullThread(val leftMenuController: LeftMenuController):Thread(){
        override fun run() {
            while (!leftMenuController.flagOstov){
                if(!leftMenuController.btOneIn()) break
                sleep(3000)
            }
        }
    }

    fun btFull(){
        val dt = FullThread(this)
        dt.start()
    }

    fun btOneIn(): Boolean{
        btClicked(NameButton.ONE_STEP_IN)
        if(actBt != NameButton.ONE_STEP_IN && actBt != NameButton.FULL) return false
        if(algKruskal == null && !flagOstov) {
            initKruskal()
        }
        if(flagOstov || algKruskal == null) return false
        else{
            var x: List<Edge?>? = ArrayList()
            x = algKruskal!!.stepForward()
            if(x == null){
                flagOstov = true
                algKruskal = null
                return false
            }
            if(x.size == 1){
                val line = (pane.getChildList()?.find { it is Line && it.id == x[0]!!.start+"_"+x[0]!!.end } as Line)
                val circle1 = (pane.getChildList()?.find { it is Group && it.id == x[0]!!.start} as Group)
                val circle2 = (pane.getChildList()?.find { it is Group && it.id == x[0]!!.end} as Group)

                val rec1 = RecolorThread(line,circle1,circle2,canvasController.gray)
                val rec2 = RecolorThread2(line,circle1,circle2,canvasController.red)
                rec1.start()
                rec2.start()

                canvasController.circleColor(circle1.id, canvasController.red)
                canvasController.circleColor(circle2.id, canvasController.red)
                canvasController.edgeColor(line.id, canvasController.red)
            }
            else if(x.size > 1) btOneIn()
        }
        return true
    }
    fun btGraphStart(){
        btClicked(NameButton.GRAPH_START)
        flagOstov = false
        algKruskal = null
        colorInBlack()
    }
    fun colorInBlack(){
        for(i in pane.children){
            if(i is Line) i.stroke = canvasController.black
            if(i is Group){
                for(j in i.children){
                    if(j is Circle) j.fill = canvasController.black
                }
            }
        }
        canvasController.colorInBlackAll()
    }
    fun incorrectName(){
        alert(type = Alert.AlertType.INFORMATION,
            title = "Node creation",
            content = "Node with this name already exist or empty name",
            header = "")
    }
    fun notConnectedGraph(){
        Platform.runLater {
            alert(type = Alert.AlertType.INFORMATION,
                title = "Kruskal",
                content = "Graph should be connected",
                header = "")
        }
    }
    fun cantFindGraph(){
        Platform.runLater {
            alert(type = Alert.AlertType.INFORMATION,
                title = "Graph from file",
                content = "Can't find file in ",
                header = "")
        }
    }
    fun printState(){
        println(btState.joinToString())
    }
}