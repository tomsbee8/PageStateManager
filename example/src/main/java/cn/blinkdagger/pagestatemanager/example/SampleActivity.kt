package cn.blinkdagger.pagestatemanager.example

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.blinkdagger.pagestatemanage.PageStateMachine
import cn.blinkdagger.pagestatemanage.PageStateManager
import cn.blinkdagger.pagestatemanage.ShowStateListener
import kotlinx.android.synthetic.main.activity_sample.*

class SampleActivity : AppCompatActivity(),ShowStateListener{

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
            .setFailedLayout(R.layout.layout_load_failed)
            .setEmptyLayout(R.layout.layout_load_empty)
            .setShowStateListener(this)
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

    override fun onShowCustomState(stateCode: Int, loadingView: View?) {}

    override fun onShowLoading(loadingView: View?) {}

    override fun onShowLoadEmpty(emptyView: View?) {}

    override fun onShowLoadFailed(failedView: View?) {
        failedView?.findViewById<TextView>(R.id.tv_failed_retry)?.setOnClickListener {
            pageStateManager?.showContent()
        }
        Toast.makeText(this,"加载数据失败",Toast.LENGTH_SHORT).show()
    }
}
