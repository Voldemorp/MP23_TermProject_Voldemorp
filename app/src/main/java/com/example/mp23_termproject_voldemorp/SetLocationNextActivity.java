package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class SetLocationNextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_next);

        WebView webView = findViewById(R.id.webView);
        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(), "Android");
        webView.setWebViewClient(new WebViewClient() {
            // Called when the WebView page finishes loading
            @Override
            public void onPageFinished(WebView view, String url) {
                // Call the JavaScript function from Android
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }

        });
        // Load the initial WebView
        webView.loadUrl("https://mp23-termproject.web.app");
    }

    // Bridge interface for communication
    // JavaScript to Android
    private class BridgeInterface {
        @JavascriptInterface
        public void processDATA(String data) {
            // Receive the result data from the address search API through the bridge interface (from JavaScript)
            Intent intent = new Intent();
            intent.putExtra("data", data);

            // Process based on the "order" value
            String order = getIntent().getStringExtra("order");
            if (order != null) {
                if (order.equals("1"))
                    intent.putExtra("order", "1");
                else if (order.equals("2"))
                    intent.putExtra("order", "2");
                else if (order.equals("3"))
                    intent.putExtra("order", "3");
                else if (order.equals("4"))
                    intent.putExtra("order", "4");
            }
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
