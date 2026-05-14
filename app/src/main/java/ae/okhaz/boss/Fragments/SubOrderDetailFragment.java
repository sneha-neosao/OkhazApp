package ae.okhaz.boss.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.maps.android.ui.IconGenerator;
import ae.okhaz.boss.Activitys.EditDetailsActivity;
import ae.okhaz.boss.Adapter.Admin.AdminProductAdapter;
import ae.okhaz.boss.Adapter.CommentsAdapter;
import ae.okhaz.boss.Model.Comments;
import ae.okhaz.boss.Model.DeliveryBoyModel;
import ae.okhaz.boss.Model.OrderStatus;
import ae.okhaz.boss.Model.ProductListModel;
import ae.okhaz.boss.Model.Products;
import ae.okhaz.boss.Model.SearchLocations;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.DetectConnection;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.Utils.Validations;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.Response.ResponseDeliveryBoyList;
import ae.okhaz.boss.rests.Response.ResponseOrderStatus;
import ae.okhaz.boss.rests.ServiceGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class SubOrderDetailFragment extends Fragment {

    private GoogleMap mMap;
    String from = "", locs = "";
    SearchLocations searchLocations;
    boolean flag = false;
    IconGenerator generator;
    ArrayList<OrderStatus> orderStatuses;
    EditText edt_search;
    ImageView img_close;
    ListView listView;
    boolean canGetLocation = false;
    ArrayList<DeliveryBoyModel> deliveryBoyModelArrayList;
    ArrayList<String> currentDBarrayList=new ArrayList<>();
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
    public Location location;
    double latitude = 0.0, longitude = 0.0;

    SessionManagement sessionManagement;
    HashMap user;
    ProgressBar orderS_pb;
    View view;
    Button btn_edt_scan;
    ImageView img_barcode_switch;
    boolean barcode_switch=true;
    EditText edt_barcode_text,edt_type_barcode_text;
    RecyclerView products_rv,comments_rv;
    AdminProductAdapter productAdapter;
    Button reject_btn,deliver_btn,pickedup_btn,pickedup_location_btn,processing_btn,
            download_print_btn;
    LinearLayout processing_order_view,pending_order_view;
    ArrayList<Products> productsArrayList;
    ArrayList<ProductListModel> productList;
    ArrayList<Comments> commentList=new ArrayList<>();
    String status="",str="";
    int orderID;
    ProductListModel order;
    //RelativeLayout comments_relative;
    LinearLayout comments_relative;
    String cancel="",primaryOrderNumber,mainOrderID;
    ImageView status_img,img_arrow_up,img_arrow_down;
    CardView navigation_cv,call_iv,email_iv,
            cv_select_orderStatus,cv_select_deliBoy,cv_order_comments;
    //CardView navigation_cv;
    int pos;
    Calendar calendar;
    TextInputEditText comment_edts;
    SimpleDateFormat input, news;
    OrderReceiver orderReceiver;
    ArrayList<String> deliList;
    ArrayList<String> orderStatList;
    ArrayAdapter<String> adapter;
    CommentsAdapter commentsAdapter;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    String[] permission_storage= {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_WRITE_PERMISSION = 1;
    private  final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    TextView txt_dialogName,tv_cust_nmae,tv_cust_address,tv_cust_contact,tv_cust_email,price,delivery,amountPayable,
    txt_promo, tax,order_price_tv,order_qty_tv,order_statuses_tv,time_tv,discount,txt_edit,txt_paid,txt_order_no;

    LinearLayout ll_customer_detail;
    RelativeLayout rl_order_summary;

    TextView mo_txt_order_no,btn_select_deliBoy,btn_select_orderStatus,add_comment;

    //Button btn_select_deliBoy,btn_select_orderStatus,add_comment;
    private int mYear, mMonth, mDay, mHour, mMinute;

    Dialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_sub_order_detail1, container, false);
        ll_customer_detail = view.findViewById(R.id.ll_customer_details);
        rl_order_summary = view.findViewById(R.id.rl_order_summary);
        products_rv = view.findViewById(R.id.products_rv);
        progressBar = view.findViewById(R.id.pb_details);
        relativeLayout = view.findViewById(R.id.rl_detail);
        btn_select_deliBoy = view.findViewById(R.id.btn_select_deliBoy);
        btn_select_orderStatus = view.findViewById(R.id.btn_select_orderStatus);
        txt_paid = view.findViewById(R.id.txt_paid);
        txt_order_no = view.findViewById(R.id.txt_order_no);
        comments_rv = view.findViewById(R.id.comments_rv);
        processing_order_view = view.findViewById(R.id.processing_order_view);
        pending_order_view = view.findViewById(R.id.pending_order_view);
        tv_cust_nmae = view.findViewById(R.id.tv_cust_nmae);
        tv_cust_address = view.findViewById(R.id.tv_cust_address);
        tv_cust_contact = view.findViewById(R.id.tv_cust_contact);
        price = view.findViewById(R.id.price);
        tv_cust_email = view.findViewById(R.id.tv_cust_email);
        delivery = view.findViewById(R.id.delivery);
        amountPayable = view.findViewById(R.id.amountPayable);
        tax = view.findViewById(R.id.tax);
        txt_promo = view.findViewById(R.id.promocode);
        status_img = view.findViewById(R.id.status_img);
        order_price_tv = view.findViewById(R.id.order_price_tv);
        order_qty_tv = view.findViewById(R.id.order_qty_tv);
        order_statuses_tv = view.findViewById(R.id.order_statuses_tv);
        time_tv = view.findViewById(R.id.time_tv);
        reject_btn = view.findViewById(R.id.reject_btn);
        deliver_btn = view.findViewById(R.id.deliver_btn);
        add_comment = view.findViewById(R.id.add_comment);
        download_print_btn = view.findViewById(R.id.download_print_btn);
        pickedup_location_btn = view.findViewById(R.id.pickedup_location_btn);
        pickedup_btn = view.findViewById(R.id.pickedup_btn);
        processing_btn = view.findViewById(R.id.processing_btn);
        comments_relative = view.findViewById(R.id.comments_relative);
        discount = view.findViewById(R.id.discount);
        call_iv = view.findViewById(R.id.call_iv);
        //sms_iv = view.findViewById(R.id.sms_iv);
        email_iv = view.findViewById(R.id.email_iv);
        navigation_cv = view.findViewById(R.id.navigation_cv);
        comment_edts = view.findViewById(R.id.comment_edts);
        orderS_pb = view.findViewById(R.id.orderS_pb);
        img_arrow_down=view.findViewById(R.id.img_arrow_down);
        img_arrow_up=view.findViewById(R.id.img_arrow_up);
        edt_barcode_text=view.findViewById(R.id.edt_barcode_text);
        cv_order_comments=view.findViewById(R.id.cv_order_comments);
        cv_select_deliBoy=view.findViewById(R.id.cv_select_deliBoy);
        cv_select_orderStatus=view.findViewById(R.id.cv_select_orderStatus);
        txt_edit=view.findViewById(R.id.txt_edit);

        edt_barcode_text.requestFocus();
        // ((MainActivity)getActivity()).HideLocations();
        sessionManagement = SessionManagement.getInstance(getContext());
        user = sessionManagement.getUserDetails();
        orderReceiver = new OrderReceiver();
        requestpermissions();
        productsArrayList = new ArrayList<>();
        productList = new ArrayList<>();
        status = getArguments().getString("status");
        orderID = getArguments().getInt("orderID");
        pos = getArguments().getInt("pos");
        str = getArguments().getString("subOrder");
        primaryOrderNumber = getArguments().getString("primaryId");
        mainOrderID = getArguments().getString("mainOrderID");

//        mo_txt_order_no.setText("(M.O #" + mainOrderID+")");

        btn_edt_scan = view.findViewById(R.id.btn_edt_scan);
        edt_type_barcode_text=view.findViewById(R.id.edt_type_barcode_text);
        img_barcode_switch=view.findViewById(R.id.img_barcode_switch);


        progressDialog = new  Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(getContext()).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);

        Log.e("order", "onCreateView: "+str);
        Type typeMyType = new TypeToken<ProductListModel>(){}.getType();
