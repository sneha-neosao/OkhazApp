package ae.okhaz.boss.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ae.okhaz.admin.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import ae.okhaz.boss.Model.ProductModel;
import ae.okhaz.boss.ViewHolder.HomeProductListHolder;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeProductListAdapter extends RecyclerView.Adapter<HomeProductListHolder>{

    ArrayList<ProductModel> productListModels;
    Context context;
    SessionManagement sessionManagement;
    HashMap user;

    public HomeProductListAdapter(Context context,ArrayList<ProductModel> productListModels)
    {
        this.context=context;
        this.productListModels=productListModels;

    }

    @NonNull
    @Override
    public HomeProductListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_product_list_new, parent, false);
        return new HomeProductListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeProductListHolder holder, int position) {

        if(productListModels.get(position)!=null) {
            holder.txt_product_name.setText(productListModels.get(position).getItemName());
            holder.txt_product_unit.setText(productListModels.get(position).getUom());
            holder.txt_product_active.setText(productListModels.get(position).getStatus());
            holder.txt_product_stock.setText(productListModels.get(position).getStockingType());

            if (productListModels.get(position).getImage() != null) {
                if (!productListModels.get(position).getImage().isEmpty()) {
                    Glide.with(context)
                            .load(productListModels.get(position).getImage())
                            .error(R.drawable.noimageavailable)
                            .placeholder(R.drawable.noimageavailable)
                            .into(holder.img_product);
                }
            } else {
                Glide.with(context)
                        .load(R.drawable.noimageavailable)
                        .error(R.drawable.noimageavailable)
                        .placeholder(R.drawable.noimageavailable)
                        .into(holder.img_product);
            }

            if (productListModels.get(position).getStockingType() != null){
                if (productListModels.get(position).getStockingType().equals("Stock")) {
                    holder.switch_stock.setChecked(true);
                }
                else {
                    holder.switch_stock.setChecked(false);
                }
           }

            if (productListModels.get(position).getStatus() != null) {
                if (productListModels.get(position).getStatus().equals("ACTIVE")) {
                    holder.switch_active.setChecked(true);
                }
                else {
                    holder.switch_active.setChecked(false);
                }
            }

            if(productListModels.get(position).getItemSellingprice()!=null)
            {
                double priced = 0;
                try {
                    priced = Double.parseDouble(productListModels.get(position).getItemSellingprice());
                    String p = String.format("%.2f", priced);
                    holder.txt_product_price1.setText(p.substring(0, p.length() - 3));
                    holder.txt_product_price2.setText(p.substring(p.length() - 3));

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }


            holder.switch_active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.switch_active.isChecked()){
                        changeStatus("ACTIVE",
                                productListModels.get(position).getItemID(), holder,position);
                    } else {
                        changeStatus("INACTIVE",
                                productListModels.get(position).getItemID(), holder,position);
                    }
                }
            });

            /*holder.switch_active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        changeStatus("ACTIVE", productListModels.get(position).getItemID(), holder);
                    } else {
                        changeStatus("INACTIVE", productListModels.get(position).getItemID(), holder);
                    }
                }
            });
*/
            holder.switch_stock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.txt_product_stock.getText().equals("Stock")) {
                        if(!holder.switch_stock.isChecked()){
                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.dialog_stock_change);
                            LinearLayout ll_unavailable_indefinitely, ll_unavailable_today;

                            ll_unavailable_indefinitely = dialog.findViewById(R.id.ll_unavailable_indefinitely);
                            ll_unavailable_today = dialog.findViewById(R.id.ll_unavailable_today);

                            dialog.setCancelable(false);
                            dialog.show();

                            ll_unavailable_indefinitely.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    changeStock("Non Stock",
                                            productListModels.get(position).getItemID(), holder,position);
                                    dialog.dismiss();
                                    holder.switch_stock.setChecked(false);
                                }
                            });

                            ll_unavailable_today.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    changeStock("One Day",
                                            productListModels.get(position).getItemID(), holder,position);
                                    dialog.dismiss();
                                    holder.switch_stock.setChecked(false);
                                }
                            });
                        }
                    } else if (holder.switch_stock.isChecked()) {
                        changeStock("Stock", productListModels.get(position).getItemID(), holder,position);
                    }
                }
            });

