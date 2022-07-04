package com.example.view

import com.example.controller.LeftMenuController
import tornadofx.*

class ExplainView : View("Explanation") {
    val lMController: LeftMenuController by inject()
    override val root = pane {
        minWidth = 400.0
        minHeight = 200.0
        maxHeight = 200.0
        maxWidth = 400.0
        label(lMController.explainText)
    }

    override fun onUndock() {
        lMController.flagExpClose = true
        super.onUndock()
    }
}
