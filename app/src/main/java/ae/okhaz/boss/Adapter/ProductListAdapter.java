package ae.okhaz.boss.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.boss.Model.ProductListModel;
import ae.okhaz.boss.Model.Products;
import ae.okhaz.admin.R;
import ae.okhaz.boss.ViewHolder.ProductListViewHolder;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListViewHolder>{
    ArrayList<ProductListModel> productsArrayList;
    ArrayList<Products> productsList;
    Context context;

    public ProductListAdapter(ArrayList<ProductListModel> productsArrayList, Context context) {
        this.productsArrayList = productsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_suborder_supplier, parent, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {

        /*Set Sub order no*/
        holder.txt_subOrder_no.setText("Supplier Details : Order Ref #"+productsArrayList.get(position).getOrderRef());

        /*Set sub order status*/
        holder.txt_subOrder_status.setText(productsArrayList.get(position).getSupplierAccepted());

        /*Set Supplier name*/
        holder.txtSupplierName.setText(productsArrayList.get(position).getSupplierName());
        /*if(productsArrayList.get(position).getSupplierName().equals("null")){
            holder.ll_supplier_name.setVisibility(View.GONE);
            //  holder.txt_subOrder_name.setText("Name : "+productsArrayList.get(position).getSupplierName());
        }
        else {
            holder.ll_supplier_name.setVisibility(View.VISIBLE);

        }*/

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




        /*if(productsArrayList.get(position).getSuppMob()==null){
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

       /* if(productsArrayList.get(position).getBankName()==null){
            holder.txt_subOrder_bankName.setVisibility(View.GONE);
            holder.ll_bank_details_supplier.setVisibility(View.GONE);
           // holder.txt_subOrder_bankName.setText("Bank Name : "+productsArrayList.get(position).getBankName());
        }
        else {
            holder.txt_subOrder_bankName.setVisibility(View.VISIBLE);
            holder.ll_bank_details_supplier.setVisibility(View.VISIBLE);
             holder.txt_subOrder_bankName.setText("Bank Name : "+productsArrayList.get(position).getBankName());
        }

        if(productsArrayList.get(position).getAccountNumber()==null){
            holder.txt_subOrder_accNum.setVisibility(View.GONE);
          //  holder.txt_subOrder_accNum.setText("Account Number : "+productsArrayList.get(position).getAccountNumber());
        }
        else {
            holder.txt_subOrder_accNum.setVisibility(View.VISIBLE);
            holder.txt_subOrder_accNum.setText("Account Number : "+productsArrayList.get(position).getAccountNumber());

        }

        if(productsArrayList.get(position).getAccountBranch()==null){
            holder.txt_subOrder_accBranch.setVisibility(View.GONE);
           // holder.txt_subOrder_accBranch.setText("Account Branch : "+productsArrayList.get(position).getAccountBranch());
        }
        else {

            holder.txt_subOrder_accBranch.setVisibility(View.VISIBLE);
            holder.txt_subOrder_accBranch.setText("Account Branch : "+productsArrayList.get(position).getAccountBranch());

        }

        if(productsArrayList.get(position).getIfscCode()==null){
            holder.txt_subOrder_ifsc.setVisibility(View.GONE);
           // holder.txt_subOrder_ifsc.setText("IFSC Code : "+productsArrayList.get(position).getIfscCode());
        }
        else {
            holder.txt_subOrder_ifsc.setVisibility(View.VISIBLE);
            holder.txt_subOrder_ifsc.setText("IFSC Code : "+productsArrayList.get(position).getIfscCode());

        }

        if(productsArrayList.get(position).getBankAddress()==null){
            holder.txt_subOrder_bankAddress.setVisibility(View.GONE);
           // holder.txt_subOrder_bankAddress.setText("Bank Address : "+productsArrayList.get(position).getBankAddress());
        }else {
            holder.txt_subOrder_bankAddress.setVisibility(View.VISIBLE);
            holder.txt_subOrder_bankAddress.setText("Bank Address : "+productsArrayList.get(position).getBankAddress());
        }*/

        if(productsArrayList.get(position).getLattitude()==null){
            holder.img_map.setVisibility(View.GONE);
        }else {
            holder.img_map.setVisibility(View.VISIBLE);
        }

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

        holder.img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName = "com.google.android.apps.maps";
                String query = "google.navigation:q="+productsArrayList.get(position).getLattitude()+","+productsArrayList.get(position).getLongitude()+"&mode=l";

                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(query));
                context.startActivity(intent);
            }
        });

        productsList=productsArrayList.get(position).getOrderItems();
        /*holder.ll_subOrder_detail.setVisibility(View.VISIBLE);
        holder.img_subOrder_upArrow.setVisibility(View.VISIBLE);
        holder.img_subOrder_downArrow.setVisibility(View.GONE);
        holder.img_subOrder_downArrow.setOnClickListener(new View.OnClickListener() {
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

        ProductAdapter productAdapter=new ProductAdapter(productsList,context,productsArrayList.get(position).getOrderStatus());
        holder.rv_items.setAdapter(productAdapter);

    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }
}
