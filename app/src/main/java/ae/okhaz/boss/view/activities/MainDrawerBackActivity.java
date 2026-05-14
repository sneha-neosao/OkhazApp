package ae.okhaz.boss.view.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import ae.okhaz.boss.Activitys.LoginActivity;
import ae.okhaz.boss.Activitys.MainActivity;
import ae.okhaz.boss.Activitys.NotificationActivity;
import ae.okhaz.boss.Data.DashboardData;
import ae.okhaz.boss.Fragments.Admin.CategoryAdminFragment;
import ae.okhaz.boss.Fragments.Admin.OrderFragment;
import ae.okhaz.boss.Fragments.HomeFragment;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Model.Counts;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.rests.Response.ResponseCheckStatus;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.Response.ResponseDailySalesDashboard;
import ae.okhaz.boss.rests.Response.ResponseDashboard;
import ae.okhaz.boss.rests.Response.ResponseDeliverymanDetails;
import ae.okhaz.boss.rests.Response.ResponseOrderList;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.fragments.DashboardFragment;
import ae.okhaz.boss.view.fragments.DeliveryBoyFragment;
import ae.okhaz.boss.view.fragments.LandingFragment;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import ae.okhaz.boss.view.fragments.SupplierListFragment;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainDrawerBackActivity extends AppCompatActivity {

    public CardView content_view,content_view1;
    public static boolean isHide = true;
    private LinearLayout nav_view;
    public static LinearLayout ll_nav_title;
    public ImageView imageView_admin;
    public static LinearLayout ll_home,ll_supplier_list,ll_dashboard,ll_orders,ll_delivery_boy,ll_faq,ll_contact,ll_locate,ll_about,ll_logout,toolbar_nav;
    public static ImageView search_iv,iv_toolbar_logo;
    LinearLayout linearLayout;

    LandingFragment landingFragment;

    public static TextView tv_toolbar_title,tv_toolbar_morder;

    public static int menuClickFlag = 0;

    SessionManagement sessionManagement;
    Location currentlocation;
    private LocationManager locationManager;
    HashMap user;
    int fromUpdate;
    public static Toolbar toolbar;
    String name,pass,email;
    public static TextView txt_mainOrder_number,tvUserName,tvUserContact,tvUserMail,tv_settings_drawer;

    public static ImageView img_menu,iv_setting_drawer,search_supplier;

    String role;

    public static View view_home, view_dashboard, view_orders, view_delivery_boy,view_supplier_list;
    public  static LinearLayout ll_login_drawer;
    public static EditText edt_search;

    public static ImageView fab_online;
    boolean flag= false;
    ProgressBar order_pb;

    SweetAlertDialog alertDialog;
    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer_back);
        landingFragment = new LandingFragment();
        init();

        sessionManagement = SessionManagement.getInstance(MainDrawerBackActivity.this);
        user=  sessionManagement.getUserDetails();

        FirebaseApp.initializeApp(this);
        role=user.get(SessionManagement.KEY_USER_TYPE).toString();


        progressDialog = new  Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(this).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);


        if (user.get(SessionManagement.KEY_LOIGIN_STATUS).toString().equals("0"))
        {
            flag = false;

            fab_online.setImageResource(R.drawable.offline);

        }
        else if (user.get(SessionManagement.KEY_LOIGIN_STATUS).toString().equals("1"))
        {
            flag  = true;
            fab_online.setImageResource(R.drawable.online);
        }

        fab_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag)
                {
                    //flag = false;
                    //fab_online.setImageResource(R.drawable.offline);
                    UpdateStatus("0");
                    //Showsnacks("Now you are on offline mode");
                }
                else {
                    // flag  = true;
                    // fab_online.setImageResource(R.drawable.online);
                    UpdateStatus("1");
                    //Showsnacks("Now you are on online mode");
                }
            }
        });

        if (role.equals("Delivery Man")){
            this.ll_delivery_boy.setVisibility(View.GONE);
            this.ll_dashboard.setVisibility(View.VISIBLE);
            this.fab_online.setVisibility(View.VISIBLE);
        }

        if(sessionManagement.isLoggedIn()){
            if (role.equals("Admin")){
                tvUserContact.setVisibility(View.GONE);
            }
            else
                {
                tvUserContact.setVisibility(View.VISIBLE);
            }
            tvUserName.setText(user.get(SessionManagement.KEY_USER_NAME).toString());
            tvUserMail.setText(user.get(SessionManagement.KEY_EMAIL).toString());
            tvUserContact.setText(user.get(SessionManagement.KEY_USER_TYPE).toString());
            ll_login_drawer.setVisibility(View.GONE);

            getDashboardData();

        }else{
            tvUserName.setText("Guest");
            tvUserContact.setText("");
            tvUserContact.setVisibility(View.GONE);
            tvUserMail.setVisibility(View.GONE);
            ll_login_drawer.setVisibility(View.VISIBLE);
            ll_logout.setVisibility(View.INVISIBLE);
            ll_login_drawer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goTOLogin();
                }
            });
        }

        fromUpdate=getIntent().getIntExtra("fromUpdate",0);
        name=getIntent().getStringExtra("userName");
        email=getIntent().getStringExtra("email");
        pass=getIntent().getStringExtra("password");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            String locationProvider = locationManager.NETWORK_PROVIDER;
            currentlocation = locationManager.getLastKnownLocation(locationProvider);
            if(currentlocation!=null) {
                if(sessionManagement.isLoggedIn()) {
                    updateLatLong(String.valueOf(currentlocation.getLatitude()), String.valueOf(currentlocation.getLongitude()));
                }
            }

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


        /*FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful())
                {
                    String token = task.getResult().getToken();

                    Log.d("tokens", token);
                    UpdateToken(token);
                }

            }
        });*/

        onClick();
        initNavigationMenu();



    }

    private void onClick() {

        search_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainDrawerBackActivity.this, NotificationActivity.class));
            }
        });


        ll_faq.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //startActivity(new Intent(MainDrawerBackActivity.this, FaqListActivity.class));
            }
        });
        ll_contact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //startActivity(new Intent(MainDrawerBackActivity.this, FaqListActivity.class));
            }
        });
        ll_locate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               // startActivity(new Intent(MainDrawerBackActivity.this, DashBoardActivity.class));
            }
        });
        ll_about.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //startActivity(new Intent(MainDrawerBackActivity.this, DashBoardActivity.class));
            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //startActivity(new Intent(MainDrawerBackActivity.this, DashBoardActivity.class));
                logout();
            }
        });


        this.imageView_admin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                    if (role.equals("Delivery Man")) {
                        startActivity(new Intent(MainDrawerBackActivity.this, EditDeliveryBoyActivity.class)
                                .putExtra("id", user.get(SessionManagement.KEY_ID).toString())
                                .putExtra("staffCode", user.get(SessionManagement.KEY_CODE).toString())
                                .putExtra("fromMain", 1));
                    }// else
                      //  startActivity(new Intent(MainDrawerBackActivity.this, AdminDetailActivity.class));
                }
                else{
                    goTOLogin();
                }
            }
        });

        if (sessionManagement.isLoggedIn()) {
            CheckDeliveryMan();
        }

    }

    private void init(){
        ll_home = findViewById(R.id.ll_home);
        ll_supplier_list = findViewById(R.id.ll_supplier_list);
        ll_dashboard = findViewById(R.id.ll_dashboard);
        ll_orders = findViewById(R.id.ll_orders);
        ll_delivery_boy = findViewById(R.id.ll_delivery_boy);
        ll_nav_title = findViewById(R.id.ll_nav_title);
        ll_nav_title.setVisibility(View.GONE);

        ll_faq = findViewById(R.id.ll_faq);
        ll_contact = findViewById(R.id.ll_contact);
        ll_locate = findViewById(R.id.ll_locate);
        ll_about = findViewById(R.id.ll_about);
        ll_logout = findViewById(R.id.ll_logout);
        toolbar_nav = findViewById(R.id.toolbar_nav);
        search_iv=findViewById(R.id.search_iv);
        fab_online=findViewById(R.id.fab);

        search_supplier=findViewById(R.id.search_supplier);
        edt_search=findViewById(R.id.edt_search);

        img_menu = (ImageView) findViewById(R.id.img_menu);

        iv_setting_drawer = (ImageView) findViewById(R.id.iv_setting_drawer);
        tv_settings_drawer=findViewById(R.id.tv_settings_drawer);

        iv_toolbar_logo = (ImageView) findViewById(R.id.iv_toolbar_logo);
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_morder = (TextView) findViewById(R.id.tv_toolbar_morder);
        tv_toolbar_morder.setVisibility(View.GONE);

        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserContact = (TextView) findViewById(R.id.tv_user_contact);
        tvUserMail = (TextView) findViewById(R.id.tv_user_mail);

        linearLayout=findViewById(R.id.ll_item_faq);
        imageView_admin=findViewById(R.id.imageView_admin);

        view_home =findViewById(R.id.view_home);
        view_dashboard =findViewById(R.id.view_dashboard);
        view_orders =findViewById(R.id.view_orders);
        view_delivery_boy =findViewById(R.id.view_delivery_boy);
        view_supplier_list =findViewById(R.id.view_supplier_list);

        ll_login_drawer =findViewById(R.id.ll_login_drawer);
        order_pb = findViewById(R.id.order_pb);
    }

    private void initNavigationMenu() {
        this.content_view = (CardView) findViewById(R.id.main_content);
        this.content_view1 = (CardView) findViewById(R.id.main_content1);
        this.nav_view = (LinearLayout) findViewById(R.id.nav_view);

        loadFragment(this.landingFragment);

        this.content_view.post(new Runnable() {
            public void run() {
                content_view.setTranslationX(0.0f);
                content_view.setRadius((float)dpToPx(getApplicationContext(), 0));
            }
        });

        this.tv_settings_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sessionManagement.isLoggedIn()) {
                    if (role.equals("Delivery Man")) {
                        startActivity(new Intent(MainDrawerBackActivity.this, EditDeliveryBoyActivity.class)
                                .putExtra("id", user.get(SessionManagement.KEY_ID).toString())
                                .putExtra("staffCode", user.get(SessionManagement.KEY_CODE).toString())
                                .putExtra("fromMain", 1));
                    } //else
                        //startActivity(new Intent(MainDrawerBackActivity.this, AdminDetailActivity.class));
                }
                else{
                    goTOLogin();
                }
            }
        });

        this.img_menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isHide) {
                    MainDrawerBackActivity menuDrawerBack = MainDrawerBackActivity.this;
                    menuDrawerBack.showMenu(menuDrawerBack.content_view,menuDrawerBack.content_view1);
                    return;
                }
                MainDrawerBackActivity menuDrawerBack2 = MainDrawerBackActivity.this;
                menuDrawerBack2.hideMenu(menuDrawerBack2.content_view,menuDrawerBack2.content_view1);
            }
        });

        this.ll_home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewSelector(getString(R.string.home));
                LoadLandingFragment();
            }
        });
        this.ll_dashboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewSelector(getString(R.string.dashboard));
                if(sessionManagement.isLoggedIn()){
                    setToolbarAndLoadFragment("Dashboard",new DashboardFragment());
                }else{
                    goTOLogin();
                }

            }
        });

        this.ll_supplier_list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewSelector("Supplier List");
                if(sessionManagement.isLoggedIn()){
                    setToolbarAndLoadFragment("Supplier List",new SupplierListFragment());
                }else{
                    goTOLogin();
                }

            }
        });

        this.ll_orders.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewSelector(getString(R.string.orders));
                if(sessionManagement.isLoggedIn()){
                    if (role.equals("Delivery Man")){
                        loadOrder("Orders",new HomeFragment(),"Landing");
                    }
                    else {
                        loadOrder("Orders",new OrderFragment(),role);
                    }
                }else{
                    goTOLogin();
                }
            }
        });

        this.ll_delivery_boy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                viewSelector(getString(R.string.delivery_boy));
                if(sessionManagement.isLoggedIn()){
                    setToolbarAndLoadFragment("Delivery boy",new DeliveryBoyFragment());
                }else{
                    goTOLogin();
                }
            }
        });


       /* new Handler(getMainLooper()).postDelayed(new Runnable() {
            public void run() {
                MainDrawerBackActivity menuDrawerBack = MainDrawerBackActivity.this;
                menuDrawerBack.showMenu(menuDrawerBack.content_view);
            }
        }, 1000);*/
    }

    public ObjectAnimator hideMenu(View view,View view2) {
        this.isHide = true;
        view2.animate().scaleX(1.0f).scaleY(1.0f).translationX(0.0f).setDuration(300);
        view.animate().scaleX(1.0f).scaleY(1.0f).translationX(0.0f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                content_view.setRadius((float)dpToPx(getApplicationContext(), 0));
            }
        }).start();

        if(menuClickFlag != 1){
            setSystemBarColor(MainDrawerBackActivity.this,R.color.green_1);
        }else{
            setSystemBarColor(MainDrawerBackActivity.this,R.color.colorprimary);
        }
        return null;
    }

    public ObjectAnimator showMenu(View view,View view2) {
        setSystemBarColor(MainDrawerBackActivity.this,R.color.colorprimary);
        this.isHide = false;
        view2.animate().scaleX(0.9f).scaleY(0.78f).translationX(((float) view.getWidth()) / 2.1f).setDuration(300);
        view.animate().scaleX(0.9f).scaleY(0.85f).translationX(((float) view.getWidth()) / 1.8f).setDuration(300).setListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                content_view.setRadius((float)dpToPx(getApplicationContext(), 15));
            }
        }).start();
        return null;
    }

    public static int dpToPx(Context context, int i) {
        return Math.round(TypedValue.applyDimension(1, (float) i, context.getResources().getDisplayMetrics()));
    }

    public static void setSystemBarColor(Activity activity, int i) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(activity.getResources().getColor(i));
        }
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_supplier_fragment, fragment)
                .commitAllowingStateLoss();
    }

    public void setToolbarAndLoadFragment(String title, Fragment fragment){
        menuClickFlag = 1;

        toolbar_nav.setBackgroundColor(getResources().getColor(R.color.colorprimary));

        ll_nav_title.setVisibility(View.VISIBLE);
        iv_toolbar_logo.setVisibility(View.GONE);
        tv_toolbar_title.setVisibility(View.VISIBLE);
        tv_toolbar_title.setText(title);
        search_iv.setColorFilter(ContextCompat.getColor(MainDrawerBackActivity.this, android.R.color.white),
                PorterDuff.Mode.MULTIPLY);
        img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));

        loadFragment(fragment);
        MainDrawerBackActivity menuDrawerBack2 = MainDrawerBackActivity.this;
        menuDrawerBack2.hideMenu(menuDrawerBack2.content_view,menuDrawerBack2.content_view1);
    }

    public void LoadLandingFragment(){
        toolbar_nav.setBackgroundColor(getResources().getColor(R.color.green_1));
        setSystemBarColor(MainDrawerBackActivity.this,R.color.green_1);
        iv_toolbar_logo.setVisibility(View.VISIBLE);
        ll_nav_title.setVisibility(View.GONE);
        tv_toolbar_title.setVisibility(View.GONE);
        search_iv.setColorFilter(ContextCompat.getColor(MainDrawerBackActivity.this, android.R.color.black),
                PorterDuff.Mode.MULTIPLY);
        img_menu.setImageDrawable(getResources().getDrawable(R.drawable.okhaz_menu));

        loadFragment(new LandingFragment());
        MainDrawerBackActivity menuDrawerBack2 = MainDrawerBackActivity.this;
        menuDrawerBack2.hideMenu(menuDrawerBack2.content_view,menuDrawerBack2.content_view1);
    }


    private void UpdateToken(String token) {

        String id = user.get(SessionManagement.KEY_ID).toString();
        ServiceGenerator.getDelivery().updateFirebaseId(id, token).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        Log.d("", "onResponse: "+response.body().getMessage());
                    }
                    else {
                        Log.d("", "onResponse: "+response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                Log.d("", "onResponse: "+t.getMessage());

            }
        });
    }

    public void loadOrder(String title,Fragment fragment,String role)
    {
        menuClickFlag = 1;

        toolbar_nav.setBackgroundColor(getResources().getColor(R.color.colorprimary));

        iv_toolbar_logo.setVisibility(View.GONE);
        ll_nav_title.setVisibility(View.VISIBLE);
        tv_toolbar_title.setVisibility(View.VISIBLE);
        tv_toolbar_title.setText(title);
        search_iv.setColorFilter(ContextCompat.getColor(MainDrawerBackActivity.this, android.R.color.white),
                PorterDuff.Mode.MULTIPLY);
        img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));
        Bundle bundle=new Bundle();
        bundle.putInt("position",0);
        bundle.putString("roleFrom",role);
        fragment.setArguments(bundle);
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_supplier_fragment, fragment)
                .commitAllowingStateLoss();
        MainDrawerBackActivity menuDrawerBack2 = MainDrawerBackActivity.this;
        menuDrawerBack2.hideMenu(menuDrawerBack2.content_view,menuDrawerBack2.content_view1);
    }

    @SuppressLint("NewApi")
    private void logout() {
        Log.e("Role",role);
         alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("Are you sure you want to logout?");
        alertDialog.setCancelText("Cancel");
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
            }
        });
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setText("Logout");


        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(300, 130);
        layoutParams.setMargins(10,0,10,0);
        btn.setLayoutParams(layoutParams);
        btn.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorprimary)));
        btn.setGravity(Gravity.CENTER);


        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        LinearLayout.LayoutParams layoutParams1  = new LinearLayout.LayoutParams(300, 130);
        layoutParams1.setMargins(10,0,10,0);
        btn1.setLayoutParams(layoutParams1);
        btn1.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btn1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
        btn1.setGravity(Gravity.CENTER);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(role.equals("Delivery Man")){
                    UpdateStatus1("0");
                    /*String id=user.get(SessionManagement.KEY_ID).toString();
                    String branchID = user.get(SessionManagement.KEY_BRANCH_ID).toString();
                    String nams="8";
                    alertDialog.dismissWithAnimation();
                    getOrdersData(id,branchID,nams,"0","","","","");*/
                }else{
                    alertDialog.dismissWithAnimation();
                    sessionManagement.logoutUser();
                    // drawer.closeDrawers();
                    finish();
                }

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permission Granted
                //Do your work here
                //Perform operations here only which requires permission
                //super.onResume();
                loadFragment(new LandingFragment());

            }
        }
    }

    public void updateLatLong(String lat,String lon){

        String id;
        id=user.get(SessionManagement.KEY_ID).toString();

        ServiceGenerator.getDelivery().updateLatLng(id,lat,lon).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {

                }
                else
                {
                    // progressDialog.dismiss();
                    //  Toast.makeText(DeliveryBoyListActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {
                }
                else if (t instanceof IOException)
                {
                }
                else
                {
                    //progressDialog.dismiss();
                    //Toast.makeText(DeliveryBoyListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void getDashboardData()
    {
        String UserId,role;
        role=user.get(SessionManagement.KEY_USER_TYPE).toString();
        if(role.equals("Admin")) {
            //UserId="1";
            UserId=user.get(SessionManagement.KEY_ID).toString();
        }
        else {
            UserId=user.get(SessionManagement.KEY_ID).toString();
        }


        ServiceGenerator.getDelivery().getDashboardData(UserId).enqueue(new Callback<ResponseDashboard>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onResponse(Call<ResponseDashboard> call, Response<ResponseDashboard> response) {
                if (response.isSuccessful())
                {
                    if(response.body().isStatus()) {
                        ResponseDashboard.Result result = response.body().getResult();
                        DashboardData dashboardData=DashboardData.getInstance();
                        dashboardData.lifetimeSale=result.getLifetimeSale();
                        dashboardData.avgMonthlySale=result.getAvgMonthlySale();
                        dashboardData.todaySale=result.getTodaySale();
                        dashboardData.monthlySale=result.getMonthlySale();
                        dashboardData.yearlySale=result.getYearlySale();
                        dashboardData.weeklySale=result.getWeeklySale();
                    }
                }
                else
                {
                   // Toast.makeText(MainDrawerBackActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDashboard> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {

                }
                else if (t instanceof IOException)
                {

                }
                else
                {

                }
            }
        });

        ServiceGenerator.getDelivery().getDailySaleData(UserId).enqueue(new Callback<ResponseDailySalesDashboard>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onResponse(Call<ResponseDailySalesDashboard> call, Response<ResponseDailySalesDashboard> response) {
                if (response.isSuccessful())
                {
                    if(response.body().isStatus()) {
                        DashboardData dashboardData = DashboardData.getInstance();
                        dashboardData.dailySaleList = response.body().getResult();
                    }
                }
                else
                {
                    //Toast.makeText(MainDrawerBackActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDailySalesDashboard> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {

                }
                else if (t instanceof IOException)
                {

                }
                else
                {

                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            Toast.makeText(this, "QR SCANNING SUCCESSFULLY"+result.getContents().toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("scanned");
            intent.putExtra("datas", result.getContents().toString());
            sendBroadcast(intent);
        }

    }

    public void setTotal(int menuClickFlag) {
        this.menuClickFlag = menuClickFlag;
    }

    public void hideShowMenu(){
        if (isHide) {
            MainDrawerBackActivity menuDrawerBack = MainDrawerBackActivity.this;
            menuDrawerBack.showMenu(menuDrawerBack.content_view,menuDrawerBack.content_view1);
            return;
        }
        MainDrawerBackActivity menuDrawerBack2 = MainDrawerBackActivity.this;
        menuDrawerBack2.hideMenu(menuDrawerBack2.content_view,menuDrawerBack2.content_view1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
    public void viewSelector(String fragmentName){
        switch (fragmentName){
            case "Home":{
                view_home.setBackgroundColor(getResources().getColor(R.color.white));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_orders.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_delivery_boy.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_supplier_list.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                break;
            }
            case "Dashboard":{
                view_home.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.white));
                view_orders.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_delivery_boy.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_supplier_list.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                break;
            }
            case "Orders":{
                view_home.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_orders.setBackgroundColor(getResources().getColor(R.color.white));
                view_delivery_boy.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_supplier_list.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                break;
            }
            case "Delivery Boy":{
                view_home.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_orders.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_delivery_boy.setBackgroundColor(getResources().getColor(R.color.white));
                view_supplier_list.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                break;
            }

            case "Supplier List":{
                view_home.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_dashboard.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_orders.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_delivery_boy.setBackgroundColor(getResources().getColor(R.color.colorprimary));
                view_supplier_list.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            }
        }
        
    }

    public void goTOLogin(){
        Intent intent =new Intent(MainDrawerBackActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void Showsnacks(String msg)
    {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void UpdateStatus(String s) {
        /*ProgressDialog progressDialog = new ProgressDialog(MainDrawerBackActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);*/
        progressDialog.show();

        String id = user.get(SessionManagement.KEY_ID).toString();
        String branchcode = user.get(SessionManagement.KEY_BRANCH_ID).toString();
        String aid = user.get(SessionManagement.KEY_AID).toString();

        ServiceGenerator.getDelivery().updateLoginStatus(id, s,branchcode, aid).enqueue(new Callback<ResponseCheckStatus>() {
            @Override
            public void onResponse(Call<ResponseCheckStatus> call, Response<ResponseCheckStatus> response) {
                progressDialog.dismiss();
                Log.e("loginResponse",response.toString());
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        if (response.body().getResult().isOnlineStatus()){
                            sessionManagement.updateLoginStatus(s);
                            Toast.makeText(MainDrawerBackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            if (s.equals("0"))
                            {
                                flag = false;
                                fab_online.setImageResource(R.drawable.offline);

                            }
                            else if (s.equals("1"))
                            {
                                flag  = true;
                                fab_online.setImageResource(R.drawable.online);
                            }
                        }
                        else {

                            Toast.makeText(MainDrawerBackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainDrawerBackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(MainDrawerBackActivity.this, "please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCheckStatus> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainDrawerBackActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /*public void onRadioButtonClick(View view) {
        OrderFragment orderFragment=new OrderFragment();
        orderFragment.onRadioButtonClick(view);
    }*/
    private void getOrdersData(String deliveryman, String BranchCode, String OrderStatus, String offset, String condition, String OrderID, String startDate,  String endDate) {

        progressDialog.show();
        //order_pb.setVisibility(View.VISIBLE);
        //Tools.loading(MainDrawerBackActivity.this).show();

        String date_range= DashboardFragment.date_range;

        ServiceGenerator.getDelivery().getOrders(deliveryman, BranchCode, OrderStatus, offset, condition, OrderID, startDate, endDate,date_range).enqueue(new Callback<ResponseOrderList>() {
            @Override
            public void onResponse(Call<ResponseOrderList> call, Response<ResponseOrderList> response) {
                //order_pb.setVisibility(View.GONE);
                //Tools.loading(MainDrawerBackActivity.this).dismiss();
                progressDialog.dismiss();
                if (response.isSuccessful())
                {
                    assert response.body() != null;
                    if (response.body().isStatus())
                    {
                        Counts counts = response.body().getCounts();

                        String count = new Gson().toJson(counts);
                        Log.e("PendingCount",count);
                        if(Integer.parseInt(count)>0){
                            Toast.makeText(MainDrawerBackActivity.this,"Orders are pending",Toast.LENGTH_SHORT).show();
                        }else{
                            sessionManagement.logoutUser();
                            finish();
                        }

                    }
                    else {
                        //order_pb.setVisibility(View.GONE);
                        //Tools.loading(MainDrawerBackActivity.this).dismiss();
                        progressDialog.dismiss();
                        sessionManagement.logoutUser();
                        finish();
                        Log.e("PendingCount",response.body().getMessage());
                    }
                }
                else {
                    //order_pb.setVisibility(View.GONE);
                    //Tools.loading(MainDrawerBackActivity.this).dismiss();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseOrderList> call, Throwable t) {
               // order_pb.setVisibility(View.GONE);
                //Tools.loading(MainDrawerBackActivity.this).dismiss();
                progressDialog.dismiss();
            }
        });


    }

    public void CheckDeliveryMan()
    {
        String id = user.get(SessionManagement.KEY_DELIVERY_MAN).toString();
        ServiceGenerator.getDelivery().deliverymanDetails(id).enqueue(new Callback<ResponseDeliverymanDetails>() {
            @Override
            public void onResponse(Call<ResponseDeliverymanDetails> call, Response<ResponseDeliverymanDetails> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        if (response.body().getResult()[0].getLoginstatus().equals("0"))
                        {
                            ShowLoginAlerts();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDeliverymanDetails> call, Throwable t) {

            }
        });

    }

    @SuppressLint("NewApi")
    public void ShowLoginAlerts() {


        SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("This Delivery Man code has been blocked by admin");
        alertDialog.setConfirmText("OK");
        alertDialog.setCancelText("Logout");
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finishAffinity();
            }
        });

        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sessionManagement.logoutUser();

            }
        });
        alertDialog.show();
        Button btns = (Button) alertDialog.findViewById(R.id.confirm_button);
        LinearLayout.LayoutParams layoutParams1s  = new LinearLayout.LayoutParams(300, 130);
        layoutParams1s.setMargins(10,0,10,0);
        btns.setLayoutParams(layoutParams1s);
        btns.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btns.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorprimary)));
        btns.setGravity(Gravity.CENTER);


        Button btn1s = (Button) alertDialog.findViewById(R.id.cancel_button);
        LinearLayout.LayoutParams layoutParams12  = new LinearLayout.LayoutParams(300, 130);
        layoutParams12.setMargins(10,0,10,0);
        btn1s.setLayoutParams(layoutParams12);
        btn1s.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btn1s.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorprimarydark)));
        btn1s.setGravity(Gravity.CENTER);

    }

    private void UpdateStatus1(String s) {
        /*ProgressDialog progressDialog = new ProgressDialog(MainDrawerBackActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);*/
        progressDialog.show();

        String id = user.get(SessionManagement.KEY_ID).toString();
        String branchcode = user.get(SessionManagement.KEY_BRANCH_ID).toString();
        String aid = user.get(SessionManagement.KEY_AID).toString();

        ServiceGenerator.getDelivery().updateLoginStatus(id, s,branchcode, aid).enqueue(new Callback<ResponseCheckStatus>() {
            @Override
            public void onResponse(Call<ResponseCheckStatus> call, Response<ResponseCheckStatus> response) {
                progressDialog.dismiss();
                Log.e("loginResponse",response.body().toString());
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        if (response.body().getResult().isOnlineStatus()){
                            sessionManagement.updateLoginStatus(s);
                            Toast.makeText(MainDrawerBackActivity.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismissWithAnimation();
                            sessionManagement.logoutUser();
                            finish();

                        }
                        else {
                            Toast.makeText(MainDrawerBackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            sessionManagement.updateLoginStatus(s);
                            alertDialog.dismissWithAnimation();
                            sessionManagement.logoutUser();
                            finish();
                        }
                    }
                    else {
                        alertDialog.dismissWithAnimation();
                        Toast.makeText(MainDrawerBackActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(MainDrawerBackActivity.this, "please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCheckStatus> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainDrawerBackActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}