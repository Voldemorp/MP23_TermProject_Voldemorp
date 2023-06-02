package com.example.mp23_termproject_voldemorp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class RankingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private FrameLayout rankingContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        // 상태 바 투명하게 하고 사진 보이게 하는 코드
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        rankingContainer = findViewById(R.id.rankingContainer);
        viewPager = new ViewPager2(this);
        viewPager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        rankingContainer.addView(viewPager);
        setupViewPager();

        // 메인으로 가는 버튼 이벤트
        Button backToMainBtn = (Button) findViewById(R.id.backToMainBtn);
        backToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RankingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager() {
        RankingActivity.MyPagerAdapter adapter = new RankingActivity.MyPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(AppCompatActivity activity) {
            super(activity);
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new MasterRankingFragment();
                case 1:
                    return new ManiaRankingFragment();
                default:
                    return null;
            }
        }
    }
}