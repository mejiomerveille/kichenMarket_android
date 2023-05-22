package com.example.mervshops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mervshops.Adapter.ViewPageAdapter;
import com.example.namespace.R;
import com.example.namespace.databinding.ActivityOnboardBinding;

public class OnboardActivity extends AppCompatActivity {
    public ActivityOnboardBinding ui;
    private ViewPager viewPager;
    private Button btnleft,btnright;
    private ViewPageAdapter adapter;
    private LinearLayout dotoLayout;
    private TextView[] doto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ui=ActivityOnboardBinding.inflate(getLayoutInflater());
        setContentView(ui.getRoot());
        init();
    }

    public void init() {
        viewPager = ui.viewPaper;
        btnleft = ui.btnLeft;
        btnright = ui.btnRigth;
        dotoLayout =ui.dotolayout;
        adapter = new ViewPageAdapter(this);
        addDoto(0);
        viewPager.addOnPageChangeListener(listener);
        viewPager.setAdapter(adapter);

        // Change the text of the right button to "Finish" when on the last page
        btnright.setOnClickListener(v -> {
            if (btnright.getText().toString().equals("Next")) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            } else {
                startActivity(new Intent(OnboardActivity.this, AuthActivity.class));
                finish();
            }
        });

        // Change the text of the left button to "Back" when on the second or third page
        btnleft.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() == 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 2);
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });
    }

    // Add dots to indicate which page is currently displayed
    private void addDoto(int position) {
        dotoLayout.removeAllViews();
        doto = new TextView[3];
        for (int i = 0; i < doto.length; i++) {
            doto[i] = new TextView(this);
            doto[i].setText(Html.fromHtml("&#8226;"));
            doto[i].setTextSize(35);

            // Change the color of the dot based on whether it represents the current page or not
            if (i == position) {
                doto[i].setTextColor(getResources().getColor(R.color.purple_200));
            } else {
                doto[i].setTextColor(getResources().getColor(R.color.black));
            }

            dotoLayout.addView(doto[i]);
        }
    }
    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addDoto(position);
            if (position == 0) {
                btnleft.setVisibility(View.VISIBLE);
                btnleft.setEnabled(true);
                btnright.setText("Next");
            } else if (position == 1) {
                btnleft.setVisibility(View.GONE);
                btnleft.setEnabled(false);
                btnright.setText("Next");
            } else {
                btnleft.setVisibility(View.GONE);
                btnleft.setEnabled(false);
                btnright.setText("Finish");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            /* Ne rien mettre ici pour l'instant*/
        }
    };
}