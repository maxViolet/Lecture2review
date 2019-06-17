package com.example.android.maximfialko.ui.intro;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.android.maximfialko.R;
import com.pixelcan.inkpageindicator.InkPageIndicator;

public class IntroActivityFragment extends FragmentActivity {
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private InkPageIndicator inkPageIndicator;

    static final int PAGE_COUNT = 3;

    @Override
    protected void onStop() {
        super.onStop();
        //exit app if backpressed after Intro
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_fragmentactivity);

        inkPageIndicator = findViewById(R.id.indicator);

        pager = findViewById(R.id.viewpager);
        pagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
//        inkPageIndicator.setViewPager(pager);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("viewPager", "current page: " + position);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class CustomFragmentPagerAdapter extends FragmentPagerAdapter {

        public CustomFragmentPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return IntroPageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}


