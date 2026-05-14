package ae.okhaz.boss.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import ae.okhaz.boss.Adapter.OrderAdapter;
import ae.okhaz.boss.Model.Counts;
import ae.okhaz.boss.Model.Locations;
import ae.okhaz.boss.Model.Order;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.DetectConnection;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.rests.Response.ResponseOrderList;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ae.okhaz.boss.view.fragments.DashboardFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryRecyclerFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {


    ProgressBar order_pb;
    RecyclerView order_recycler;
    View view;

    ArrayList<Order> orderArrayList;
    private BottomSheetBehavior sheetBehavior, sheetBehavior2;
    private LinearLayout bottom_sheet,bottom_sheet2 ;
    CardView sort_tv, filter_tv;
    CoordinatorLayout coordinates;
    public int productLimitCount=0;
    boolean isLoading = false;
    int offset = 0;
    RecyclerView.OnScrollListener onScrollListener;
    OrderAdapter orderAdapter;
    SessionManagement sessionManagement;
    TextView no_data_txt;
    HashMap user;
    String nams="";
    int pos ;
    LinearLayout offline_layout;
    Spinner status_spiner;
    TextView start_date_tv,end_date_tv,txt_apply,txt_reset;
    EditText edt_order_id;
    ArrayList<Locations> locationsArrayList;
    String m,id;
    FloatingActionButton location_fab,fab_deli_boy_info;
    DatePickerDialog.OnDateSetListener setstartListner, setendListner;
    String condition ="",  OrderID ="",  startDate ="",   endDate ="",deliveryMan="",
            branchID="",role="";

    Filters filters;
    LinearLayout filters_layout;
    Animation animation;
    ArrayList<Counts>countsArrayList;

    RadioGroup shortRadioGroup;
    View viewShortBottomSheet,viewFilterBottomSheet;
    BottomSheetDialog dialogShortBottomSheet,dialogFilterBottomSheet;

    TextView tvDateFrom,tvDateTo;
    Dialog bottomSheetDialog;
    String sort="";

    String dateFrom="",dateTo="",startDateFilter="",endDateFilter="";

    private int mStartYear, mStartMonth, mStartDay, mHour, mMinute,mEndYear, mEndMonth, mEndDay;

    Dialog progressDialog;

    public static CategoryRecyclerFragment newInstance(String name, int position,String id) {
        Bundle args = new Bundle();
        args.putString("code", name);
        args.putInt("position", position);
        args.putString("id", id);
        CategoryRecyclerFragment f = new CategoryRecyclerFragment();
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category_recycler, container, false);

         nams =  getArguments().getString("code");
         id =  getArguments().getString("id");
         pos =  getArguments().getInt("position");
        onScrollListener = null;
        filters = new Filters();
         animation = AnimationUtils.loadAnimation(getContext(), R.anim.simple_grow);

        progressDialog = new  Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(getContext()).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);

        //MainActivity.txt_mainOrder_number.setVisibility(View.GONE);
        //MainActivity.txt_mainOrder_number.setVisibility(View.GONE);

        MainDrawerBackActivity.tv_toolbar_morder.setVisibility(View.GONE);
        MainDrawerBackActivity.tv_toolbar_title.setText("Order List");
        MainDrawerBackActivity.img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));
        MainDrawerBackActivity.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((MainDrawerBackActivity)getActivity())!=null)
                    ((MainDrawerBackActivity)getActivity()).hideShowMenu();

            }
        });

        sessionManagement = SessionManagement.getInstance(getContext());
        user = sessionManagement.getUserDetails();
        deliveryMan = user.get(SessionManagement.KEY_DELIVERY_MAN).toString();
        role = user.get(SessionManagement.KEY_USER_TYPE).toString();
        branchID = user.get(SessionManagement.KEY_BRANCH_ID).toString();
        location_fab = view.findViewById(R.id.location_fab);
        order_pb = view.findViewById(R.id.order_pb);
        //order_pb.setVisibility(View.VISIBLE);
        //Tools.loading(getActivity()).show();
        progressDialog.show();
        order_recycler = view.findViewById(R.id.order_recycler);
        coordinates = view.findViewById(R.id.coordinates);
        no_data_txt = view.findViewById(R.id.no_data_txt);
        offline_layout = view.findViewById(R.id.offline_layout);
        filters_layout = view.findViewById(R.id.filters_layout);
        locationsArrayList = new ArrayList<>();
        countsArrayList = new ArrayList<>();
        orderArrayList = new ArrayList<>();

        if (nams.equals("4"))
        {
            nams = "4-11";
        }

        final Calendar c = Calendar.getInstance();
        mStartYear = c.get(Calendar.YEAR);
        mStartMonth = c.get(Calendar.MONTH);
        mStartDay = c.get(Calendar.DAY_OF_MONTH);

        mEndYear = c.get(Calendar.YEAR);
        mEndMonth = c.get(Calendar.MONTH);
        mEndDay = c.get(Calendar.DAY_OF_MONTH);

        /*Set default DateFrom to one week before date*/
        startDateFilter = getCalculatedDate("dd/MM/yyyy",-7);
        dateFrom = getCalculatedDate("yyyy/MM/dd",-7);
        startDate =dateFrom;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date d = sdf.parse(startDateFilter);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            mStartYear = Integer.parseInt(checkDigit(cal.get(Calendar.YEAR)));
            mStartMonth = Integer.parseInt(checkDigit(cal.get(Calendar.MONTH)));
            mStartDay = Integer.parseInt(checkDigit(cal.get(Calendar.DATE)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Set current date to DateTo as default date*/
        endDateFilter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        dateTo = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(new Date());
        endDate = dateTo;

        if(user.get(SessionManagement.KEY_LOIGIN_STATUS).toString().equals("0"))
        {
            offline_layout.setVisibility(View.VISIBLE);
            order_recycler.setVisibility(View.GONE);
            no_data_txt.setVisibility(View.GONE);
           // order_pb.setVisibility(View.GONE);
           // Tools.loading(getActivity()).dismiss();
            progressDialog.dismiss();
        }
        else {
            offline_layout.setVisibility(View.GONE);
            //order_pb.setVisibility(View.VISIBLE);
            //Tools.loading(getActivity()).show();
            progressDialog.show();
            getOrdersData(id,branchID,nams,"0","","",startDate,endDate);
        }

        if (nams.equals("6"))
        {
//            ((MainActivity)getActivity()).ShowsLoction();
            location_fab.setVisibility(View.VISIBLE);

        }
        else {
//            ((MainActivity)getActivity()).HideLocations();
            location_fab.setVisibility(View.GONE);
        }



        location_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderArrayList.size()>0)
                {
                    String orders = new Gson().toJson(orderArrayList);
                    Bundle bundle= new Bundle();
                    bundle.putString("list",orders);
                    bundle.putInt("admin",0);
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.goto_map, bundle);
                    location_fab.setVisibility(View.GONE);
                }
                else
                {
                    Toast.makeText(getContext(), "You have no orders to deliver", Toast.LENGTH_SHORT).show();
                }

            }
        });







