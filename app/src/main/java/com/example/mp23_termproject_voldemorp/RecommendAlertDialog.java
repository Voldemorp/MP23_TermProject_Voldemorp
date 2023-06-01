package com.example.mp23_termproject_voldemorp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class RecommendAlertDialog extends Dialog {

    private Button recommendBtn;
    private Button notRecommendBtn;
    private Button closeButton;
    private TextView numOfVisit;
    private TextView descriptionField;

    public RecommendAlertDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recommend_alert);

        // 레이아웃 요소 연결
        numOfVisit = findViewById(R.id.numOfVisit);
        notRecommendBtn = findViewById(R.id.notRecommendBtn);
        recommendBtn = findViewById(R.id.recommendBtn);
        descriptionField = findViewById(R.id.descriptionField);
        closeButton = findViewById(R.id.closeButton);

        // 다이얼로그 닫기 버튼의 클릭 이벤트 처리
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
