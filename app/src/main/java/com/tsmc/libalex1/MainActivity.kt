package com.tsmc.libalex1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.viewinterop.AndroidView
import com.tsmc.alexweb.AlexForWeb

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var w = AlexForWeb(this)
        setContent {
            var result = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult(), onResult = {
                w.remfilefirst(it)
            })

            val permission = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(), onResult = {
                w.remfilesecond(it, this@MainActivity, result)
            })

            AndroidView(factory = {
                w.nastroykiOne(w, true)
                w.nastroykiTwo(w, true)
                w.nastroykiThree(w, true, permission)
                w.zapusk(w, true, "http://tsapptest.xyz/")
                w
            })



        }
    }
}