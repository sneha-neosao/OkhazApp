package ae.okhaz.boss.Adapter.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import ae.okhaz.boss.Fragments.Admin.AdminOrderDetailFragment;
import ae.okhaz.boss.Fragments.OrderDetailsFragment;

import ae.okhaz.boss.Model.Order;
import ae.okhaz.admin.R;
import ae.okhaz.boss.ViewHolder.OrderViewHolder;
import com.google.gson.Gson;
import ae.okhaz.boss.rests.Requests.RequestOrderStatus;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Avinash on 27,November,2020
 */
public class AdminOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Order> orderArrayList;
    Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    int pos;
    SessionManagement sessionManagement;
    HashMap user;
    int from;

    public AdminOrderAdapter(ArrayList<Order> orderArrayList, Context context, int pos,int from) {
        this.orderArrayList = orderArrayList;
        this.context = context;
        this.pos = pos;
        this.from=from;
    }

    @Override
    public int getItemViewType(int position) {
        return orderArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.item_order_layout, parent, false);
            return new OrderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder1, int position) {

        sessionManagement = SessionManagement.getInstance(context);
        user=  sessionManagement.getUserDetails();

        if (holder1 instanceof OrderViewHolder) {
            final OrderViewHolder holder = (OrderViewHolder) holder1;
            Order order = orderArrayList.get(position);

            if (order != null) {
                if (orderArrayList.get(position).getOrderStatus() == 8) {
                    holder.status_img.setImageResource(R.drawable.pending_cv);
                    //Glide.with(context).load(R.drawable.pending_cv).into(holder.status_img);
                    holder.order_status_tv.setText("   " + orderArrayList.get(position).getOrderStatus() + "   ");

                } else if (orderArrayList.get(position).getOrderStatus() == 4) {
                    holder.status_img.setImageResource(R.drawable.ready_for_pickup);
                    // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                }
                else if (orderArrayList.get(position).getOrderStatus() == 2) {
                    holder.status_img.setImageResource(R.drawable.pending_cv);
                    // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                }
                else if (orderArrayList.get(position).getOrderStatus() == 3) {
                    holder.status_img.setImageResource(R.drawable.delivered);
                    // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                }
                else if (orderArrayList.get(position).getOrderStatus() == 5) {
                    holder.status_img.setImageResource(R.drawable.processing);
                    // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                }
                else if (orderArrayList.get(position).getOrderStatus() == 1) {
                    holder.status_img.setImageResource(R.drawable.order_place);
                    // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                }
                else if (orderArrayList.get(position).getOrderStatus() == 21) {
                    holder.status_img.setImageResource(R.drawable.partially_accepted);
                    // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                }
                else if (orderArrayList.get(position).getOrderStatus() == 6) {
                    holder.status_img.setImageResource(R.drawable.on_the_way);
                    // Glide.with(context).load(R.drawable.ontheway_cv).into(holder.status_img);

                } else if (orderArrayList.get(position).getOrderStatus() == 11) {
                    holder.status_img.setImageResource(R.drawable.processing);
                    // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                } else if (orderArrayList.get(position).getOrderStatus() == 7) {
                    holder.status_img.setImageResource(R.drawable.delivered_new);
                    //Glide.with(context).load(R.drawable.delivered).into(holder.status_img);
                }
                else if (orderArrayList.get(position).getOrderStatus() == 9) {
                    holder.status_img.setImageResource(R.drawable.calceled);
                    //Glide.with(context).load(R.drawable.calceled).into(holder.status_img);
                }
                else if (orderArrayList.get(position).getOrderStatus() == 10) {
                    holder.status_img.setImageResource(R.drawable.calceled);
                    //Glide.with(context).load(R.drawable.calceled).into(holder.status_img);
                }
                else if (orderArrayList.get(position).getOrderStatus() == 20) {
                    holder.status_img.setImageResource(R.drawable.delivered);
                    //Glide.with(context).load(R.drawable.calceled).into(holder.status_img);
                }

                if (orderArrayList.get(position).getOrderStatus1().equals("Pending")) {
                    holder.order_status_tv.setText("  " + orderArrayList.get(position).getOrderStatus1() + "  ");

                } else {
                    holder.order_status_tv.setText("" + orderArrayList.get(position).getOrderStatus1() + "");

                }
                if(orderArrayList.get(position).getGrandtotal()!=null){
                    double grandTotal=Double.parseDouble(orderArrayList.get(position).getGrandtotal());
                    holder.price_tv.setText("AED " + String.format("%.2f",grandTotal ) + "/ Qty : " + orderArrayList.get(position).getItemCount());

                }
                else {
                    holder.price_tv.setText( "Qty : " + orderArrayList.get(position).getItemCount());

                }

                if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Supplier")){

                    holder.main_order_no_tv.setVisibility(View.VISIBLE);
                    holder.main_order_no_tv.setText("(M.O #"+order.getPrimaryOrderNumber()+")");
                }
                else {
                    holder.main_order_no_tv.setVisibility(View.GONE);
                }

                    if (orderArrayList.get(position).getSupplierName() != null && !orderArrayList.get(position).getSupplierName().isEmpty()) {
                        holder.ll_supplier_name.setVisibility(View.VISIBLE);
                        holder.supplier_nm_tv.setText(orderArrayList.get(position).getSupplierName());
                    }
                    else {
                        holder.ll_supplier_name.setVisibility(View.INVISIBLE);
                    }


                holder.order_no_tv.setText(""+orderArrayList.get(position).getOrderID());
                holder.time_tv.setText(orderArrayList.get(position).getOrderDate() + " " + orderArrayList.get(position).getOrderTime());

                holder.customer_nm_tv.setText(orderArrayList.get(position).getFirstName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (orderArrayList.get(position).getOrderStatus() == 1) {
                            updateOrderStatus(orderArrayList.get(position).getOrderID(),
                                    user.get(SessionManagement.KEY_ID).toString(),
                                    user.get(SessionManagement.KEY_USER_TYPE).toString());

                                Bundle bundle = new Bundle();
                                String str = new Gson().toJson(order);
                                bundle.putString("role", "Admin");
                                bundle.putString("qty", String.valueOf(orderArrayList.get(position).getItemCount()));

                                bundle.putString("orderID",""+ orderArrayList.get(position).getOrderID());
                                bundle.putInt("pos", pos);

                                if (user.get(SessionManagement.KEY_USER_TYPE).equals("Supplier")) {
                                    bundle.putString("primaryId", orderArrayList.get(position).getPrimaryOrderNumber());
                                }
                        /*NavController navController = Navigation.findNavController(((MainActivity) context), R.id.nav_supplier_fragment);
                        navController.navigate(R.id.goto_order_details_admin, bundle);*/

                                if (from == 1) {
                                    //DeliveryBoyListActivity activity = (DeliveryBoyListActivity) view.getContext();
                                    MainDrawerBackActivity activity = (MainDrawerBackActivity) view.getContext();
                                    OrderDetailsFragment myFragment = new OrderDetailsFragment();
                                    //AdminOrderDetailFragment myFragment = new AdminOrderDetailFragment();
                                    myFragment.setArguments(bundle);
                                    //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_deli_list, myFragment).addToBackStack(null).commit();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                                } else {
                                    //SupplierActivity activity = (SupplierActivity) view.getContext();
                                    MainDrawerBackActivity activity = (MainDrawerBackActivity) view.getContext();
                                    AdminOrderDetailFragment myFragment = new AdminOrderDetailFragment();
                                    myFragment.setArguments(bundle);
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();

                                }

                            } else {

                            Bundle bundle = new Bundle();
                            String str = new Gson().toJson(order);
                            bundle.putString("status", orderArrayList.get(position).getOrderStatus1());
                            bundle.putString("order", str);
                            bundle.putString("role", "Admin");
                            bundle.putString("orderID",""+ orderArrayList.get(position).getOrderID());
                            bundle.putInt("pos", pos);

                            if (user.get(SessionManagement.KEY_USER_TYPE).equals("Supplier")) {
                                bundle.putString("primaryId", orderArrayList.get(position).getPrimaryOrderNumber());
                            }
                        /*NavController navController = Navigation.findNavController(((MainActivity) context), R.id.nav_supplier_fragment);
                        navController.navigate(R.id.goto_order_details_admin, bundle);*/

                            if (from == 1) {
                                //DeliveryBoyListActivity activity = (DeliveryBoyListActivity) view.getContext();
                                MainDrawerBackActivity activity = (MainDrawerBackActivity) view.getContext();
                                OrderDetailsFragment myFragment = new OrderDetailsFragment();
                                //AdminOrderDetailFragment myFragment = new AdminOrderDetailFragment();
                                myFragment.setArguments(bundle);
                                //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_deli_list, myFragment).addToBackStack(null).commit();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                            } else {
                                //SupplierActivity activity = (SupplierActivity) view.getContext();
                                MainDrawerBackActivity activity = (MainDrawerBackActivity) view.getContext();
                                AdminOrderDetailFragment myFragment = new AdminOrderDetailFragment();
                                myFragment.setArguments(bundle);
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();

                            }


                        }
                    }
                });
            }
        }
        else if (holder1 instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder1, position);
        }

    }

    private void showLoadingView(LoadingViewHolder holder, int position) {
    }


    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }



    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public void updateOrderStatus(int orderID,String custId,String ownerID){

        RequestOrderStatus requestOrderStatus=new RequestOrderStatus();
        requestOrderStatus.setOrderID(String.valueOf(orderID));
        requestOrderStatus.setOrderStatus("2");
        requestOrderStatus.setOrderType("MainOrder");
        requestOrderStatus.setOwnerID(custId);
        requestOrderStatus.setRole(ownerID);

        ServiceGenerator.getDelivery().updateOrderStatusAdmin("2",String.valueOf(orderID),ownerID,custId,"MainOrder","").enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(context, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
