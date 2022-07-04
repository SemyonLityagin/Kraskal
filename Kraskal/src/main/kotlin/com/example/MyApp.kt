package com.example

import com.example.view.AnMainView
import com.example.view.Interface
import com.example.view.MainView
import javafx.stage.Stage
import tornadofx.App

class MyApp: App(Interface::class, Styles::class){
    override fun start(stage: Stage) {
        with(stage){
            minWidth = 1200.0
            minHeight = 600.0
            maxHeight = 600.0
            maxWidth = 1200.0
        }
        super.start(stage)
    }
}