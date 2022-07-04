package com.example.controller

import tornadofx.Controller
import tornadofx.c
import java.lang.Math.abs

enum class CircleState{
    BLACK,
    RED,
    GREY
}

class CanvasController: Controller() {
    val lMController: LeftMenuController by inject()
    var nodes = mutableListOf<Node>()
    var edges = mutableListOf<Edge>()
    val black = c(0,0,0,1.0)
    val gray = c(156,156,156,1.0)
    val red = c(139,0,0,1.0)
    var canvasWidth = 970.0
    var canvasHeight = 560.0


    fun addCircle(x: Double, y: Double, radius: Double, state: CircleState): Boolean{
        if((x-radius) <=0 || (x+radius) >= canvasWidth || (y - radius) <= 0 || (y+radius) >= canvasHeight) return false
        nodes.forEach {
            if(Math.sqrt(Math.pow(it.x-x,2.0) + Math.pow(it.y-y,2.0))<=2*radius) return false
        }
        nodes.add(Node(x, y, radius, state, nodes.size.toString()))
        when(state){
            CircleState.GREY ->
                lMController.btNode(x,y,radius,gray)
            CircleState.BLACK ->
                lMController.btNode(x,y,radius,black)
            CircleState.RED ->
                lMController.btNode(x,y,radius,red)
        }
        return true
    }
    fun delCircle(x: Double, y:Double){
        for(i in nodes){
            if(abs(i.x - x) <= 0.1 && abs(i.y - y) <= 0.1) {
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
    fun addEdge(x1: Double,x2: Double, y1:Double, y2:Double, state: CircleState){
        edges.add(Edge(x1,x2,y1,y2,state))
    }
    inner class Node(val x: Double, val y: Double, val radius: Double, val state: CircleState,val str: String)
    inner class Edge(val x1: Double, val x2: Double, val y1: Double, val y2: Double, val state: CircleState)

}