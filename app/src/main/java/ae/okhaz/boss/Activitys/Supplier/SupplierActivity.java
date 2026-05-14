package ae.okhaz.boss.Activitys.Supplier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import ae.okhaz.boss.Activitys.NotificationActivity;
import ae.okhaz.boss.Data.DashboardData;
import ae.okhaz.boss.Fragments.Admin.OrderFragment;
import ae.okhaz.boss.Fragments.Admin.SupplierHomeFragment;
import ae.okhaz.admin.R;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.Response.ResponseDailySalesDashboard;
import ae.okhaz.boss.rests.Response.ResponseDashboard;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    SessionManagement sessionManagement;
    Location currentlocation;
    //DrawerLayout drawer;
    private LocationManager locationManager;
    HashMap user;
    int fromUpdate;
    public static Toolbar toolbar;
    String name,pass,email;
    public static DrawerLayout drawer;
    public static TextView txt_mainOrder_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        sessionManagement = SessionManagement.getInstance(SupplierActivity.this);
        user=  sessionManagement.getUserDetails();
        getDashboardData();
        fromUpdate=getIntent().getIntExtra("fromUpdate",0);
        name=getIntent().getStringExtra("userName");
        email=getIntent().getStringExtra("email");
        pass=getIntent().getStringExtra("password");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Dashboard");
        txt_mainOrder_number = findViewById(R.id.txt_mainOrder_number);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setTitle("Dashboard");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(drawer);

            }
        });

        View headerView=navigationView.getHeaderView(0);
        CircleImageView circleImageView=headerView.findViewById(R.id.imageView);
        TextView textView=headerView.findViewById(R.id.delivery_boy_nm_tv);

        textView.setText(user.get(SessionManagement.KEY_USER_NAME).toString());

       // Glide.with(this).load(user.get(SessionManagement).toString())

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            // Permission already Granted
            //Do your work here
            //Perform operations here only which requires permission
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            String locationProvider = locationManager.NETWORK_PROVIDER;
            currentlocation = locationManager.getLastKnownLocation(locationProvider);
            if(currentlocation!=null) {
                updateLatLong(String.valueOf(currentlocation.getLatitude()), String.valueOf(currentlocation.getLongitude()));
            }

            loadFragment(new SupplierHomeFragment());
            //loadFragment(new MapsFragment());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_supplier_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.notification) {
            startActivity(new Intent(SupplierActivity.this, NotificationActivity.class));
            // Toast.makeText(this, "This is notification", Toast.LENGTH_LONG).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setTitle("Dashboard");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.nav_deli_boy) {
            // Handle the camera action
            startActivity(new Intent(SupplierActivity.this,DeliveryBoyListActivity.class));
            return true;
        }
      else if (id == R.id.nav_deli_logout) {
            // Handle the camera action
           logout();
            return true;
        }
      else if(id==R.id.nav_order){
          loadOrder(new OrderFragment());
          drawer.closeDrawers();

          return true;
        }
        return false;
    }

    public void loadFragment(Fragment fragment) {
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_supplier_fragment, fragment)
                .commitAllowingStateLoss();
    }

    public void loadOrder(Fragment fragment)
    {
        Bundle bundle=new Bundle();
        bundle.putInt("position",0);
        fragment.setArguments(bundle);
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_supplier_fragment, fragment)
                .commitAllowingStateLoss();
    }

    @SuppressLint("NewApi")
    private void logout() {

        final SweetAlertDialog alertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
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
        btn1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorprimarydark)));
        btn1.setGravity(Gravity.CENTER);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismissWithAnimation();
                sessionManagement.logoutUser();
                drawer.closeDrawers();
                finish();
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
                loadFragment(new SupplierHomeFragment());

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
            UserId="";
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
                     Toast.makeText(SupplierActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SupplierActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Dashboard");
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              drawer.openDrawer(Gravity.LEFT);

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
}