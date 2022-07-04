package com.example.view

import com.sun.tools.javac.Main
import javafx.geometry.Pos
import javafx.scene.paint.Color
import tornadofx.*

class AnMainView : View("My View") {
    val mainView: MainView by inject()

    override val root = borderpane {
        //top, bottom, center, left, right
        top{
            hbox{
                setOnMouseClicked {
                    circle {
                        centerX = 10.0
                        centerY = 20.0
                        radius = 3.0
                    }
                }
                alignment = Pos.CENTER
                label("Top")
            }
        }
        center{
            label("center")
        }
        //bottom<MainView>()
        bottom = mainView.root
        left{
            label("left")
        }
        right{
            label("right")
        }

    }
}
