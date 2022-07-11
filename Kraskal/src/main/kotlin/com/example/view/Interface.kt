package com.example.view

import com.example.Styles
import com.example.controller.*
import com.sun.glass.ui.Screen
import tornadofx.*

class Interface : View("Kruskal") {
    val canvasController: CanvasController by inject()
    val mController: MenuController by inject()

    override val root = borderpane {
        addClass(Styles.forStage)
        left<LeftMenu>()
        right<RightMenu>()
        mController.leftMenu = this@borderpane.left
        mController.rightMenu = this@borderpane.right

        center{
            this@center.setPrefSize(Screen.getMainScreen().width.toDouble()/2, Screen.getMainScreen().height.toDouble()/2)
            canvasController.canvasHeight.bind(this@center.heightProperty())
            canvasController.canvasWidth.bind(this@center.widthProperty())
            scrollpane {
                pane{
                    setPrefSize(Screen.getMainScreen().width.toDouble()-150, Screen.getMainScreen().height.toDouble()-80)
                    mController.pane = this
                    canvasController.pane = this
                    canvasController.canvasMaxHeight.bind(this.heightProperty())
                    canvasController.canvasMaxWidth.bind(this.widthProperty())
                    addClass(Styles.forCanv)
                    setOnMouseClicked {
                        if(mController.actBt == NameButton.NODE){
                            mController.btNode(it.x, it.y)
                        }
                    }
                }
            }
        }
    }
}
