import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun loadHtmlFromWebView(context: Context, url: String): String {
    return suspendCancellableCoroutine { continuation ->
        val webView = WebView(context.applicationContext)
        webView.settings.javaScriptEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // After the page is fully loaded, extract the HTML
                webView.evaluateJavascript("(function() { return document.documentElement.outerHTML; })();") { html ->
                    continuation.resume(html)
                    webView.stopLoading()
                    webView.destroy()
                }
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                continuation.resumeWithException(Exception("WebView loading error: $description"))
                webView.stopLoading()
                webView.destroy()
            }
        }

        webView.loadUrl(url)
    }
}
