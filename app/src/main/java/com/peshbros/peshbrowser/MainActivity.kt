package com.peshbros.peshbrowser

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.inputmethod.EditorInfo
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.WebChromeClient
import android.widget.*

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

class MainActivity : AppCompatActivity() {

    lateinit var fullScreenView: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get references to Views
        val editText = findViewById<EditText>(R.id.editText)
        val goButton = findViewById<Button>(R.id.goButton)
        val backButton = findViewById<Button>(R.id.backButton)
        val forwardButton = findViewById<Button>(R.id.forwardButton)
        val webView = findViewById<WebView>(R.id.webView)
        val mainContainer = findViewById<LinearLayout>(R.id.mainContainer)
        val fullScreenContainer = findViewById<FrameLayout>(R.id.fullScreenContainer)

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
        // set webview fullscreen handler
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                if(view is FrameLayout) {
                    fullScreenView = view
                    fullScreenContainer.addView(fullScreenView)
                    fullScreenContainer.visibility = View.VISIBLE
                    mainContainer.visibility = View.GONE
                }
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                fullScreenContainer.removeView(fullScreenView)
                fullScreenContainer.visibility = View.GONE
                mainContainer.visibility = View.VISIBLE
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
