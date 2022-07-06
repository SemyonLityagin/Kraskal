package com.example.view

import com.example.Styles
import com.example.controller.LeftMenuController
import com.example.controller.NameButton
import javafx.geometry.Pos
import tornadofx.*

class RightMenu : View("My View") {
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
        alignment = Pos.TOP_CENTER
        spacing = 10.0

        label {
            text = "Visualisation:"
            addClass(Styles.forLabels)
        }
        button {
            text = "Full"
            addClass(Styles.forButton)
            action {
                lMController.btFull()
            }
            tooltip("Input the unique node's name")
        }
        button {
            text = "One step in"
            addClass(Styles.forButton)
            action {
                lMController.btOneIn()
            }
            tooltip("Input the unique node's name")
        }
        button {
            text = "One step out"
            addClass(Styles.forButton)
            action {
                lMController.btClicked(NameButton.ONE_STEP_OUT)
            }
            tooltip("Input the unique node's name")
        }

        label {
            text = "Initial state:"
            addClass(Styles.forLabels)
        }
        button {
            text = "Graph"
            addClass(Styles.forButton)
            action {
                lMController.btGraphStart()
            }
            tooltip("Input the unique node's name")
        }

        label {
            text = "Explanations:"
            addClass(Styles.forLabels)
        }
        button {
            text = "Show"
            addClass(Styles.forButton)
            action {
                lMController.btShow()
            }
            tooltip("Input the unique node's name")
        }

    }
}
