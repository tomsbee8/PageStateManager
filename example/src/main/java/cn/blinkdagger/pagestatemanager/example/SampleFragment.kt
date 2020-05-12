package cn.blinkdagger.pagestatemanager.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.blinkdagger.pagestatemanager.PageStateMachine
import cn.blinkdagger.pagestatemanager.PageStateManager
import kotlinx.android.synthetic.main.fragment_sample.*

class SampleFragment : Fragment() {

    private var pageStateManager : PageStateManager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        pageStateManager = PageStateMachine.with(this@SampleFragment)
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
