package com.example.view

import com.example.Styles
import com.example.controller.MainController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import tornadofx.*
import javax.swing.JSplitPane.LEFT

class MainView : View("Hello TornadoFX") {

    val mainController: MainController by inject()

    private var labelText1 = SimpleBooleanProperty()

    override val root = vbox {
        spacing = 10.0
        alignment = Pos.CENTER

        label(labelText1) {
            bind(labelText1)
            id = "lb"
            addClass(Styles.heading)
        }
        label(mainController.labelText) {
            //bind(mainController.labelText)
            id = "lb"
            addClass(Styles.heading)
        }

        button{
            this.text = "Click Me!"
            action {
                labelText1.value = !labelText1.value
                mainController.randomName()
            }
        }
    }

}
