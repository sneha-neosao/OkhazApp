package ae.okhaz.boss.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import ae.okhaz.admin.R;

import ae.okhaz.boss.Activitys.TutorialActivity;
import ae.okhaz.boss.Adapter.CustomPagerAdapter;

public class GetStartedActivity extends AppCompatActivity {


    public final static int PAGES = 3;
    public final static int FIRST_PAGE = 0  ;
    public CustomPagerAdapter mAdapter;
    public ViewPager mViewPager;
    public Button btn_next;

    private ImageView[] ivArrayDotsPager;
    private LinearLayout llPagerDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);


        mViewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
        btn_next =findViewById(R.id.btn_next);
        llPagerDots = (LinearLayout) findViewById(R.id.pager_dots);

        setupPagerIndidcatorDots();

        ivArrayDotsPager[0].setImageResource(R.drawable.page_indicator_selected);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < ivArrayDotsPager.length; i++) {
                    ivArrayDotsPager[i].setImageResource(R.drawable.page_indicator_unselected);
                }
                ivArrayDotsPager[position].setImageResource(R.drawable.page_indicator_selected);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        mAdapter = new CustomPagerAdapter(this, this.getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPageTransformer(false, mAdapter);

        // Set current item to the middle page so we can fling to both
        // directions left and right
        mViewPager.setCurrentItem(FIRST_PAGE);

        // Necessary or the mViewPager will only have one extra page to show
        // make this at least however many pages you can see
        mViewPager.setOffscreenPageLimit(3);

        // Set margin for pages as a negative number, so a part of next and
        // previous pages will be showed
        mViewPager.setPageMargin(-400);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mViewPager.getCurrentItem()==0){
                        mViewPager.setCurrentItem(1);
                }
               else if (mViewPager.getCurrentItem()==1){
                    mViewPager.setCurrentItem(2);
                }
               else if (mViewPager.getCurrentItem()==2) {
                    startActivity(new Intent(GetStartedActivity.this, TutorialActivity.class));
                    finish();
                }
            }
        });
    }

    private void setupPagerIndidcatorDots()
    {
        ivArrayDotsPager = new ImageView[PAGES];
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            ivArrayDotsPager[i] = new ImageView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            ivArrayDotsPager[i].setLayoutParams(params);
            ivArrayDotsPager[i].setImageResource(R.drawable.page_indicator_unselected);
            //ivArrayDotsPager[i].setAlpha(0.4f);
            ivArrayDotsPager[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setAlpha(1);
                }
            });
            llPagerDots.addView(ivArrayDotsPager[i]);
            llPagerDots.bringToFront();
        }
    }

}