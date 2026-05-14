package ae.okhaz.boss.Fragments.Admin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.maps.android.ui.IconGenerator;
import ae.okhaz.boss.Adapter.Admin.HomeAdapter;
import ae.okhaz.boss.Adapter.Admin.OrderStatusAdapter;
import ae.okhaz.boss.Model.DeliveryBoyModel;
import ae.okhaz.boss.Model.OrderStatus;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.MySupportFragment;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.Utils.WorkaroundMapFragment;
import ae.okhaz.boss.rests.Response.ResponseDeliveryBoyList;
import ae.okhaz.boss.rests.Response.ResponseOrderStatus;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.Maps1Activity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class SupplierHomeFragment extends Fragment implements OnMapReadyCallback {
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 3000;
    MySupportFragment mapView;
    NestedScrollView ns_map;
    GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    Location currentlocation;
    private LocationManager locationManager;
    IconGenerator generator;
    LayoutInflater inflater;
    RecyclerView recy_order_status;
    View markerView ;
    TextView textView;
    MarkerOptions markerOptions;
    Marker marker;
    Bitmap icon;
    LatLng latLng;
    boolean canGetLocation = false;
    TabLayout tabLayout;
    ViewPager viewPager;
    HomeAdapter adapter;
    SessionManagement sessionManagement;
    HashMap user;
    View custom_view;
    private LocationRequest mLocationRequest;
    private Location location;
    double latitude = 0.0, longitude = 0.0;
    RelativeLayout rl_mapview;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    //double latitude = 0.0, longitude = 0.0;
    private GoogleMap mMap;
    GridLayoutManager gridLayoutManager;
    public SupplierHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_supplier_home, container, false);

        Tools.setSystemBarColor(getActivity(),R.color.green);

        //Location permission
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            currentlocation = locationManager.getLastKnownLocation(locationProvider);
            latitude = currentlocation.getLatitude();
            longitude = currentlocation.getLongitude();
            Log.e("lat",""+currentlocation.getLatitude());
            Log.e("long",""+currentlocation.getLongitude());
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        ns_map = view.findViewById(R.id.ns_map);
        custom_view = view.findViewById(R.id.customView);
        recy_order_status = view.findViewById(R.id.recy_order_status);
        rl_mapview = view.findViewById(R.id.rl_mapview);

        gridLayoutManager = new GridLayoutManager(getContext(),2);

        viewPager.setAdapter(adapter = new HomeAdapter(getChildFragmentManager(), 2));
