package ae.okhaz.boss.Activitys.Supplier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ae.okhaz.boss.Adapter.Admin.DeliveryBoyAdapter;
import ae.okhaz.boss.Model.DeliveryBoyModel;
import ae.okhaz.admin.R;
import ae.okhaz.boss.rests.Response.ResponseDeliveryBoyList;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.DeliveryBoyDetailsActivity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryBoyListActivity extends AppCompatActivity {
FloatingActionButton btn_addDeliveryBoy;
RecyclerView recyclerView;
//ProgressDialog progressDialog;
Dialog progressDialog;

public static Toolbar toolbar;
    SessionManagement sessionManagement;
    HashMap user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_list);

        //progressDialog=new ProgressDialog(this);

        progressDialog = new  Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(this).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);

        btn_addDeliveryBoy=findViewById(R.id.btn_addDeliveryBoy);
        recyclerView=findViewById(R.id.rv_deliveryBoy);
        toolbar=findViewById(R.id.toolbar);

        sessionManagement = SessionManagement.getInstance(DeliveryBoyListActivity.this);
        user=  sessionManagement.getUserDetails();



        /*ArrayList<DeliveryBoyModel> deliveryBoyModels=new ArrayList<>();

        DeliveryBoyModel deliveryBoyModel=new DeliveryBoyModel();
        deliveryBoyModel.setUserName("abcd");

        DeliveryBoyModel deliveryBoyModel1=new DeliveryBoyModel();
        deliveryBoyModel1.setUserName("abc");

        DeliveryBoyModel deliveryBoyModel2=new DeliveryBoyModel();
        deliveryBoyModel2.setUserName("ab");

        deliveryBoyModels.add(deliveryBoyModel);
        deliveryBoyModels.add(deliveryBoyModel1);
        deliveryBoyModels.add(deliveryBoyModel2);
*/

        toolbar.setTitle("Delivery Boy List");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                // SupplierActivity.toolbar.setNavigationOnClickListener();
            }
        });


        getDeliveryBoyList();


//        setSupportActionBar(toolbar);

        btn_addDeliveryBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DeliveryBoyListActivity.this, DeliveryBoyDetailsActivity.class)
                        .putExtra("fromAdd",1));

            }
        });

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_supplier_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification:
                Toast.makeText(this, "This is notification", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }
*/
    public void getDeliveryBoyList()
    {
        progressDialog.show();
        String ID,role,BranchCode;
        ID=user.get(SessionManagement.KEY_ID).toString();
        role=user.get(SessionManagement.KEY_USER_TYPE).toString();
        BranchCode=user.get(SessionManagement.KEY_BRANCH_ID).toString();

        ServiceGenerator.getDelivery().deliveryBoyList(ID,role,BranchCode).enqueue(new Callback<ResponseDeliveryBoyList>() {
            @Override
            public void onResponse(Call<ResponseDeliveryBoyList> call, Response<ResponseDeliveryBoyList> response) {
                if (response.isSuccessful())
                {
                    progressDialog.dismiss();
                    if (!response.body().isStatus()) {
                        progressDialog.dismiss();
                        Toast.makeText(DeliveryBoyListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {


                        progressDialog.dismiss();
                   ArrayList<DeliveryBoyModel> deliveryBoyModels=response.body().getResult().getDeliveryManList();
                    DeliveryBoyAdapter deliveryBoyAdapter=new DeliveryBoyAdapter(deliveryBoyModels,DeliveryBoyListActivity.this);
                    recyclerView.setAdapter(deliveryBoyAdapter);
                    }
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(DeliveryBoyListActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDeliveryBoyList> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {
                    progressDialog.dismiss();
                    // "Connection Timeout";
                    //updateBarcode(barcode,itemId,orderItemId);
                }
                else if (t instanceof IOException)
                {
                    progressDialog.dismiss();
                    // "Timeout";
                    // updateBarcode(barcode,itemId,orderItemId);
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(DeliveryBoyListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDeliveryBoyList();

        toolbar.setTitle("Delivery Boy List");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                // SupplierActivity.toolbar.setNavigationOnClickListener();
            }
        });

    }
}