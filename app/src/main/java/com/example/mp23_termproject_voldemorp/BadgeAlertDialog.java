package com.example.mp23_termproject_voldemorp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class BadgeAlertDialog extends Dialog {

    private Button closeButton;
    private TextView nameOfNewBadge;
    private ImageView badgeImage;

    public BadgeAlertDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_badge_alert);

        // 레이아웃 요소 연결
        badgeImage = findViewById(R.id.badgeImage);
        closeButton = findViewById(R.id.closeButton);
        nameOfNewBadge = findViewById(R.id.nameOfNewBadge);

        // 다이얼로그 닫기 버튼의 클릭 이벤트 처리
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
