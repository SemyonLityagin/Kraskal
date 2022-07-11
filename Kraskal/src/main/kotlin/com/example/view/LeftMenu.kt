package com.example.view

import com.example.Styles
import com.example.controller.MenuController
import com.example.controller.NameButton
import javafx.application.Platform
import javafx.geometry.Pos
import tornadofx.*

class LeftMenu : View("My View") {
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
        maxWidth = 120.0
        alignment = Pos.TOP_CENTER
        spacing = 10.0
        Platform.runLater {
            label {
                text = "Create:"
                addClass(Styles.forLabels)
            }
            button() {
                id = "NODE"
                text = "Node"
                tooltip("You should click on canvas to create node\n" +
                        "Note: if you click on exist node - you can move it on canvas")
                addClass(Styles.forActButton)
                action {
                    mController.btClicked(NameButton.NODE)
                }
            }
            textfield{
                mController.setNodeText(this)
                id = "node"
                promptText = "Input node's name"
                tooltip("Input the unique node's name")
            }

            button() {
                id = "EDGE"
                tooltip("You should click on node A and node B to create edge")
                text = "Edge"
                addClass(Styles.forDisButton)
                action {
                    mController.btClicked(NameButton.EDGE)
                }
            }
            textfield{
                mController.setWeightText(this)
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
                id = "GRAPH"
                text = "Graph"
                tooltip("Import graph from file \"graph.txt\"")
                addClass(Styles.forDisButton)
                action {
                    mController.btGraph()
                }
            }

            label {
                text = "Clear canvas:"
                addClass(Styles.forLabels)
            }
            button {
                id = "CLEAR"
                text = "Clear"
                addClass(Styles.forDisButton)
                action {
                    mController.btClear()
                }
            }
            label {
                text = "Delete edge/node:"
                addClass(Styles.forLabels)
            }
            button {
                id = "DELETE"
                text = "Delete"
                tooltip("Click on edge or circle to delete it")
                addClass(Styles.forDisButton)
                action {
                    mController.btClicked(NameButton.DELETE)
                }
            }
        }
    }
}