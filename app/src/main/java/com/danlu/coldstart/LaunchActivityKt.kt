package com.danlu.coldstart

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity

/**
 * author: lixin(<a href="mailto:lixin@danlu.com">lixin@danlu.com</a>)<br/>
 * version: 1.0.0<br/>
 * since: 2018-05-04 11:24<br/>
 *
 * <p>
 * 启动页 Activity
 * </p>
 */
class LaunchActivityKt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        Handler(Handler.Callback {
            startActivity(Intent(this@LaunchActivityKt, MainActivityKt::class.java))
            finish()
            true
        }).sendEmptyMessageDelayed(0, TIME)
    }

    companion object {

        const val TIME = 2000L
    }
}