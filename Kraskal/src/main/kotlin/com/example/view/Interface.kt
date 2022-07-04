package com.example.view

import com.example.Styles
import com.example.controller.*
import tornadofx.*

class Interface : View("My View") {
    val canvasController: CanvasController by inject()
    val lMCController: LeftMenuController by inject()
    var nodeRadius = 10.0

    override val root = borderpane {
        addClass(Styles.forStage)
        center{
            setMaxSize(975.0,600.0)
            pane{
                lMCController.pane = this
                setMinSize(canvasController.canvasWidth,canvasController.canvasHeight)
                setMaxSize(canvasController.canvasWidth,canvasController.canvasHeight)
                addClass(Styles.forCanv)

                setOnMouseClicked {
                    when(lMCController.actBt){
                        NameButton.NODE -> {
                            canvasController.addCircle(it.x, it.y, nodeRadius, CircleState.BLACK)
                            /*if(canvasController.addCircle(it.x, it.y, nodeRadius, CircleState.BLACK)){
                                circle {
                                    centerX = it.x
                                    centerY = it.y
                                    println("///"+it.x.toString() + " , " + it.y.toString())
                                    radius = nodeRadius
                                    setOnMouseClicked {
                                        if(lMCController.actBt == NameButton.DELETE) {
                                            canvasController.delCircle(centerX, centerY)
                                            parent.getChildList()?.remove(this)
                                        }
                                    }
                                }
                            }*/
                        }
                        //NameButton.GRAPH ->
                        //NameButton.EDGE ->
                    }
                }
            }
        }
        left<LeftMenu>()
        right<RightMenu>()

    }

}
