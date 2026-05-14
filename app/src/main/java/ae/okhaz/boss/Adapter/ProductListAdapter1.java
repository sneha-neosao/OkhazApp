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

public class ProductListAdapter1 extends RecyclerView.Adapter<ProductListViewHolder>{
    ArrayList<ProductListModel> productsArrayList;
    ArrayList<Products> productsList;
    Context context;

    public ProductListAdapter1(ArrayList<ProductListModel> productsArrayList, Context context) {
        this.productsArrayList = productsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_layout_product_list, parent, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {

        holder.txt_subOrder_no.setText("Supplier Details : Order Ref #"+productsArrayList.get(position).getOrderRef());

        if(productsArrayList.get(position).getSupplierName().equals("null")){
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
        }

        if(productsArrayList.get(position).getBankName()==null){
            holder.txt_subOrder_bankName.setVisibility(View.GONE);
           // holder.txt_subOrder_bankName.setText("Bank Name : "+productsArrayList.get(position).getBankName());
        }
        else {
            holder.txt_subOrder_bankName.setVisibility(View.VISIBLE);
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
        }

        if(productsArrayList.get(position).getLattitude()==null){
            holder.img_map.setVisibility(View.GONE);
        }else {
            holder.img_map.setVisibility(View.VISIBLE);
        }

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
        holder.ll_subOrder_detail.setVisibility(View.VISIBLE);
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
        });

        ProductAdapter productAdapter=new ProductAdapter(productsList,context,productsArrayList.get(position).getOrderStatus());
        holder.rv_items.setAdapter(productAdapter);

    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }
}
