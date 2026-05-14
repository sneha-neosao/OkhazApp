package ae.okhaz.boss.Activitys;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.maps.android.ui.IconGenerator;
import ae.okhaz.boss.Model.SearchLocations;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.WorkaroundMapFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    TextInputEditText edt_fullName, edt_Contact, edt_Email, edt_Street, edt_buildingNo, edt_City;
    TextView txt_address;
    Button update_details_btn;
    Toolbar toolbar;
    ImageView img_up,img_down;
    LinearLayout ll_details;
    String name,contact,address1,address2,city,email;

    private GoogleMap mMap;
    String from = "", locs = "";
    SearchLocations searchLocations;
    boolean flag = false;
    IconGenerator generator;
    boolean canGetLocation = false;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 3000;
    LayoutInflater inflater;
    View markerView;
    TextView textView;
    MarkerOptions markerOptions;
    Marker marker;
    Bitmap icon;
    Location currentlocation;
    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    double latitude = 0.0, longitude = 0.0;
    SupportMapFragment mapFragment;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
        edt_fullName = findViewById(R.id.username);
        edt_Contact = findViewById(R.id.usercontact);
        edt_Email = findViewById(R.id.userEmail);
        edt_Street = findViewById(R.id.userStreet);
        edt_buildingNo = findViewById(R.id.userBuildingNo);
        edt_City = findViewById(R.id.userCity);
        //txt_address = findViewById(R.id.txt_address);
        txt_address = findViewById(R.id.txt_address);
        toolbar = findViewById(R.id.toolbar_edit);
        img_down = findViewById(R.id.img_down_arrow);
        img_up = findViewById(R.id.img_up_arrow);
        ll_details = findViewById(R.id.ll_details);
        update_details_btn = findViewById(R.id.update_details_btn);
        setSupportActionBar(toolbar);
        getLocation();

        name=getIntent().getStringExtra("cust_name");
        contact=getIntent().getStringExtra("contact");
        address1=getIntent().getStringExtra("address1");
        address2=getIntent().getStringExtra("address2");
        email=getIntent().getStringExtra("email");
        city=getIntent().getStringExtra("city");
        //latitude=Double.parseDouble(getIntent().getStringExtra("lat"));
       //longitude=Double.parseDouble(getIntent().getStringExtra("longs"));
        //Log.e("currentlocation", "onMapReady: "+longitude);
        //Log.e("currentlocation", "onMapReady: "+latitude);


        currentlocation.setLongitude(longitude);
        currentlocation.setLatitude(latitude);


        edt_fullName.setText(name);
        edt_Contact.setText(contact);
        edt_Street.setText(address1);
        edt_City.setText(city);
        edt_Email.setText(email);

        update_details_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        img_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_down.setVisibility(View.GONE);
                img_up.setVisibility(View.VISIBLE);
                ll_details.setVisibility(View.VISIBLE);
            }
        });

        img_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_down.setVisibility(View.VISIBLE);
                img_up.setVisibility(View.GONE);
                ll_details.setVisibility(View.GONE);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDetailsActivity.super.onBackPressed();
            }
        });

        /*mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }*/


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
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        if (mMap == null) {
            WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_views);
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        currentlocation = location;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
       /* mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        Log.e("currentlocation", "onMapReady: "+currentlocation);

        marker =  mMap.addMarker( new MarkerOptions()
                .position( new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude()))
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
        );

        if(currentlocation!=null){

            marker =  mMap.addMarker( new MarkerOptions()
                    .position( new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(icon))
            );
        Log.e("currentlocation", "onMapReady: "+currentlocation);

            generator = new IconGenerator(this);
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            markerView = inflater.inflate(R.layout.icon_layout, null);
            generator.setContentView(markerView);
            generator.setBackground(null);
            icon = generator.makeIcon();

            LatLng CurrentLatlang = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
            markerOptions = new MarkerOptions().position(CurrentLatlang).icon(BitmapDescriptorFactory.fromBitmap(icon));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(CurrentLatlang));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CurrentLatlang, 18.0f));
            marker =  mMap.addMarker(markerOptions);
            LatLng updatedlatlong = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
            latitude = currentlocation.getLatitude();
            longitude = currentlocation.getLongitude();
            updateLoction(updatedlatlong);
        }
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                mMap.clear();
                LatLng midLatLng = mMap.getCameraPosition().target;
                if (marker!= null)
                {
                    marker = mMap.addMarker(new MarkerOptions().
                            position(midLatLng).icon(BitmapDescriptorFactory.fromBitmap(icon)));
                }
            }
        });
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (marker!= null)
                {
                    marker.remove();
                    LatLng position = marker.getPosition();
//               LatLng updatedlatlong = new LatLng(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                    LatLng updatedlatlong = new LatLng(position.latitude, position.longitude);
                    latitude = position.latitude;
                    longitude = position.longitude;
                    marker =  mMap.addMarker( new MarkerOptions()
                            .position( updatedlatlong)
                            .icon(BitmapDescriptorFactory.fromBitmap(icon))
                    );
                    updateLoction(updatedlatlong);
                }
            }
        });*/

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng currentLatLong = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(currentLatLong));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLong));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,16.0f));

        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_views))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch()
                    {
                        //mScrollView.requestDisallowInterceptTouchEvent(true);
                        //startActivity(new Intent(DeliveryBoyDetailsActivity.this, Maps1Activity.class).putExtra("from",0));
                        //overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    }
                });

    }
    private void updateLoction(LatLng newlatlangs) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(newlatlangs.latitude,newlatlangs.longitude, 1);
            String address = "Could not find location :(";
            if (addresses != null && addresses.size() > 0) {
                if (addresses.get(0).getThoroughfare() != null) {
                    address = addresses.get(0).getThoroughfare() + " ";
                }
                if (addresses.get(0).getLocality() != null) {
                    address += addresses.get(0).getLocality() + " ";
                }
                if (addresses.get(0).getPostalCode() != null) {
                    address += addresses.get(0).getPostalCode() + " ";
                }
                if (addresses.get(0).getAdminArea() != null) {
                    address += addresses.get(0).getAdminArea();
                }
            }

            txt_address.setText(address);
            Log.i("Address",address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLocation()
    {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                setSupLocation();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,this::onLocationChanged);
                    if (locationManager != null) {
                        currentlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (currentlocation != null) {
                            latitude = currentlocation.getLatitude();
                            longitude = currentlocation.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (currentlocation == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,this::onLocationChanged);
                        if (locationManager != null) {
                            currentlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = currentlocation.getLatitude();
                                longitude = currentlocation.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private void setSupLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        enableLocationSettings();
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                }
            }
        });
    }

    protected void enableLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(this, (LocationSettingsResponse response) -> {
                    // startUpdatingLocation(...);
                    getLocation();
                })
                .addOnFailureListener(this, ex -> {
                    if (ex instanceof ResolvableApiException) {
                        // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(EditDetailsActivity.this, 101);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (101 == requestCode) {
            if (RESULT_OK == resultCode) {
                //user clicked OK, you can startUpdatingLocation(...);
                getLocation();
            } else {
                //user clicked cancel: informUserImportanceOfLocationAndPresentRequestAgain();
            }
        }
    }
}