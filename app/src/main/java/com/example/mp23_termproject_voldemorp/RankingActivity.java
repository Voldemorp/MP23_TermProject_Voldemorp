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

        // Make the status bar transparent and the image visible
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        rankingContainer = findViewById(R.id.rankingContainer);
        viewPager = new ViewPager2(this);
        viewPager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        rankingContainer.addView(viewPager);
        setupViewPager();

        // Event for the button to go back to the main page
        Button backToMainBtn = findViewById(R.id.backToMainBtn);
        backToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RankingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Set up the ViewPager with the adapter and the fragments for each ranking type.
     */
    private void setupViewPager() {
        RankingActivity.MyPagerAdapter adapter = new RankingActivity.MyPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    /**
     * Custom FragmentStateAdapter for managing the fragments in the ViewPager.
     */
    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(AppCompatActivity activity) {
            super(activity);
        }

        @Override
        public int getItemCount() {
            return 2; // Total number of ranking fragments
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new MasterRankingFragment(); // Fragment for master ranking
                case 1:
                    return new ManiaRankingFragment(); // Fragment for mania ranking
                default:
                    return null;
            }
        }
    }
}
