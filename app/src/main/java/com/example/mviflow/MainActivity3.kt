package com.example.mviflow

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mviflow.main.DetailUiSate
import com.example.mviflow.main.MainIntent
import com.example.mviflow.main.MainViewModel
import com.example.mviflow.model.bean.ArticleItem
import kotlinx.coroutines.flow.map

class MainActivity3 : AppCompatActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        findViewById<View>(R.id.btn_loadData).setOnClickListener {
            viewModel.sendUiIntent(MainIntent.GetDetail(0))
        }

        textView = findViewById(R.id.tv_showdata)
        initData()
    }

    private fun initData() {
        lifecycleScope.launchWhenStarted {

            //请求数据flow的流的监听和处理
            viewModel.uiStateFlow.map { it.detailUiSate }
                .collect { detailUiState ->
                    when(detailUiState) {
                        is DetailUiSate.SUCCESS -> {
                            showText(detailUiState.articles.datas)
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun showText(item: List<ArticleItem>) {
        val sb = java.lang.StringBuilder()
        item.forEach {
            sb.append(it.title + "   " + it.niceDate + "\n")
        }
        textView?.text = sb.toString()
    }
}