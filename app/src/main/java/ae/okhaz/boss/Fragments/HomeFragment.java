package ae.okhaz.boss.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ae.okhaz.boss.Adapter.TabAdapter;
import ae.okhaz.boss.Model.Categories;
import ae.okhaz.boss.Model.Counts;
import ae.okhaz.boss.Model.DeliveryBoyModel;
import ae.okhaz.admin.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    AppBarLayout appBarLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabAdapter tabAdapter;
    ArrayList<Categories> categoriesArrayList;
    String code= "";
    SessionManagement sessionManagement;
    HashMap user;
    String selected = null;
    HomeReceiver homeReceiver;
    String id,deliID,lat,lng,str;
    View root;
    DeliveryBoyModel deliveryBoyModel;
    private String role,name,email,password;
    private int fromUpdate;
    private String roleFromLanding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        Tools.setSystemBarColor(getActivity(),R.color.colorprimary);

        /*Set icon and text in place of setting in nav header*/
        //MainDrawerBackActivity.tv_settings_drawer.setText("Orders");
       // MainDrawerBackActivity.iv_setting_drawer.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_feather_clipboard));

        /*Selected item in navigation menu list*/
        ((MainDrawerBackActivity)getActivity()).viewSelector(getString(R.string.orders));;

        MainDrawerBackActivity.ll_nav_title.setVisibility(View.VISIBLE);
        MainDrawerBackActivity.fab_online.setVisibility(View.GONE);

        appBarLayout = root.findViewById(R.id.appbars);
         tabLayout = root.findViewById(R.id.tab_layout);
        viewPager = root.findViewById(R.id.main_tab_pager);
        categoriesArrayList = new ArrayList<>();
        sessionManagement = SessionManagement.getInstance(getContext());
        user = sessionManagement.getUserDetails();

        homeReceiver =new HomeReceiver();
        selected = user.get(SessionManagement.KEY_SELECTED_TAB).toString();
        role = user.get(SessionManagement.KEY_USER_TYPE).toString();
        id=user.get(SessionManagement.KEY_ID).toString();
        tabAdapter = new TabAdapter(getChildFragmentManager());
        tabLayout.setSmoothScrollingEnabled(true);

        if(getArguments()!=null){
            deliID = getArguments().getString("id");
            name = getArguments().getString("userName");
            email = getArguments().getString("email");
            password = getArguments().getString("password");
            fromUpdate = getArguments().getInt("fromUpdate");
            lat = getArguments().getString("lat");

            str = getArguments().getString("deliveryBoy");

            roleFromLanding = getArguments().getString("roleFrom");
        }


        getCategory();

        if (categoriesArrayList != null)
        {
            for (int i = 0; i <categoriesArrayList.size() ; i++)
            {
                String titles = categoriesArrayList.get(i).getCategoryName();
                String code = categoriesArrayList.get(i).getCategorySName();

                if(role.equals("Admin")|| role.equals("Supplier")){
                    tabAdapter.addFragment(CategoryDeliveryBoyFragment.newInstance(code, i,fromUpdate,str,deliID), titles);
                }else {
                    tabAdapter.addFragment(CategoryRecyclerFragment.newInstance(code, i, id), titles);
                }
                tabAdapter.setFragmentTiles(i, titles);
            }
        }


        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (categoriesArrayList != null)
        {
            for (int i = 0; i <categoriesArrayList.size() ; i++)
            {
               tabLayout.getTabAt(i).setCustomView(R.layout.notification_badge);

            }

            for (int i = 0; i <categoriesArrayList.size() ; i++)
            {

                TextView textView =  tabLayout.getTabAt(i).getCustomView().findViewById(R.id.name_tv);
                textView.setText(categoriesArrayList.get(i).getCategoryName());
            }
        }


        if (!selected.isEmpty())
        {
            final TabLayout.Tab currentTab = tabLayout.getTabAt(Integer.parseInt(selected));
            sessionManagement.updateTABStatus("");
            if(currentTab != null)
            {
                tabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        currentTab.select();
                    }
                });
            }

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    String getcat_id = categoriesArrayList.get(tab.getPosition()).getCategorySName();
                    CategoryRecyclerFragment.newInstance(getcat_id , tab.getPosition(),id);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }

        if(roleFromLanding.equals( "Landing")) {
            //Back pressed
            root.setFocusableInTouchMode(true);
            root.requestFocus();
            root.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        if (((MainDrawerBackActivity) getActivity()) != null)
                            ((MainDrawerBackActivity) getActivity()).LoadLandingFragment();

                        return true;
                    }
                    return false;
                }
            });
        }


        return root;
    }

    private void getCategory() {

        Categories categories = new Categories("All","All");
        categoriesArrayList.add(categories);

        Categories categories1 = new Categories("Pending","8");
        categoriesArrayList.add(categories1);


        Categories categories3 = new Categories("Processing","4-11");
        categoriesArrayList.add(categories3);


        Categories categories4 = new Categories("OnTheWay","6");
        categoriesArrayList.add(categories4);

        Categories categories5 = new Categories("Delivered","7");
        categoriesArrayList.add(categories5);

        Categories categories6 = new Categories("Canceled","9");
        categoriesArrayList.add(categories6);
    }

    @Override
    public void onStart() {
        super.onStart();

        /*if(fromUpdate!=1){
            ((MainActivity) getActivity()).CheckDeliveryMan();
        }*/
        IntentFilter intentFilter= new IntentFilter("home");
        getActivity().registerReceiver(homeReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (homeReceiver!= null)
        {
            getActivity().unregisterReceiver(homeReceiver);
        }
    }

    public class HomeReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle!= null)
            {
                String count = bundle.getString("counts");
                Counts countsArrayList ;
                Type myTye = new TypeToken<Counts>(){}.getType();

                countsArrayList = new Gson().fromJson(count, myTye);

                if (countsArrayList!= null)
                {
                    for (int i = 0; i <categoriesArrayList.size() ; i++)
                    {

                        TextView textView =  tabLayout.getTabAt(i).getCustomView().findViewById(R.id.text);

                        if (i==0)
                        {
                            textView.setText(countsArrayList.getAllCount());
                        }
                        else  if (i==1)
                        {
                            textView.setText(countsArrayList.getPendingCount());

                        }
                        else  if (i==2)
                        {
                            textView.setText(countsArrayList.getProcessingCount());

                        } else  if (i==3)
                        {
                            textView.setText(countsArrayList.getOnTheWayCount());

                        } else  if (i==4)
                        {
                            textView.setText(countsArrayList.getDeliveredCount());

                        } else  if (i==5)
                        {
                            textView.setText(countsArrayList.getCanceledCount());

                        }

                    }
                }


            }
        }
    }
}