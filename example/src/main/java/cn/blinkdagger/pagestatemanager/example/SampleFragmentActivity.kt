package cn.blinkdagger.pagestatemanager.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SampleFragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_sample)
        afterViews()
    }

    fun afterViews() {
        val trans =  supportFragmentManager.beginTransaction()
        trans.add(R.id.container, SampleFragment())
        trans.commit()
    }
}
