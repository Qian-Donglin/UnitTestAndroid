package com.rikoten.testplacetomonokai

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.rikoten.testplacetomonokai.LinkUtils.autoLink


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val testStr =
            "URLの形式のみブラウザで開きます。http://www.yahoo.co.jp/とhttp://d.hatena.ne.jp/をリンク表示にしてクリック可能にしました。"
        val textView = TextView(this)
        textView.textSize = 20f
        textView.text = testStr
        autoLink(textView, object : LinkUtils.OnClickListener {
            override fun onLinkClicked(link: String?) {
                Log.i("SensibleUrlSpan", "リンククリック:$link")
            }

            override fun onClicked() {
                Log.i("SensibleUrlSpan", "ビュークリック")
            }
        })
        setContentView(textView)
    }

}