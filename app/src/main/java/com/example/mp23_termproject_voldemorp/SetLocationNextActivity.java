package com.example.mp23_termproject_voldemorp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SetLocationNextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location_next);

        WebView webView=findViewById(R.id.webView);
        //javascript 허용
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(),"Android");
        webView.setWebViewClient(new WebViewClient(){
            //웹뷰 페이지 로딩이 완료되었을 떄
            @Override
            public void onPageFinished(WebView view, String url) {
                //android->javascript 함수 호출
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }

        });
        //최초 웹뷰 로드
        webView.loadUrl("https://mp23-termproject.web.app");
    }

    //연결 통로(bridge)
    //javascript->android
    private class BridgeInterface {
        @JavascriptInterface
        public void processDATA(String data){
            //주소 검색 API의 결과 값이 브릿지 통로를 통해 전달 받는다. (javascript로 부터)
            Intent intent=new Intent();
            intent.putExtra("data",data);

            // order 값에 따라 처리
            String order = getIntent().getStringExtra("order");
            if(order !=null){
                if(order.equals("1"))
                    intent.putExtra("order","1");
                else if(order.equals("2"))
                    intent.putExtra("order","2");
                else if(order.equals("3"))
                    intent.putExtra("order","3");
                else if(order.equals("4"))
                    intent.putExtra("order","4");
            }
            setResult(RESULT_OK,intent);
            finish();


        }
    }
}