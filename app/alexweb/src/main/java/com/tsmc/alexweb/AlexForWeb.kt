package com.tsmc.alexweb

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.core.content.FileProvider
import java.io.File

class AlexForWeb(val ontext: Context): WebView(ontext) {
    fun backMove(w: WebView) {
        if (w.canGoBack()) {
            w.goBack()
        }
    }


    val thisTwoAct = this

    companion object {
        var smUri: Uri? = null
        var bgValueCallback: ValueCallback<Array<Uri>>? = null
    }

    public fun remfilefirst(sup: ActivityResult) {
        when {
            sup.resultCode == -1 -> {
                when {
                    sup.data?.dataString != null -> {
                        bgValueCallback?.onReceiveValue(arrayOf(Uri.parse(sup.data?.dataString)))
                    }
                    sup.data?.dataString == null -> {
                        when {
                            smUri == null -> {
                                bgValueCallback?.onReceiveValue(null)
                            }
                            smUri != null -> {
                                bgValueCallback?.onReceiveValue(arrayOf(smUri!!))
                            }
                        }
                    }
                }
            }
            sup.resultCode != -1 -> {
                bgValueCallback?.onReceiveValue(null)
            }
        }
        bgValueCallback = null
    }

    fun remfilesecond(
        sup1: Boolean,
        sup2: Context,
        sup3: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) {
        val sp1 = Intent("android.media.action.IMAGE_CAPTURE")
        val sp2 = Intent("android.intent.action.GET_CONTENT")
        when {
            sup1 -> {
                val sp3 = Intent("android.intent.action.CHOOSER")
                val sp4 = File.createTempFile(
                    "img",
                    ".jpg",
                    sup2.getExternalFilesDir(
                        "Pictures"
                    )
                )
                val sp5 = FileProvider.getUriForFile(sup2,
                    sup2.packageName,sp4)
                sp2.type = "*/*"
                smUri = sp5
                sp2.addCategory(
                    "android.in" +
                            "tent.category.OP" +
                            "ENABLE"
                )
                sp3.putExtra(Intent.EXTRA_INTENT, sp2)
                sp1.putExtra(MediaStore.EXTRA_OUTPUT, sp5)
                sp3.putExtra(
                    "android.intent.e" +
                            "xtra.INITIAL_INTE" +
                            "NTS", arrayOf(sp1)
                )
                sup3.launch(sp3)
            }
            else -> {
                bgValueCallback?.onReceiveValue(null)
            }
        }
    }

    public fun nastroykiOne(w: WebView, bool: Boolean) {
        w.apply {
            CookieManager.getInstance().setAcceptCookie(true)
            settings.javaScriptCanOpenWindowsAutomatically = bool
            settings.databaseEnabled = true
            settings.allowFileAccessFromFileURLs = bool
        }
    }

    public fun nastroykiTwo(w: WebView, bool: Boolean) {
        w.apply {
            settings.allowUniversalAccessFromFileURLs = bool
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url.toString()
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        return false
                    } else {
                        try {
                            val intent: Intent
                            if (url.startsWith("intent:")) {
                                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                                if (intent.action == "com.google.firebase.dynamiclinks.VIEW_DYNAMIC_LINK") {
                                    intent.extras?.getString("browser_fallback_url")
                                        ?.let { view?.loadUrl(it) }
                                    return true
                                }
                            } else {
                                intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            }
                            view!!.context.startActivity(intent)

                        } catch (_: Exception) {
                        }
                        return true
                    }

                }
            }
            settings.javaScriptEnabled = bool
            settings.allowFileAccess = bool
        }
    }

    public fun nastroykiThree(w: WebView, bool: Boolean, manager:  ManagedActivityResultLauncher<String, Boolean>) {
        w.apply {
            settings.allowContentAccess = true
            settings.mixedContentMode = 0
            settings.useWideViewPort = true
            webChromeClient = object : WebChromeClient() {
                override fun onShowFileChooser(
                    webView: WebView?,
                    valueCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    bgValueCallback = valueCallback
                    manager.launch(Manifest.permission.CAMERA)
                    return true
                }
            }
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = bool
        }
    }

    public fun zapusk(w: WebView, bool: Boolean, linka: String) {
        w.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        w.loadUrl(linka)
    }
}