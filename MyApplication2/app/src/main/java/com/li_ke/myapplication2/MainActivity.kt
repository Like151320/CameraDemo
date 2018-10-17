package com.li_ke.myapplication2

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun onSnack(view: View) {
        Snackbar.make(noRootView, "被软键盘遮挡吗?", Snackbar.LENGTH_LONG).show()
    }
}
