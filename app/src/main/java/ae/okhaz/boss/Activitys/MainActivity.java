package ae.okhaz.boss.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.DetectConnection;
import ae.okhaz.boss.Utils.NetworkChangeReceiver;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.rests.Response.ResponseCheckStatus;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.Response.ResponseDeliverymanDetails;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReciverListner {

    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    boolean flag= false;
    public FloatingActionButton fab, location_fab;
    public  static Toolbar toolbar;
    NetworkChangeReceiver networkChangeReceive;
    Location currentlocation;
    AlertDialog no_internets;
    private static final int PERMISSION_CODE = 101;
    String[] permissions_all = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_CODE = 101;
    LocationManager locationManager;
    boolean isGpsLocation;
    NavController navController;
    public static MenuItem online, offline,navigation;
    String types="";
    SessionManagement sessionManagement;
    HashMap user;
    public static TextView txt_mainOrder_number;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkChangeReceive = new NetworkChangeReceiver();
        setContentView(R.layout.activity_main);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getLocation();
         fab = findViewById(R.id.fab);
         fab.setVisibility(View.GONE);
        location_fab = findViewById(R.id.location_fab);
        txt_mainOrder_number = findViewById(R.id.txt_mainOrder_number);

        location_fab.setImageResource(R.drawable.nears);
        location_fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag)
                {
                    flag = false;
                    fab.setImageResource(R.drawable.offline);
                    Showsnacks("Now you are on offline mode");
                }
                else {
                    flag  = true;
                    fab.setImageResource(R.drawable.online);
                    Showsnacks("Now you are on online mode");
                }
            }
        });


         progressDialog = new  Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(this).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);
        progressDialog.show();

        sessionManagement = SessionManagement.getInstance(MainActivity.this);
        user=  sessionManagement.getUserDetails();

        sessionManagement.ClearSortandFilter();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_order, R.id.nav_profile, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_boy_nm_tv);
        navUsername.setText(user.get(SessionManagement.KEY_USER_NAME).toString());


        Menu menu = navigationView.getMenu();

        MenuItem logouts = menu.findItem(R.id.nav_logout);

        logouts.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                drawer.closeDrawers();
                logout();
                return false;
            }
        });

        navController.addOnDestinationChangedListener(
                new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller,
                                                     @NonNull NavDestination destination,
                                                     @Nullable Bundle arguments) {

                        int id = destination.getId();

                        if (id == R.id.nav_order) {
                            // nothing special
                        } else if (id == R.id.nav_order_details) {
                            toolbar.setNavigationIcon(R.drawable.dual_arrows);
                        } else if (id == R.id.nav_maps) {
                            toolbar.setNavigationIcon(R.drawable.dual_arrows);
                        }
                    }
                }
        );

        location_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.goto_map);
            }
        });

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

        CheckDeliveryMan();
    }

    private void UpdateToken(String token) {

        String id = user.get(SessionManagement.KEY_DELIVERY_MAN).toString();
        ServiceGenerator.getDelivery().updateFirebaseId(id, token).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        Log.d(TAG, "onResponse: "+response.body().getMessage());
                    }
                    else {
                        Log.d(TAG, "onResponse: "+response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                Log.d(TAG, "onResponse: "+t.getMessage());

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        online = menu.findItem(R.id.action_online);
        offline = menu.findItem(R.id.action_offline);
        navigation = menu.findItem(R.id.action_tracking);
        online.setVisible(false);
        navigation.setVisible(false);
        offline.setVisible(false);

        if (user.get(SessionManagement.KEY_LOIGIN_STATUS).toString().equals("0"))
        {
            offline.setVisible(true);
            online.setVisible(false);
            navigation.setVisible(false);

        }
        else if (user.get(SessionManagement.KEY_LOIGIN_STATUS).toString().equals("1"))
        {
            online.setVisible(true);
            navigation.setVisible(false);
            offline.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Action goes here
            navController.navigate(R.id.nav_slideshow);
            return true;

        } else if (id == R.id.action_online) {
            // Action goes here
            // Showsnacks("Now you are on online mode");
            UpdateStatus("0");
            return true;

        } else if (id == R.id.action_offline) {
            // Action goes here
            // Showsnacks("Now you are on offline mode");
            UpdateStatus("1");
            return true;

        } else if (id == R.id.action_tracking) {
            navController.navigate(R.id.goto_map);
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void UpdateStatus(String s) {
        /*ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();*/
        ///Tools.loading(MainActivity.this).show();


        String id = user.get(SessionManagement.KEY_DELIVERY_MAN).toString();
        String branchcode = user.get(SessionManagement.KEY_BRANCH_ID).toString();
        String aid = user.get(SessionManagement.KEY_AID).toString();

        ServiceGenerator.getDelivery().updateLoginStatus(id, s,branchcode, aid).enqueue(new Callback<ResponseCheckStatus>() {
            @Override
            public void onResponse(Call<ResponseCheckStatus> call, Response<ResponseCheckStatus> response) {
                progressDialog.dismiss();
                //Tools.loading(MainActivity.this).dismiss();
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {

                        if (response.body().getResult().isOnlineStatus()){
                            sessionManagement.updateLoginStatus(s);
                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            if (s.equals("0"))
                            {
                                offline.setVisible(true);
                                online.setVisible(false);
                            }
                            else if (s.equals("1")){
                                offline.setVisible(false);
                                online.setVisible(true);
                            }
                        }
                        else {

                            Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "please try again...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCheckStatus> call, Throwable t) {
                progressDialog.dismiss();
               // Tools.loading(MainActivity.this).dismiss();
                Toast.makeText(MainActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void Showsnacks(String msg)
    {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void ShowFab()
    {
        fab.setVisibility(View.VISIBLE);
    }

    public void HideFab()
    {
        fab.setVisibility(View.GONE);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String orderItemId=data.getStringExtra("orderItemId");
            int position=data.getIntExtra("position",0);
           // Toast.makeText(this, "QR SCANNING SUCCESSFULLY"+result.getContents().toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent("scanned");
            //result.getContents()
            intent.putExtra("datas", result.getContents().toString());
            sendBroadcast(intent);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceive, intentFilter);
        setConnectionListners(this);
    }

    @Override
    public void OnNetworkChange(boolean isConnected) {

        if (isConnected)
        {
            if (no_internets != null)
            {
                if (no_internets.isShowing())
                {
                    no_internets.dismiss();
                }
            }
        }
        else
        {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater factory = LayoutInflater.from(MainActivity.this);
            final View viewsones = factory.inflate(R.layout.item_no_internet, null);
            alertDialog.setCancelable(false);
            alertDialog.setView(viewsones);
            no_internets = alertDialog.create();
            no_internets.show();

            ImageView iv = viewsones.findViewById(R.id.no_internet_iv);
            Button retry_btn = viewsones.findViewById(R.id.retry_btn);


            retry_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DetectConnection.checkInternetConnection(MainActivity.this))
                    {
                        if (no_internets != null)
                        {
                            if (no_internets.isShowing())
                            {
                                no_internets.dismiss();
                            }
                        }
                    }
                }
            });

            Glide.with(getBaseContext())
                    .load(R.drawable.no_internet_connection)
                    .into(iv);
        }

    }


    public void setConnectionListners(NetworkChangeReceiver.ConnectivityReciverListner listners)
    {
        NetworkChangeReceiver.connectivityReciverListner = listners;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (networkChangeReceive!= null)
        {
            unregisterReceiver(networkChangeReceive);
        }
    }

    public void ShowsLoction()
    {

        location_fab.setVisibility(View.VISIBLE);

    }

    public void HideLocations()
    {

        location_fab.setVisibility(View.GONE);

    }

    public void HideAll()
    {
        fab.setVisibility(View.GONE);
        location_fab.setVisibility(View.GONE);
    }


    private void getLocation() {

        if(Build.VERSION.SDK_INT>=23){
            if(checkPermission()){
                getDeviceLocation();
            }
            else{
                requestPermission();
            }
        }
        else{
            getDeviceLocation();
        }
    }

    @SuppressLint("NewApi")
    private void requestPermission() {
        requestPermissions(permissions_all,PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {
                        getDeviceLocation();
                    }

                } else {

                    Toast.makeText(MainActivity.this, "Permission Failed", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }


    private boolean checkPermission() {
        for(int i=0;i<permissions_all.length;i++){
            int result= ContextCompat.checkSelfPermission(MainActivity.this,permissions_all[i]);
            if(result== PackageManager.PERMISSION_GRANTED){return true;
            }
            else {
                return false;
            }
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {

        locationManager= (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGpsLocation=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        String locationProvider = locationManager.NETWORK_PROVIDER;
        currentlocation = locationManager.getLastKnownLocation(locationProvider);
        if(currentlocation!=null) {
            updateLatLong(String.valueOf(currentlocation.getLatitude()), String.valueOf(currentlocation.getLongitude()));
        }
//        isNetworklocation=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(!isGpsLocation ){
            showSettingForLocation();
//            fetchLastLocation();
        }
        else{


        }
    }


    public void updateLatLong(String lat,String lon){

        String id;
        SessionManagement sessionManagement;
        sessionManagement = SessionManagement.getInstance(MainActivity.this);
        HashMap user;
        user=sessionManagement.getUserDetails();
        id=user.get(SessionManagement.KEY_ID).toString();
        if(id!=null){
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
        Log.e(TAG, "updateLatLong: "+id );



    }


    private void showSettingForLocation() {
        android.app.AlertDialog.Builder al=new android.app.AlertDialog.Builder(MainActivity.this
        );
        al.setTitle("Location Not Enabled!");
        al.setMessage("Enable Location ?");
        al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        });
        al.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        al.show();
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
        Button btn = (Button) alertDialog.findViewById(cn.pedant.SweetAlert.R.id.confirm_button);
        btn.setText("Logout");


        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(300, 130);
        layoutParams.setMargins(10,0,10,0);
        btn.setLayoutParams(layoutParams);
        btn.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorprimary)));
        btn.setGravity(Gravity.CENTER);


        Button btn1 = (Button) alertDialog.findViewById(cn.pedant.SweetAlert.R.id.cancel_button);
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


            SweetAlertDialog alertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
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


}