//        planOrderListArrayList = new Gson().fromJson(str, typeMyType);
        order = new Gson().fromJson(str, typeMyType);
        calendar = Calendar.getInstance();
        input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        news = new SimpleDateFormat("dd MMM yyyy");

        productsArrayList=order.getOrderItems();
        setProducts_rv();
        getDeliveryBoyList();
        orderStatList=getOrderStatus();

       /* if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Supplier")){
            txt_order_no.setText("S.O #" + order.getOrderID());
        }
        else {
            txt_order_no.setText("M.O #" + order.getOrderID());
        }*/

        /*AdminOrderDetailActivity1.toolbar.setTitle("#"+orderID);
        AdminOrderDetailActivity1.txt_mainOrder_number.setVisibility(View.VISIBLE);
        AdminOrderDetailActivity1.txt_mainOrder_number.setText("(M.O #" + mainOrderID+")");
        //AdminOrderDetailActivity1.toolbar.setNavigationIcon(R.drawable.ic_back);
        AdminOrderDetailActivity1.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((AdminOrderDetailActivity1)getActivity()).onBackPressed();
            }
        });*/

        if(order.getPromo()!=null){
            txt_promo.setText(order.getPromo());
        }
        else {
            txt_promo.setText("Not Applied");
        }

        MainDrawerBackActivity.tv_toolbar_title.setText("#"+orderID);
        MainDrawerBackActivity.tv_toolbar_morder.setVisibility(View.VISIBLE);
        MainDrawerBackActivity.tv_toolbar_morder.setText(" (M.O #" + mainOrderID+")");
        //MainDrawerBackActivity.txt_mainOrder_number.setVisibility(View.VISIBLE);
        //MainDrawerBackActivity.txt_mainOrder_number.setText("(M.O #" + mainOrderID+")");
        MainDrawerBackActivity.img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_back));
        MainDrawerBackActivity.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainDrawerBackActivity)getActivity()).onBackPressed();
            }
        });
        if (user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Supplier"))
        {

            if(user.get(SessionManagement.KEY_customerDetailSection_On_OD_Available4Supplier).toString().equals("1")){
                //show edit details
                ll_customer_detail.setVisibility(View.VISIBLE);
            }
            else {
                ll_customer_detail.setVisibility(View.GONE);
            }
            if(user.get(SessionManagement.KEY_orderSummarySection_On_OD_Available4Supplier).toString().equals("1")){
                //show order summary
                rl_order_summary.setVisibility(View.VISIBLE);

            }
            else {
                rl_order_summary.setVisibility(View.GONE);
            }
        }

        btn_select_orderStatus.setText(status);

        btn_edt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int index=0;
                boolean flag=true;
                if(!edt_type_barcode_text.getText().toString().equals("")) {
                    for (Products product : productsArrayList) {
                        if (product.getBarcode()!=null && product.getBarcode().equals(edt_type_barcode_text.getText().toString())) {
                            scannedBarcode(product.getOrderItemId(), index);
                            flag=false;
                            Log.e("Barcodem : ", product.getBarcode());

                            break;
                        }
                        index++;
                    }
                    if(flag)
                    {
                        edt_type_barcode_text.setText("");
                        Toast.makeText(getContext(), "This Barcode is not available in this list", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_select_orderStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog dialog = new BottomSheetDialog(getContext(),R.style.SheetDialog);
                dialog.setContentView(R.layout.bottom_layout_delveryboy_list);
                dialog.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                listView=dialog.findViewById(R.id.listView);
                edt_search=dialog.findViewById(R.id.edt_search_deliBoy);
                img_close=dialog.findViewById(R.id.img_close);
                txt_dialogName=dialog.findViewById(R.id.txt_dialogName);
                txt_dialogName.setText("Order Status");
                edt_search.setVisibility(View.GONE);
                adapter=new ArrayAdapter<>(getContext(),R.layout.simple_list_item_single_choice,orderStatList);
                listView.setAdapter(adapter);
                dialog.show();

                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String id=orderStatuses.get(i).getOrderStatusID();
                        String name=orderStatuses.get(i).getOrderStatusName();
                        Log.e("oId",id);
                        Log.e("oName",name);
                        dialog.dismiss();

                        if(name.equals("Cancelled") || name.equals("Rejected"))
                        {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                            // ...Irrelevant code for customizing the buttons and title
                            LayoutInflater inflater = getActivity().getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.layout_update_deliboy, null);
                            dialogBuilder.setView(dialogView);

                            EditText editText = (EditText) dialogView.findViewById(R.id.edt_reason);
                            TextView textView=(TextView) dialogView.findViewById(R.id.txt_reason);
                            Button button=(Button)dialogView.findViewById(R.id.btn_confirm);

                            AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();

                            textView.setText("Why you want to change from Order Status from "+btn_select_orderStatus.getText()+" to "+name +"?");

                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (editText.getText().toString().isEmpty()) {
                                        editText.requestFocus();
                                        editText.setError("Please mention your reason");
                                    } else {
                                        alertDialog.dismiss();
                                        updateOrderStatus(id,name,user.get(SessionManagement.KEY_USER_TYPE).toString(),user.get(SessionManagement.KEY_ID).toString(),
                                                editText.getText().toString());
                                    }
                                }
                            });
                        }
                        else if(order.getOrderStatus()==3&&name.equals("Ready For PickUp")){

                            orderStatusForReadyForPickup(id,name);

                        }else {
                            Log.e("oName",name);
                            updateOrderStatus(id,name,user.get(SessionManagement.KEY_USER_TYPE).toString(),user.get(SessionManagement.KEY_ID).toString(),
                                    "");
                        }
                    }
                });

                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    private int mLastFirstVisibleItem;
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {
                        //  dialogStatus.setCancelable(false);
                    }
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem,
                                         int visibleItemCount, int totalItemCount) {
                        // dialogStatus.setCancelable(false);
                        if(mLastFirstVisibleItem<firstVisibleItem)
                        {
                            Log.i("SCROLLING DOWN","TRUE");
                            // dialogStatus.setCancelable(false);
                            dialog.setCancelable(false);
                        }
                        if(mLastFirstVisibleItem>firstVisibleItem)
                        {
                            Log.i("SCROLLING UP","TRUE");
                            dialog.setCancelable(true);
                        }
                        mLastFirstVisibleItem=firstVisibleItem;
                    }
                });

            }
        });

        BottomSheetDialog dialog = new BottomSheetDialog(getContext(),R.style.SheetDialog);
        dialog.setContentView(R.layout.bottom_layout_delveryboy_list);
        dialog.getWindow().setBackgroundDrawableResource(R.color.white_trans);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        listView=dialog.findViewById(R.id.listView);
        txt_dialogName=dialog.findViewById(R.id.txt_dialogName);
        txt_dialogName.setText("Delivery Boy");
        edt_search=dialog.findViewById(R.id.edt_search_deliBoy);
        img_close=dialog.findViewById(R.id.img_close);



        edt_search.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            public void afterTextChanged(Editable arg0) {
                adapter.getFilter().filter(arg0);
                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String deliID= getSelectedDeliveryMan(currentDBarrayList.get(i));
                String deliName=currentDBarrayList.get(i);
                //String deliName=currentDBarrayList.get(i).getUserName();

                dialog.dismiss();

                String btn_text=btn_select_deliBoy.getText().toString();


                if(!btn_text.equals("Assign Delivery Men")){
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
// ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.layout_update_deliboy, null);
                    dialogBuilder.setView(dialogView);

                    EditText editText = (EditText) dialogView.findViewById(R.id.edt_reason);
                    TextView textView=(TextView) dialogView.findViewById(R.id.txt_reason);
                    Button button=(Button)dialogView.findViewById(R.id.btn_confirm);

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    textView.setText("Why you want to change from Delivery Men "+btn_select_deliBoy.getText()+" to "+deliName +"?");

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (editText.getText().toString().isEmpty()) {
                                editText.requestFocus();
                                editText.setError("Please mention your reason");
                            } else {
                                alertDialog.dismiss();
                                updateDeliveryBoy(deliID, deliName, user.get(SessionManagement.KEY_ID).toString(),
                                        user.get(SessionManagement.KEY_USER_TYPE).toString(), editText.getText().toString());
                            }
                        }
                    });

                }
                else
                {
                    ShowAlertsForAssignDeliveryBoy("Are you sure", deliID,deliName);
                    /*updateDeliveryBoy(deliID,deliName, user.get(SessionManagement.KEY_ID).toString(),
                            user.get(SessionManagement.KEY_USER_TYPE).toString(),"");*/
                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                //  dialogStatus.setCancelable(false);
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // dialogStatus.setCancelable(false);
                if(mLastFirstVisibleItem<firstVisibleItem)
                {
                    Log.i("SCROLLING DOWN","TRUE");
                    // dialogStatus.setCancelable(false);
                    dialog.setCancelable(false);
                }
                if(mLastFirstVisibleItem>firstVisibleItem)
                {
                    Log.i("SCROLLING UP","TRUE");
                    dialog.setCancelable(true);
                }
                mLastFirstVisibleItem=firstVisibleItem;
            }
        });

        btn_select_deliBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listView.setTextFilterEnabled(true);
                dialog.show();
            }
        });


        txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // location=getLocation();

                startActivity(new Intent(getContext(), EditDetailsActivity.class)
                        .putExtra("cust_name",order.getFirstName()+order.getMiddleName())
                        .putExtra("contact",order.getMobile())
                        .putExtra("address1",order.getAddressLine1())
                        .putExtra("address2",order.getAddressLine2())
                        .putExtra("email",order.getEmail())
                        .putExtra("city",order.getCity())
                        .putExtra("lat",order.getLattitude())
                        .putExtra("longs",order.getLongitude())
                );
            }
        });


        String isPaid="";

        if(order.getIsPaymentSuccessful().equals("true"))
        {
            isPaid="PAID";
        }
        else if(order.getIsPaymentSuccessful().equals("false"))
        {
            isPaid="UNPAID";
        }

        if(order.getOrderTransactionType()!=null) {
            if (order.getOrderTransactionType().equals("COD")) {
                txt_paid.setText(isPaid+"-(COD)");
            } else if (order.getOrderTransactionType().equals("CC")) {
                txt_paid.setText(isPaid+"-(CC)");
            } else if (order.getOrderTransactionType().equals("C")) {
                txt_paid.setText("CREDIT-(C)");
            } else if (order.getOrderTransactionType().equals("BT")) {
                txt_paid.setText(isPaid+"-(BT)");
            } else {
                txt_paid.setText("UNPAID");
            }
        } else {
            txt_paid.setText("UNPAID");
        }

        txt_order_no.setText("S.O #"+order.getOrderID());

        img_arrow_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_arrow_up.setVisibility(View.VISIBLE);
                img_arrow_down.setVisibility(View.GONE);
                comments_rv.setVisibility(View.VISIBLE);
            }
        });
        img_arrow_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_arrow_up.setVisibility(View.GONE);
                img_arrow_down.setVisibility(View.VISIBLE);
                comments_rv.setVisibility(View.GONE);
            }
        });
        if (order!= null)
        {
//            ((MainActivity)getActivity()).toolbar.setTitle("Order #"+order.getOrderID());
            order_statuses_tv.setText("status : "+order.getOrderStatus1());
            if (order.getOrderStatus()==8)
            {
                //UpdateOrderStatus("11");
                if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Delivery Man")){
                UpdateOrderStatus("11");
                }else{
                updateOrderStatus("11","Processing",user.get(SessionManagement.KEY_USER_TYPE).toString(),
                        user.get(SessionManagement.KEY_ID).toString(),
                        "");
            }
                reject_btn.setVisibility(View.VISIBLE);
                processing_order_view.setVisibility(View.GONE);
                pending_order_view.setVisibility(View.GONE);
                comments_relative.setVisibility(View.VISIBLE);
                processing_btn.setVisibility(View.VISIBLE);
               // edt_barcode_text.setVisibility(View.GONE);
            }
            else if (order.getOrderStatus()==4)
            {
                reject_btn.setVisibility(View.VISIBLE);
                processing_order_view.setVisibility(View.GONE);
                pending_order_view.setVisibility(View.VISIBLE);
                comments_relative.setVisibility(View.VISIBLE);
                processing_btn.setVisibility(View.GONE);
               // edt_barcode_text.setVisibility(View.VISIBLE);
            }
            else if (order.getOrderStatus()==6)
            {
                processing_btn.setVisibility(View.GONE);
                reject_btn.setVisibility(View.VISIBLE);
                processing_order_view.setVisibility(View.VISIBLE);
                pending_order_view.setVisibility(View.GONE);
                comments_relative.setVisibility(View.VISIBLE);
               // edt_barcode_text.setVisibility(View.GONE);
            }
            else if (order.getOrderStatus()==11)
            {
                processing_btn.setVisibility(View.GONE);
                reject_btn.setVisibility(View.VISIBLE);
                processing_order_view.setVisibility(View.GONE);
                pending_order_view.setVisibility(View.VISIBLE);
                comments_relative.setVisibility(View.VISIBLE);
               // edt_barcode_text.setVisibility(View.GONE);
            }
            else if (order.getOrderStatus()==7)
            {
                reject_btn.setVisibility(View.GONE);
                processing_order_view.setVisibility(View.GONE);
                pending_order_view.setVisibility(View.GONE);
                comments_relative.setVisibility(View.GONE);
                processing_btn.setVisibility(View.GONE);
                cv_select_orderStatus.setVisibility(View.GONE);
                cv_select_deliBoy.setVisibility(View.GONE);
               // edt_barcode_text.setVisibility(View.GONE);
            }
            else if (order.getOrderStatus()==9)
            {
                reject_btn.setVisibility(View.GONE);
                processing_order_view.setVisibility(View.GONE);
                pending_order_view.setVisibility(View.GONE);
                comments_relative.setVisibility(View.GONE);
                processing_btn.setVisibility(View.GONE);
               // edt_barcode_text.setVisibility(View.GONE);
                cv_select_orderStatus.setVisibility(View.GONE);
                cv_select_deliBoy.setVisibility(View.GONE);
            }else if(order.getOrderStatus()==3){
                processing_btn.setVisibility(View.VISIBLE);
                processing_btn.setText("Ready For Pick Up");
            }


            int stat= order.getOrderStatus();

            if(stat==2)
            {
                Glide.with(getContext()).load(R.drawable.pending_cv).into(status_img);
            }
            else if(stat==3)
            {
                Glide.with(getContext()).load(R.drawable.delivered).into(status_img);
            }
            else if(stat==4)
            {
                Glide.with(getContext()).load(R.drawable.ready_for_pickup).into(status_img);
            }
            else if(stat==5 )
            {
                Glide.with(getContext()).load(R.drawable.processing).into(status_img);
            }
            else if(stat==8)
            {
                Glide.with(getContext()).load(R.drawable.processing).into(status_img);
            }
            else if(stat==11)
            {
                Glide.with(getContext()).load(R.drawable.processing).into(status_img);
            }
            else if(stat==6)
            {
                Glide.with(getContext()).load(R.drawable.on_the_way).into(status_img);
            }
            else if(stat==7)
            {
                Glide.with(getContext()).load(R.drawable.delivered_new).into(status_img);
            }
            else if(stat==1)
            {
                Glide.with(getContext()).load(R.drawable.order_place).into(status_img);
            }
            else if(stat==21)
            {
                Glide.with(getContext()).load(R.drawable.partially_accepted).into(status_img);
            }
            else if(stat==20)
            {
                Glide.with(getContext()).load(R.drawable.delivered).into(status_img);
            }
            else if(stat==9)
            {
                Glide.with(getContext()).load(R.drawable.calceled).into(status_img);
            }
            else if(stat==10)
            {
                Glide.with(getContext()).load(R.drawable.calceled).into(status_img);
            }
            else {
                Glide.with(getContext()).load(R.drawable.pending_cv).into(status_img);
            }
            time_tv.setText(order.getOrderDate()+" "+order.getOrderTime());
            if (order.getMiddleName()!= null)
            {
                tv_cust_nmae.setText(order.getFirstName()+" "+order.getMiddleName());
            }
            else {
                tv_cust_nmae.setText(order.getFirstName());
            }
            if (order.getAddressLine2()!= null)
            {
                tv_cust_address.setText(order.getAddressLine1() +" ,"+order.getAddressLine2());
            }
            else {
                tv_cust_address.setText(order.getAddressLine1());
            }
            tv_cust_contact.setText(order.getMobile());
            tv_cust_email.setText(order.getEmail());

            double subTotal=order.getSubTotal();
            price.setText("AED " +String.format("%.2f",subTotal ));


            if (order.getShipping() != null)
            {
                delivery.setText("AED "+  order.getShipping());
            }
            else {
                delivery.setText("AED " + 0);
            }
            if (order.getTax() != null)
            {
                double dtax=Double.parseDouble(order.getTax());
                tax.setText("AED " + String.format("%.2f", dtax));
            }
            else {
                tax.setText("AED " + 0);
            }

            if (order.getDiscount() != null)
            {
                double dis= Double.parseDouble(order.getDiscount());
                discount.setText("AED " + String.format("%.2f", dis));
            }
            else {
                discount.setText("AED " + 0);
            }
            if (order.getGrandtotal() != null)
            {
                double d=Double.parseDouble(order.getGrandtotal());
                amountPayable.setText("AED " +String.format("%.2f", d));
                order_price_tv.setText("Amount : AED " +String.format("%.2f", d));

                order_qty_tv.setText("Qty : "+order.getOrderItems().size());
            }
            else {
                amountPayable.setText("AED " + 0);
                order_price_tv.setText("Amount : AED " + 0);
                order_qty_tv.setText("Qty : "+order.getOrderItems().size());
            }
        }

        pickedup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(getContext()))
                {
                    if (productsArrayList.size()>0)
                    {
                        boolean flag=false;
                        for (Products product : productsArrayList) {
                            if (product.getBarcode()==null || product.getBarcode().equals("0") || product.getBarcode().equals("")) {

                                flag = true;
                                break;
                            }
                        }
                        if(flag)
                        {
                            Toast.makeText(getContext(), "Please first add item barcode", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            for (int i = 0; i < productsArrayList.size(); i++) {
                                if (productsArrayList.get(i).getItemScanStatus() == null) {
                                    Ask("Are you sure you checked the item in the order? ", "6","Order on the way");
                                    break;
                                } else if (i == productsArrayList.size() - 1) {
                                    ShowAlerts("Are u sure ?", "6", "Order on the way");
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                    }
                }
                else {
                    Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deliver_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(getContext()))
                {
                    ShowAlerts("Are u sure ?","7","Delivered");
                }
                else {
                    Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        processing_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(getContext()))
                {
                    if(order.getOrderStatus()==3){
                        ShowAlerts1("Are u sure ?","4","","Ready For PickUp");
                    }else if(order.getOrderStatus()==2){
                        ShowAlerts("Are u sure ?","3","Accepted");
                    }else{
                        ShowAlerts("Are u sure ?","3","Accepted");
                    }

                }
                else {
                    Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reject_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(getContext()))
                {
                    AlertDialog.Builder alerts = new AlertDialog.Builder(getContext());
                    View view1 = LayoutInflater.from(getContext()).inflate(R.layout.item_cancel_order_comment_layout, null);
                    alerts.setView(view1);
                    alerts.setCancelable(false);
                    AlertDialog alertDialog = alerts.create();
                    alertDialog.show();
                    TextInputEditText textInputEditText = view1.findViewById(R.id.comment_edts);
                    Button add_comments= view1.findViewById(R.id.add_comment);
                    ImageView close_iv= view1.findViewById(R.id.close_iv);
                    close_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    add_comments.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (DetectConnection.checkInternetConnection(getContext()))
                            {
                                if (!Validations.requireTILValidate(textInputEditText))
                                {
                                    textInputEditText.setError("Enter comments");
                                    textInputEditText.requestFocus();
                                }
                                else {
                                    alertDialog.dismiss();
                                    AddNewComment(textInputEditText.getText().toString().trim(),user.get(SessionManagement.KEY_DELIVERY_MAN).toString(), Integer.parseInt(order.getOrderID()),user.get(SessionManagement.KEY_BRANCH_ID).toString(), "9" );
                                    //UpdateOrderStatus("9");
                                    if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Delivery Man")){
                                        UpdateOrderStatus("9");
                                    }else{
                                        updateOrderStatus("9","Canceled",user.get(SessionManagement.KEY_USER_TYPE).toString(),
                                                user.get(SessionManagement.KEY_ID).toString(),
                                                "");
                                    }
                                }
                            }
                            else
                            {
                                Toast.makeText(getContext(), "please check internet connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    ShowAlerts("Are u sure ?","9","");
                }
                else {
                    Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        call_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!order.getMobile().isEmpty()) {
                    String phone = order.getMobile();
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(), "Contact Number Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        email_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = order.getEmail();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, order.getOrderID());
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        /*sms_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!order.getMobile().isEmpty()) {
                    String phone = order.getMobile();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phone, null)));
                }
                else
                {
                    Toast.makeText(getActivity(), "Contact Number Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        navigation_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(order.getLattitude()==null || order.getLongitude()==null)
                {
                    Toast.makeText(getContext(), "Location Not Available" , Toast.LENGTH_SHORT).show();
                }
                else if(order.getLattitude().equals("") || order.getLongitude().equals("")){
                    Toast.makeText(getContext(), "Location Not Available" , Toast.LENGTH_SHORT).show();
                }
                else {
                    String packageName = "com.google.android.apps.maps";
                    String query = "google.navigation:q="+order.getLattitude()+","+order.getLongitude()+"&mode=l";

                    Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(query));
                    startActivity(intent);
                }
            }
        });

        edt_barcode_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Toast.makeText(getContext(), ""+charSequence, Toast.LENGTH_SHORT).show();
                int index=0;
                boolean flag=true;
                if(!charSequence.toString().equals("")) {
                    for (Products product : productsArrayList) {
                        if (product.getBarcode().equals(charSequence.toString())) {
                            scannedBarcode(product.getOrderItemId(), index);
                            flag=false;
                            Log.e("Barcodem : ", product.getBarcode());

                            break;
                        }
                        index++;
                    }
                    if(flag)
                    {
                        edt_barcode_text.setText("");
                        Toast.makeText(getContext(), "This Barcode is not available in this list", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(commentList.size()>0){
            cv_order_comments.setVisibility(View.VISIBLE);
        }
        else {
            cv_order_comments.setVisibility(View.GONE);
        }

        // getProdust(String.valueOf(orderID));
        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(getContext()))
                {
                    if (!Validations.requireTILValidate(comment_edts))
                    {
                        comment_edts.setError("Enter comments");
                        comment_edts.requestFocus();
                    }
                    else {
                        AddNewComment(comment_edts.getText().toString().trim(),user.get(SessionManagement.KEY_DELIVERY_MAN).toString(), Integer.parseInt(order.getOrderID()),user.get(SessionManagement.KEY_BRANCH_ID).toString(), cancel );
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "please check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        download_print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(getContext()))
                {
                    @SuppressLint({"NewApi", "LocalSuppress"})
                    Instant instant = Instant.parse(order.getCreateAt());
                    @SuppressLint({"NewApi", "LocalSuppress"}) Date d = Date.from( instant ) ;
                    String da = news.format(d).replace(" ", "");;
                    String urls="https://qtp.ae/QTPMobileApp/OnlineOrderPrints/OrderBillNo_"+order.getOrderID()+"_"+da+"_"+user.get(SessionManagement.KEY_BRANCH_ID)+".pdf";
                    AsyncTaskExample asyncTask=new AsyncTaskExample();
                    asyncTask.execute(urls);
                    Log.d("download_print_btn", "onClick: "+urls);
                  /*  if(urls.isEmpty()){
                        Toast.makeText(getContext(), "Bill for print is not available, kindly contact Admin", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        startDownloading(urls, String.valueOf(order.getOrderID()));
                    }*/
                }
                else
                {
                    Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(order.getOrderStatus()!=21){
            pickedup_btn.setVisibility(View.GONE);
        }

        if(order.getOrderStatus()==4 || order.getOrderStatus()==5 ||order.getOrderStatus()==8 || order.getOrderStatus()==11){
            edt_barcode_text.setVisibility(View.VISIBLE);
            btn_edt_scan.setVisibility(View.GONE);
            img_barcode_switch.setVisibility(View.VISIBLE);
            edt_type_barcode_text.setVisibility(View.GONE);
            edt_barcode_text.requestFocus();

            img_barcode_switch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(barcode_switch)
                    {
                        barcode_switch=false;
                        img_barcode_switch.setImageDrawable(getContext().getResources().getDrawable(R.drawable.scann_barcode));
                        edt_type_barcode_text.setVisibility(View.VISIBLE);
                        edt_barcode_text.setVisibility(View.GONE);
                        btn_edt_scan.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        barcode_switch=true;
                        img_barcode_switch.setImageDrawable(getContext().getResources().getDrawable(R.drawable.edit_barcode));
                        edt_type_barcode_text.setVisibility(View.GONE);
                        edt_barcode_text.setVisibility(View.VISIBLE);
                        btn_edt_scan.setVisibility(View.GONE);
                        edt_barcode_text.requestFocus();
                    }
                }
            });

            edt_barcode_text.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int inType = edt_barcode_text.getInputType(); // backup the input type
                    edt_barcode_text.setInputType(InputType.TYPE_NULL); // disable soft input
                    edt_barcode_text.onTouchEvent(motionEvent); // call native handler
                    edt_barcode_text.setInputType(inType); // restore input type
                    return true;
                }
            });


        }
        else {
            edt_barcode_text.setVisibility(View.GONE);
            btn_edt_scan.setVisibility(View.GONE);
            img_barcode_switch.setVisibility(View.GONE);
        }

        return  view;
    }
    public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
        URL u = new URL(urlString);
        HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
        huc.setRequestMethod("HEAD");
        huc.connect();
        int res=huc.getResponseCode();
        // huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
        // return res;
        return res;
    }
    private void Ask(String message, String code,String name) {
//        final SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
//        alertDialog.setTitleText(message);
//        alertDialog.setCancelText("No");
//        alertDialog.setCancelable(false);
//        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                alertDialog.dismissWithAnimation();
//            }
//        });
//        alertDialog.show();
//        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
//        btn.setText("Yes");
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismissWithAnimation();
//                for (int i=0; i<productsArrayList.size(); i++)
//                {
//                    setScanned(productsArrayList.get(i).getOrderItemId());
//                }
//                if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Delivery Man")){
//                    UpdateOrderStatus(code);
//                }else{
//                    updateOrderStatus(code,name,user.get(SessionManagement.KEY_USER_TYPE).toString(),
//                            user.get(SessionManagement.KEY_ID).toString(),
//                            "");
//                }
//                //UpdateOrderStatus(code);
//            }
//        });
//        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(300, 130);
//        layoutParams.setMargins(10,0,10,0);
//        btn.setLayoutParams(layoutParams);
//        btn.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
//        btn.setGravity(Gravity.CENTER);
//        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
//        LinearLayout.LayoutParams layoutParams1  = new LinearLayout.LayoutParams(300, 130);
//        layoutParams1.setMargins(10,0,10,0);
//        btn1.setLayoutParams(layoutParams1);
//        btn1.setGravity(Gravity.CENTER);
    }
    private void AddNewComment(String message, String userId, int orderID, String BranchID, String status) {
        /*ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);*/
        progressDialog.show();
        ServiceGenerator.getDelivery().AddComment(message, userId,String.valueOf(orderID),BranchID, status).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                progressDialog.dismiss();
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        comment_edts.setText("");
                        Comments comments =new Comments(message,userId,input.format(calendar.getTime()));
                        commentList.add(comments);
                        if (commentsAdapter != null)
                        {
                            commentsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            setComments_rv();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "something wen't wrong please try again", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    AddNewComment( message,  userId,  orderID,  BranchID,  status);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    AddNewComment( message,  userId,  orderID,  BranchID,  status);
                }
                else
                {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void ShowAlertsForAssignDeliveryBoy(String message,String deliID, String deliName) {
//        final SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
//        alertDialog.setTitleText(message);
//        alertDialog.setCancelText("No");
//        alertDialog.setCancelable(false);
//        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//            @Override
//            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                alertDialog.dismissWithAnimation();
//            }
//        });
//        alertDialog.show();
//        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
//        btn.setText("Yes");
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alertDialog.dismissWithAnimation();
//                updateDeliveryBoy(deliID,deliName, user.get(SessionManagement.KEY_ID).toString(),
//                        user.get(SessionManagement.KEY_USER_TYPE).toString(),"");
//
//                /*if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Delivery Man")){
//                    UpdateOrderStatus(accpetcode);
//                }else{
//
//                    updateOrderStatus(accpetcode,name,user.get(SessionManagement.KEY_USER_TYPE).toString(),
//                            user.get(SessionManagement.KEY_ID).toString(),
//                            "");
//                }*/
//
//            }
//        });
//        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(300, 130);
//        layoutParams.setMargins(10,0,10,0);
//        btn.setLayoutParams(layoutParams);
//        btn.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
//        btn.setGravity(Gravity.CENTER);
//        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
//        LinearLayout.LayoutParams layoutParams1  = new LinearLayout.LayoutParams(300, 130);
//        layoutParams1.setMargins(10,0,10,0);
//        btn1.setLayoutParams(layoutParams1);
//        btn1.setGravity(Gravity.CENTER);
    }

    private void ShowAlerts(String message,String accpetcode, String name) {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText(message);
        alertDialog.setCancelText("No");
        alertDialog.setCancelable(false);
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
            }
        });
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setText("Yes");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismissWithAnimation();
                if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Delivery Man")){
                    UpdateOrderStatus(accpetcode);
                }else{
                    updateOrderStatus(accpetcode,name,user.get(SessionManagement.KEY_USER_TYPE).toString(),
                            user.get(SessionManagement.KEY_ID).toString(),
                            "");
                }
               // UpdateOrderStatus(accpetcode);
            }
        });
        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(300, 130);
        layoutParams.setMargins(10,0,10,0);
        btn.setLayoutParams(layoutParams);
        btn.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btn.setGravity(Gravity.CENTER);
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        LinearLayout.LayoutParams layoutParams1  = new LinearLayout.LayoutParams(300, 130);
        layoutParams1.setMargins(10,0,10,0);
        btn1.setLayoutParams(layoutParams1);
        btn1.setGravity(Gravity.CENTER);
    }

    private void ShowAlerts1(String message,String accpetcode, String rejectcode,String name) {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText(message);
        alertDialog.setCancelText("No");
        alertDialog.setCancelable(false);
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
            }
        });
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setText("Yes");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismissWithAnimation();
                orderStatusForReadyForPickup(accpetcode,name);
                //UpdateOrderStatus(accpetcode);
            }
        });
        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(300, 130);
        layoutParams.setMargins(10,0,10,0);
        btn.setLayoutParams(layoutParams);
        btn.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btn.setGravity(Gravity.CENTER);
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        LinearLayout.LayoutParams layoutParams1  = new LinearLayout.LayoutParams(300, 130);
        layoutParams1.setMargins(10,0,10,0);
        btn1.setLayoutParams(layoutParams1);
        btn1.setGravity(Gravity.CENTER);
    }


    public void setProducts_rv()
    {
        products_rv.setHasFixedSize(true);
        products_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new AdminProductAdapter(productsArrayList, getContext(),order.getOrderStatus());
        products_rv.setAdapter(productAdapter);
    }

    public void setComments_rv()
    {

        comments_rv.setHasFixedSize(true);
        comments_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        commentsAdapter = new CommentsAdapter(commentList, getContext());
        comments_rv.setAdapter(commentsAdapter);
    }
    public void UpdateOrderStatus(String status)
    {
        /*ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);*/
        progressDialog.show();
        //Tools.loading(getActivity()).show();
        String deliveryman, orderid, orderstatus;
        deliveryman = user.get(SessionManagement.KEY_DELIVERY_MAN).toString();
        orderid = String.valueOf(order.getOrderID());
        orderstatus = status;
        ServiceGenerator.getDelivery().updateOrderStatus(deliveryman,orderid,orderstatus).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                progressDialog.dismiss();
                //Tools.loading(getActivity()).dismiss();
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        sessionManagement.updateTABStatus(String.valueOf(pos));
                        if(!status.equals("11")) {
                           /* NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                            navController.navigate(R.id.goto_home);*/
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            processing_btn.setVisibility(View.GONE);
                        }
                    }
                    else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "something went wrong try again...!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                progressDialog.dismiss();
                //Tools.loading(getActivity()).dismiss();
                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    UpdateOrderStatus(status);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    UpdateOrderStatus(status);
                }
                else
                {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
//        ((MainActivity)getActivity()).CheckDeliveryMan();
        IntentFilter intentFilter = new IntentFilter("orders");
        getActivity().registerReceiver(orderReceiver, intentFilter);
    }
    private void call(String contactNo) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + contactNo));
        startActivity(intent);
    }
    private void startDownloading(String invoicePath,String name) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(invoicePath));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+name+".pdf");
        @SuppressLint({"NewApi", "LocalSuppress"})
        DownloadManager manager = (DownloadManager)getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
        manager.enqueue(request);
        Toast.makeText(getContext(), "Invoice download started ", Toast.LENGTH_SHORT).show();
    }
    private void requestpermissions() {
        if(Build.VERSION.SDK_INT>=23){
            if(checkPermission()){
            }
            else{
                requestgallery();
            }
        }
        else
        {
        }
    }
    private boolean checkPermission() {
        for(int i=0;i<permission_storage.length;i++){
            int result= ContextCompat.checkSelfPermission(getContext(),permission_storage[i]);
            if(result== PackageManager.PERMISSION_GRANTED){return true;
            }
            else {
                return false;
            }
        }
        return true;
    }
    public void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
    @SuppressLint("NewApi")
    private void requestgallery(){requestPermissions(permission_storage, REQUEST_WRITE_PERMISSION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // check whether storage permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //do what you want;
                    }
                }
                break;
            default:
                break;
        }
    }
    public class OrderReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle!= null)
            {
                //getProdust(String.valueOf(orderID));
            }
        }
    }
    private void setScanned(String itemId) {
        orderS_pb.setVisibility(View.VISIBLE);
        ServiceGenerator.getDelivery().updateBarcodeScanStatus("1",itemId).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                orderS_pb.setVisibility(View.GONE);
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                    }
                    else
                    {
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                orderS_pb.setVisibility(View.GONE);
                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    setScanned(itemId);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    setScanned(itemId);
                }
                else
                {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private class AsyncTaskExample extends AsyncTask<String, String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL u = new URL(strings[0]);
                HttpURLConnection huc =  (HttpURLConnection)  u.openConnection();
                huc.setRequestMethod("HEAD");
                huc.connect();
                int res=huc.getResponseCode();
                if (res==200)
                {
                    startDownloading(strings[0], String.valueOf(order.getOrderID()));
                }
                else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            showToast("Bill for print is not available, kindly contact Admin");
                        }
                    });
