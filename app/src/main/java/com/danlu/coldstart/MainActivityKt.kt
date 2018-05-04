package com.danlu.coldstart

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast

class MainActivityKt : AppCompatActivity() {

    private var homeKeyReceiver: HomeKeyReceiver? = null

    private var mCanExit: Boolean = false
    /**
     * 记录点击的是不是 back 键
     */
    private var mClickBack: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeKeyReceiver = HomeKeyReceiver()
        registerReceiver(homeKeyReceiver, IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    override fun onRestart() {
        if (mClickBack) {
            //启动之前点击的是返回键，此处可能需要切换首页 tab，刷新数据等
            Log.e("hidetag","上次是返回动作")
        } else {
            //启动之前不是返回键
            Log.e("hidetag","上次非返回")
        }
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
        homeKeyReceiver?.let { LocalBroadcastManager.getInstance(this).unregisterReceiver(it) }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (!mCanExit) {
                Toast.makeText(this, "再按一次退出 app", Toast.LENGTH_SHORT).show()
                mCanExit = true
                Handler().postDelayed({ mCanExit = false }, TIME)
                mClickBack = false
                return true
            }
            mClickBack = true
            moveTaskToBack(true)
            return true
        } else {
            mClickBack = false
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 监听 Home 键的广播
     */
    internal inner class HomeKeyReceiver : BroadcastReceiver() {
        private val SYSTEM_DIALOG_REASON_KEY = "reason"
        private val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps" // 长按home键
        private val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey" // 短按home键

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS == action) {
                val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
                if (reason != null) {
                    if (SYSTEM_DIALOG_REASON_HOME_KEY == reason) {
                        mClickBack = false
                    } else if (SYSTEM_DIALOG_REASON_RECENT_APPS == reason) {
                        mClickBack = false
                    }
                }
            }
        }
    }

    companion object {
        const val TIME: Long = 2000
    }
}
