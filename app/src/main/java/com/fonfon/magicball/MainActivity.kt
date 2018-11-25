package com.fonfon.magicball

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {

    lateinit var ui: MainActivityUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = MainActivityUI()
        ui.setContentView(this)
    }
}

//Класс, содержащий в себе верстку
class MainActivityUI: AnkoComponent<MainActivity> {

    //lateinit объявляет, что эта переменная никогда не будет пустой, но инициализируется она не сразу
    lateinit var textView: TextView

    //kotlin позволяет начиниать функции с =
    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {
        frameLayout {
            textView = textView {
                text = "Потряси меня"
                textSize = 64f
                textColor = Color.WHITE
                gravity = Gravity.CENTER
            }.lparams {
                gravity = Gravity.CENTER
                margin = 16
            }
        }
    }

}
