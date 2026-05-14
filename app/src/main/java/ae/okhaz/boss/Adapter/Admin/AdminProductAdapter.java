package ae.okhaz.boss.Adapter.Admin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import ae.okhaz.boss.Model.Products;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.DetectConnection;
import ae.okhaz.boss.Utils.Validations;
import ae.okhaz.boss.ViewHolder.ProductViewHolder;
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
 * Created by Avinash on 01,December,2020
 */
public class AdminProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    ArrayList<Products> productsArrayList;
    Context context;
    ProductViewHolder productViewHolderss;
    int status;
    int positions=0;
    String OrderItemId="";
    String scanstatus="";
    BarcodeListners barcodeListners;
    IntentFilter intentFilter;

    SessionManagement sessionManagement;
    HashMap user;

    public AdminProductAdapter(ArrayList<Products> productsArrayList, Context context,int status) {
        this.productsArrayList = productsArrayList;
        this.context = context;
        this.status=status;

        barcodeListners = new BarcodeListners();
        intentFilter = new IntentFilter("scanned");

        context.registerReceiver(barcodeListners, intentFilter);

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_layout_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        sessionManagement = SessionManagement.getInstance(context);
        user = sessionManagement.getUserDetails();

        if (user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Supplier")) {
            if (user.get(SessionManagement.KEY_isPaymentDetailSection_On_OD_Available4Supplier).toString().equals("1")) {
                //
                holder.product_prices_tv.setVisibility(View.VISIBLE);
            } else {
                holder.product_prices_tv.setVisibility(View.GONE);
            }
        }

        if (status == 6 || status == 9)
        {
            holder.barcode_iv.setVisibility(View.GONE);
        }
        else {
            holder.barcode_iv.setVisibility(View.VISIBLE);
        }

        if (productsArrayList.get(position).getItemScanStatus() != null)
        {
            Glide.with(context).load(R.drawable.checked).into(holder.barcode_iv);
        }
        if(status==7){
            Glide.with(context).load(R.drawable.checked).into(holder.barcode_iv);
        }

        String urls= productsArrayList.get(position).getImage();

        Glide.with(context).load(urls).placeholder(R.drawable.logo).into(holder.product_iv);

        double itemSellingPrice=Double.parseDouble(productsArrayList.get(position).getItemSellingprice());
        String price=String.format("%.2f",itemSellingPrice);

        holder.product_names_tv.setText(productsArrayList.get(position).getItemName()+"- "+productsArrayList.get(position).getUom());
        holder.product_barcode_tv.setText(productsArrayList.get(position).getBarcode());
        holder.product_prices_tv.setText("AED "+price);
        holder.product_qty_tv.setText("(QTY : "+productsArrayList.get(position).getQuantity()+")");
        holder.product_suplier_tv.setText(productsArrayList.get(position).getSuppName());

        if(productsArrayList.get(position).getBarcode()==null || productsArrayList.get(position).getBarcode().equals("0") || productsArrayList.get(position).getBarcode().equals("")){
            holder.product_names_tv.setTextColor(context.getResources().getColor(R.color.barcode));
            holder.btn_add_barcode.setVisibility(View.VISIBLE);
            holder.barcode_iv.setVisibility(View.GONE);
            holder.add_barcode_iv.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.btn_add_barcode.setVisibility(View.GONE);
            holder.add_barcode_iv.setVisibility(View.GONE);
        }

       /* if(status==5){
            holder.btn_add_barcode.setEnabled(true);
            holder.barcode_iv.setEnabled(true);
        }
        else {
            holder.btn_add_barcode.setEnabled(false);
            holder.barcode_iv.setEnabled(false);
           // Toast.makeText(context,"Barcode facility is not available",Toast.LENGTH_LONG).show();
        }*/

        holder.add_barcode_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status==4 || status==5 || status==8 || status==11){
                   // holder.btn_add_barcode.setEnabled(true);

                AlertDialog.Builder alerts = new AlertDialog.Builder(context);
                View view1 = LayoutInflater.from(context).inflate(R.layout.item_layout_scan_barcode, null);
                alerts.setView(view1);
                alerts.setCancelable(false);

                AlertDialog alertDialog = alerts.create();
                alertDialog.show();

                EditText textInputEditText = view1.findViewById(R.id.barcode_edts);
                textInputEditText.requestFocus();
                Button add_comments= view1.findViewById(R.id.add_Barcode);
                ImageView close_iv= view1.findViewById(R.id.close_iv);
                LinearLayout ll_scan_barcode= view1.findViewById(R.id.ll_scan_barcode);

                close_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                ll_scan_barcode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DetectConnection.checkInternetConnection(context)) {

                            if (productsArrayList.get(position).getItemScanStatus() == null) {

//                        productsArrayList.get(position).setFlag(true);
                                positions = position;
                                productViewHolderss = holder;
                                IntentIntegrator intentIntegrator = new IntentIntegrator(((MainDrawerBackActivity) context));
                                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                                intentIntegrator.setCameraId(0);
                                intentIntegrator.setOrientationLocked(false);
                                intentIntegrator.setPrompt("scanning");
                                intentIntegrator.setBeepEnabled(true);
                                intentIntegrator.setBarcodeImageEnabled(true);
                                intentIntegrator.initiateScan();
                            }
                        } else {
                            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                add_comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (DetectConnection.checkInternetConnection(context))
                        {

                            if (!Validations.requireValidate(textInputEditText))
                            {
                                textInputEditText.setError("Enter Barcode");
                                textInputEditText.requestFocus();
                            }
                            else {
                                alertDialog.dismiss();
                                productViewHolderss = holder;

                                textInputEditText.setText(textInputEditText.getText().toString().replaceAll(" ",""));
                                textInputEditText.setSelection(textInputEditText.getText().length());

                                String barcode=textInputEditText.getText().toString();

                                updateBarcode(barcode,productsArrayList.get(position).getItemId(),productsArrayList.get(position).getOrderItemId(),holder.barcode_iv);
                            }
                        }
                        else
                        {
                            Toast.makeText(context, "please check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                }
                else {
                    //holder.btn_add_barcode.setEnabled(false);
                    Toast.makeText(context,"Barcode facility is not available",Toast.LENGTH_LONG).show();
                }

            }

        });

        holder.barcode_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status==4 || status==5 || status==8 || status==11){
                    if (DetectConnection.checkInternetConnection(context)) {

                        if (productsArrayList.get(position).getItemScanStatus() == null) {

//                        productsArrayList.get(position).setFlag(true);
                            positions = position;
                            productViewHolderss = holder;
                            IntentIntegrator intentIntegrator = new IntentIntegrator(((MainDrawerBackActivity) context));
                            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                            intentIntegrator.setCameraId(0);
                            intentIntegrator.setOrientationLocked(false);
                            intentIntegrator.setPrompt("scanning");
                            intentIntegrator.setBeepEnabled(true);
                            intentIntegrator.setBarcodeImageEnabled(true);
                            intentIntegrator.initiateScan();
                        }
                    } else {
                        Toast.makeText(context, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                 else
                    {
                      //  holder.barcode_iv.setEnabled(false);
                        Toast.makeText(context,"Barcode facility is not available",Toast.LENGTH_LONG).show();
                    }
            }

        });
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public class BarcodeListners extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle!= null)
            {
                String datas;

                datas = bundle.getString("datas");
                setScanned(datas);
            }
        }
    }

    private void setScanned(String datas) {
        if (datas.equals(productsArrayList.get(positions).getBarcode()))
        {
            ServiceGenerator.getDelivery().updateBarcodeScanStatus("1",productsArrayList.get(positions).getOrderItemId()).enqueue(new Callback<ResponseCommon>() {
                @Override
                public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                    if (response.isSuccessful())
                    {
                        if (response.body().isStatus())
                        {
                            Glide.with(context).load(R.drawable.checked).into(productViewHolderss.barcode_iv);
                            Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            productsArrayList.get(positions).setItemScanStatus("1");

                           /* Intent intent = new Intent("orders");
                            intent.putExtra("orders", "orders");
                            context.sendBroadcast(intent);*/
                            notifyDataSetChanged();

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
                        setScanned(datas);
                    }
                    else if (t instanceof IOException)
                    {
                        // "Timeout";
                        setScanned(datas);
                    }
                    else
                    {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(context, "Barcode not matched !, please try again", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateBarcode(String barcode,String itemId,String orderItemId,ImageView imageView) {

        ServiceGenerator.getDelivery().updateBarcode(barcode,orderItemId,itemId).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        Glide.with(context).load(R.drawable.checked).into(productViewHolderss.barcode_iv);
                        Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        productsArrayList.get(positions).setItemScanStatus("1");

                     //   productViewHolderss.btn_add_barcode.setVisibility(View.GONE);
                        Intent intent = new Intent("orders");
                        intent.putExtra("orders", "orders");
                        context.sendBroadcast(intent);

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
                    updateBarcode(barcode,itemId,orderItemId,imageView);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    updateBarcode(barcode,itemId,orderItemId,imageView);
                }
                else
                {
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
