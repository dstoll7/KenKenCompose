package com.danstoll.kenkencompose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.ui.core.setContent


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val boards = mutableListOf(listOf<String>())
        for(i in 3..8) {
            boards.add( KenKenGenerator().generate(i))
        }
        setContent {
            MyAppTheme {
                KenKenScreen()
            }
        }
    }
}


