package ae.okhaz.boss.Activitys.Supplier;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import ae.okhaz.boss.Model.DeliveryBoyModel;
import ae.okhaz.admin.R;
import ae.okhaz.boss.rests.Response.ResponseDeliveryBoyList;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SessionManagement sessionManagement;
    HashMap user;
    IconGenerator generator;
    LayoutInflater inflater;
    String latt;
    View markerView ;
    TextView textView;
    private LocationManager locationManager;
    Location currentlocation;

    MarkerOptions markerOptions;
    Marker marker;
    Bitmap icon;

    int from=0;
    double lat,lng;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        from=getIntent().getIntExtra("from",0);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        String locationProvider = locationManager.NETWORK_PROVIDER;
        currentlocation = locationManager.getLastKnownLocation(locationProvider);
        latt=String.valueOf(getIntent().getDoubleExtra("lat",0));

           /* lat=getIntent().getDoubleExtra("lat",0);
            lng=getIntent().getDoubleExtra("lng",0);*/

        lat=currentlocation.getLatitude();
        lng=currentlocation.getLongitude();



        sessionManagement = SessionManagement.getInstance(this);
        user=  sessionManagement.getUserDetails();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (from==1){
            String latt=String.valueOf(lat);

                LatLng sydney = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(sydney));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                        (sydney,
                                16.0f));
        }
        else {

            getDeliveryBoyList(mMap);
        }
        // Add a marker in Sydney and move the camera
      /*  LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    public void getDeliveryBoyList(GoogleMap map)
    {
        String ID,role,BranchCode;
        ID=user.get(SessionManagement.KEY_ID).toString();
        role=user.get(SessionManagement.KEY_USER_TYPE).toString();
        BranchCode=user.get(SessionManagement.KEY_BRANCH_ID).toString();


        ServiceGenerator.getDelivery().deliveryBoyList(ID,role,BranchCode).enqueue(new Callback<ResponseDeliveryBoyList>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onResponse(Call<ResponseDeliveryBoyList> call, Response<ResponseDeliveryBoyList> response) {
                if (response.isSuccessful())
                {
                    map.clear();
                    ArrayList<DeliveryBoyModel> deliveryBoyModels=response.body().getResult().getDeliveryManList();
                    for(int i=0;i<deliveryBoyModels.size();i++)
                    {
                        if(deliveryBoyModels.get(i).getLatitude()==null){
                            //  currentlocation=map.getMyLocation();
                            //Toast.makeText(getContext(), "No delivery Boys Available..", Toast.LENGTH_SHORT).show();
                            /*map.addMarker(new MarkerOptions().position(new LatLng
                                    (currentlocation.getLatitude(),
                                            currentlocation.getLongitude())));*/

                        }
                        else if(deliveryBoyModels.get(i).getLatitude()!=null){
                            /*map.addMarker(new MarkerOptions().position(new LatLng
                                    (Double.parseDouble(deliveryBoyModels.get(i).getLatitude()),
                                    Double.parseDouble(deliveryBoyModels.get(i).getLongitude()))));*/

                            generator = new IconGenerator(MapsActivity.this);
                            inflater = (LayoutInflater)MapsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            markerView = inflater.inflate(R.layout.icon_layout, null);
                            generator.setContentView(markerView);
                            generator.setBackground(null);
                            icon = generator.makeIcon();

                            LatLng CurrentLatlang = new LatLng
                                    (Double.parseDouble(deliveryBoyModels.get(i).getLatitude()),
                                            Double.parseDouble(deliveryBoyModels.get(i).getLongitude()));
                            markerOptions = new MarkerOptions().position(CurrentLatlang).icon(BitmapDescriptorFactory.fromBitmap(icon)).title(deliveryBoyModels.get(i).getUserName());
                            map.animateCamera(CameraUpdateFactory.newLatLng(CurrentLatlang));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(CurrentLatlang, 16.0f));
                            marker =  map.addMarker(markerOptions);

                        }
                    }
                    // DeliveryBoyAdapter deliveryBoyAdapter=new DeliveryBoyAdapter(deliveryBoyModels,DeliveryBoyListActivity.this);
                }
                else
                {
                     Toast.makeText(MapsActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDeliveryBoyList> call, Throwable t) {
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

}