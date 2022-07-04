package com.example

import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val heading by cssclass()
        val forLabels by cssclass()
        val forCanv by cssclass()
        val forButton by cssclass()
        val forStage by cssclass()
    }

    init {
        heading {
            padding = box(5.px)
            fontSize = 12.px
            fontWeight = FontWeight.BOLD
        }
        forLabels {
            padding = box(4.px)
            fontSize = 12.px
            fontWeight = FontWeight.BOLD
            borderColor += box(
                top = javafx.scene.paint.Color.RED,
                right = javafx.scene.paint.Color.BLACK,
                left = javafx.scene.paint.Color.BLACK,
                bottom = javafx.scene.paint.Color.BLACK
            )
        }
        forStage{
            //backgroundColor += c(30, 33, 61, 1.0)
        }
        forCanv{
            backgroundColor += c(189,186,207, 1.0)
            //backgroundColor += c(83, 55, 122, 1.0)
        }
        forButton{
            fontWeight = FontWeight.EXTRA_BOLD
            borderColor += box(
                top = javafx.scene.paint.Color.RED,
                right = javafx.scene.paint.Color.DARKGREEN,
                left = javafx.scene.paint.Color.ORANGE,
                bottom = javafx.scene.paint.Color.PURPLE
            )
        }

    }
}