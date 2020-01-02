package com.peshbros.peshbrowser

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.view.inputmethod.EditorInfo
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get reference to editText
        val editText = findViewById<EditText>(R.id.editText)
        // get reference to buttons
        val goButton = findViewById<Button>(R.id.goButton)
        val backButton = findViewById<Button>(R.id.backButton)
        val forwardButton = findViewById<Button>(R.id.forwardButton)
        // get reference to webview
        val webView = findViewById<WebView>(R.id.webView)

        // set default URL
        webView.loadUrl("https://www.google.com/")
        // enable JavaScript
        webView.settings.javaScriptEnabled = true
        // set webview redirect handler
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                editText.setText(url)
            }
        }

        // set behavior for go keyboard button
        editText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    goButton.callOnClick()
                    return true
                }
                return false
            }
        })

        //  set onClickListeners
        goButton.setOnClickListener {
            var site = editText.text.toString().trim()

            if(site.contains(' ') || !site.contains('.'))
                site = "https://www.google.com/search?q=$site"

            if(!site.startsWith("https://"))
                site = "https://$site"

            webView.loadUrl(site)
            editText.hideKeyboard()
        }
        backButton.setOnClickListener {
            if(webView.canGoBack())
                webView.goBack()
        }
        forwardButton.setOnClickListener {
            if(webView.canGoForward())
                webView.goForward()
        }
    }

    // phone back button behavior
    override fun onBackPressed() {
        // get reference to webview
        val webView = findViewById<WebView>(R.id.webView)

        if (webView.canGoBack()) {
            webView.goBack()
            return
        }
        super.onBackPressed()
    }
}
