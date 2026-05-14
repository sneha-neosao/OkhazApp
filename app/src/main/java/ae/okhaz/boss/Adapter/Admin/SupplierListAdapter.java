package ae.okhaz.boss.Adapter.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ae.okhaz.admin.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import ae.okhaz.boss.Model.SupplierModel;
import ae.okhaz.boss.ViewHolder.SupplierListHolder;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.ProductListActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierListAdapter extends RecyclerView.Adapter<SupplierListHolder>{
    ArrayList<SupplierModel> modelArrayList;
    Context context;
    SessionManagement sessionManagement;
    HashMap user;

    public SupplierListAdapter (Context context,ArrayList<SupplierModel> modelArrayList){
        this.context=context;
        this.modelArrayList=modelArrayList;
    }


    @NonNull
    @Override
    public SupplierListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_layout_supplier_list, parent, false);
        return new SupplierListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierListHolder holder, int position) {

        if(modelArrayList.get(position)!=null) {
            holder.txt_supplier_name.setText(modelArrayList.get(position).getSuppName());
            holder.txt_supplier_address.setText(modelArrayList.get(position).getSuppID());
            holder.txt_supplierStatus.setText(modelArrayList.get(position).getSuppStatus());
            holder.txt_close_time.setText(modelArrayList.get(position).getSuppAdd1());

            if (holder.txt_supplierStatus.getText().equals("Active") || holder.txt_supplierStatus.getText().equals("ACTIVE")) {
                holder.switch_supplier.setChecked(true);
            } else {
                holder.switch_supplier.setChecked(false);
            }

            if (modelArrayList.get(position).getStoreImageLocation() != null) {
                if (!modelArrayList.get(position).getStoreImageLocation().isEmpty()) {
                    Glide.with(context)
                            .load(modelArrayList.get(position).getStoreImageLocation())
                            .error(R.drawable.home_logo)
                            .placeholder(R.drawable.home_logo)
                            .into(holder.img_supplier);
                }
            }

            holder.switch_supplier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   /* if (isChecked) {
                        changeSupplierStatus("ACTIVE", modelArrayList.get(position).getSuppID(), holder);
                    } else {
                        changeSupplierStatus("INACTIVE", modelArrayList.get(position).getSuppID(), holder);
                    }*/
                }
            });

            holder.switch_supplier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.switch_supplier.isChecked()){
                        changeSupplierStatus("ACTIVE",
                                modelArrayList.get(position).getSuppID(), holder,position);
                    }
                    else{
                        changeSupplierStatus("INACTIVE",
                                modelArrayList.get(position).getSuppID(), holder,position);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, ProductListActivity.class)
                            .putExtra("suppID", modelArrayList.get(position).getSuppID()));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public void changeSupplierStatus(String suppStatus, String suppID,SupplierListHolder supplierListHolder,int pos){
        sessionManagement = SessionManagement.getInstance(context);
        user = sessionManagement.getUserDetails();
        String branchId=user.get(SessionManagement.KEY_BRANCH_ID).toString();
        ServiceGenerator.getDelivery().updateSupplierStatus(branchId,suppStatus,suppID).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        //notifyDataSetChanged();
                        supplierListHolder.txt_supplierStatus.setText(suppStatus);
                        if(suppStatus.equals("Active") || suppStatus.equals("ACTIVE")){
                            supplierListHolder.switch_supplier.setChecked(true);

                            modelArrayList.get(pos).setSuppStatus(suppStatus);
                        }
                        Toast.makeText(context, response.body().getMessage()+" "+suppID, Toast.LENGTH_SHORT).show();
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
