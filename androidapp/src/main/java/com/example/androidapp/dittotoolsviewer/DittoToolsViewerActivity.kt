package com.example.androidapp.dittotoolsviewer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.androidapp.DittoHandler.Companion.ditto
import live.ditto.dittotoolsviewer.presentation.DittoToolsViewer

class DittoToolsViewerActivity :  AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DittoToolsViewer(
                ditto = ditto,
                onExitTools = { finish() }
            )
        }
    }
}
