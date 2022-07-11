package com.example.controller

import com.example.Styles
import com.example.view.ExplainView
import javafx.animation.PauseTransition
import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.scene.text.Text
import javafx.stage.FileChooser
import javafx.util.Duration
import kraskal.Edge
import kraskal.Kruskal
import tornadofx.*
import java.io.File
import java.lang.Double.min
import java.nio.file.Paths


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
    SHOW,
    SAVE
}

class MenuController: Controller() {
    val canvasController: CanvasController by inject()
    val fileChooser = FileChooser()
    lateinit var pane: Pane
    lateinit var rightMenu: Node
    lateinit var leftMenu: Node
    lateinit var nodeTextField: TextField
    lateinit var weightTextField: TextField
    lateinit var speedTextField: TextField
    var algKruskal: Kruskal? = null
    var actBt: NameButton = NameButton.NODE
    val btState = Array<SimpleBooleanProperty>(11,{SimpleBooleanProperty()})
    var explainText = SimpleStringProperty("")
    var flagExpClose = true
    var flagOstov = false
    var lineThick = 4.0
    var nodeRadius = 20.0

    fun setNodeText(textField: TextField){
        nodeTextField = textField
    }
    fun setWeightText(textField: TextField){
        weightTextField = textField
    }
    fun setSpeedText(textField: TextField){
        speedTextField = textField
    }
    fun btClicked(nameButton: NameButton){
        leftMenu.getChildList()!!.find { it is Button && it.id == actBt.toString() }?.removeClass(Styles.forActButton)
        rightMenu.getChildList()!!.find { it is Button && it.id == actBt.toString() }?.removeClass(Styles.forActButton)
        leftMenu.getChildList()!!.find { it is Button && it.id == actBt.toString() }?.addClass(Styles.forDisButton)
        rightMenu.getChildList()!!.find { it is Button && it.id == actBt.toString() }?.addClass(Styles.forDisButton)

        btState.forEach { state -> state.value = false }
        btState[nameButton.ordinal].value = true

        (leftMenu.getChildList()!!.find { it is Button && it.id == nameButton.toString() }?.removeClass(Styles.forDisButton))
        (rightMenu.getChildList()!!.find { it is Button && it.id == nameButton.toString() }?.removeClass(Styles.forDisButton))
        (leftMenu.getChildList()!!.find { it is Button && it.id == nameButton.toString() }?.addClass(Styles.forActButton))
        (rightMenu.getChildList()!!.find { it is Button && it.id == nameButton.toString() }?.addClass(Styles.forActButton))

        actBt = nameButton
        canvasController.tempNode = null
        printState()
    }


    fun btNode(x: Double, y: Double){
        btClicked(NameButton.NODE)
        var currText = nodeTextField.text
        if(currText in canvasController.names || currText==""){
            var ind = 1
            while(currText+ind.toString() in canvasController.names){
                ind++
            }
            currText += ind.toString()
        }
        canvasController.addCircle(x, y, nodeRadius, currText)
    }

    fun underLine(){
        val tempOther = mutableListOf<Node>()
        for(i in pane.getChildList()!!){
            if(!(i is Line)) tempOther.add(i)
        }
        pane.getChildList()?.removeAll(tempOther)
        pane.getChildList()?.addAll(tempOther)
        tempOther.clear()
    }

    var defaultGraphPath = Paths.get("").toAbsolutePath().toString()+"/graph.txt"

