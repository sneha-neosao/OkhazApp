package ae.okhaz.boss.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ae.okhaz.boss.Activitys.EditDetailsActivity;
import ae.okhaz.boss.Adapter.CommentsAdapter;
import ae.okhaz.boss.Adapter.ProductListAdapter1;
import ae.okhaz.boss.Model.Comments;
import ae.okhaz.boss.Model.Order;
import ae.okhaz.boss.Model.ProductListModel;
import ae.okhaz.boss.Model.Products;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.DetectConnection;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.Utils.Validations;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.Response.ResponseOrderDetails;
import ae.okhaz.boss.rests.ServiceGenerator;
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
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class OrderDetailsFragment extends Fragment {

    boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 3000;

    Location currentlocation;
    private LocationManager locationManager;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    public Location location;
    double latitude = 0.0, longitude = 0.0;

    SessionManagement sessionManagement;
    ProgressBar orderS_pb;
    HashMap user;
    View view;
    EditText edt_barcode_text;
    RecyclerView products_rv,comments_rv;
    ProductListAdapter1 productAdapter;
    Button reject_btn,deliver_btn,pickedup_btn,pickedup_location_btn,processing_btn,
            add_comment,download_print_btn;
    LinearLayout processing_order_view,pending_order_view;
    ArrayList<Products> productsArrayList;
    ArrayList<ProductListModel> productList;
    ArrayList<Comments> commentList;
    //String role="";
    String orderID;
    RelativeLayout comments_relative;
    String cancel="";
    ImageView status_img,call_iv,sms_iv,email_iv,img_arrow_up,img_arrow_down;
    CardView navigation_cv;
    int pos;
    Calendar calendar;
    TextInputEditText comment_edts;
    SimpleDateFormat input, news;
    OrderReceiver orderReceiver;
    CommentsAdapter commentsAdapter;
    String[] permission_storage= {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_WRITE_PERMISSION = 1;
    private  final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    TextView tv_cust_nmae,tv_cust_address,tv_cust_contact,tv_cust_email,price,delivery,amountPayable,
            tax,order_price_tv,order_statuses_tv,time_tv,discount,txt_edit,txt_paid,txt_order_no;

    TextView mo_txt_order_no;

    Dialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.dm_fragment_order_details, container, false);
        products_rv = view.findViewById(R.id.products_rv);
        txt_paid = view.findViewById(R.id.txt_paid);
        txt_order_no = view.findViewById(R.id.txt_order_no);
        comments_rv = view.findViewById(R.id.comments_rv);
        processing_order_view = view.findViewById(R.id.processing_order_view);
        pending_order_view = view.findViewById(R.id.pending_order_view);


        price = view.findViewById(R.id.price);

        delivery = view.findViewById(R.id.delivery);
        amountPayable = view.findViewById(R.id.amountPayable);
        tax = view.findViewById(R.id.tax);
        status_img = view.findViewById(R.id.status_img);

        //mainorder binding
        mo_txt_order_no = view.findViewById(R.id.mo_txt_order_no);

        //customer detail section
        tv_cust_nmae = view.findViewById(R.id.tv_cust_nmae);
        tv_cust_address = view.findViewById(R.id.tv_cust_address);
        tv_cust_contact = view.findViewById(R.id.tv_cust_contact);
        tv_cust_email = view.findViewById(R.id.tv_cust_email);
        navigation_cv = view.findViewById(R.id.navigation_cv);

        order_price_tv = view.findViewById(R.id.order_price_tv);
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
        sms_iv = view.findViewById(R.id.sms_iv);
        email_iv = view.findViewById(R.id.email_iv);

        comment_edts = view.findViewById(R.id.comment_edts);
        orderS_pb = view.findViewById(R.id.orderS_pb);
        img_arrow_down=view.findViewById(R.id.img_arrow_down);
        img_arrow_up=view.findViewById(R.id.img_arrow_up);
        edt_barcode_text=view.findViewById(R.id.edt_barcode_text);
        txt_edit=view.findViewById(R.id.txt_edit);
        edt_barcode_text.requestFocus();
        sessionManagement = SessionManagement.getInstance(getContext());
        user = sessionManagement.getUserDetails();
        orderReceiver = new OrderReceiver();
        requestpermissions();
        productsArrayList = new ArrayList<>();
        productList = new ArrayList<>();

        //data from intent
        orderID = getArguments().getString("orderID");
        pos = getArguments().getInt("pos");

        progressDialog = new  Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(getContext()).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);

        String role= user.get(SessionManagement.KEY_USER_TYPE).toString();
        if(role.equals("Admin") || role.equals("Supplier"))
        {
            MainDrawerBackActivity.tv_toolbar_title.setText("Order #"+orderID);

           /* DeliveryBoyListActivity.toolbar.setTitle("Order #"+orderID);
            DeliveryBoyListActivity.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
            DeliveryBoyListActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((DeliveryBoyListActivity)getActivity())!=null)
                        ((DeliveryBoyListActivity)getActivity()).onBackPressed();
                }
            });*/

        }
        else
        {
            //((MainActivity) getActivity()).toolbar.setTitle("Order #" + orderID);

        }

        calendar = Calendar.getInstance();
        input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        news = new SimpleDateFormat("dd MMM yyyy");


        getProdust(String.valueOf(orderID));

        return  view;
    }

    // get orderdata from server - mainorder,suborder and its items from suborderid
    private void getProdust(String orderID) {
        ServiceGenerator.getDelivery().orderDetail(orderID).enqueue(new Callback<ResponseOrderDetails>() {
            @Override
            public void onResponse(Call<ResponseOrderDetails> call, Response<ResponseOrderDetails> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        productList.clear();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<ProductListModel>>() {
                        }.getType();
                        Order mainOrder=response.body().getResult().getOrder();
                        setmainOrderData(mainOrder);
                        productList = response.body().getResult().getSubOrders();
                        if(!productList.isEmpty()) {
                            setData(productList.get(0));
                            productsArrayList.clear();
                            if(!productList.get(0).getOrderItems().isEmpty()) {
                                productsArrayList = productList.get(0).getOrderItems();
                            }
                        }

                        commentList = response.body().getResult().getOrderComments();
                        setProducts_rv();
                        if(!commentList.isEmpty()) {
                            setComments_rv();
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseOrderDetails> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    getProdust(orderID);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    getProdust(orderID);
                }
                else
                {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Ask(String message, String code) {
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
                for (int i=0; i<productsArrayList.size(); i++)
                {
                    setScanned(productsArrayList.get(i).getOrderItemId());
                }
                UpdateOrderStatus(code);
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

    private void AddNewComment(String message, String userId, String orderID, String BranchID, String status) {
       /* ProgressDialog progressDialog = new ProgressDialog(getContext());
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

    private void ShowAlerts(String message,String accpetcode, String rejectcode) {
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
                UpdateOrderStatus(accpetcode);
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
        productAdapter = new ProductListAdapter1(productList, getContext());
        products_rv.setAdapter(productAdapter);
    }

    void  setmainOrderData( Order mainOrder)
    {
        mo_txt_order_no.setText("M.O #" + mainOrder.getOrderID()+" ("+mainOrder.getOrderStatus1()+")");
    }

    public void setData(ProductListModel order){

        //order upper section
        txt_order_no.setText("S.O #" + order.getOrderID());
        order_statuses_tv.setText("status : "+order.getOrderStatus1());

        if(order.getOrderTransactionType()!=null) {
            if (order.getOrderTransactionType().equals("COD")) {
                txt_paid.setText("UNPAID-(COD)");
            } else if (order.getOrderTransactionType().equals("CC")) {
                txt_paid.setText("PAID-(CC)");
            } else if (order.getOrderTransactionType().equals("C")) {
                txt_paid.setText("CREDIT-(C)");
            } else if (order.getOrderTransactionType().equals("BT")) {
                txt_paid.setText("UNPAID-(BT)");
            } else {
                txt_paid.setText("UNPAID");
            }
        } else {
            txt_paid.setText("UNPAID");
        }
        time_tv.setText(order.getOrderDate()+" "+order.getOrderTime());


        //customer detail section
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
        price.setText("AED " +String.format("%.2f",order.getSubTotal()));


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
            discount.setText("AED " + order.getDiscount());
        }
        else {
            discount.setText("AED " + 0);
        }
        if (order.getGrandtotal() != null)
        {
            double d=Double.parseDouble(order.getGrandtotal());
            amountPayable.setText("AED " +String.format("%.2f", d));
            order_price_tv.setText("Amount : AED " +String.format("%.2f", d)
                    + "  ("+order.getItemCount()+" Items"+")");
        }
        else {
            amountPayable.setText("AED " + 0);
            order_price_tv.setText("Amount : AED " + 0 + "("+order.getItemCount()+" Items"+")");
        }

        if(order.getOrderStatus()==4 || order.getOrderStatus()==5 || order.getOrderStatus()==8){
            edt_barcode_text.setVisibility(View.VISIBLE);
            edt_barcode_text.requestFocus();
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
        }

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

        if (order.getOrderStatus()==8)
        {
           // UpdateOrderStatus("11");
            reject_btn.setVisibility(View.VISIBLE);
            processing_order_view.setVisibility(View.GONE);
            pending_order_view.setVisibility(View.GONE);
            comments_relative.setVisibility(View.VISIBLE);
            processing_btn.setVisibility(View.VISIBLE);
            edt_barcode_text.setVisibility(View.GONE);
        }
        else if (order.getOrderStatus()==4)
        {
            reject_btn.setVisibility(View.VISIBLE);
            processing_order_view.setVisibility(View.GONE);
            pending_order_view.setVisibility(View.VISIBLE);
            comments_relative.setVisibility(View.VISIBLE);
            processing_btn.setVisibility(View.GONE);
            edt_barcode_text.setVisibility(View.VISIBLE);
        }
        else if (order.getOrderStatus()==6)
        {
            processing_btn.setVisibility(View.GONE);
            reject_btn.setVisibility(View.VISIBLE);
            processing_order_view.setVisibility(View.VISIBLE);
            pending_order_view.setVisibility(View.GONE);
            comments_relative.setVisibility(View.VISIBLE);
            edt_barcode_text.setVisibility(View.GONE);
        }
        else if (order.getOrderStatus()==11)
        {
            processing_btn.setVisibility(View.GONE);
            reject_btn.setVisibility(View.VISIBLE);
            processing_order_view.setVisibility(View.GONE);
            pending_order_view.setVisibility(View.VISIBLE);
            comments_relative.setVisibility(View.VISIBLE);
            edt_barcode_text.setVisibility(View.GONE);
        }
        else if (order.getOrderStatus()==7)
        {
            reject_btn.setVisibility(View.GONE);
            processing_order_view.setVisibility(View.GONE);
            pending_order_view.setVisibility(View.GONE);
            comments_relative.setVisibility(View.GONE);
            processing_btn.setVisibility(View.GONE);
            edt_barcode_text.setVisibility(View.GONE);
        }
        else if (order.getOrderStatus()==9)
        {
            reject_btn.setVisibility(View.GONE);
            processing_order_view.setVisibility(View.GONE);
            pending_order_view.setVisibility(View.GONE);
            comments_relative.setVisibility(View.GONE);
            processing_btn.setVisibility(View.GONE);
            edt_barcode_text.setVisibility(View.GONE);
        }
        else
        {
            processing_btn.setVisibility(View.GONE);
            reject_btn.setVisibility(View.GONE);
            processing_order_view.setVisibility(View.GONE);
            pending_order_view.setVisibility(View.GONE);
            comments_relative.setVisibility(View.GONE);
            edt_barcode_text.setVisibility(View.GONE);
        }

        if(order.getOrderStatus()==2)
        {
            Glide.with(getContext()).load(R.drawable.pending_cv).into(status_img);
        }
        else if(order.getOrderStatus()==3)
        {
            Glide.with(getContext()).load(R.drawable.delivered).into(status_img);
        }
        else if(order.getOrderStatus()==4)
        {
            Glide.with(getContext()).load(R.drawable.ready_for_pickup).into(status_img);
        }
        else if(order.getOrderStatus()==5 )
        {
            Glide.with(getContext()).load(R.drawable.processing).into(status_img);
        }
        else if(order.getOrderStatus()==8)
        {
            Glide.with(getContext()).load(R.drawable.processing).into(status_img);
        }
        else if(order.getOrderStatus()==11)
        {
            Glide.with(getContext()).load(R.drawable.processing).into(status_img);
        }
        else if(order.getOrderStatus()==6)
        {
            Glide.with(getContext()).load(R.drawable.on_the_way).into(status_img);
        }
        else if(order.getOrderStatus()==7)
        {
            Glide.with(getContext()).load(R.drawable.delivered_new).into(status_img);
        }
        else if(order.getOrderStatus()==1)
        {
            Glide.with(getContext()).load(R.drawable.order_place).into(status_img);
        }
        else if(order.getOrderStatus()==21)
        {
            Glide.with(getContext()).load(R.drawable.partially_accepted).into(status_img);
        }
        else if(order.getOrderStatus()==20)
        {
            Glide.with(getContext()).load(R.drawable.delivered).into(status_img);
        }
        else if(order.getOrderStatus()==9)
        {
            Glide.with(getContext()).load(R.drawable.calceled).into(status_img);
        }
        else if(order.getOrderStatus()==10)
        {
            Glide.with(getContext()).load(R.drawable.calceled).into(status_img);
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
                                    Ask("Are you sure you checked the item in the order? ", "6");
                                    break;
                                } else if (i == productsArrayList.size() - 1) {
                                    ShowAlerts("Are u sure ?", "6", "");
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Product not found...", Toast.LENGTH_SHORT).show();
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
                    ShowAlerts("Are u sure ?","7","");
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
                    ShowAlerts("Are u sure ?","4","");
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
                                    AddNewComment(textInputEditText.getText().toString().trim(),user.get(SessionManagement.KEY_DELIVERY_MAN).toString(),order.getOrderID(),user.get(SessionManagement.KEY_BRANCH_ID).toString(), "9" );
                                    UpdateOrderStatus("9");
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
        sms_iv.setOnClickListener(new View.OnClickListener() {
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
                        AddNewComment(comment_edts.getText().toString().trim(),user.get(SessionManagement.KEY_DELIVERY_MAN).toString(),order.getOrderID(),user.get(SessionManagement.KEY_BRANCH_ID).toString(), cancel );
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
                    String urls="https://qtp.ae/QTPMobileApp/OnlineOrderPrints/OrderBillNo_"+order.getPrimaryOrderNumber()+"_"+da+"_"+user.get(SessionManagement.KEY_BRANCH_ID)+".pdf";
                    AsyncTaskExample asyncTask=new AsyncTaskExample();
                    asyncTask.execute(urls,order.getPrimaryOrderNumber());
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
        orderid = orderID;
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
                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                            navController.navigate(R.id.goto_home);
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
                getProdust(String.valueOf(orderID));
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
                    startDownloading(strings[0], strings[1]);
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

}
