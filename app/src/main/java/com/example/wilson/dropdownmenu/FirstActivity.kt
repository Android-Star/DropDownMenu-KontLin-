package com.example.wilson.dropdownmenu

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.wilson.dropdownmenu.java.JavaActivity
import com.example.wilson.dropdownmenu.kotlin.KotlinActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by ggg on 2018/12/7.
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnJava.setOnClickListener(this)
        btnKotlin.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            btnJava -> {
                startActivity(Intent(this, JavaActivity::class.java))
            }
            btnKotlin -> {
                startActivity(Intent(this, KotlinActivity::class.java))

            }
            else -> {

            }
        }
    }
}