package ae.okhaz.boss.Adapter.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import ae.okhaz.boss.Fragments.Admin.OrderTabFragment;
import ae.okhaz.boss.Fragments.Admin.SalesFragment;

public class HomeAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;
    private boolean isPagingEnabled = true;
    String[] titles ={"SALES","ORDERS"};

    public HomeAdapter(@NonNull FragmentManager fm,int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SalesFragment tab1 = new SalesFragment();
                return tab1;
            case 1:
                OrderTabFragment tab2 = new OrderTabFragment();
                return tab2;

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return titles[position];
    }

    @Override
    public int getCount() {

        return noOfTabs;
    }
}
