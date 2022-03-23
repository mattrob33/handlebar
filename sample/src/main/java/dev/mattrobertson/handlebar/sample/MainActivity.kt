package dev.mattrobertson.handlebar.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.mattrobertson.handlebar.sample.R
import dev.mattrobertson.handlebar.sample.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}