//        ArrayList<Order> temp = new ArrayList();
//
//        if (nams.equals("All"))
//        {
//            temp  = orderArrayList;
//        }
//        else {
//            for(Order d: orderArrayList){
//                //or use .equal(text) with you want equal match
//                //use .toLowerCase() for better matches
//                if(d.getOrderStatus().equals(nams)){
//                    temp.add(d);
//                }
//            }
//        }

      //  bottom_sheet = view.findViewById(R.id.bottom_lins);
      //  bottom_sheet2 = view.findViewById(R.id.sort_lins);
        sort_tv = view.findViewById(R.id.sort_tv);
        filter_tv = view.findViewById(R.id.filter_tv);

        //Short
        viewShortBottomSheet = getLayoutInflater().inflate(R.layout.item_sort_layout, null);
        shortRadioGroup = viewShortBottomSheet.findViewById(R.id.rgShort);
        dialogShortBottomSheet = new BottomSheetDialog(getActivity());

        //Filter
        viewFilterBottomSheet = getLayoutInflater().inflate(R.layout.item_filter_layout, null);
        // shortRadioGroup = viewShortBottomSheet.findViewById(R.id.rgF);
        dialogFilterBottomSheet = new BottomSheetDialog(getActivity());


        /**Short click listener*/
        sort_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog=new Dialog(getContext(),R.style.CustomDialog);
                bottomSheetDialog.setContentView(R.layout.item_sort_layout);
                int width = (int)(getResources().getDisplayMetrics().widthPixels*1);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*1);

                shortRadioGroup = bottomSheetDialog.findViewById(R.id.rgShort);
                RadioButton DOTR = bottomSheetDialog.findViewById(R.id.DOTR);
                RadioButton DRTO = bottomSheetDialog.findViewById(R.id.DRTO);
                RadioButton OOTR = bottomSheetDialog.findViewById(R.id.OOTR);
                RadioButton ORTO = bottomSheetDialog.findViewById(R.id.ORTO);
                bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_color)));
                bottomSheetDialog.show();
                bottomSheetDialog.setCancelable(true);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                if(sort.equals("DateOTR"))
                    DOTR.setChecked(true);
                if(sort.equals("DateRTO"))
                    DRTO.setChecked(true);
                if(sort.equals("OrderOTR"))
                    OOTR.setChecked(true);
                if(sort.equals("OrderRTO"))
                    ORTO.setChecked(true);

                /**Set Short Radio group check change listener */
                shortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {

                        bottomSheetDialog.dismiss();

                        if (i == R.id.DOTR) {
                            send("DateOTR");
                            UpdateSessions("DOTR");
                            sort = "DateOTR";

                        } else if (i == R.id.DRTO) {
                            send("DateRTO");
                            UpdateSessions("DRTO");
                            sort = "DateRTO";

                        } else if (i == R.id.OOTR) {
                            send("OrderOTR");
                            UpdateSessions("OOTR");
                            sort = "OrderOTR";

                        } else if (i == R.id.ORTO) {
                            send("OrderRTO");
                            UpdateSessions("ORTO");
                            sort = "OrderRTO";
                        }

                        RadioButton checkedRadioButton = shortRadioGroup.findViewById(i);
                        if (checkedRadioButton != null && checkedRadioButton.isChecked()) {
                            checkedRadioButton.setChecked(true);
                            bottomSheetDialog.dismiss();
                        }
                    }
                });

               /* dialogShortBottomSheet.setContentView(viewShortBottomSheet);
                dialogShortBottomSheet.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white_trans)));

               // dialogShortBottomSheet.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                dialogShortBottomSheet.show();*/
            }
        });

        /**Filter click listener*/
        filter_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialButton btn_filter_apply;

                bottomSheetDialog=new Dialog(getContext(),R.style.CustomDialog);
                bottomSheetDialog.setContentView(R.layout.item_filter_layout);

                bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_color)));
                bottomSheetDialog.show();
                bottomSheetDialog.setCancelable(true);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                edt_order_id = bottomSheetDialog.findViewById(R.id.edt_order_id);
                status_spiner = bottomSheetDialog.findViewById(R.id.status_spiner);
                end_date_tv = bottomSheetDialog.findViewById(R.id.end_date_tv);
                start_date_tv = bottomSheetDialog.findViewById(R.id.start_date_tv);
                txt_reset = bottomSheetDialog.findViewById(R.id.resetFilter);
                btn_filter_apply = bottomSheetDialog.findViewById(R.id.btn_filter_apply);

                start_date_tv.setText(startDateFilter);

                end_date_tv.setText(endDateFilter);


                /*Date From click*/
                start_date_tv.setOnClickListener(view1 -> {

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    startDateFilter = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    start_date_tv.setText(startDateFilter);

                                    dateFrom= year+ "/" + (monthOfYear + 1) + "/" + dayOfMonth;

                                    mStartYear = year;
                                    mStartMonth = monthOfYear;
                                    mStartDay=dayOfMonth;
                                }
                            }, mStartYear, mStartMonth, mStartDay);
                    datePickerDialog.show();
                });

                /*Date To click*/
                end_date_tv.setOnClickListener(view1 -> {

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    endDateFilter=dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                    end_date_tv.setText(endDateFilter);
                                    dateTo= year+ "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                                    mEndYear = year;
                                    mEndMonth = monthOfYear;
                                    mEndDay=dayOfMonth;
                                }
                            }, mEndYear, mEndMonth, mEndDay);
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();
                });

                /*Reset*/
                txt_reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("filter");
                        intent.putExtra("orderId","");
                        intent.putExtra("startdate","");
                        intent.putExtra("endDate","");
                        intent.putExtra("status","");
                        intent.putExtra("type","filter");
                        getContext().sendBroadcast(intent);
                    }
                });

                /*Apply*/
                btn_filter_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (pos == 0)
                        {
                            sessionManagement.UpdateAllFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                        }
                        else if (pos == 1)
                        {
                            sessionManagement.UpdatePlacedFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                        }
                        else if (pos == 2)
                        {
                            sessionManagement.UpdatePendingFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                        }
                        else if (pos == 11)
                        {
                            sessionManagement.UpdateProcessingFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                        }
                        else if (pos == 3)
                        {
                            sessionManagement.UpdateAcceptedFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                        }
                        else if (pos == 6)
                        {
                            sessionManagement.UpdateOntheWayFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                        }
                        else if (pos == 4)
                        {
                            sessionManagement.UpdateReadyPickFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                        }
                        else if (pos == 7)
                        {
                            sessionManagement.UpdateDeliveredFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                        }
                        else if (pos == 9)
                        {
                            sessionManagement.UpdateCancelledFilter(edt_order_id.getText().toString().trim(),start_date_tv.getText().toString(),end_date_tv.getText().toString(),status_spiner.getSelectedItem().toString());
                        }

                        Intent intent = new Intent("filter");
                        intent.putExtra("orderId",edt_order_id.getText().toString().trim());
                        intent.putExtra("startdate",start_date_tv.getText().toString().equals("Add dates")?"":dateFrom);
                        intent.putExtra("endDate",end_date_tv.getText().toString().equals("Add dates")?"":dateTo);
                        intent.putExtra("status",status_spiner.getSelectedItem().toString());
                        intent.putExtra("type","filter");
                        getContext().sendBroadcast(intent);

                    }
                });
            }
        });

        return view;
    }

    private void UpdateSessions(String dotr) {

        if (pos == 0)
        {
            sessionManagement.UpdateAllSort(dotr);
        }
        else if (pos == 1)
        {
            sessionManagement.UpdatePendingSort(dotr);
        }
        else if (pos == 2)
        {
            sessionManagement.UpdateProcessingSort(dotr);
        }
        else if (pos == 3)
        {
            sessionManagement.UpdateOntheWaySort(dotr);
        }
        else if (pos == 4)
        {
            sessionManagement.UpdateDeliveredSort(dotr);
        }
        else if (pos == 5)
        {
            sessionManagement.UpdateCancelSort(dotr);
        }
    }

    public void send(String msg)
    {
        Intent intent = new Intent("filter");
        intent.putExtra("btn",msg);
        intent.putExtra("type","sort");
        getContext().sendBroadcast(intent);

        // dismiss();
    }

    private void getOrdersData(String deliveryman, String BranchCode, String OrderStatus, String offset, String condition, String OrderID, String startDate,  String endDate) {


        //order_pb.setVisibility(View.VISIBLE);
       // Tools.loading(getActivity()).show();
        String date_range= DashboardFragment.date_range;
        progressDialog.show();

        ServiceGenerator.getDelivery().getOrders(deliveryman, BranchCode,
                OrderStatus, offset, condition, OrderID, startDate, endDate,date_range).enqueue(new Callback<ResponseOrderList>() {
            @Override
            public void onResponse(Call<ResponseOrderList> call, Response<ResponseOrderList> response) {
                //order_pb.setVisibility(View.GONE);
                //Tools.loading(getActivity()).dismiss();
                progressDialog.dismiss();
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        orderArrayList.clear();
                        orderArrayList = response.body().getResult().getOrderList();
                       Counts counts = response.body().getCounts();

                        String count = new Gson().toJson(counts);
                        Intent intent = new Intent("home");
                        intent.putExtra("counts", count);
                        getActivity().sendBroadcast(intent);


                        if (orderArrayList.size()>0)
                        {
                            order_recycler.setVisibility(View.VISIBLE);
                            no_data_txt.setVisibility(View.GONE);


                            setRecycler(orderArrayList);
                            initScrollListener(deliveryman,BranchCode, OrderStatus, condition, OrderID, startDate, endDate);
                        }
                        else {
                            order_recycler.setVisibility(View.GONE);
                            no_data_txt.setVisibility(View.VISIBLE);
                        }


                    }
                    else {
                        //order_pb.setVisibility(View.GONE);
                        //Tools.loading(getActivity()).dismiss();
                        progressDialog.dismiss();
                        order_recycler.setVisibility(View.GONE);
                        no_data_txt.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    //order_pb.setVisibility(View.GONE);
                    //Tools.loading(getActivity()).dismiss();
                    progressDialog.dismiss();
                    order_recycler.setVisibility(View.GONE);
                    no_data_txt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseOrderList> call, Throwable t) {
                //order_pb.setVisibility(View.GONE);
                //Tools.loading(getActivity()).dismiss();
                progressDialog.dismiss();

                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    getOrdersData(id, BranchCode, OrderStatus, offset, condition, OrderID, startDate, endDate);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    getOrdersData(id, BranchCode, OrderStatus, offset, condition, OrderID, startDate, endDate);

                }
                else
                {

                    order_recycler.setVisibility(View.GONE);
                    no_data_txt.setVisibility(View.VISIBLE);
                }


            }
        });





    }

    private void setRecycler(ArrayList<Order> orderArrayList) {
        order_recycler.setHasFixedSize(true);
        order_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
         orderAdapter = new OrderAdapter(orderArrayList, getContext(),pos);
        order_recycler.setAdapter(orderAdapter);
        //order_pb.setVisibility(View.GONE);
        //Tools.loading(getActivity()).dismiss();
        progressDialog.dismiss();
    }

    private void initScrollListener(String deliveryman, String branchCode, String orderStatus, String condition, String orderID, String startDate, String endDate) {

        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager gridLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() == orderArrayList.size() - 1) {
                        //bottom of list!
                        if (DetectConnection.checkInternetConnection(getContext())) {
                            try {


                                Handler handler = new Handler();

                                final Runnable r = new Runnable() {
                                    public void run() {

                                        orderArrayList.add(null);
                                        orderAdapter.notifyItemInserted(orderArrayList.size() + 1);
                                        final int scrollPosition = orderArrayList.size();

                                        int currentSize = scrollPosition;
                                        productLimitCount = currentSize-1;
                                        loadMore(productLimitCount,deliveryman,branchCode,orderStatus, condition, orderID, startDate, endDate);
                                        isLoading = true;
                                    }
                                };
                                handler.post(r);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();

                        }

                    }
                }


                if (dy > 0) {
                    //Scrolling down.
                    filters_layout.setVisibility(View.GONE);
//                    ((MainActivity)getActivity()).toolbar.setVisibility(View.GONE);


                }
                else if (dy < 0) {
                    //​Scrolling up.
                    filters_layout.setVisibility(View.VISIBLE);
//                    ((MainActivity)getActivity()).toolbar.setVisibility(View.VISIBLE);
                }
            }
        };


        order_recycler.setOnScrollListener(onScrollListener);



    }



    private void loadMore(final int scrollPosition, String deliveryman, String branchCode, String orderStatus, String condition, String orderID, String startDate, String endDate) {
        String date_range= DashboardFragment.date_range;
        ServiceGenerator.getDelivery().getOrders(deliveryman, branchCode, orderStatus, String.valueOf(scrollPosition),
                condition,orderID,startDate,endDate,date_range).enqueue(new Callback<ResponseOrderList>() {
            @Override
            public void onResponse(Call<ResponseOrderList> call, Response<ResponseOrderList> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        orderArrayList.remove(orderArrayList.size()-1);
                        orderAdapter.notifyItemRemoved(scrollPosition);
                        List<Order> tempList = response.body().getResult().getOrderList();
                        orderArrayList.addAll(tempList);
                        orderAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                    else {
                        orderArrayList.remove(orderArrayList.size()-1);
                        orderAdapter.notifyItemRemoved(scrollPosition);
                        isLoading = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLoading = false;
                            }
                        }, 2000);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseOrderList> call, Throwable t) {
                orderArrayList.remove(orderArrayList.size()-1);
                orderAdapter.notifyItemRemoved(scrollPosition);
                isLoading = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                    }
                }, 2000);

            }
        });
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


        String wall =sharedPreferences.getString("loginstatus","");

        if(wall.equals("0"))
        {
            offline_layout.setVisibility(View.VISIBLE);
            order_recycler.setVisibility(View.GONE);
            no_data_txt.setVisibility(View.GONE);
            //order_pb.setVisibility(View.GONE);
            //Tools.loading(getActivity()).dismiss();
            progressDialog.dismiss();
        }
        else {
            offline_layout.setVisibility(View.GONE);
            order_recycler.setVisibility(View.VISIBLE);
            getOrdersData(user.get(SessionManagement.KEY_ID).toString(),user.get(SessionManagement.KEY_BRANCH_ID).toString(),nams,"0",condition,OrderID,startDate, endDate);

        }

    }


    @Override
    public void onStart() {
        super.onStart();

        getActivity().getSharedPreferences("qtp_del_sf",0)
                .registerOnSharedPreferenceChangeListener(this);

               // ((MainActivity)getActivity()).CheckDeliveryMan();

        IntentFilter intentFilter = new IntentFilter("filter");

        getActivity().registerReceiver(filters,intentFilter);

    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSharedPreferences("qtp_del_sf",0)
                .unregisterOnSharedPreferenceChangeListener(this);

        getActivity().unregisterReceiver(filters);
    }




    public class Filters extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle!= null)
            {
                String  type ="",status="";



                type = bundle.getString("type");

                if (type.equals("filter"))
                {
                    if(bottomSheetDialog!=null)
                        bottomSheetDialog.dismiss();
                    OrderID = bundle.getString("orderId");
                    startDate = bundle.getString("startdate");
                    endDate = bundle.getString("endDate");
                    status = bundle.getString("status");

                    if (status.equals("All"))
                    {
                        nams = "All";
                    }
                    else  if (status.equals("Pending"))
                    {
                        nams="8";
                    }
                    else  if (status.equals("Processing"))
                    {
                        nams="4-11";
                      //  nams="5";
                    }
                    else  if (status.equals("Ontheway"))
                    {
                        nams="6";
                    }
                    else  if (status.equals("Delivered"))
                    {
                        nams="7";
                    }
                    else  if (status.equals("Canceled"))
                    {
                        nams="9";
                    }


                    getOrdersData( id,  branchID, nams, "0",  condition,  OrderID, startDate,  endDate);
                }
                else if (type.equals("sort")){

                    condition  = bundle.getString("btn");
                    getOrdersData( id,  branchID, nams, "0",  condition,  OrderID, startDate,  endDate);

                }

            }

        }
    }

    public static String getCalculatedDate(String dateFormat, int days) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(dateFormat);
        cal.add(Calendar.DAY_OF_YEAR, days);
        return s.format(new Date(cal.getTimeInMillis()));
    }

    public String checkDigit (int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }
}