//                    showToast("Bill for print is not available, kindly contact Admin");
                    //   Toast.makeText(getContext(), "Bill for print is not available, kindly contact Admin", Toast.LENGTH_SHORT).show();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return strings[0];
        }
        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            //startDownloading(string, String.valueOf(order.getOrderID()));
        }
    }

    void scannedBarcode(String itemId,int position)
    {
        ServiceGenerator.getDelivery().updateBarcodeScanStatus("1",itemId).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
//                        Glide.with(context).load(R.drawable.checked).into(productViewHolderss.barcode_iv);
//                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        productsArrayList.get(position).setItemScanStatus("1");
                        productAdapter.notifyDataSetChanged();

                        edt_barcode_text.setText("");
                    }
                    else
                    {
                        edt_barcode_text.setText("");
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    edt_barcode_text.setText("");
                    Toast.makeText(getContext(), "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                edt_barcode_text.setText("");
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Location getLocation()
    {
        try {
            locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
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
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (android.location.LocationListener) this);
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

    public void getDeliveryBoyList()
    {

        ArrayList<String> arrayList=new ArrayList<>();
        String ID,role,BranchCode;
        ID=user.get(SessionManagement.KEY_ID).toString();
        role=user.get(SessionManagement.KEY_USER_TYPE).toString();
        BranchCode=user.get(SessionManagement.KEY_BRANCH_ID).toString();

        ServiceGenerator.getDelivery().deliveryBoyList(ID,role,BranchCode).enqueue(new Callback<ResponseDeliveryBoyList>() {
            @Override
            public void onResponse(Call<ResponseDeliveryBoyList> call, Response<ResponseDeliveryBoyList> response) {
                if (response.isSuccessful())
                {
                    currentDBarrayList.clear();
                    deliveryBoyModelArrayList=response.body().getResult().getDeliveryManList();
                    for (int i=0;i<deliveryBoyModelArrayList.size();i++){
                    //    arrayList.add(deliveryBoyModelArrayList.get(i).getUserName());
                        Log.e("SupId",order.getSupplierID());

                        if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Admin")&&deliveryBoyModelArrayList.get(i).getOwnerID().equals(user.get(SessionManagement.KEY_ID).toString()) || order.getSupplierID().equals(deliveryBoyModelArrayList.get(i).getOwnerID()))
                        {
                            currentDBarrayList.add(deliveryBoyModelArrayList.get(i).getUserName());
                        }

                        if(order.getDeliveryMan()!=null){
                            if(order.getDeliveryMan().equals(deliveryBoyModelArrayList.get(i).getId())){
                                btn_select_deliBoy.setText(deliveryBoyModelArrayList.get(i).getUserName());
                            }
                        }

                        //deliListID.add(Integer.valueOf(deliveryBoyModelArrayList.get(i).getId()));
                    }

                   /* if (currentDBarrayList.size()>0){
                        cv_select_deliBoy.setVisibility(View.VISIBLE);
                    }else {
                        cv_select_deliBoy.setVisibility(View.GONE);
                    }*/
                    // DeliveryBoyAdapter deliveryBoyAdapter=new DeliveryBoyAdapter(deliveryBoyModels, DeliveryBoyListActivity.this);
                    adapter=new ArrayAdapter<>(getContext(),R.layout.simple_list_item_single_choice,currentDBarrayList);
                    listView.setAdapter(adapter);
                }
                else
                {
                    // progressDialog.dismiss();
                    //Toast.makeText(DeliveryBoyListActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
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
    public ArrayList<String> getOrderStatus(){
        ArrayList<String> arrayList=new ArrayList<>();
        ServiceGenerator.getDelivery().getOrderStatus().enqueue(new Callback<ResponseOrderStatus>() {
            @Override
            public void onResponse(Call<ResponseOrderStatus> call, Response<ResponseOrderStatus> response) {
                if (response.isSuccessful())
                {
                    if(response.body().getStatus().equals("true")){
                       orderStatuses=response.body().getResult();
                        for (int i=0;i<orderStatuses.size();i++){
                            arrayList.add(orderStatuses.get(i).getOrderStatusName());
                        }
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

        return arrayList;
    }


    public void updateDeliveryBoy(String deliID,String deliName,String ownerID,String role,String reason)
    {
       // progressBar.setVisibility(View.VISIBLE);
       // relativeLayout.setVisibility(View.GONE);

        ServiceGenerator.getDelivery().updateOwnerOrderDeliveryMan(deliID,String.valueOf(orderID),role,ownerID,"MainOrder",reason).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    //progressBar.setVisibility(View.GONE);
                    //relativeLayout.setVisibility(View.VISIBLE);
                    if (response.body().isStatus())
                    {
                        btn_select_deliBoy.setText(deliName);
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                   // progressBar.setVisibility(View.GONE);
                   // relativeLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateOrderStatus(String orderStatus,String name,String role,String ownerID,String reason)
    {

       // progressBar.setVisibility(View.VISIBLE);
//        relativeLayout.setVisibility(View.GONE);
        ServiceGenerator.getDelivery().updateOrderStatusAdmin(orderStatus,String.valueOf(orderID),role,ownerID,"SubOrder",reason).enqueue(new Callback<ResponseCommon>()
        {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    //progressBar.setVisibility(View.GONE);
//                    relativeLayout.setVisibility(View.VISIBLE);
                    if (response.body().isStatus())
                    {
                        /*SupplierActivity activity = (SupplierActivity) view.getContext();
                        OrderFragment myFragment = new OrderFragment();
                        Bundle bundle=new Bundle();
                        bundle.putInt("position",Integer.parseInt(orderStatus));
                        myFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).commit();
*/
                        getActivity().onBackPressed();
                        btn_select_orderStatus.setText(name);
                        Toast.makeText(getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(
                                getContext(), ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                 //   progressBar.setVisibility(View.GONE);
                    //relativeLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public String getSelectedDeliveryMan(String  name)
    {
        for (DeliveryBoyModel model:deliveryBoyModelArrayList)
        {
            if(name.equals(model.getUserName()))
            {
                return model.getId();
            }
        }
        return "";
    }

    private void orderStatusForReadyForPickup(String id, String name){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Log.e("date",year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        // Get Current Time
                        final Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        Log.e("dateTime",year + "-" + (monthOfYear + 1) + "-" + dayOfMonth+" "+hourOfDay + ":" + minute);
                                        String dateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth+" "+hourOfDay + ":" + minute;
                                        updateOrderStatus(id,name,user.get(SessionManagement.KEY_USER_TYPE).toString(),user.get(SessionManagement.KEY_ID).toString(),
                                                dateTime);

                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.setCancelable(false);
                        timePickerDialog.show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.setCancelable(false);
        datePickerDialog.show();
    }

}
