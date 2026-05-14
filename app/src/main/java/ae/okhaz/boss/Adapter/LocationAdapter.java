package ae.okhaz.boss.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.boss.Activitys.MainActivity;
import ae.okhaz.boss.Activitys.Supplier.SupplierActivity;
import ae.okhaz.boss.Fragments.Admin.OrderFragment;
import ae.okhaz.boss.Fragments.MapsFragment;
import ae.okhaz.boss.Model.Locations;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.DetectConnection;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.ViewHolder.LocationsViewHolder;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Avinash on 02,December,2020
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationsViewHolder> {

    ArrayList<Locations> locationsArrayList;
    Context context;
    HashMap user;
    String orderID;
    SessionManagement sessionManagement;
    ArrayList<LinearLayout> linearLayoutArrayList;


    public LocationAdapter(ArrayList<Locations> locationsArrayList, Context context) {
        this.locationsArrayList = locationsArrayList;
        this.context = context;
        linearLayoutArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(ae.okhaz.admin.R.layout.item_list_layout, parent, false);
        return new LocationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsViewHolder holder, int position) {
        sessionManagement = SessionManagement.getInstance(context);
        user = sessionManagement.getUserDetails();
        linearLayoutArrayList.add(holder.navigations_item_lin);
        if (!locationsArrayList.get(position).getName().contains("null"))
        {
            holder.location_nm.setText(locationsArrayList.get(position).getName());
            orderID=locationsArrayList.get(position).getName().replace("Order #","");
        }
        else {
            holder.location_nm.setText("-");
        }

        if (!locationsArrayList.get(position).getAddress().contains("null"))
        {
            holder.location_address.setText(locationsArrayList.get(position).getAddress());
        }
        else {
            holder.location_address.setText("-");
        }




        holder.start_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position!= 1)
                {
                    if (!MapsFragment.isStarted)
                    {
                        if (MapsFragment.isRoutes)
                    {
                        holder.navigations_item_lin.setVisibility(View.GONE);
                        Intent intent = new Intent("locations");
                        intent.putExtra("lat", locationsArrayList.get(position).getLat());
                        intent.putExtra("lang", locationsArrayList.get(position).getLng());
                        intent.putExtra("navigates", "navigates");
                        intent.putExtra("position",position);
                        context.sendBroadcast(intent);
                    }

                    }
                    else {
                        Intent intent = new Intent("locations");
                        intent.putExtra("lat", locationsArrayList.get(position).getLat());
                        intent.putExtra("lang", locationsArrayList.get(position).getLng());
                        intent.putExtra("started", "start");
                        intent.putExtra("position",position);
                        context.sendBroadcast(intent);
                    }


                }
                else {
                    Intent intent = new Intent("locations");
                    intent.putExtra("lat", locationsArrayList.get(position).getLat());
                    intent.putExtra("lang", locationsArrayList.get(position).getLng());
                    intent.putExtra("started", "start");
                    intent.putExtra("position",position);
                    context.sendBroadcast(intent);
                }

            }
        });

        holder.done_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MapsFragment.isStarted) {
                    holder.navigations_item_lin.setVisibility(View.GONE);
                    /*if (position!= linearLayoutArrayList.size()-1)
                    {*/
                      //  linearLayoutArrayList.get(position+1).setVisibility(View.VISIBLE);

                      /*  Intent intent = new Intent("locations");
                        intent.putExtra("lat", locationsArrayList.get(position).getLat());
                        intent.putExtra("lang", locationsArrayList.get(position).getLng());
                        intent.putExtra("done", "done");
                        intent.putExtra("position", position);
                        context.sendBroadcast(intent);
*/

                        if (DetectConnection.checkInternetConnection(context))
                        {
                            final SweetAlertDialog alertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                            alertDialog.setTitleText("Are you sure ?");
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
                                    /*ProgressDialog progressDialog = new ProgressDialog(context);
                                    progressDialog.setMessage("please wait...");
                                    progressDialog.setCancelable(false);
                                    progressDialog.setCanceledOnTouchOutside(false);
                                    progressDialog.show();*/
                                    Dialog progressDialog = new  Dialog(context);
                                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    progressDialog.setContentView(R.layout.custom_loading_dialog);
                                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
                                    Glide.with(context).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);
                                    progressDialog.show();
                                    //Tools.loading(context).show();
                                    String deliveryman, orderid, orderstatus;
                                    deliveryman = user.get(SessionManagement.KEY_DELIVERY_MAN).toString();
                                    orderid = String.valueOf(orderID);
                                    orderstatus = "7";
                                    ServiceGenerator.getDelivery().updateOrderStatus(deliveryman,orderid,orderstatus).enqueue(new Callback<ResponseCommon>() {
                                        @Override
                                        public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                                            progressDialog.dismiss();
                                            //Tools.loading(context).dismiss();
                                            if (response.isSuccessful())
                                            {
                                                if (response.body().isStatus())
                                                {
                                                    holder.done_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checked, 0, 0, 0);

                                                    if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Supplier")){
                                                        SupplierActivity activity = unwrap(view.getContext());
                                                        //  SupplierActivity activity = (SupplierActivity) view.getContext();
                                                        OrderFragment myFragment = new OrderFragment();
                                                        Bundle bundle=new Bundle();
                                                        bundle.putInt("position",7);
                                                        myFragment.setArguments(bundle);
                                                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                                                    }
                                                    else {
                                                        NavController navController = Navigation.findNavController(((MainActivity)context), R.id.nav_host_fragment);
                                                         navController.navigate(R.id.goto_orders);
                                                    }

                                                     }
                                                else {
                                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else {
                                                Toast.makeText(context, "something went wrong try again...!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<ResponseCommon> call, Throwable t) {
                                            progressDialog.dismiss();
                                            //Tools.loading(context).dismiss();
                                            if (t instanceof SocketTimeoutException)
                                            {
                                                // "Connection Timeout";
                                              //  UpdateOrderStatus(status);
                                            }
                                            else if (t instanceof IOException)
                                            {
                                                // "Timeout";
                                              //  UpdateOrderStatus(status);
                                            }
                                            else
                                            {
                                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                            LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(300, 130);
                            layoutParams.setMargins(10,0,10,0);
                            btn.setLayoutParams(layoutParams);
                            btn.setBackground(context.getResources().getDrawable(R.drawable.custom_dialog_button));
                            btn.setGravity(Gravity.CENTER);
                            Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
                            LinearLayout.LayoutParams layoutParams1  = new LinearLayout.LayoutParams(300, 130);
                            layoutParams1.setMargins(10,0,10,0);
                            btn1.setLayoutParams(layoutParams1);
                            btn1.setGravity(Gravity.CENTER);
                        }
                        else {
                            Toast.makeText(context, "check internet connection", Toast.LENGTH_SHORT).show();
                        }
                   /* }
                    else {
                        //NavController navController = Navigation.findNavController(((MainActivity)context), R.id.nav_host_fragment);
                       // navController.navigate(R.id.goto_orders);

                        SupplierActivity activity = (SupplierActivity) view.getContext();
                        OrderFragment myFragment = new OrderFragment();
                        Bundle bundle=new Bundle();
                        bundle.putInt("position",8);
                        myFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();

                    }*/
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MapsFragment.isRoutes)
                {
                    for (LinearLayout linearLayout: linearLayoutArrayList)
                    {
                        linearLayout.setVisibility(View.GONE);
                    }

                    if (position!=0)
                    {
                        holder.navigations_item_lin.setVisibility(View.VISIBLE);
                    }

                    if (!MapsFragment.isStarted)
                    {
                        Intent intent = new Intent("locations");
                        intent.putExtra("lat", locationsArrayList.get(position).getLat());
                        intent.putExtra("lang", locationsArrayList.get(position).getLng());
                        context.sendBroadcast(intent);

                    }
                    else {

                    }

                }
            }
        });
    }


   /* private void ShowAlerts(String message,String accpetcode, String rejectcode) {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
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
                ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("please wait...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                String deliveryman, orderid, orderstatus;
                deliveryman = user.get(SessionManagement.KEY_DELIVERY_MAN).toString();
                orderid = String.valueOf(orderID);
                orderstatus = status;
                ServiceGenerator.getDelivery().updateOrderStatus(deliveryman,orderid,orderstatus).enqueue(new Callback<ResponseCommon>() {
                    @Override
                    public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                        progressDialog.dismiss();
                        if (response.isSuccessful())
                        {
                            if (response.body().isStatus())
                            {
                                holder.done_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checked, 0, 0, 0);
                            }
                            else {
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(context, "something went wrong try again...!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseCommon> call, Throwable t) {
                        progressDialog.dismiss();
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
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(300, 130);
        layoutParams.setMargins(10,0,10,0);
        btn.setLayoutParams(layoutParams);
        btn.setBackground(context.getResources().getDrawable(R.drawable.custom_dialog_button));
        btn.setGravity(Gravity.CENTER);
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        LinearLayout.LayoutParams layoutParams1  = new LinearLayout.LayoutParams(300, 130);
        layoutParams1.setMargins(10,0,10,0);
        btn1.setLayoutParams(layoutParams1);
        btn1.setGravity(Gravity.CENTER);
    }*/


   /* public void UpdateOrderStatus(String status)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String deliveryman, orderid, orderstatus;
        deliveryman = user.get(SessionManagement.KEY_DELIVERY_MAN).toString();
        orderid = String.valueOf(orderID);
        orderstatus = status;
        ServiceGenerator.getDelivery().updateOrderStatus(deliveryman,orderid,orderstatus).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                progressDialog.dismiss();
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        holder.done_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.checked, 0, 0, 0);
                    }
                    else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, "something went wrong try again...!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                progressDialog.dismiss();
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
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    private static SupplierActivity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (SupplierActivity) context;
    }

    @Override
    public int getItemCount() {
        return locationsArrayList.size();
    }
}
