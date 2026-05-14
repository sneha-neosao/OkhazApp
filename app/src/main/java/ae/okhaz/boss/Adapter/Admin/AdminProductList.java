package ae.okhaz.boss.Adapter.Admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import ae.okhaz.boss.Fragments.SubOrderDetailFragment;
import ae.okhaz.boss.Model.ProductListModel;
import ae.okhaz.boss.Model.Products;
import ae.okhaz.admin.R;
import ae.okhaz.boss.ViewHolder.ProductListViewHolder;
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

public class AdminProductList extends RecyclerView.Adapter<ProductListViewHolder>{
    ArrayList<ProductListModel> productsArrayList;
    ArrayList<Products> productsList;
    Context context;

    SessionManagement sessionManagement;
    HashMap user;
    int status;
    String orderID;

    public AdminProductList(ArrayList<ProductListModel> productsArrayList, Context context,int status,String orderID) {
        this.productsArrayList = productsArrayList;
        this.context = context;
        this.status=status;
        this.orderID=orderID;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_suborder_supplier, parent, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        ProductListModel listModel=productsArrayList.get(position);
        sessionManagement = SessionManagement.getInstance(context);
        user=  sessionManagement.getUserDetails();

        /*Set Sub order no*/
        holder.txt_subOrder_no.setText("Sub Order #"+productsArrayList.get(position).getOrderRef());
        holder.txt_suborder_total.setText("Total : AED "+productsArrayList.get(position).getTotal());

        /*Set sub order status*/
        holder.txt_subOrder_status.setText(productsArrayList.get(position).getOrderStatus1());
        String role= user.get(SessionManagement.KEY_USER_TYPE).toString();
        if(role.equals("Admin")){
            holder.txt_subOrder_status.setVisibility(View.VISIBLE);
        }

        if (productsArrayList.get(position).getOrderStatus() == 8) {
            holder.img_suborder_status.setImageResource(R.drawable.assigned_dm);
            //Glide.with(context).load(R.drawable.pending_cv).into(holder.status_img);
        }
        else if (productsArrayList.get(position).getOrderStatus() == 4) {
            holder.img_suborder_status.setImageResource(R.drawable.ready_for_pickup);
            // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

        }
        else if (productsArrayList.get(position).getOrderStatus() == -1) {
            holder.img_suborder_status.setImageResource(R.drawable.alll_order);
            // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

        }
        else if (productsArrayList.get(position).getOrderStatus()== 2) {
            holder.img_suborder_status.setImageResource(R.drawable.pending_cv);
            // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

        }
        else if (productsArrayList.get(position).getOrderStatus() == 3) {
            holder.img_suborder_status.setImageResource(R.drawable.delivered);
            // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

        }
        else if (productsArrayList.get(position).getOrderStatus() == 5) {
            holder.img_suborder_status.setImageResource(R.drawable.processing);
            // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

        }
        else if (productsArrayList.get(position).getOrderStatus()== 1) {
            holder.img_suborder_status.setImageResource(R.drawable.order_placed);
            // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

        }
        else if (productsArrayList.get(position).getOrderStatus()== 0) {
            holder.img_suborder_status.setImageResource(R.drawable.test_order);
            // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

        }
        else if (productsArrayList.get(position).getOrderStatus() == 21) {
            holder.img_suborder_status.setImageResource(R.drawable.partial_accpted);
            // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

        }
        else if (productsArrayList.get(position).getOrderStatus() == 6) {
            holder.img_suborder_status.setImageResource(R.drawable.on_the_way);
            // Glide.with(context).load(R.drawable.ontheway_cv).into(holder.status_img);

        }
        else if (productsArrayList.get(position).getOrderStatus() == 11) {
            holder.img_suborder_status.setImageResource(R.drawable.processing);
            // Glide.with(context).load(R.drawable.processing).into(holder.status_img);
        }
        else if (productsArrayList.get(position).getOrderStatus() == 7) {
            holder.img_suborder_status.setImageResource(R.drawable.delivered_green);
            //Glide.with(context).load(R.drawable.delivered).into(holder.status_img);
        }
        else if (productsArrayList.get(position).getOrderStatus() == 9) {
            holder.img_suborder_status.setImageResource(R.drawable.calceled);
            //Glide.with(context).load(R.drawable.calceled).into(holder.status_img);
        }
        else if (productsArrayList.get(position).getOrderStatus() == 10) {
            holder.img_suborder_status.setImageResource(R.drawable.order_rejected);
            //Glide.with(context).load(R.drawable.calceled).into(holder.status_img);
        }
        else if (productsArrayList.get(position).getOrderStatus() == 20) {
            holder.img_suborder_status.setImageResource(R.drawable.delivered);
            //Glide.with(context).load(R.drawable.calceled).into(holder.status_img);
        }
        else if(productsArrayList.get(position).getOrderStatus() == 12)
        {
            holder.img_suborder_status.setImageResource(R.drawable.awaiting_payment);
        }
        else if(productsArrayList.get(position).getOrderStatus() == 22)
        {
            holder.img_suborder_status.setImageResource(R.drawable.awaiting_payment);
        }

        /*Set Supplier name*/
        holder.txtSupplierName.setText(productsArrayList.get(position).getSupplierName());

        /*Set Supplier address*/
        if((productsArrayList.get(position).getSuppAdd2()==null || productsArrayList.get(position).getSuppAdd2().isEmpty()) && (productsArrayList.get(position).getSuppAdd1()==null || productsArrayList.get(position).getSuppAdd1().isEmpty())){
            holder.ll_address_supplier.setVisibility(View.GONE);
        }
        else{
            holder.ll_address_supplier.setVisibility(View.VISIBLE);
            if(productsArrayList.get(position).getSuppAdd1()==null || productsArrayList.get(position).getSuppAdd1().isEmpty()|| productsArrayList.get(position).getSuppAdd1().equals("NA")){
                holder.txtSupplierAddress.setText(productsArrayList.get(position).getSuppAdd2());
            }else if(productsArrayList.get(position).getSuppAdd2()==null || productsArrayList.get(position).getSuppAdd2().isEmpty() || productsArrayList.get(position).getSuppAdd2().equals("NA")){
                holder.txtSupplierAddress.setText(productsArrayList.get(position).getSuppAdd1());
            }else{
                holder.txtSupplierAddress.setText(productsArrayList.get(position).getSuppAdd1()
                        +"\n"+productsArrayList.get(position).getSuppAdd2());
            }
        }

        /*Set Supplier contact no*/
        if((productsArrayList.get(position).getSuppMob()==null || productsArrayList.get(position).getSuppMob().isEmpty()) && (productsArrayList.get(position).getSuppTele()==null || productsArrayList.get(position).getSuppTele().isEmpty())){
            holder.ll_supplier_contact_no.setVisibility(View.GONE);
        }
        else{
            holder.ll_supplier_contact_no.setVisibility(View.VISIBLE);
            if(productsArrayList.get(position).getSuppMob()==null || productsArrayList.get(position).getSuppMob().isEmpty()|| productsArrayList.get(position).getSuppMob().equals("NA")){
                holder.txtSupplierContactNo.setText(productsArrayList.get(position).getSuppTele());
            }else if(productsArrayList.get(position).getSuppTele()==null || productsArrayList.get(position).getSuppTele().isEmpty() || productsArrayList.get(position).getSuppTele().equals("NA")){
                holder.txtSupplierContactNo.setText(productsArrayList.get(position).getSuppMob());
            }else{
                holder.txtSupplierContactNo.setText(productsArrayList.get(position).getSuppMob()
                        +"\n"+productsArrayList.get(position).getSuppTele());
            }
        }

        /*if(productsArrayList.get(position).getSupplierName().equals("null")){
            holder.txt_subOrder_name.setVisibility(View.GONE);
            //  holder.txt_subOrder_name.setText("Name : "+productsArrayList.get(position).getSupplierName());
        }
        else {
            holder.txt_subOrder_name.setVisibility(View.VISIBLE);
            holder.txt_subOrder_name.setText("Supplier : "+productsArrayList.get(position).getSupplierName());
        }

        if(productsArrayList.get(position).getSuppMob()==null){
            holder.txt_subOrder_Tele.setVisibility(View.GONE);
            // holder.txt_subOrder_Tele.setText("Tele : "+productsArrayList.get(position).getSupplierTele());
        }
        else if(productsArrayList.get(position).getSuppMob().isEmpty()){
            holder.txt_subOrder_Tele.setVisibility(View.GONE);
            // holder.txt_subOrder_Tele.setText("Tele : "+productsArrayList.get(position).getSupplierTele());
        }
        else {
            holder.txt_subOrder_Tele.setVisibility(View.VISIBLE);
            holder.txt_subOrder_Tele.setText("Mobile : "+productsArrayList.get(position).getSuppMob());
        }*/



        /*More button click*/
        holder.btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( holder.btn_more.getText().equals("More...")){
                    holder.btn_more.setText("less...");
                    holder.ll_view_supplier_info.setVisibility(View.VISIBLE);
                    holder.ll_email_bank_supplier_info.setVisibility(View.VISIBLE);

                    /*Set Supplier email*/
                    if(productsArrayList.get(position).getSuppEmail()==null || productsArrayList.get(position).getSuppEmail().isEmpty()){
                        holder.ll_email_supplier.setVisibility(View.GONE);
                    }
                    else{
                        holder.ll_email_supplier.setVisibility(View.VISIBLE);
                        if(productsArrayList.get(position).getSuppEmail().equals("NA")){
                            holder.cv_supplier_email.setVisibility(View.GONE);
                        }
                        holder.tvEmailSupplier.setText(productsArrayList.get(position).getSuppEmail());
                    }


                    /**Set Supplier bank details*/

                    /*Set Supplier bank name*/
                    if(productsArrayList.get(position).getBankName()==null){
                        holder.txt_subOrder_bankName.setVisibility(View.GONE);
                        holder.ll_bank_details_supplier.setVisibility(View.GONE);
                    }
                    else {
                        holder.txt_subOrder_bankName.setVisibility(View.VISIBLE);
                        holder.ll_bank_details_supplier.setVisibility(View.VISIBLE);
                        holder.txt_subOrder_bankName.setText("Bank Name : "+productsArrayList.get(position).getBankName());
                    }

                    /*Set Supplier bank account no*/
                    if(productsArrayList.get(position).getAccountNumber()==null){
                        holder.txt_subOrder_accNum.setVisibility(View.GONE);
                    }
                    else {
                        holder.txt_subOrder_accNum.setVisibility(View.VISIBLE);
                        holder.txt_subOrder_accNum.setText("Account Number : "+productsArrayList.get(position).getAccountNumber());

                    }

                    /*Set Supplier bank branch*/
                    if(productsArrayList.get(position).getAccountBranch()==null){
                        holder.txt_subOrder_accBranch.setVisibility(View.GONE);
                    }
                    else {
                        holder.txt_subOrder_accBranch.setVisibility(View.VISIBLE);
                        holder.txt_subOrder_accBranch.setText("Account Branch : "+productsArrayList.get(position).getAccountBranch());

                    }

                    /*Set Supplier bank IFSC*/
                    if(productsArrayList.get(position).getIfscCode()==null){
                        holder.txt_subOrder_ifsc.setVisibility(View.GONE); }
                    else {
                        holder.txt_subOrder_ifsc.setVisibility(View.VISIBLE);
                        holder.txt_subOrder_ifsc.setText("IFSC Code : "+productsArrayList.get(position).getIfscCode());

                    }

                    /*Set Supplier bank address*/
                    if(productsArrayList.get(position).getBankAddress()==null){
                        holder.txt_subOrder_bankAddress.setVisibility(View.GONE);
                    }else {
                        holder.txt_subOrder_bankAddress.setVisibility(View.VISIBLE);
                        holder.txt_subOrder_bankAddress.setText("Bank Address : "+productsArrayList.get(position).getBankAddress());
                    }

                }else{
                    holder. btn_more.setText("More...");
                    holder.ll_view_supplier_info.setVisibility(View.GONE);
                    holder.ll_email_bank_supplier_info.setVisibility(View.GONE);
                }

            }
        });

        if(productsArrayList.get(position).getLattitude()==null){
            holder.img_map.setVisibility(View.GONE);
        }else {
            holder.img_map.setVisibility(View.VISIBLE);
        }

        holder.img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             /*   if(productsArrayList.get(position).getLattitude()!=null && productsArrayList.get(position).getLongitude()!=null){
                String packageName = "com.google.android.apps.maps";
                String query = "google.navigation:q="+productsArrayList.get(position).getLattitude()+","+productsArrayList.get(position).getLongitude()+"&mode=l";

                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(query));
                context.startActivity(intent);
                }*/
            }
        });

        holder.txt_subOrder_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listModel.getOrderStatus()==1){
                    updateOrderStatus(listModel.getOrderID(),
                            listModel.getSupplierID(),
                            user.get(SessionManagement.KEY_USER_TYPE).toString());
                }
               String role= user.get(SessionManagement.KEY_USER_TYPE).toString();
                if(role.equals("Admin")) {
                    Bundle bundle = new Bundle();
                    String str = new Gson().toJson(listModel);
                    bundle.putString("subOrder", str);
                    bundle.putString("status", listModel.getOrderStatus1());
                    bundle.putString("mainOrderID", orderID);
                    bundle.putInt("orderID", Integer.parseInt(listModel.getOrderID()));
                    //bundle.putInt("pos", position);
                    //SupplierActivity activity = (SupplierActivity) view.getContext();
                    MainDrawerBackActivity activity = (MainDrawerBackActivity) view.getContext();
                    //AdminOrderDetailActivity1 activity = (AdminOrderDetailActivity1) view.getContext();
                    SubOrderDetailFragment myFragment = new SubOrderDetailFragment();
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
            }
        });



        productsList=productsArrayList.get(position).getOrderItems();

       /* holder.img_subOrder_downArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.ll_subOrder_detail.setVisibility(View.VISIBLE);
                holder.img_subOrder_upArrow.setVisibility(View.VISIBLE);
                holder.img_subOrder_downArrow.setVisibility(View.GONE);
            }
        });

        holder.img_subOrder_upArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.img_subOrder_downArrow.setVisibility(View.VISIBLE);
                holder.ll_subOrder_detail.setVisibility(View.GONE);
                holder.img_subOrder_upArrow.setVisibility(View.GONE);
            }
        });*/

        AdminProductAdapter productAdapter=new AdminProductAdapter(productsList,context,status);
        holder.rv_items.setAdapter(productAdapter);

    }

    public void updateOrderStatus(String orderID,String custId,String ownerID){

        RequestOrderStatus requestOrderStatus=new RequestOrderStatus();
        requestOrderStatus.setOrderID(String.valueOf(orderID));
        requestOrderStatus.setOrderStatus("2");
        requestOrderStatus.setOrderType("SubOrder");
        requestOrderStatus.setOwnerID(custId);
        requestOrderStatus.setRole(ownerID);

        ServiceGenerator.getDelivery().updateOrderStatusAdmin("2",String.valueOf(orderID),ownerID,custId,"SubOrder","").enqueue(new Callback<ResponseCommon>() {
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

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }
}
