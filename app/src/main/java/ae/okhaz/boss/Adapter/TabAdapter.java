package ae.okhaz.boss.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avinash on 27,November,2020
 */
public class TabAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();
    private  List<String> fragmentTiles = new ArrayList<>();



    public TabAdapter(FragmentManager childFragmentManager) {
        super(childFragmentManager);
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        fragmentTiles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTiles.get(position);
    }

    public void setFragmentTiles(int index, String title) {
        fragmentTiles.set(index, title);

    }

    public void clear() {
        fragments.clear();
        fragmentTiles.clear();
        notifyDataSetChanged();
    }
}
