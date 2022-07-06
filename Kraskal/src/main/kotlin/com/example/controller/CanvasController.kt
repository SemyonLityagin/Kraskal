package com.example.controller

import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import tornadofx.Controller
import tornadofx.c
import tornadofx.text
import java.lang.Math.abs

class CanvasController: Controller() {
    val lMController: LeftMenuController by inject()
    var nodes = mutableListOf<Node>()
    var names = mutableListOf<String>()
    var edges = mutableListOf<Edge>()
    val black = c(0,0,0,1.0)
    //val gray = c(156,156,156,1.0)
    val gray = c(50,143,156,1.0)
    val red = c(139,0,0,1.0)
    var canvasWidth = 970.0
    var canvasHeight = 560.0

    fun restoreCircle(x: Double, y: Double, radius: Double, state: Paint,text:String) {
        names.add(text)
        nodes.add(Node(x, y, radius, state, text))
    }
    fun addCircle(x: Double, y: Double, radius: Double, state: Paint,text:String?=null): Boolean{
        val currText = text ?: lMController.nodeTextField.text
        if (lMController.wasDragged) {
            lMController.wasDragged = false
            return false
        }
        if(currText in names || currText==""){
            lMController.incorrectName()
            return false
        }
        if((x-radius) <=0 || (x+radius) >= canvasWidth || (y - radius) <= 0 || (y+radius) >= canvasHeight) return false
        nodes.forEach {
            if(Math.sqrt(Math.pow(it.x-x,2.0) + Math.pow(it.y-y,2.0))<=2*radius) return false
        }
        names.add(currText)
        nodes.add(Node(x, y, radius, state, currText))
                lMController.btNode(x,y,radius,state, currText)
        return true
    }
    fun delCircle(x: Double, y:Double){
        for(i in nodes){
            if(abs(i.x - x) <= 0.1 && abs(i.y - y) <= 0.1) {
                names.remove(i.id)
                nodes.remove(i)
                break
            }
        }
    }
    fun delEdge1Side(x: Double, y:Double){
        var tempEdge = mutableListOf<Edge>()
        for(i in edges){
            if((abs(i.x1 - x) <= 0.1 && abs(i.y1 - y) <= 0.1) ||
                (abs(i.x2 - x) <= 0.1 && abs(i.y2 - y) <= 0.1)) {
                tempEdge.add(i)
            }
        }
        edges.removeAll(tempEdge)
        tempEdge.clear()
    }
    fun delEdge(x1: Double,x2: Double, y1:Double,y2: Double,){
        for(i in edges){
            if(abs(i.x1 - x1) <= 0.1 && abs(i.y1 - y1) <= 0.1 &&
                abs(i.x2 - x2) <= 0.1 && abs(i.y2 - y2) <= 0.1) {
                edges.remove(i)
                break
            }
        }
    }
    fun addEdge(x1: Double, x2: Double, y1:Double, y2:Double, state: Paint, weight:Int, id: String): Boolean{
        for(i in edges){
            if((abs(i.x1 - x1) <= 0.1 && abs(i.y1 - y1) <= 0.1 &&
                abs(i.x2 - x2) <= 0.1 && abs(i.y2 - y2) <= 0.1) ||
               (abs(i.x2 - x1) <= 0.1 && abs(i.y2 - y1) <= 0.1 &&
                abs(i.x1 - x2) <= 0.1 && abs(i.y1 - y2) <= 0.1)){
                return false
            }
        }
        edges.add(Edge(x1,x2,y1,y2,state,weight,id))
        return true
    }
    fun addGraphEdge(node1:String, node2:String,weight: Int){
        val circle1 = nodes.find { it.id == node1}
        val circle2 = nodes.find { it.id == node2}
        addEdge(circle1!!.x,circle2!!.x,circle1.y,circle2.y,black,weight,node1+"_"+node2)
        lMController.addGraphEdge(circle1.x,circle2.x,circle1.y,circle2.y,black,weight,node1+"_"+node2)
    }
    fun clearAllObject(){
        nodes.clear()
        edges.clear()
        names.clear()
    }
    fun getEdge(): MutableList<Edge>{
        return edges
    }
    fun colorInBlackAll(){
        nodes.forEach{it.state = black}
        edges.forEach{it.state = black}
    }
    fun circleColor(id: String, state: Paint){

    }
    fun edgeColor(id: String, state: Paint){

    }
    fun aloneNode(): Boolean{
        for(i in nodes){
            if(!findEdge(i.id)) return true
        }
        return false
    }
    fun findEdge(id: String): Boolean{
        for(i in edges){
            if(id in i.id.split("_")) return true
        }
        return false
    }
    inner class Node(val x: Double, val y: Double, val radius: Double, var state: Paint,val id: String)
    inner class Edge(val x1: Double, val x2: Double, val y1: Double, val y2: Double, var state: Paint, val weight: Int, val id: String)

}