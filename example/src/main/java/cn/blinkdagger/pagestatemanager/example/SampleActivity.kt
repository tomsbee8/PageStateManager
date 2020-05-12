package cn.blinkdagger.pagestatemanager.example

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import cn.blinkdagger.pagestatemanager.PageStateMachine
import cn.blinkdagger.pagestatemanager.PageStateManager
import kotlinx.android.synthetic.main.activity_sample.*

class SampleActivity : AppCompatActivity() {

    private var pageStateManager : PageStateManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        initView()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.go_fragment){
            startActivity(Intent(this, SampleFragmentActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initView(){
        pageStateManager = PageStateMachine.with(this@SampleActivity)
            .setContentViewId(R.id.ll_content)
            .setLoadingLayout(R.layout.layout_content_loading)
            .get()
        tv_loading.setOnClickListener {
            pageStateManager?.showLoading()
        }
        tv_load_empty.setOnClickListener {
            pageStateManager?.showLoadEmpty()
        }
        tv_load_failed.setOnClickListener {
            pageStateManager?.showLoadFailed()
        }
        tv_load_content.setOnClickListener {
            pageStateManager?.showContent()
        }
    }
}
