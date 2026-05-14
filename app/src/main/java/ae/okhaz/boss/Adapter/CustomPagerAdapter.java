package ae.okhaz.boss.Adapter;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import ae.okhaz.admin.R;

import ae.okhaz.boss.CustomFragment;
import ae.okhaz.boss.CustomLinear;

import static ae.okhaz.boss.view.activities.GetStartedActivity.FIRST_PAGE;
import static ae.okhaz.boss.view.activities.GetStartedActivity.PAGES;


public class CustomPagerAdapter extends FragmentPagerAdapter implements ViewPager.PageTransformer {
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;

    private Activity mContext;
    private FragmentManager mFragmentManager;
    private float mScale;

    public CustomPagerAdapter(Activity context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mFragmentManager = fragmentManager;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // make the first mViewPager bigger than others
        if (position == FIRST_PAGE)
            mScale = BIG_SCALE;
        else
            mScale = SMALL_SCALE;

        return CustomFragment.newInstance(mContext, position, mScale);
    }

    @Override
    public int getCount() {
        return PAGES;
    }

    @Override
    public void transformPage(View page, float position) {
        CustomLinear myLinearLayout = (CustomLinear) page.findViewById(R.id.item_root);
        float scale = BIG_SCALE;
        if (position > 0) {
            scale = scale - position * DIFF_SCALE;
        } else {
            scale = scale + position * DIFF_SCALE;
        }
        if (scale < 0) scale = 0;
        myLinearLayout.setScaleBoth(scale);
    }
}