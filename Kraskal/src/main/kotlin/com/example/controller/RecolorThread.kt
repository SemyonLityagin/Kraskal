package com.example.controller

import javafx.scene.Group
import javafx.scene.paint.Paint
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import tornadofx.getChildList

class RecolorThread(val line: Line, val gr1: Group, val gr2: Group, val color: Paint): Thread() {
    override fun run() {
        line.stroke = color
        (gr1.getChildList()?.find{it is Circle } as Circle).fill = color
        (gr2.getChildList()?.find{it is Circle } as Circle).fill = color
    }
}

class RecolorThread2(val line: Line, val gr1: Group, val gr2: Group, val color: Paint): Thread() {
    override fun run() {
        Thread.sleep(2000)
        line.stroke = color
        (gr1.getChildList()?.find{it is Circle } as Circle).fill = color
        (gr2.getChildList()?.find{it is Circle } as Circle).fill = color
    }
}