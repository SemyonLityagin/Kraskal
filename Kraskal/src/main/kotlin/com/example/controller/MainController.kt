package com.example.controller

import javafx.beans.property.SimpleStringProperty
import tornadofx.Controller

class MainController: Controller() {

    var labelText = SimpleStringProperty()
    private val namesList = listOf<String>(
        "Dino",
        "Nino",
        "Nigga",
        "Click me!!",
        "Yooo"
    )

    fun randomName(){
        val randInteger = (0..(namesList.size-1)).shuffled().first()
        labelText.value = namesList[randInteger]
    }
}