//        pager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);

        sessionManagement = SessionManagement.getInstance(getContext());
        user=  sessionManagement.getUserDetails();

        if (mMap == null) {
            WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getChildFragmentManager().findFragmentById(R.id.mapview);
            mapFragment.getMapAsync(this);
        }

       String role=user.get(SessionManagement.KEY_USER_TYPE).toString();
        if (role.equals("Supplier")){
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);
        }
        else {
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
        }


        getOrderStatus();



        return view;
    }


   /* public void getOrderStatus(){

        String ownerID=user.get(SessionManagement.KEY_ID).toString();
        String role=user.get(SessionManagement.KEY_USER_TYPE).toString();
        String branchID = user.get(SessionManagement.KEY_BRANCH_ID).toString();
        String deliMan = user.get(SessionManagement.KEY_DELIVERY_MAN).toString();
        // order_pb.setVisibility(View.VISIBLE);

        ServiceGenerator.getDelivery().getOwnerOrders(ownerID,deliMan, branchID,role,"All", "0", "", "", "", "").enqueue(new Callback<ResponseOrderAdminList>() {
            @Override
            public void onResponse(Call<ResponseOrderAdminList> call, Response<ResponseOrderAdminList> response) {
                //  order_pb.setVisibility(View.GONE);
                if (response.isSuccessful())
                {
                    if(response.body().isStatus()){
                        ArrayList<CountOrder> counts = response.body().getCounts();
                        OrderStatusAdapter orderStatusAdapter=new OrderStatusAdapter(counts,getContext());

                        recy_order_status.setLayoutManager(gridLayoutManager);
                        recy_order_status.setAdapter(orderStatusAdapter);
                    }
                }
                else
                {
                    // progressDialog.dismiss();
                    Toast.makeText(getContext(), "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }                }

            @Override
            public void onFailure(Call<ResponseOrderAdminList> call, Throwable t) {
                //   order_pb.setVisibility(View.GONE);

                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    //    getOrdersData(deliveryman, BranchCode, OrderStatus, offset, condition, OrderID, startDate, endDate);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    //   getOrdersData(deliveryman, BranchCode, OrderStatus, offset, condition, OrderID, startDate, endDate);

                }
                else
                {
                }
            }
        });


*//*
        ServiceGenerator.getDelivery().getOrderStatus().enqueue(new Callback<ResponseOrderStatus>() {
            @Override
            public void onResponse(Call<ResponseOrderStatus> call, Response<ResponseOrderStatus> response) {
                if (response.isSuccessful())
                {
                    if(response.body().getStatus().equals("true")){
                        ArrayList<OrderStatus> res=response.body().getResult();
                        OrderStatusAdapter orderStatusAdapter=new OrderStatusAdapter(res,getContext());

                        recy_order_status.setLayoutManager(gridLayoutManager);
                        recy_order_status.setAdapter(orderStatusAdapter);
                    }
                }
                else
                {
                    // progressDialog.dismiss();
                    Toast.makeText(getContext(), "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOrderStatus> call, Throwable t) {
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
*//*

    }*/

    public void getOrderStatus(){
        ServiceGenerator.getDelivery().getOrderStatus().enqueue(new Callback<ResponseOrderStatus>() {
            @Override
            public void onResponse(Call<ResponseOrderStatus> call, Response<ResponseOrderStatus> response) {
                if (response.isSuccessful())
                {
                    if(response.body().getStatus().equals("true")){
                        ArrayList<OrderStatus> res=response.body().getResult();
                        OrderStatusAdapter orderStatusAdapter=new OrderStatusAdapter(res,getContext());

                        recy_order_status.setLayoutManager(gridLayoutManager);
                        recy_order_status.setAdapter(orderStatusAdapter);
                    }
                }
                else
                {
                    // progressDialog.dismiss();
                    Toast.makeText(getContext(), "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOrderStatus> call, Throwable t) {
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
                    if (!response.body().isStatus()){

                        //rl_mapview.setVisibility(View.GONE);
                         Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        map.addMarker(new MarkerOptions().position(new LatLng
                                (currentlocation.getLatitude(),
                                        currentlocation.getLongitude())));
                    }
                    else {

                        rl_mapview.setVisibility(View.VISIBLE);
                    map.clear();
                    ArrayList<DeliveryBoyModel> deliveryBoyModels=response.body().getResult().getDeliveryManList();

                    if(deliveryBoyModels!=null) {
                        for (int i = 0; i < deliveryBoyModels.size(); i++) {
                            ArrayList<String> lattitudeList=new ArrayList<>();

                            if (deliveryBoyModels.get(i).getLatitude() != null && !deliveryBoyModels.get(i).getLatitude().isEmpty())
                            {
                                lattitudeList.add(deliveryBoyModels.get(i).getLatitude());
                                if(lattitudeList.size()>0)
                                {
                                    if (deliveryBoyModels.get(i).getLatitude() == null) {
                                        //  currentlocation=map.getMyLocation();
                                        //Toast.makeText(getContext(), "No delivery Boys Available..", Toast.LENGTH_SHORT).show();

                                        if(currentlocation!=null)
                                            map.addMarker(new MarkerOptions().position(new LatLng
                                                    (currentlocation.getLatitude(),
                                                            currentlocation.getLongitude())));

                                    } else if (deliveryBoyModels.get(i).getLatitude() != null) {
                            /*map.addMarker(new MarkerOptions().position(new LatLng
                                    (Double.parseDouble(deliveryBoyModels.get(i).getLatitude()),
                                    Double.parseDouble(deliveryBoyModels.get(i).getLongitude()))));*/

                                        rl_mapview.setVisibility(View.VISIBLE);
                                        generator = new IconGenerator(getContext());
                                        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        markerView = inflater.inflate(R.layout.icon_layout, null);
                                        generator.setContentView(markerView);
                                        TextView textView=markerView.findViewById(R.id.txt_deli_name);
                                        textView.setText(deliveryBoyModels.get(i).getUserName());
                                        generator.setBackground(null);
                                        icon = generator.makeIcon();

                                        LatLng CurrentLatlang = new LatLng
                                                (Double.parseDouble(deliveryBoyModels.get(i).getLatitude()),
                                                        Double.parseDouble(deliveryBoyModels.get(i).getLongitude()));
                                        markerOptions = new MarkerOptions().position(CurrentLatlang).icon(BitmapDescriptorFactory.fromBitmap(icon)).title(deliveryBoyModels.get(i).getUserName());
                                        map.animateCamera(CameraUpdateFactory.newLatLng(CurrentLatlang));
                                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(CurrentLatlang, 16.0f));
                                        marker = map.addMarker(markerOptions);
                                    }
                                }
                                else {
                                    rl_mapview.setVisibility(View.VISIBLE);
                                }
                            }
                            else {
                                rl_mapview.setVisibility(View.VISIBLE);
                            }



                        }
                    }else {
                        map.addMarker(new MarkerOptions().position(new LatLng
                                (currentlocation.getLatitude(),
                                        currentlocation.getLongitude())));
                    }
                    }
                    // DeliveryBoyAdapter deliveryBoyAdapter=new DeliveryBoyAdapter(deliveryBoyModels,DeliveryBoyListActivity.this);
                }
                else
                {
                    // Toast.makeText(DeliveryBoyListActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
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

        ((WorkaroundMapFragment)(getChildFragmentManager().findFragmentById(R.id.mapview)))
                .setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch()
                    {
                        ns_map.requestDisallowInterceptTouchEvent(true);
                        startActivity(new Intent(getActivity(), Maps1Activity.class).putExtra("from",0));
                        getActivity().overridePendingTransition(R.anim.slide_up,R.anim.slide_down);
                    }
                });
    }
}