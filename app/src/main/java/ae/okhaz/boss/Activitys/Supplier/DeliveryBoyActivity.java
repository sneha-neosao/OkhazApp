package ae.okhaz.boss.Activitys.Supplier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ae.okhaz.boss.Model.DeliveryBoyModel;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.MySupportFragment;
import ae.okhaz.boss.Utils.Validations;
import ae.okhaz.boss.rests.Requests.RequestDeliveryBoy;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.Response.ResponseDeliveryMan;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryBoyActivity extends AppCompatActivity implements OnMapReadyCallback {
    SessionManagement sessionManagement;
    NestedScrollView nsv_deliveryBoy;
    HashMap user;
    private LocationManager locationManager;
    MySupportFragment mapView;
    private TextInputEditText edt_deliveryBoy_userName,edt_deliveryBoy_email,edt_deliveryBoy_pass,
            edt_deliveryBoy_confirmPass,edt_deliveryBoy_userStatus,edt_deliveryBoy_userMobile,
            edt_deliveryBoy_userVehicle,edt_deliveryBoy_userVehiNo,edt_deliveryBoy_userAdd,
            edt_deliveryBoy_userGender,edt_deliveryBoy_userFirstName;
    private Button btn_add_deliveryBoy,btn_update_deliveryBoy,btn_delete_deliveryBoy;
    private LinearLayout ll_update;
    GoogleMap mGoogleMap;
    private TextInputLayout tl_deliBoyStatus,tl_gender,tl_vehicle_type;
    double lat,lng;
    Toolbar toolbar;
    String str,staffID,id;
    DeliveryBoyModel deliveryBoyModel;
    int fromAdd=0,fromUpdate;
    Location currentlocation;
    RelativeLayout rl_map;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy);

        sessionManagement = SessionManagement.getInstance(getApplicationContext());
        user = sessionManagement.getUserDetails();

        rl_map=findViewById(R.id.rl_map);
        tl_deliBoyStatus=findViewById(R.id.tl_deliBoyStatus);
        tl_gender=findViewById(R.id.tl_gender);
        tl_vehicle_type=findViewById(R.id.tl_vehicle_type);

        nsv_deliveryBoy=findViewById(R.id.nsv_deliveryBoy);

        edt_deliveryBoy_userFirstName=findViewById(R.id.edt_deliveryBoy_userFirstName);
        edt_deliveryBoy_userName=findViewById(R.id.edt_deliveryBoy_userName);
        edt_deliveryBoy_email=findViewById(R.id.edt_deliveryBoy_email);
        edt_deliveryBoy_userStatus=findViewById(R.id.edt_deliveryBoy_userStatus);
        edt_deliveryBoy_userGender=findViewById(R.id.edt_deliveryBoy_userGender);
        edt_deliveryBoy_userMobile=findViewById(R.id.edt_deliveryBoy_userMobile);
        edt_deliveryBoy_userVehicle=findViewById(R.id.edt_deliveryBoy_userVehicle);
        edt_deliveryBoy_userVehiNo=findViewById(R.id.edt_deliveryBoy_userVehiNo);
        edt_deliveryBoy_userAdd=findViewById(R.id.edt_deliveryBoy_userAdd);
        edt_deliveryBoy_pass=findViewById(R.id.edt_deliveryBoy_pass);
        edt_deliveryBoy_confirmPass=findViewById(R.id.edt_deliveryBoy_confirmPass);

        btn_add_deliveryBoy=findViewById(R.id.btn_add_deliveryBoy);
        btn_update_deliveryBoy=findViewById(R.id.btn_update_deliveryBoy);
        btn_delete_deliveryBoy=findViewById(R.id.btn_delete_deliveryBoy);

        ll_update=findViewById(R.id.ll_update);
        toolbar=findViewById(R.id.toolbar);

        fromAdd=getIntent().getIntExtra("fromAdd",0);
        fromUpdate=getIntent().getIntExtra("fromUpdate",0);
        str=getIntent().getStringExtra("deliveryBoy");

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        String locationProvider = locationManager.NETWORK_PROVIDER;
        currentlocation = locationManager.getLastKnownLocation(locationProvider);

        mapView = (MySupportFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        toolbar.setTitle("Delivery Boy List");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edt_deliveryBoy_userStatus.setFocusable(false);
        edt_deliveryBoy_userVehicle.setFocusable(false);
        edt_deliveryBoy_userGender.setFocusable(false);
        // mapView.getView().getParent().requestDisallowInterceptTouchEvent(true);

        if (mapView != null) {
            mapView.getMapAsync(this);
        }

        /* Log.e("str1",str);
        Log.e("str2",fromAdd+"");
        Log.e("str3",fromUpdate+"");*/

        if(fromAdd==1)
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

        }

        mapView.setListener(new MySupportFragment.OnTouchListener() {
            @Override
            public void onTouch() {
                nsv_deliveryBoy.requestDisallowInterceptTouchEvent(true);
                startActivity(new Intent(DeliveryBoyActivity.this,MapsActivity.class)
                        .putExtra("lat",lat)
                        .putExtra("lng",lng)
                        .putExtra("from",1));
            }
        });

        edt_deliveryBoy_userStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_deliveryBoy_userStatus.setFocusable(false);
                PopupMenu popup = new PopupMenu(DeliveryBoyActivity.this, edt_deliveryBoy_userStatus);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.deliveryboystatus, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        edt_deliveryBoy_userStatus.setText(item.getTitle());
                        popup.dismiss();
                     //   Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
        }
        });

        edt_deliveryBoy_userVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_deliveryBoy_userVehicle.setFocusable(false);
                PopupMenu popup = new PopupMenu(DeliveryBoyActivity.this, edt_deliveryBoy_userVehicle);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_vehicle_type, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        edt_deliveryBoy_userVehicle.setText(item.getTitle());
                        popup.dismiss();
                        //   Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

        edt_deliveryBoy_userGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_deliveryBoy_userGender.setFocusable(false);
                PopupMenu popup = new PopupMenu(DeliveryBoyActivity.this, edt_deliveryBoy_userGender);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_gender, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        edt_deliveryBoy_userGender.setText(item.getTitle());
                        popup.dismiss();
                        //   Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });



        btn_add_deliveryBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidation()){
                    addDeliveryBoy();
                }
            }
        });

        btn_update_deliveryBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidation()){
                    updateDeliveryBoy();
                }
            }
        });

        btn_delete_deliveryBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edt_deliveryBoy_userName.getText().toString().isEmpty()){
                    deleteDeliveryBoy();
                }

            }
        });

    }

    public boolean checkValidation(){
        if(edt_deliveryBoy_userStatus.getText().toString().isEmpty()){
            edt_deliveryBoy_userVehicle.setError("Please select user status");
        }
        else if(edt_deliveryBoy_userFirstName.getText().toString().isEmpty()){
            edt_deliveryBoy_userFirstName.setError("Please enter first Name");
        }
        else if(edt_deliveryBoy_userName.getText().toString().isEmpty()){
            edt_deliveryBoy_userName.setError("Please enter User Name");
        }
        else if(edt_deliveryBoy_userGender.getText().toString().isEmpty()){
            edt_deliveryBoy_userGender.setError("Please Select Gender");
        }
        else if(edt_deliveryBoy_userMobile.getText().toString().isEmpty()){
            edt_deliveryBoy_userMobile.setError("Please enter mobile number");
        }
        else if(!Validations.isValidEmail(edt_deliveryBoy_email.getText().toString())){
            edt_deliveryBoy_email.setError("Please enter valid email address");
        }
        else if(edt_deliveryBoy_userVehicle.getText().toString().isEmpty()){
            edt_deliveryBoy_userVehicle.setError("Please Select Vehicle Type");
        }
        else if(edt_deliveryBoy_userAdd.getText().toString().isEmpty()){
            edt_deliveryBoy_userAdd.setError("Please enter address");
        }
        else if(edt_deliveryBoy_pass.getText().toString().isEmpty()){
            edt_deliveryBoy_pass.setError("Please enter password");
        }
        else if(edt_deliveryBoy_confirmPass.getText().toString().isEmpty()){
            edt_deliveryBoy_confirmPass.setError("Please confirm password");
        }
        else if(!edt_deliveryBoy_pass.getText().toString().equals(edt_deliveryBoy_confirmPass.getText().toString().trim()))
        {
            edt_deliveryBoy_confirmPass.setError("Password not matching");
        }

        else
            {
            return true;
        }

        return false;
    }

    public void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

    }

    public void setData(DeliveryBoyModel deliveryBoyModel){
        String userName=deliveryBoyModel.getUserName();
        String firstName=deliveryBoyModel.getFirstName();
        String gender=deliveryBoyModel.getGender();
        String vehicleType=deliveryBoyModel.getVehicleType();
        String vehiclNo=deliveryBoyModel.getVehicleNo();
        String add=deliveryBoyModel.getAddress2();
        String loginStatus=deliveryBoyModel.getLoginstatus();
        String mobile=deliveryBoyModel.getMobile();

        staffID=deliveryBoyModel.getStaffId();

        String pass=getIntent().getStringExtra("password");
        String email=getIntent().getStringExtra("email");
        if(loginStatus.equals("1")){
            edt_deliveryBoy_userStatus.setText("Enabled");
        }
        else if(loginStatus.equals("0"))
        {
            edt_deliveryBoy_userStatus.setText("Disabled");
        }
        edt_deliveryBoy_userFirstName.setText(""+firstName);
        edt_deliveryBoy_userName.setText(""+userName);
        edt_deliveryBoy_userMobile.setText(""+mobile);
        edt_deliveryBoy_userGender.setText(""+gender);
        edt_deliveryBoy_userVehicle.setText(""+vehicleType);
        edt_deliveryBoy_userVehiNo.setText(""+vehiclNo);
        edt_deliveryBoy_userAdd.setText(""+add);
        edt_deliveryBoy_pass.setText(""+pass);
        edt_deliveryBoy_email.setText(""+email);
        String latitude=deliveryBoyModel.getLatitude();
        if (latitude!=null){
            lat=Double.parseDouble(deliveryBoyModel.getLatitude());
            lng=Double.parseDouble(deliveryBoyModel.getLongitude());

        }
        else {
            lat=currentlocation.getLatitude();
            lng=currentlocation.getLongitude();
        }

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
                        Toast.makeText(DeliveryBoyActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DeliveryBoyModel deliveryBoyModel=response.body().getResult();
                        setData(deliveryBoyModel);

                    }
                }
                else
                {
                    Toast.makeText(DeliveryBoyActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(DeliveryBoyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void addDeliveryBoy(){

        RequestDeliveryBoy requestDeliveryBoy=new RequestDeliveryBoy();
        requestDeliveryBoy.setFirstName(edt_deliveryBoy_userFirstName.getText().toString());
        requestDeliveryBoy.setUserName(edt_deliveryBoy_userName.getText().toString());
        requestDeliveryBoy.setPassWord(edt_deliveryBoy_confirmPass.getText().toString());
        requestDeliveryBoy.setOwnerID(user.get(SessionManagement.KEY_ID).toString());
        requestDeliveryBoy.setEmailId(edt_deliveryBoy_email.getText().toString());
        requestDeliveryBoy.setGender(edt_deliveryBoy_userGender.getText().toString());
        requestDeliveryBoy.setVehicleType(edt_deliveryBoy_userVehicle.getText().toString());
        requestDeliveryBoy.setMobile(edt_deliveryBoy_userMobile.getText().toString());
        requestDeliveryBoy.setVehicleNo(edt_deliveryBoy_userVehiNo.getText().toString());
        requestDeliveryBoy.setAddress(edt_deliveryBoy_userAdd.getText().toString());

        if(edt_deliveryBoy_userStatus.getText().toString().equals("Enabled"))
        {
            requestDeliveryBoy.setLoginStatus(1);
        }
        else if(edt_deliveryBoy_userStatus.getText().toString().equals("Disabled")){
            requestDeliveryBoy.setLoginStatus(0);
        }

        ServiceGenerator.getDelivery().addDeliveryBoy(requestDeliveryBoy).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                       onBackPressed();
                        Toast.makeText(DeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(DeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(DeliveryBoyActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    //updateBarcode(barcode,itemId,orderItemId);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                   // updateBarcode(barcode,itemId,orderItemId);
                }
                else
                {
                    Toast.makeText(DeliveryBoyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void updateDeliveryBoy(){

        RequestDeliveryBoy requestDeliveryBoy=new RequestDeliveryBoy();
        requestDeliveryBoy.setFirstName(edt_deliveryBoy_userFirstName.getText().toString());
        requestDeliveryBoy.setUserName(edt_deliveryBoy_userName.getText().toString());
        requestDeliveryBoy.setPassWord(edt_deliveryBoy_confirmPass.getText().toString());
        requestDeliveryBoy.setOwnerID(user.get(SessionManagement.KEY_ID).toString());
        requestDeliveryBoy.setEmailId(edt_deliveryBoy_email.getText().toString());
        requestDeliveryBoy.setGender(edt_deliveryBoy_userGender.getText().toString());
        requestDeliveryBoy.setVehicleType(edt_deliveryBoy_userVehicle.getText().toString());
        requestDeliveryBoy.setMobile(edt_deliveryBoy_userMobile.getText().toString());
        requestDeliveryBoy.setVehicleNo(edt_deliveryBoy_userVehiNo.getText().toString());
        requestDeliveryBoy.setAddress(edt_deliveryBoy_userAdd.getText().toString());
        requestDeliveryBoy.setStaffId(staffID);

        if(edt_deliveryBoy_userStatus.getText().toString().equals("Enabled"))
        {
            requestDeliveryBoy.setLoginStatus(1);
        }
        else if(edt_deliveryBoy_userStatus.getText().toString().equals("Disabled")){
            requestDeliveryBoy.setLoginStatus(0);
        }

        ServiceGenerator.getDelivery().editDeliveryBoy(requestDeliveryBoy).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        onBackPressed();
                        Toast.makeText(DeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(DeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(DeliveryBoyActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    //updateBarcode(barcode,itemId,orderItemId);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    // updateBarcode(barcode,itemId,orderItemId);
                }
                else
                {
                    Toast.makeText(DeliveryBoyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void deleteDeliveryBoy(){

        RequestDeliveryBoy requestDeliveryBoy=new RequestDeliveryBoy();
        requestDeliveryBoy.setUserName(edt_deliveryBoy_userName.getText().toString());
        requestDeliveryBoy.setStaffId(staffID);

        ServiceGenerator.getDelivery().deleteDeliveryBoy(requestDeliveryBoy).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        onBackPressed();
                        Toast.makeText(DeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(DeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(DeliveryBoyActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    //updateBarcode(barcode,itemId,orderItemId);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    // updateBarcode(barcode,itemId,orderItemId);
                }
                else
                {
                    Toast.makeText(DeliveryBoyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng
                (currentlocation.getLatitude(),
                        currentlocation.getLongitude())));
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude()),
                16.0f));


    }
}