package com.example

import javafx.scene.paint.*
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val forLabels by cssclass()
        val forCanv by cssclass()
        val forActButton by cssclass()
        val forDisButton by cssclass()
        val forStage by cssclass()
    }

    init {
        forLabels {
            padding = box(4.px)
            fontSize = 12.px
            fontWeight = FontWeight.BOLD
            underline = true

        }
        forStage{
        }
        forCanv{
            backgroundColor += c(213, 213, 213, 1.0)
        }
        forActButton{
            fontWeight = FontWeight.EXTRA_BOLD
            borderColor += box(
                top = javafx.scene.paint.Color.DARKRED,
                right = javafx.scene.paint.Color.DARKGREEN,
                left = javafx.scene.paint.Color.DARKORANGE,
                bottom = javafx.scene.paint.Color.PURPLE
            )
            backgroundColor += LinearGradient(0.0, 0.0, 1.0, 1.0, true, CycleMethod.REPEAT, Stop(0.0, c(0,26,51,1.0)),Stop(0.5, c(50,142,161,1.0)), Stop(1.0, c(1,93,82,1.0)))
            textFill = c(254,229,174,1.0)
        }
        forDisButton{
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