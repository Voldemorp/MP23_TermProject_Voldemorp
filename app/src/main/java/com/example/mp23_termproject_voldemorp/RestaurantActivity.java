package com.example.mp23_termproject_voldemorp;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class RestaurantActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private LinearLayout restaurantContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        restaurantContainer = findViewById(R.id.restaurantContainer);
        viewPager = new ViewPager2(this);
        viewPager.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        restaurantContainer.addView(viewPager);
        setupViewPager();
    }

    private void setupViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private class MyPagerAdapter extends FragmentStateAdapter {
        public MyPagerAdapter(AppCompatActivity activity) {
            super(activity);
        }

        @Override
        public int getItemCount() {
            return 3;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // 각 position에 맞는 Fragment를 생성하여 반환하는 코드를 작성하세요.
            // 예를 들어, 첫 번째 Fragment는 Fragment1로 설정하려면 다음과 같이 작성할 수 있습니다:
            return new Fragment1();
        }
    }
}