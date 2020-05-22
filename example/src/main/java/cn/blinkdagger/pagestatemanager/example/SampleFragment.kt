package cn.blinkdagger.pagestatemanager.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.blinkdagger.pagestatemanage.PageStateMachine
import cn.blinkdagger.pagestatemanage.PageStateManager
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

    private fun initViews() {
        pageStateManager = PageStateMachine.with(this@SampleFragment)
            .setLoadingLayout(R.layout.layout_content_loading)
            .setShowLoadingWhenCreate(false)
            .addCustomStateLayout(1, R.layout.layout_load_failed)
            .get()
        tv_loading.setOnClickListener {
            pageStateManager?.showLoading()
        }
        tv_custom_state_1.setOnClickListener {
            pageStateManager?.showCustomStateView(1)
        }
        tv_load_content.setOnClickListener {
            pageStateManager?.showContent()
        }
    }

}
