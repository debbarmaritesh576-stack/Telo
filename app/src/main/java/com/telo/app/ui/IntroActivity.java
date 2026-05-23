package com.telo.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.telo.app.R;
import com.telo.app.util.PreferenceHelper;

public class IntroActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button     btnNext;
    private Button     btnSkip;
    private TextView   tvGetStarted;

    private static final int TOTAL_PAGES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        viewPager    = findViewById(R.id.viewpager_intro);
        btnNext      = findViewById(R.id.btn_intro_next);
        btnSkip      = findViewById(R.id.btn_intro_skip);
        tvGetStarted = findViewById(R.id.tv_get_started);

        setupViewPager();
        setupButtons();
    }

    private void setupViewPager() {
        // IntroSlideAdapter would go here
        viewPager.registerOnPageChangeCallback(
            new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    if (position == TOTAL_PAGES - 1) {
                        btnNext.setVisibility(View.GONE);
                        btnSkip.setVisibility(View.GONE);
                        tvGetStarted.setVisibility(View.VISIBLE);
                    } else {
                        btnNext.setVisibility(View.VISIBLE);
                        btnSkip.setVisibility(View.VISIBLE);
                        tvGetStarted.setVisibility(View.GONE);
                    }
                }
            }
        );
    }

    private void setupButtons() {
        btnNext.setOnClickListener(v -> {
            int next = viewPager.getCurrentItem() + 1;
            if (next < TOTAL_PAGES) {
                viewPager.setCurrentItem(next);
            }
        });

        btnSkip.setOnClickListener(v -> goToSetup());

        tvGetStarted.setOnClickListener(v -> goToSetup());
    }

    private void goToSetup() {
        PreferenceHelper.setFirstLaunchDone();
        startActivity(new Intent(this, UnlockActivity.class));
        finish();
    }
}