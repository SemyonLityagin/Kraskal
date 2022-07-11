package com.example.view

import com.example.controller.MenuController
import tornadofx.*

class ExplainView : View("Explanation") {
    val mController: MenuController by inject()
    
    override val root = textarea(mController.explainText){
        setPrefSize(400.0, 200.0)
        isWrapText = true
    }
    override fun onUndock() {
        mController.flagExpClose = true
        super.onUndock()
    }
}