    fun btGraph(){
        btClicked(NameButton.GRAPH)
        val nodeNames = mutableListOf<String>()
        val listString = mutableListOf<String>()

        if (File(defaultGraphPath).exists())
            File(defaultGraphPath).readLines().forEach{
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
        else
        {
            try {
                defaultGraphPath = fileChooser.showOpenDialog(primaryStage).path
                File(defaultGraphPath).readLines().forEach{
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
        }

        val radius = min(canvasController.canvasWidth.value/2, canvasController.canvasHeight.value/2) - 100
        if(radius <= 40) return
        val centerX = (canvasController.canvasWidth-260).value/2
        val centerY = canvasController.canvasHeight.value/2
        for (i in 1..nodeNames.size){
            val angle = 2*Math.PI*i/nodeNames.size
            canvasController.addCircle(centerX+radius*Math.cos(angle),centerY+radius*Math.sin(angle),20.0, nodeNames[i-1])
        }
        for (str in listString){
            if (str.contains(" "))
            {
                val tmplist = str.split(" ")
                try{
                    canvasController.addGraphEdge(tmplist[0],tmplist[1],tmplist[2].toInt())
                }catch(e: Exception){
                    println(e)
                }
            }
        }
    }

    fun btClear(){
        btClicked(NameButton.CLEAR)
        canvasController.clearAllObject()
        algKruskal = null
        flagOstov = false
        pane.getChildList()?.removeAll(pane.children)
    }

    var defaultLogPath = Paths.get("").toAbsolutePath().toString()+"/log.txt"

    fun btShow(){
        btClicked(NameButton.SHOW)
        if(flagExpClose){
            flagExpClose = false
            ExplainView().openWindow()
        }
    }

    fun btSave() {
        btClicked(NameButton.SAVE)
        if(File(defaultLogPath).exists()){
            File(defaultLogPath).writeText(explainText.value)
        }
        else{
            try {
                val temp = defaultLogPath
                defaultLogPath = fileChooser.showOpenDialog(primaryStage).path
                File(defaultLogPath).writeText(explainText.value)
                File(defaultLogPath).writeText(temp)
            } catch(e: Exception){
                println(e)
            }
        }
    }

    fun initKruskal(){
        val canvEdges = canvasController.edges
        val edges = mutableListOf<Edge>()
        if(canvEdges.size == 0){
            println(1)
            notConnectedGraph()
            return
        }
        canvEdges.forEach{ edge ->
            val gr = edge.id.split("_")
            val weight = (pane.children.find { it is Text && it.id == edge.id } as Text).text
            edges.add(Edge(gr[0], gr[1], weight.toInt()))
        }
        algKruskal = Kruskal(edges)
        if(!algKruskal!!.isConnected || canvasController.aloneNode()){
            println((!algKruskal!!.isConnected).toString() + canvasController.aloneNode().toString())
            notConnectedGraph()
            algKruskal = null
            return
        }
        algKruskal!!.run()
    }

    fun visDisable(value: Boolean){
        pane.isDisable = value
        (rightMenu.getChildList()!!.find { it is Button && it.text == "Full" } as Button).isDisable = value
        (rightMenu.getChildList()!!.find { it is Button && it.text == "Full" } as Button).isDisable = value
        (rightMenu.getChildList()!!.find { it is Button && it.text == "One step in" } as Button).isDisable = value
        (rightMenu.getChildList()!!.find { it is Button && it.text == "One step out" } as Button).isDisable = value
        (rightMenu.getChildList()!!.find { it is Button && it.text == "Graph" } as Button).isDisable = value
        leftMenu.isDisable = value
    }

    fun visualisationSpeed(): Double{
        var speed = if(speedTextField.text == "") 1.0 else speedTextField.text.toDouble()
        speed = when(speed) {
            20.0 -> 0.0
            19.0 -> 50.0
            18.0 -> 100.0
            17.0 -> 500.0
            16.0 -> 1000.0
            in 1.0..15.00 -> (17.0-speed)*1000
            else -> 1000.0
        }
        return speed
    }

    fun btFull(){
        btClicked(NameButton.FULL)
        Thread{
            while (!flagOstov){
                visDisable(true)
                if(actBt != NameButton.ONE_STEP_IN && actBt != NameButton.FULL) break
                if(!btOneIn()) break
                Thread.sleep(visualisationSpeed().toLong())
            }
            visDisable(false)
            btClicked(NameButton.FULL)
        }.start()
    }

    fun btOneIn(): Boolean{
        if(actBt != NameButton.FULL) btClicked(NameButton.ONE_STEP_IN)
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
                explainText.value=algKruskal!!.log
                return false
            }
            visDisable(true)
            if(x.size >= 1) {
                val edgeToRecolor = mutableListOf<Line>()
                val circleToRecolor = mutableListOf<Circle>()
                val circleColor = mutableListOf<Paint>()
                val edgeColor = mutableListOf<Paint>()
                x.forEach { edge ->
                    val line = (pane.getChildList()?.find { it is Line && it.id == edge.start+"_"+edge.end }
                        ?: (pane.getChildList()?.find { it is Line && it.id == edge.end+"_"+edge.start })) as Line
                    edgeToRecolor.add(line)
                    edgeColor.add(line.stroke)
                    val gr1 = (pane.getChildList()?.find { it is Group && it.id == line.id.split("_")[0]} as Group)
                    val gr2 = (pane.getChildList()?.find { it is Group && it.id == line.id.split("_")[1]} as Group)
                    if((gr1.children[0] as Circle) !in circleToRecolor) {
                        circleToRecolor.add((gr1.children[0] as Circle))
                        circleColor.add((gr1.children[0] as Circle).fill)
                    }
                    if((gr2.children[0] as Circle) !in circleToRecolor) {
                        circleToRecolor.add((gr2.children[0] as Circle))
                        circleColor.add((gr2.children[0] as Circle).fill)
                    }
                }
                when(x.size){
                    1 -> {
                        edgeToRecolor.forEach{it.stroke = canvasController.gray}
                        circleToRecolor.forEach{it.fill = canvasController.gray}
                    }
                    else ->{
                        edgeToRecolor.forEach{it.stroke = canvasController.violent}
                        circleToRecolor.forEach{it.fill = canvasController.violent}
                    }
                }
                val pt = PauseTransition(Duration.millis(visualisationSpeed()))
                pt.setOnFinished {
                    if(x.size > 1){
                        for(i in 0..edgeToRecolor.size-1){ edgeToRecolor[i].stroke = edgeColor[i] }
                        for(i in 0..circleToRecolor.size-1){ circleToRecolor[i].fill = circleColor[i] }
                    } else{
                        edgeToRecolor.forEach{it.stroke = canvasController.red}
                        circleToRecolor.forEach{it.fill = canvasController.red}
                    }
                    circleColor.clear()
                    edgeColor.clear()
                    circleToRecolor.clear()
                    edgeToRecolor.clear()
                    if(actBt == NameButton.ONE_STEP_IN) visDisable(false)
                }
                pt.play()
            }
        }
        explainText.value=algKruskal!!.log
        return true
    }

    fun btOneOut(){
        btClicked(NameButton.ONE_STEP_OUT)
        if (algKruskal == null)
            return
        if (flagOstov)
            flagOstov = false
        val x = algKruskal!!.stepBack()
        if(x == null){
            explainText.value=algKruskal!!.log
            algKruskal = null
            return
        }
        visDisable(true)
        if(x.size >= 1) {
            val edgeToRecolor = mutableListOf<Line>()
            val circleToRecolor = mutableListOf<Circle>()
            val circleColor = mutableListOf<Paint>()
            val edgeColor = mutableListOf<Paint>()
            x.forEach { edge ->
                val line = (pane.getChildList()?.find { it is Line && it.id == edge.start+"_"+edge.end }
                    ?: (pane.getChildList()?.find { it is Line && it.id == edge.end+"_"+edge.start })) as Line
                edgeToRecolor.add(line)
                edgeColor.add(line.stroke)
                val gr1 = (pane.getChildList()?.find { it is Group && it.id == line.id.split("_")[0]} as Group)
                val gr2 = (pane.getChildList()?.find { it is Group && it.id == line.id.split("_")[1]} as Group)
                if((gr1.children[0] as Circle) !in circleToRecolor) {
                    circleToRecolor.add((gr1.children[0] as Circle))
                    circleColor.add((gr1.children[0] as Circle).fill)
                }
                if((gr2.children[0] as Circle) !in circleToRecolor) {
                    circleToRecolor.add((gr2.children[0] as Circle))
                    circleColor.add((gr2.children[0] as Circle).fill)
                }
            }
            when(x.size){
                1 -> {
                    edgeToRecolor.forEach{it.stroke = canvasController.gray}
                    circleToRecolor.forEach{it.fill = canvasController.gray}
                }
                else ->{
                    edgeToRecolor.forEach{it.stroke = canvasController.violent}
                    circleToRecolor.forEach{it.fill = canvasController.violent}
                }
            }
            val pt = PauseTransition(Duration.millis(visualisationSpeed()))
            pt.setOnFinished {
                if(x.size > 1){
                    for(i in 0..edgeToRecolor.size-1){
                        edgeToRecolor[i].stroke = edgeColor[i]
                    }
                    for(i in 0..circleToRecolor.size-1){
                        circleToRecolor[i].fill = circleColor[i]
                    }
                }
                else{
                    edgeToRecolor.forEach{it.stroke = canvasController.black}
                    circleToRecolor.forEach{ circle ->
                        var redFlag = false
                        for(node in pane.children){
                            if(node is Line && circle.id in node.id.split("_")){
                                if(node.stroke == canvasController.red){
                                    redFlag = true
                                    break
                                }
                            }
                        }
                        if(redFlag) circle.fill = canvasController.red
                        else circle.fill = canvasController.black
                    }
                }
                circleColor.clear()
                edgeColor.clear()
                circleToRecolor.clear()
                edgeToRecolor.clear()
                visDisable(false)
            }
            pt.play()
        }
        explainText.value=algKruskal!!.log
    }

    fun btGraphStart(bt: NameButton = NameButton.GRAPH_START){
        btClicked(bt)
        explainText.value = ""
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
    }

    fun notConnectedGraph(){
        Platform.runLater {
            alert(type = Alert.AlertType.INFORMATION,
                title = "Kruskal",
                content = "Graph should be connected",
                header = "")
        }
    }

    fun printState(){
        println(btState.joinToString())
        println(actBt)
    }
}