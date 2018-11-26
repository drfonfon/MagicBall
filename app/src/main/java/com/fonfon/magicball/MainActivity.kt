package com.fonfon.magicball

import android.content.Context
import android.graphics.Color
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var vibrator: Vibrator
    private val random = Random()

    lateinit var ui: MainActivityUI
    lateinit var shakeDetector: ShakeDetector

    private var block = false
    private val handler = Handler()
    private val runnable = Runnable {
        block = false
        ui.textView.text = "Потряси меня"
    }

    //В kotlin есть множество полезных встроенныых функицй
    private var answers = listOf(
        Answer("Да"),
        Answer("Нет")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = MainActivityUI().also {
            it.setContentView(this)
        }

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        api.answers().enqueue(object : Callback<List<Answer>> {
            override fun onFailure(call: Call<List<Answer>>, t: Throwable) {
            }

            override fun onResponse(call: Call<List<Answer>>, response: Response<List<Answer>>) {
                response.body()?.let {
                    answers = it
                }
            }

        })

        shakeDetector = ShakeDetector(this) {
            if (block.not()) {
                block = true
                ui.textView.text = answers[random.nextInt(answers.size)].text
                vibrate()
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 1500)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ui.textView.text = "Потряси меня"
        shakeDetector.start()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
        shakeDetector.stop()
    }

    fun vibrate() {
        vibrator.cancel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(300)
        }
    }

}

//Класс, содержащий в себе верстку
class MainActivityUI : AnkoComponent<MainActivity> {

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
