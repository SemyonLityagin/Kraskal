package com.example.view

import com.example.Styles
import com.example.controller.MenuController
import javafx.application.Platform
import javafx.geometry.Pos
import javafx.scene.text.TextAlignment
import tornadofx.*

class RightMenu : View("My View") {
    val mController: MenuController by inject()

    override val root = vbox {
        style {
            borderColor += box(
                top = javafx.scene.paint.Color.DARKGRAY,
                right = javafx.scene.paint.Color.DARKGRAY,
                left = javafx.scene.paint.Color.DARKGRAY,
                bottom = javafx.scene.paint.Color.DARKGRAY
            )
        }
        alignment = Pos.TOP_CENTER
        spacing = 10.0
        maxWidth = 120.0
        Platform.runLater {
            label {
                text = "Visualisation:"
                addClass(Styles.forLabels)
            }
            button {
                id = "FULL"
                text = "Full"
                addClass(Styles.forDisButton)
                action {
                    mController.btFull()
                }
                tooltip("Visualisation all Kruskal steps")
            }
            button {
                id = "ONE_STEP_IN"
                text = "One step in"
                addClass(Styles.forDisButton)
                action {
                    mController.btOneIn()
                }
                tooltip("Visualisation one Kruskal steps forward")
            }
            button {
                id = "ONE_STEP_OUT"
                text = "One step out"
                addClass(Styles.forDisButton)
                action {
                    mController.btOneOut()
                }
                tooltip("Visualisation one Kruskal steps back")
            }
            textfield{
                mController.setSpeedText(this)
                id = "weight"
                filterInput { it.controlNewText.isInt() }
                promptText = "Input speed (1..20)"
                tooltip("Input visualisation speed, where 1 - extremely slow, 20 - instantly")
                action {
                    if(text!="") {
                        var value = text.toInt()
                        if(value < 1) value = 1
                        else if(value > 20) value = 10
                        text = value.toString()
                    }
                    else{
                        text = "1"
                    }
                    positionCaret(text.length)
                }
            }
            label {
                text = "Initial state:"
                addClass(Styles.forLabels)
            }
            button {
                id = "GRAPH_START"
                text = "Graph"
                addClass(Styles.forDisButton)
                action {
                    Platform.runLater {
                        mController.btGraphStart()
                    }
                }
                tooltip("Resetting the graph to the beginning")
            }
            label {
                text = "Explanations:"
                addClass(Styles.forLabels)
            }
            button {
                id = "SHOW"
                text = "Show"
                addClass(Styles.forDisButton)
                action {
                    mController.btShow()
                }
                tooltip("Click to open window with explanations")
            }
            label {
                text = "Save explanations\n in file:"
                addClass(Styles.forLabels)
                style{
                    textAlignment = TextAlignment.CENTER
                }
            }
            button {
                id = "SAVE"
                text = "Save"
                addClass(Styles.forDisButton)
                action {
                    mController.btSave()
                }
                tooltip("Click to save explanations in file")
            }
        }
    }
}