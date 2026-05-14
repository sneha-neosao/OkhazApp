package ae.okhaz.boss.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import ae.okhaz.boss.Model.Products;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.DetectConnection;
import ae.okhaz.boss.Utils.Validations;
import ae.okhaz.boss.ViewHolder.ProductViewHolder;
import com.google.zxing.integration.android.IntentIntegrator;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Avinash on 01,December,2020
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    ArrayList<Products> productsArrayList;
    Context context;
    ProductViewHolder productViewHolderss;
    int status;
    int clickedPosition=0;
    String OrderItemId="";
    String scanstatus="";
    BarcodeListners  barcodeListners;
    IntentFilter intentFilter;

    public ProductAdapter(ArrayList<Products> productsArrayList, Context context, int orderStatus) {
        this.productsArrayList = productsArrayList;
        this.context = context;
        this.status = orderStatus;

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



        if (status == 6  || status == 9)
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

        holder.product_names_tv.setText(productsArrayList.get(position).getItemName()+"-"+productsArrayList.get(position).getUom());
        holder.product_barcode_tv.setText(productsArrayList.get(position).getBarcode());
        holder.product_prices_tv.setText("AED "+price);
        holder.product_qty_tv.setText("(QTY : "+productsArrayList.get(position).getQuantity()+")");
        holder.product_suplier_tv.setText(productsArrayList.get(position).getSuppName());

        if(productsArrayList.get(position).getBarcode()==null || productsArrayList.get(position).getBarcode().equals("0") || productsArrayList.get(position).getBarcode().equals("")){
            holder.product_names_tv.setTextColor(context.getResources().getColor(R.color.barcode));
            holder.btn_add_barcode.setVisibility(View.VISIBLE);
            holder.barcode_iv.setVisibility(View.GONE);
        }
        else
        {
            holder.btn_add_barcode.setVisibility(View.GONE);
        }

      /*  if(status==5){
            holder.btn_add_barcode.setEnabled(true);
        }
        else {
            holder.btn_add_barcode.setEnabled(false);
            Toast.makeText(context,"Barcode facility is not available",Toast.LENGTH_LONG).show();

        }
*/
        holder.btn_add_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status==4 || status==5 || status==8 || status==11){


                AlertDialog.Builder alerts = new AlertDialog.Builder(context);
                View view1 = LayoutInflater.from(context).inflate(R.layout.item_add_barcode_layout, null);
                alerts.setView(view1);
                alerts.setCancelable(false);

                AlertDialog alertDialog = alerts.create();
                alertDialog.show();

                TextInputEditText textInputEditText = view1.findViewById(R.id.barcode_edts);
                textInputEditText.requestFocus();
                Button add_comments= view1.findViewById(R.id.add_Barcode);
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
                        if (DetectConnection.checkInternetConnection(context))
                        {

                            if (!Validations.requireTILValidate(textInputEditText))
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
                                updateBarcode(barcode,productsArrayList.get(position).getItemId(),productsArrayList.get(position).getOrderItemId());
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
                    Toast.makeText(context,"Barcode facility is not available",Toast.LENGTH_LONG).show();

                }
            }
        });

        holder.barcode_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status==4 || status==5 || status==8 || status==11)
                {


                if(DetectConnection.checkInternetConnection(context))
                {

                    if (productsArrayList.get(position).getItemScanStatus() == null) {
                        clickedPosition = position;
                        productViewHolderss = holder;

                        IntentIntegrator intentIntegrator = new IntentIntegrator(((MainDrawerBackActivity) context));
                        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                        intentIntegrator.setCameraId(0);
                        intentIntegrator.setOrientationLocked(false);
                        intentIntegrator.setPrompt("Scanning");
                        intentIntegrator.setBeepEnabled(true);
                        intentIntegrator.setBarcodeImageEnabled(true);
                        intentIntegrator.initiateScan();
                    }
                }
                else
                {
                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
                else {
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
        if (datas.equals(productsArrayList.get(clickedPosition).getBarcode()))
        {

            ServiceGenerator.getDelivery().updateBarcodeScanStatus("1",productsArrayList.get(clickedPosition).getOrderItemId()).enqueue(new Callback<ResponseCommon>() {
                @Override
                public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                    if (response.isSuccessful())
                    {
                        if (response.body().isStatus())
                        {
                            Glide.with(context).load(R.drawable.checked).into(productViewHolderss.barcode_iv);
                            Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            productsArrayList.get(clickedPosition).setItemScanStatus("1");

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

    private void updateBarcode(String barcode,String itemId,String orderItemId) {

            ServiceGenerator.getDelivery().updateBarcode(barcode,orderItemId,itemId).enqueue(new Callback<ResponseCommon>() {
                @Override
                public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                    if (response.isSuccessful())
                    {
                        if (response.body().isStatus())
                        {
                            Glide.with(context).load(R.drawable.checked).into(productViewHolderss.barcode_iv);
                            Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            productsArrayList.get(clickedPosition).setItemScanStatus("1");

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
                        updateBarcode(barcode,itemId,orderItemId);
                    }
                    else if (t instanceof IOException)
                    {
                        // "Timeout";
                        updateBarcode(barcode,itemId,orderItemId);
                    }
                    else
                    {
                        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }


}
