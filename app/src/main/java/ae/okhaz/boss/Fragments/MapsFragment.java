package ae.okhaz.boss.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ae.okhaz.boss.Activitys.MainActivity;
import ae.okhaz.boss.Adapter.LocationAdapter;
import ae.okhaz.boss.Model.Locations;
import ae.okhaz.boss.Model.Order;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.SortPlaces;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements DirectionCallback {

    private static final int PERMISSION_CODE = 101;
    String[] permissions_all = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int REQUEST_CODE = 101;
    private GoogleMap mMap;
    private static final String TAG = "MapsFragment";
    LocationManager locationManager;
    boolean isGpsLocation;
    ArrayList<Locations> locationsArrayList;
    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    RecyclerView locations_recycler,list_recycler;
    private List<Polyline> polylines;
    TextView optimize_txt;
    androidx.appcompat.app.AlertDialog alertDialog;
    CardView optimize_cv;
    ProgressDialog progressDialog;
    public  static boolean isRoutes = false,isStarted= false;
    ImageView imageView;
    ArrayList<Order> orderArrayList;

    boolean flag = false;
    IconGenerator generator;

    LayoutInflater inflater;
    View markerView ;
    TextView textView;
    LocationAdapter locationAdapter;
    TextView total_stops,start_tvs,end_tvs;
    int admin=0;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {

            mMap= googleMap;
            mMap.setMyLocationEnabled(true);

            LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            currentlocation = locationManager.getLastKnownLocation(locationProvider);
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getContext(), R.raw.style_json));

                if (!success) {
                    Log.e(TAG, "Style parsing failed.");
                }
                else {
                    Log.e(TAG, "Style parsing Success.");
                }



            } catch (Resources.NotFoundException e) {
                Log.e(TAG, "Can't find style. Error: ", e);
            }


            if (locationsArrayList.size() > 0) {

                if (currentlocation!= null)
                {
                    LatLng currents = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());

                    ArrayList<Locations> places = new ArrayList<Locations>();

                    for (Locations p: locationsArrayList){
                        Log.i(TAG, "locations: " + p.getName());
                    }

                    for (int j= 0; j<locationsArrayList.size(); j++)
                    {
                        Locations locations = new Locations(locationsArrayList.get(j).getLat(), locationsArrayList.get(j).getLng(),locationsArrayList.get(j).getName(), locationsArrayList.get(j).getAddress());
                        places.add(locations);
                    }

                    Collections.sort(places, new SortPlaces(currents));

                    for (Locations p: places){
                        Log.i(TAG, "Place: " + p.getName());
                    }

                    locationsArrayList.clear();

                    for (int i = 0; i < places.size(); i++)
                    {
                        locationsArrayList.add(places.get(i));
                    }
//                        getRouteToMarkerSingles(currents, latLngs);
                }


                for (int i = 0; i < locationsArrayList.size(); i++) {

                    int a = i;
                    if(i==0)
                    {

                        generator = new IconGenerator(getContext());
                        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        markerView = inflater.inflate(R.layout.icon_home_layout, null);
                        generator.setContentView(markerView);
                        generator.setBackground(null);
                        Bitmap icon = generator.makeIcon();
                        LatLng location = new LatLng(locationsArrayList.get(i).getLat(), locationsArrayList.get(i).getLng());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(location).visible(true)
                                .icon(BitmapDescriptorFactory.fromBitmap(icon));

                        googleMap.addMarker(markerOptions);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(location));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 14.0f));
                    }
                    else {
                        generator = new IconGenerator(getContext());
                        inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        markerView = inflater.inflate(R.layout.icon_layout, null);
                        textView = markerView.findViewById(R.id.priceTag);
//                        textView.setText(String.valueOf(a));
                        generator.setContentView(markerView);
                        generator.setBackground(null);
                        Bitmap icon = generator.makeIcon();
                        LatLng location = new LatLng(locationsArrayList.get(i).getLat(), locationsArrayList.get(i).getLng());
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(location).visible(true)
                                .icon(BitmapDescriptorFactory.fromBitmap(icon));
                        markerOptions.title("" + a);
                        googleMap.addMarker(markerOptions);

                    }




                }
            }




        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_layout_maps, container, false);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list_recycler = view.findViewById(R.id.list_recycler);
        locations_recycler = view.findViewById(R.id.locations_recycler);
        optimize_txt = view.findViewById(R.id.optimize_txt);
        optimize_cv = view.findViewById(R.id.optimize_cv);
        end_tvs = view.findViewById(R.id.end_tvs);
        start_tvs = view.findViewById(R.id.start_tvs);
        total_stops = view.findViewById(R.id.total_stops);
        orderArrayList = new ArrayList<>();
        locationsArrayList = new ArrayList<>();
        String list = getArguments().getString("list");
        admin=getArguments().getInt("admin");

        if(admin==0){
            ((MainActivity)getActivity()).HideLocations();
        }

        Type typess = new TypeToken<ArrayList<Order>>(){}.getType();
        orderArrayList = new Gson().fromJson(list, typess);



