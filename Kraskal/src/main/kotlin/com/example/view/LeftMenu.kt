package com.example.view

import com.example.Styles
import com.example.controller.LeftMenuController
import com.example.controller.NameButton
import javafx.geometry.Pos
import javafx.scene.control.TextFormatter
import tornadofx.*
import kotlin.math.roundToInt

class LeftMenu : View("My View") {
    val lMController: LeftMenuController by inject()

    override val root = vbox {
        style {
            borderColor += box(
                top = javafx.scene.paint.Color.DARKGRAY,
                right = javafx.scene.paint.Color.DARKGRAY,
                left = javafx.scene.paint.Color.DARKGRAY,
                bottom = javafx.scene.paint.Color.DARKGRAY
            )
        }
        maxWidth = 120.0
        alignment = Pos.TOP_CENTER
        spacing = 10.0

        label {
            text = "Create:"
            addClass(Styles.forLabels)
        }
        button() {
            text = "Node"
            tooltip("You should click on canvas to create node\n" +
                    "Note: if you click on exist node - you can move it on canvas")
            addClass(Styles.forButton)
            action {
                lMController.btClicked(NameButton.NODE)
            }
        }
        textfield{
            lMController.setNodeText(this)
            id = "node"
            promptText = "Input node's name"
            tooltip("Input the unique node's name")
        }

        button() {
            tooltip("You should click on node A and node B to create edge")
            text = "Edge"
            addClass(Styles.forButton)
            action {
                lMController.btClicked(NameButton.EDGE)
            }
        }
        textfield{
            lMController.setWeightText(this)
            id = "weight"
            filterInput { it.controlNewText.isInt() }
            promptText = "Input edge's weight"
            tooltip("Input the edge's weight")
            action {
                if(text!="") text = text.toInt().toString()
                positionCaret(text.length)
            }
        }

        label {
            text = "Import from file:"
            addClass(Styles.forLabels)
        }
        button {
            text = "Graph"
            tooltip("Import graph from file \"graph.txt\"")
            addClass(Styles.forButton)
            action {
                lMController.btClicked(NameButton.GRAPH)
                lMController.btGraph()
            }
        }

        label {
            text = "Clear canvas:"
            addClass(Styles.forLabels)
        }
        button {
            text = "Clear"
            addClass(Styles.forButton)
            action {
                lMController.btClear()
            }
        }
        label {
            text = "Delete edge/node:"
            addClass(Styles.forLabels)
        }
        button {
            text = "Delete"
            tooltip("Click on edge or circle to delete it")
            addClass(Styles.forButton)
            action {
                lMController.btClicked(NameButton.DELETE)
                //lMController.btGraphStart()
            }
        }
    }
}