/*
            holder.switch_stock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (holder.txt_product_stock.getText().equals("Stock")) {
                        if (!isChecked) {
                            Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.dialog_stock_change);
                            LinearLayout ll_unavailable_indefinitely, ll_unavailable_today;

                            ll_unavailable_indefinitely = dialog.findViewById(R.id.ll_unavailable_indefinitely);
                            ll_unavailable_today = dialog.findViewById(R.id.ll_unavailable_today);

                            dialog.setCancelable(false);
                            dialog.show();

                            ll_unavailable_indefinitely.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    changeStock("Non Stock", productListModels.get(position).getItemID(), holder);
                                    dialog.dismiss();
                                    holder.switch_stock.setChecked(false);
                                }
                            });

                            ll_unavailable_today.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    changeStock("One Day", productListModels.get(position).getItemID(), holder);
                                    dialog.dismiss();
                                    holder.switch_stock.setChecked(false);
                                }
                            });
                        }
                    } else if (isChecked) {
                        changeStock("Stock", productListModels.get(position).getItemID(), holder);

                    }
                }
            });
*/

        }

      /*  holder.switch_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialog_stock_change);
                LinearLayout ll_unavailable_indefinitely,ll_unavailable_today;

                ll_unavailable_indefinitely=dialog.findViewById(R.id.ll_unavailable_indefinitely);
                ll_unavailable_today=dialog.findViewById(R.id.ll_unavailable_today);

                dialog.setCancelable(false);
                dialog.show();

                ll_unavailable_indefinitely.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        changeStock("Non Stock",productListModels.get(position).getItemID(),holder);
                        dialog.dismiss();
                        holder.switch_stock.setChecked(false);
                    }
                });

                ll_unavailable_today.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        changeStock("One Day",productListModels.get(position).getItemID(),holder);
                        dialog.dismiss();
                        holder.switch_stock.setChecked(false);
                    }
                });

            }
        });*/


    }

    @Override
    public int getItemCount() {
        return productListModels.size();
    }

    public void changeStock(String stockStatus, String itemID,HomeProductListHolder homeProductListHolder,int pos){
        sessionManagement = SessionManagement.getInstance(context);
        user = sessionManagement.getUserDetails();
        String branchId=user.get(SessionManagement.KEY_BRANCH_ID).toString();
        ServiceGenerator.getDelivery().updateProductStockStatus(branchId,stockStatus,itemID).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        Toast.makeText(context, response.body().getMessage()+" "+homeProductListHolder.txt_product_name.getText(), Toast.LENGTH_SHORT).show();

                        homeProductListHolder.txt_product_stock.setText(stockStatus);
                        if(stockStatus.equals("Stock") || stockStatus.equals("STOCK")){
                            homeProductListHolder.switch_stock.setChecked(true);
                        }
                        else {
                            homeProductListHolder.switch_stock.setChecked(false);
                        }
                        productListModels.get(pos).setStockingType(stockStatus);
                    }
                    else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {

                }
                else if (t instanceof IOException)
                {

                }
                else
                {
                    //  Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void changeStatus(String stockStatus, String itemID,HomeProductListHolder homeProductListHolder,int pos){
        sessionManagement = SessionManagement.getInstance(context);
        user = sessionManagement.getUserDetails();
        String branchId=user.get(SessionManagement.KEY_BRANCH_ID).toString();
        ServiceGenerator.getDelivery().updateProductStatus(branchId,stockStatus,itemID).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {

                        homeProductListHolder.txt_product_active.setText(stockStatus);
                        if(stockStatus.equals("Active") || stockStatus.equals("ACTIVE")){
                            homeProductListHolder.switch_active.setChecked(true);
                        }
                        productListModels.get(pos).setStatus(stockStatus);
                        Toast.makeText(context, response.body().getMessage()+" "+homeProductListHolder.txt_product_name.getText(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {

                }
                else if (t instanceof IOException)
                {

                }
                else
                {
                    //  Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