//        getList();
        polylines = new ArrayList<>();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        getDeviceLocation();
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        currentlocation = locationManager.getLastKnownLocation(locationProvider);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


        int l=0;

        for (Order order: orderArrayList)
        {
            if(order!=null){


            if (order.getLatitude() != null && !order.getLatitude().isEmpty())
            {
                locationsArrayList.add(new Locations(Double.parseDouble(order.getLatitude()),Double.parseDouble(order.getLongitude()),"Order #"+order.getOrderID(), order.getAddressLine1()));
            }
            else
            {
                Toast.makeText(getActivity(), "Location is not available for "+order.getOrderID(), Toast.LENGTH_SHORT).show();
              //  getActivity().onBackPressed();
            }
            }
            else {
                Toast.makeText(getActivity(), "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }

        }




        total_stops.setText(locationsArrayList.size()-1+" stops");

        if (locationsArrayList.size() > 0) {

            if (currentlocation!= null)
            {
                LatLng currents = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());

                locationsArrayList.add(new Locations(currentlocation.getLatitude(), currentlocation.getLongitude(),"Self", "-"));

                ArrayList<Locations> places = new ArrayList<Locations>();

                for (Locations p: locationsArrayList){
                    Log.i(TAG, "locations: " + p.getName());
                }

                for (int j= 0; j<locationsArrayList.size(); j++)
                {
                    Locations locations = new Locations(locationsArrayList.get(j).getLat(), locationsArrayList.get(j).getLng(),locationsArrayList.get(j).getName(), locationsArrayList.get(j).getAddress());
                    places.add(locations);
                }

                Collections.sort(places, new SortPlaces(currents));

                for (Locations p: places){
                    Log.i(TAG, "Place: " + p.getName());
                }

                locationsArrayList.clear();

                for (int i = 0; i < places.size(); i++)
                {
                    locationsArrayList.add(places.get(i));

                    if (i==0)
                    {
                        start_tvs.setText(places.get(0).getName());
                    }
                    else if (i== places.size()-1)
                    {
                        end_tvs.setText(places.get(places.size()-1).getName());
                    }
                }
//                        getRouteToMarkerSingles(currents, latLngs);
            }
            else
            {
                start_tvs.setText(locationsArrayList.get(0).getName());
                end_tvs.setText(locationsArrayList.get(locationsArrayList.size()-1).getName());
            }

        }

        setRecyclers();

        optimize_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationsArrayList.size()>0) {
                    if (currentlocation != null) {
                        optimize_cv.setVisibility(View.GONE);
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("please wait...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        start_tvs.setText(locationsArrayList.get(0).getName());
                        end_tvs.setText(locationsArrayList.get(locationsArrayList.size()-1).getName());

                        LatLng location1 = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
                        LatLng location2 = new LatLng(locationsArrayList.get(locationsArrayList.size() - 1).getLat(), locationsArrayList.get(locationsArrayList.size() - 1).getLng());


                        List<LatLng> wayPoints = new ArrayList<>();
                        for (int j = 1; j < locationsArrayList.size(); j++) {

                            if (j != locationsArrayList.size() - 1) {
                                LatLng locations = new LatLng(locationsArrayList.get(j).getLat(), locationsArrayList.get(j).getLng());
                                wayPoints.add(locations);
                            } else {
                                break;
                            }

                        }

                        getRouteToMarker(location1, location2, wayPoints);
                    }
                }
            }
        });
    }

    private void setRecyclers() {
        list_recycler.setHasFixedSize(true);
        list_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        locationAdapter = new LocationAdapter(locationsArrayList, getContext());
        list_recycler.setAdapter(locationAdapter);
        list_recycler.setVisibility(View.VISIBLE);
    }

    private void getList() {

        Locations locations = new Locations(16.703286,74.236115,"Shahupuri","Adc adsasd asdasdasd 416001");
        locationsArrayList.add(locations);
        Locations locations1 = new Locations(16.699135,74.239808,"CBS","Adc adsasd asdasdasd 416001");
        locationsArrayList.add(locations1);
        Locations locations2 = new Locations(16.700121,74.236073,"Rajarampuri","Adc adsasd asdasdasd 416001");
        locationsArrayList.add(locations2);
        Locations locations3 = new Locations(16.700635,74.232830,"VenusCorner","Adc adsasd asdasdasd 416001");
        locationsArrayList.add(locations3);
        Locations locations4 = new Locations(16.699217,74.228080,"Dasrachowk","Adc adsasd asdasdasd 416001");
        locationsArrayList.add(locations4);
        Locations locations5 = new Locations(16.695928,74.226320,"Station","Adc adsasd asdasdasd 416001");
        locationsArrayList.add(locations5);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (admin==0){
            ((MainActivity)getActivity()).CheckDeliveryMan();
            ((MainActivity)getActivity()).HideAll();
        }

        LocationListner locationListner = new LocationListner();
        IntentFilter intentFilter = new IntentFilter("locations");
        getContext().registerReceiver(locationListner, intentFilter);
    }

    private Bitmap changeBitmapColor(int color) {

        Bitmap ob = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.trans_loc);
