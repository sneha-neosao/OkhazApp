package ae.okhaz.boss.view.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ae.okhaz.boss.Activitys.NotificationActivity;
import ae.okhaz.boss.Model.DeliveryBoyModel;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.WorkaroundMapFragment;
import ae.okhaz.boss.rests.Response.ResponseDeliveryMan;
import ae.okhaz.boss.rests.ServiceGenerator;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;

import ae.okhaz.boss.sessionHandling.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryBoyDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {


    RelativeLayout map_layout;
    Location currentlocation;
    ScrollView mScrollView;
    LinearLayout ll_edit_delivery_boy;

    ImageView img_back_delivery_boy,img_noti_delivery_boy;

    LinearLayout ll_contact_delivery,ll_email_delivery,ll_info_delivery,ll_vehicle_delivery,ll_status_delivery;

    TextView tv_contact_delivery,tv_email_delivery,tv_info_delivery,tv_vehicle_delivery,tv_online_status_delivery;

    SwitchMaterial switch_delivery;

    private GoogleMap mMap;

    double latitude = 0.0, longitude = 0.0;


    DeliveryBoyModel deliveryBoyModel;
    String str,staffID,id;

    int fromAdd=0,fromUpdate;

    ImageView iv_image_delivery,iv_vehicle_delivery;
    TextView tv_name_delivery;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_details);


        fromAdd=getIntent().getIntExtra("fromAdd",0);
        fromUpdate=getIntent().getIntExtra("fromUpdate",0);
        str=getIntent().getStringExtra("deliveryBoy");

        map_layout = findViewById(R.id.map_layout);
        mScrollView = findViewById(R.id.main_scrollview);
        ll_edit_delivery_boy = findViewById(R.id.ll_edit_delivery_boy);
        img_back_delivery_boy = findViewById(R.id.img_back_delivery_boy);
        img_noti_delivery_boy = findViewById(R.id.img_noti_delivery_boy);

        ll_contact_delivery = findViewById(R.id.ll_contact_delivery);
        ll_email_delivery = findViewById(R.id.ll_email_delivery);
        ll_info_delivery = findViewById(R.id.ll_info_delivery);
        ll_vehicle_delivery = findViewById(R.id.ll_vehicle_delivery);
        ll_status_delivery = findViewById(R.id.ll_online_status_delivery);

        tv_contact_delivery = findViewById(R.id.tv_contact_delivery);
        tv_email_delivery = findViewById(R.id.tv_email_delivery);
        tv_info_delivery = findViewById(R.id.tv_info_delivery);
        tv_vehicle_delivery = findViewById(R.id.tv_vehicle_delivery);
        tv_online_status_delivery = findViewById(R.id.tv_online_status_delivery);

        iv_image_delivery = findViewById(R.id.iv_image_delivery);

        iv_vehicle_delivery = findViewById(R.id.iv_vehicle_delivery);
        tv_name_delivery = findViewById(R.id.tv_name_delivery);
        switch_delivery = findViewById(R.id.switch_delivery);

        img_back_delivery_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        img_noti_delivery_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeliveryBoyDetailsActivity.this, NotificationActivity.class));
            }
        });

        //Location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            currentlocation = locationManager.getLastKnownLocation(locationProvider);
            latitude = currentlocation.getLatitude();
            longitude = currentlocation.getLongitude();
            Log.e("lat",""+currentlocation.getLatitude());
            Log.e("long",""+currentlocation.getLongitude());
            map_layout.setVisibility(View.VISIBLE);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


        if (mMap == null) {
            WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }



       /* if(fromAdd==1)
        {
            ll_update.setVisibility(View.GONE);
            rl_map.setVisibility(View.GONE);
        }
        if(fromUpdate==1){
            btn_add_deliveryBoy.setVisibility(View.GONE);

            Type typeMyType = new TypeToken<DeliveryBoyModel>(){}.getType();
            deliveryBoyModel = new Gson().fromJson(str, typeMyType);

            id=deliveryBoyModel.getStaffId();

            // setData(deliveryBoyModel);
            getDetails(id);

        }*/

        Type typeMyType = new TypeToken<DeliveryBoyModel>(){}.getType();
        deliveryBoyModel = new Gson().fromJson(str, typeMyType);

        id=deliveryBoyModel.getStaffId();

        // setData(deliveryBoyModel);
        getDetails(id);

        ll_edit_delivery_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeliveryBoyDetailsActivity.this, EditDeliveryBoyActivity.class)
                        .putExtra("deliveryBoy",str)
                        .putExtra("staffCode", id)
                        .putExtra("fromUpdate",1));
            }
        });

    }



    public void getDetails(String id){

        ServiceGenerator.getDelivery().getdeliverymanbyid(id).enqueue(new Callback<ResponseDeliveryMan>() {
            @Override
            public void onResponse(Call<ResponseDeliveryMan> call, Response<ResponseDeliveryMan> response) {
                if (response.isSuccessful())
                {
                    // progressDialog.dismiss();
                    if (!response.body().isStatus()) {
                        //  progressDialog.dismiss();
                        Toast.makeText(DeliveryBoyDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DeliveryBoyModel deliveryBoyModel=response.body().getResult();
                        //setData(deliveryBoyModel);
                        Log.e("delivery",deliveryBoyModel.toString());

                        if(deliveryBoyModel.getFirstName()!= null){
                            tv_name_delivery.setText(deliveryBoyModel.getFirstName());
                        }


                        if(deliveryBoyModel.getImage()!= null){
                            Glide.with(DeliveryBoyDetailsActivity.this).load(deliveryBoyModel.getImage()).into(iv_image_delivery);
                        }

                        if(deliveryBoyModel.getMobile()!= null){
                            tv_contact_delivery.setText(deliveryBoyModel.getMobile());
                        }

                        if(deliveryBoyModel.getEmailId()!= null){
                            tv_email_delivery.setText(deliveryBoyModel.getEmailId());
                        }

                        if(deliveryBoyModel.getVehicleType()!= null){
                            tv_vehicle_delivery.setText(deliveryBoyModel.getVehicleType());
                        }

                        if(deliveryBoyModel.getLatitude()!= null && (deliveryBoyModel.getLongitude()!= null)){
                            latitude = Double.parseDouble(deliveryBoyModel.getLatitude());
                            longitude = Double.parseDouble(deliveryBoyModel.getLatitude());
                        }







                        //tv_online_status_delivery.setText(deliveryBoyModel.getOnlineStatus());

                       // Glide.with(DeliveryBoyDetailsActivity.this).load(deliveryBoyModel.get()).into(iv_vehicle_delivery);

                        /*if(deliveryBoyModel.getOnlineStatus().equals("online")){
                            switch_delivery.isChecked();
                        }*/

                    }
                }
                else
                {
                    Toast.makeText(DeliveryBoyDetailsActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDeliveryMan> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {
                }
                else if (t instanceof IOException)
                {

                }
                else
                {

                    Toast.makeText(DeliveryBoyDetailsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng currentLatLong = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(currentLatLong));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLong));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,16.0f));


        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch()
                    {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                        startActivity(new Intent(DeliveryBoyDetailsActivity.this, Maps1Activity.class).putExtra("from",0));
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    }
                });
    }




}