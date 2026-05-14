package ae.okhaz.boss.Fragments.Admin;

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

import ae.okhaz.boss.Adapter.Admin.AdminTabAdapter;
import ae.okhaz.boss.Model.Categories;
import ae.okhaz.boss.Model.CountOrder;
import ae.okhaz.admin.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;
import ae.okhaz.boss.view.fragments.DashboardFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderFragment extends Fragment {

    AppBarLayout appBarLayout;
    TabLayout tabLayout;
    ViewPager viewPager;
    AdminTabAdapter tabAdapter;
    ArrayList<Categories> categoriesArrayList;
    String code= "";
    SessionManagement sessionManagement;
    HashMap user;
    String selected = null;
    HomeReceiver homeReceiver;
    int position=0;
    String from="";
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_order, container, false);
        Tools.setSystemBarColor(getActivity(),R.color.colorprimary);

        /*Set icon and text in place of setting in nav header*/
        MainDrawerBackActivity.ll_nav_title.setVisibility(View.VISIBLE);
       // MainDrawerBackActivity.tv_settings_drawer.setText(getString(R.string.orders));
      //  MainDrawerBackActivity.iv_setting_drawer.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_feather_clipboard));

        /*Selected item in navigation menu list*/
        ((MainDrawerBackActivity)getActivity()).viewSelector(getString(R.string.orders));

        appBarLayout = root.findViewById(R.id.appbars);
        tabLayout = root.findViewById(R.id.tab_layout);
        viewPager = root.findViewById(R.id.main_tab_pager);
        categoriesArrayList = new ArrayList<>();
        sessionManagement = SessionManagement.getInstance(getContext());
        user = sessionManagement.getUserDetails();

        homeReceiver =new HomeReceiver();
        selected = user.get(SessionManagement.KEY_SELECTED_TAB).toString();

        tabAdapter = new AdminTabAdapter(getChildFragmentManager());

        tabLayout.setSmoothScrollingEnabled(true);
        position=getArguments().getInt("position");

        /*get argument from OrderStatusAdapter*/
        if(getArguments().getString("fromFragment")!= null){
            from = getArguments().getString("fromFragment");
        }

        viewPager.setOffscreenPageLimit(1);
        getCategory();

        if (categoriesArrayList != null)
        {
            for (int i = 0; i <categoriesArrayList.size() ; i++)
            {
                String titles = categoriesArrayList.get(i).getCategoryName();
                String code = categoriesArrayList.get(i).getCategorySName();
                tabAdapter.addFragment(CategoryAdminFragment.newInstance(code, i),titles);
                tabAdapter.setFragmentTiles(i,titles);
            }
        }

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (position>0){
            viewPager.setCurrentItem(position);
        }
        else {
            viewPager.setCurrentItem(0);
        }


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
                    CategoryAdminFragment.newInstance(getcat_id , tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }

        //Back pressed
        root.setFocusableInTouchMode(true);
        root.requestFocus();
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if(!from.equals("Dashboard")){
                        if(((MainDrawerBackActivity)getActivity())!=null)
                            ((MainDrawerBackActivity)getActivity()).LoadLandingFragment();
                    }else{
                        ((MainDrawerBackActivity)getActivity()).setToolbarAndLoadFragment("Dashboard",new DashboardFragment());
                    }
                    return true;
                }
                return false;
            }
        });



        return root;
    }



    private void getCategory() {


        Categories categories = new Categories("All","All");
        categoriesArrayList.add(categories);

        Categories categories1 = new Categories("Order Placed","1");
        categoriesArrayList.add(categories1);

        Categories categories2 = new Categories("Pending","2");
        categoriesArrayList.add(categories2);

        Categories categories3 = new Categories("Accepted","3");
        categoriesArrayList.add(categories3);

        Categories categories4 = new Categories("Ready For PickUp","4");
        categoriesArrayList.add(categories4);

        Categories categories5 = new Categories("Oder In Progress","5");
        categoriesArrayList.add(categories5);

        Categories categories7 = new Categories("OnTheWay","6");
        categoriesArrayList.add(categories7);

        Categories categories8 = new Categories("Delivered","7");
        categoriesArrayList.add(categories8);

        Categories categories10 = new Categories("AssignDM","8");
        categoriesArrayList.add(categories10);

        Categories categories9 = new Categories("Canceled","9");
        categoriesArrayList.add(categories9);

        Categories categories11 = new Categories("Order Rejected","10");
        categoriesArrayList.add(categories11);

        Categories categories6 = new Categories("Processing","11");
        categoriesArrayList.add(categories6);

        Categories categories12 = new Categories("Awaiting Payment","12");
        categoriesArrayList.add(categories12);

        Categories categories13 = new Categories("Awaiting Fulfillment","13");
        categoriesArrayList.add(categories13);

        Categories categories14 = new Categories("Awaiting Shipment","14");
        categoriesArrayList.add(categories14);

        Categories categories15 = new Categories("High Risk","15");
        categoriesArrayList.add(categories15);

        Categories categories16 = new Categories("Pre-Orders","16");
        categoriesArrayList.add(categories16);

        Categories categories17 = new Categories("Incomplete","17");
        categoriesArrayList.add(categories17);

        Categories categories18 = new Categories("Archived","18");
        categoriesArrayList.add(categories18);

        Categories categories19 = new Categories("Refunded","19");
        categoriesArrayList.add(categories19);

        Categories categories20 = new Categories("Shipped","20");
        categoriesArrayList.add(categories20);

        Categories categories21 = new Categories("Partial Confirmed","21");
        categoriesArrayList.add(categories21);

    }

    @Override
    public void onStart() {
        super.onStart();
      //  ((MainActivity)getActivity()).CheckDeliveryMan();

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

    /*public void onRadioButtonClick(View view) {
        CategoryAdminFragment categoryAdminFragment=CategoryAdminFragment.newInstance(code, 0);
        categoryAdminFragment.onRadioButtonClick(view);
    }*/

    public class HomeReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle!= null)
            {
                String count = bundle.getString("counts");
                ArrayList<CountOrder> countsArrayList ;
               // Type myTye = new TypeToken<CountOrder>(){}.getType();
                //countsArrayList = new Gson().fromJson(count, myTye);
                countsArrayList = new ArrayList<>();
                try {
                    JSONArray jsonArray=new JSONArray(count);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        CountOrder countOrder=new CountOrder();
                        countOrder.setOrderStatusID(explrObject.getInt("orderStatusID"));
                        countOrder.setOrderStatusName(explrObject.getString("orderStatusName"));
                        countOrder.setOrdercount(explrObject.getString("ordercount"));
                        countsArrayList.add(countOrder);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (countsArrayList!= null)
                {
                    for (int i = 0; i <categoriesArrayList.size() ; i++)
                    {

                        TextView textView =  tabLayout.getTabAt(i).getCustomView().findViewById(R.id.text);

                        textView.setText(countsArrayList.get(i).getOrdercount());

                        /*if (i==0)
                        {
                            textView.setText(countsArrayList.get(0).getOrdercount());
                        } else  if (i==1)
                        {
                            textView.setText(countsArrayList.get(1).getOrdercount());

                        } else  if (i==2)
                        {
                            textView.setText(countsArrayList.get(2).getOrdercount());

                        } else  if (i==3)
                        {
                            textView.setText(countsArrayList.get(3).getOrdercount());

                        } else  if (i==4)
                        {
                            textView.setText(countsArrayList.get(4).getOrdercount());

                        } else  if (i==5)
                        {
                            textView.setText(countsArrayList.get(5).getOrdercount());

                        } else  if (i==6)
                        {
                            textView.setText(countsArrayList.get(6).getOrdercount()); } else  if (i==7)
                        {
                            textView.setText(countsArrayList.get(7).getOrdercount());
                        } else  if (i==8)
                        {
                            textView.setText(countsArrayList.get(8).getOrdercount());
                        } else  if (i==9)
                        {
                            textView.setText(countsArrayList.get(9).getOrdercount());
                        } else  if (i==10)
                        {
                            textView.setText(countsArrayList.get(10).getOrdercount());
                        } else  if (i==11)
                        {
                            textView.setText(countsArrayList.get(11).getOrdercount());
                        } else  if (i==12)
                        {
                            textView.setText(countsArrayList.get(12).getOrdercount());
                        } else  if (i==13)
                        {
                            textView.setText(countsArrayList.get(13).getOrdercount());
                        } else  if (i==14)
                        {
                            textView.setText(countsArrayList.get(14).getOrdercount());
                        } else  if (i==15)
                        {
                            textView.setText(countsArrayList.get(15).getOrdercount());
                        } else  if (i==16)
                        {
                            textView.setText(countsArrayList.get(16).getOrdercount());
                        } else  if (i==17)
                        {
                            textView.setText(countsArrayList.get(17).getOrdercount());
                        } else  if (i==18)
                        {
                            textView.setText(countsArrayList.get(18).getOrdercount());
                        } else  if (i==19)
                        {
                            textView.setText(countsArrayList.get(19).getOrdercount());
                        } else  if (i==20)
                        {
                            textView.setText(countsArrayList.get(20).getOrdercount());
                        } else  if (i==21)
                        {
                            textView.setText(countsArrayList.get(21).getOrdercount());
                        } else  if (i==22)
                        {
                            textView.setText(countsArrayList.get(22).getOrdercount());
                        }*/
                    }
                }


            }

        }

    }

    /*public void onSortRadioButtonClick(View view) {
        CategoryAdminFragment categoryAdminFragment=CategoryAdminFragment.newInstance("",0);
        categoryAdminFragment.onRadioButtonClick(view);
    }*/

}