//        Bitmap obm = Bitmap.createBitmap(ob.getWidth(), ob.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap overlay = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.trans_loc);
        Bitmap overlaym = Bitmap.createBitmap(overlay.getWidth(), overlay.getHeight(), Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(overlaym);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(ob, 0f, 0f, paint);
        canvas.drawBitmap(overlay, 0f, 0f, null);
        return overlaym;
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
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {
                        getDeviceLocation();
                    }

                } else {

                    Toast.makeText(getActivity(), "Permission Failed", Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }

    private void updateLoction(LatLng newlatlangs) {


        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());


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

            Log.i("Address",address);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private boolean checkPermission() {
        for(int i=0;i<permissions_all.length;i++){
            int result= ContextCompat.checkSelfPermission(getContext(),permissions_all[i]);
            if(result== PackageManager.PERMISSION_GRANTED){return true;
            }
            else {
                return false;
            }
        }
        return true;
    }

    private void getDeviceLocation() {

        locationManager=(LocationManager)getActivity().getSystemService(Service.LOCATION_SERVICE);
        isGpsLocation=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        isNetworklocation=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(!isGpsLocation ){
            showSettingForLocation();
//            fetchLastLocation();
        }
        else{
            fetchLastLocation();

        }
    }

    private void showSettingForLocation() {
        AlertDialog.Builder al=new AlertDialog.Builder(getContext());
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

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {

        if (direction.isOK()) {
            if (getContext()!= null)
            {
                if (flag)
                {
                      if (progressDialog!= null && progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }
                    if (isStarted)
                    {

                        erasePolylines();
//                        ArrayList<Route> route = (ArrayList<Route>) direction.getRouteList();
                        ArrayList<LatLng> directionPositionList = new ArrayList<>();
                        Route route = direction.getRouteList().get(0);


//                        for (int i = 0; i<route.size(); i++)
//                        {
//                            for (int j= 0;j<route.get(i).getLegList().size(); j++)
//                            {
                                directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                Polyline polyline = mMap.addPolyline(DirectionConverter.createPolyline(getContext(), directionPositionList, 4, getResources().getColor(R.color.dark_blue)));
                                polylines.add(polyline);
//                            }
//                        }
                    }
                    else
                    {
                        erasePolylines();
                        ArrayList<Route> route = (ArrayList<Route>) direction.getRouteList();
                        ArrayList<LatLng> directionPositionList = new ArrayList<>();

                        for (int i = 0; i<route.size(); i++)
                        {
                            for (int j= 0;j<route.get(i).getLegList().size(); j++)
                            {
                                directionPositionList = route.get(i).getLegList().get(j).getDirectionPoint();
                                Polyline polyline = mMap.addPolyline(DirectionConverter.createPolyline(getContext(), directionPositionList, 4, getResources().getColor(R.color.light_blue)));
                                polylines.add(polyline);
                            }
                        }
                    }
                }
                else {
                    flag = true;
                    if (progressDialog!= null && progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }
                   erasePolylines();
                    ArrayList<Route> route = (ArrayList<Route>) direction.getRouteList();
                    ArrayList<LatLng> directionPositionList = new ArrayList<>();

                    for (int i = 0; i<route.size(); i++)
                    {
                        for (int j= 0;j<route.get(i).getLegList().size(); j++)
                        {
                            directionPositionList = route.get(i).getLegList().get(j).getDirectionPoint();
                            Polyline polyline = mMap.addPolyline(DirectionConverter.createPolyline(getContext(), directionPositionList, 4, getResources().getColor(R.color.dark_blue)));
                            polylines.add(polyline);
                        }
                    }
                }

            }
        } else
            {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Please try again"+rawBody, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }

    private void getRouteToMarker(LatLng pickupLatLng, LatLng currentlatlangs, List waypoits) {

        isRoutes = true;

        String serverKey = getResources().getString(R.string.google_maps_key);
        if (getContext()!= null)
        {
            if (pickupLatLng != null && currentlatlangs != null){
                GoogleDirection.withServerKey(serverKey)
                        .from(pickupLatLng)
                        .and(waypoits)
                        .to(currentlatlangs)
                        .alternativeRoute(true)
                        .optimizeWaypoints(true)
                        .execute(this);
            }
        }

    }

    private void getRouteToMarkerSingles(LatLng pickupLatLng, LatLng currentlatlangs) {
        flag = true;

        Log.e(TAG, "getRouteToMarkerSingles: "+pickupLatLng+","+ currentlatlangs);
        String serverKey = getResources().getString(R.string.google_maps_key);
        if (getContext()!= null)
        {
            if (pickupLatLng != null && currentlatlangs != null){
                GoogleDirection.withServerKey(serverKey)
                        .from(pickupLatLng)
                        .to(currentlatlangs)
                        .alternativeRoute(false)
                        .optimizeWaypoints(false)
                        .transportMode(TransportMode.DRIVING)
                        .execute(this);
            }
        }

    }

    public class LocationListner extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle= intent.getExtras();

            if (bundle != null)
            {
                Double lats, langs;
                String navigates,started,done;
                int position;

                lats = bundle.getDouble("lat");
                langs = bundle.getDouble("lang");
                navigates = bundle.getString("navigates");
                started = bundle.getString("started");
                done = bundle.getString("done");
                position = bundle.getInt("position");

                if (navigates != null && !navigates.isEmpty())
                {

                    erasePolylines();
                    mMap.clear();

                    if (currentlocation!= null)
                    {  LatLng latLngs = new LatLng(lats, langs);
                        LatLng currents = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());


                        ArrayList<Locations> places = new ArrayList<Locations>();

//                        places.add(new Locations(lats, langs,locationsArrayList.get(position).getName(), locationsArrayList.get(position).getName()));

                        for (int j= 0; j<locationsArrayList.size(); j++)
                        {


                                Locations locations = new Locations(locationsArrayList.get(j).getLat(), locationsArrayList.get(j).getLng(),locationsArrayList.get(j).getName(), locationsArrayList.get(j).getAddress());
                                places.add(locations);


                        }

                        Collections.sort(places, new SortPlaces(latLngs));

                        for (Locations p: places){
                            Log.i(TAG, "Place: " + p.getName());
                        }

                        locationsArrayList.clear();

                        start_tvs.setText(places.get(0).getName());
                        end_tvs.setText(places.get(places.size()-1).getName());

                            for (int i = 0; i < places.size(); i++)
                            {
                                locationsArrayList.add(places.get(i));
                                int a = i+1;
                                textView.setText(String.valueOf(a));
                                generator.setContentView(markerView);
                                generator.setBackground(null);
                                Bitmap icon = generator.makeIcon();
                                LatLng location = new LatLng(places.get(i).getLat(), places.get(i).getLng());
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(location).visible(true)
                                        .icon(BitmapDescriptorFactory.fromBitmap(icon));
                                markerOptions.title("" + a);
                                if(i==0)
                                {
                                    mMap.animateCamera(CameraUpdateFactory.newLatLng(currents));
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currents, 14.0f));
                                }
                                mMap.addMarker(markerOptions);

                            }

                            locationAdapter.notifyDataSetChanged();
                            list_recycler.scrollToPosition(0);




                        List<LatLng> wayPoints = new ArrayList<>();
                        wayPoints.add(latLngs);
                        for (int j= 0; j<places.size(); j++)
                        {

                            if (j != position)
                            {
                                LatLng locations = new LatLng(places.get(j).getLat(), places.get(j).getLng());
                                wayPoints.add(locations);
                                Log.d(TAG, "onReceive: "+locations);
                            }

                        }


                        LatLng locations = new LatLng(places.get(places.size()-1).getLat(), places.get(places.size()-1).getLng());

                        getRouteToMarker(currents, locations,wayPoints);

//                        getRouteToMarkerSingles(currents, latLngs);
                    }
                    else {
                        Toast.makeText(context, "Please check your location", Toast.LENGTH_SHORT).show();
                    }


                }
                else if (started!= null && !started.isEmpty())
                {


                        if (position!= 1)
                        {

                            LatLng currents = new LatLng(lats, langs);
                            LatLng latLngs  =  new LatLng(locationsArrayList.get(position-1).getLat(), locationsArrayList.get(position-1).getLng());
//                            getRouteToMarkerSingles(latLngs,currents);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(currents));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currents, 16.0f));


                            String url = "http://maps.google.com/maps?saddr=" + latLngs.latitude + "," + latLngs.longitude+" &daddr="+lats+","+langs;
                            Intent intents = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            intents.setPackage("com.google.android.apps.maps");
                            startActivity(intents);
                        }
                        else {
                           isStarted = true;
                           if(currentlocation!= null)
                           {
                               LatLng currents = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
                               LatLng latLngs  =  new LatLng(locationsArrayList.get(position).getLat(), locationsArrayList.get(position).getLng());
                               getRouteToMarkerSingles(currents,latLngs);

                               String url = "http://maps.google.com/maps?saddr=" + currents.latitude + "," + currents.longitude+" &daddr="+latLngs.latitude + "," + latLngs.longitude;
                               Intent intents = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                               intents.setPackage("com.google.android.apps.maps");
                               startActivity(intents);
                           }
                    }

                }
                else if (done!= null && !done.isEmpty())
                {
//                    if (position!= locationsArrayList.size()-1)
//                    {
                       for (Polyline polyline: polylines)
                       {
                           polyline.remove();
                       }
                       polylines.clear();

                        if (position!= 1)
                        {

                            if (position!= locationsArrayList.size()-1) {
                                start_tvs.setText(locationsArrayList.get(position).getName());
                                end_tvs.setText(locationsArrayList.get(position+1).getName());
                                list_recycler.scrollToPosition(position+1);
                                LatLng currents = new LatLng(lats, langs);
                                LatLng latLngs = new LatLng(locationsArrayList.get(position + 1).getLat(), locationsArrayList.get(position + 1).getLng());
                                mMap.animateCamera(CameraUpdateFactory.newLatLng(currents));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currents, 16.0f));
                                getRouteToMarkerSingles(currents, latLngs);
                            }

                        }
                        else {
                            start_tvs.setText(locationsArrayList.get(position).getName());
                            end_tvs.setText(locationsArrayList.get(position+1).getName());

                            list_recycler.scrollToPosition(position+1);
                            LatLng currents = new LatLng(lats, langs);
                            LatLng latLngs = new LatLng(locationsArrayList.get(position + 1).getLat(), locationsArrayList.get(position + 1).getLng());
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(currents));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currents, 16.0f));
                            getRouteToMarkerSingles(currents, latLngs);

//                            LatLng currents = new LatLng(currentlocation.getLatitude(), currentlocation.getLongitude());
//                            LatLng latLngs  =  new LatLng(locationsArrayList.get(position).getLat(), locationsArrayList.get(position).getLng());
//                            getRouteToMarkerSingles(currents,latLngs);
                        }

//                    }
                }
                else {
                    LatLng latLngs = new LatLng(lats, langs);
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLngs));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs, 16.0f));
                }
            }

        }
    }

    private void fetchLastLocation() {

        @SuppressLint("MissingPermission") Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null)
                {

                    currentlocation = location;

                }
            }
        });
    }

    private void erasePolylines(){
        for(Polyline line : polylines){
            line.remove();
        }
        polylines.clear();
    }

    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        mMap.addMarker(markerOptions);
    }

}