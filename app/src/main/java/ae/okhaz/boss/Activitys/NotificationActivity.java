package ae.okhaz.boss.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import ae.okhaz.admin.R;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import ae.okhaz.boss.Adapter.Admin.NotificationAdapter;
import ae.okhaz.boss.Model.NotificationModel;
import ae.okhaz.boss.rests.Response.ResponseNotification;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    ImageView iv_back_notification;
    //ProgressDialog progressDialog;
    NotificationAdapter notificationAdapter;
    RecyclerView recyclerView;
    SessionManagement sessionManagement;
    HashMap user;
    TextView txt_noti_count;
    Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        iv_back_notification = findViewById(R.id.iv_back_notification);
        recyclerView = findViewById(R.id.rv_notification);
        txt_noti_count = findViewById(R.id.txt_noti_count);
        //progressDialog=new ProgressDialog(this);

        progressDialog = new  Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(this).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);

        sessionManagement = SessionManagement.getInstance(NotificationActivity.this);
        user=  sessionManagement.getUserDetails();
        String id,branchId;
        id=user.get(SessionManagement.KEY_ID).toString();
        branchId=user.get(SessionManagement.KEY_BRANCH_ID).toString();

        getNotification(id,branchId);

        iv_back_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    public void getNotification(String ID,String branchId){
        progressDialog.show();
        ServiceGenerator.getDelivery().getNotifications("4","B001").enqueue(new Callback<ResponseNotification>() {
            @Override
            public void onResponse(Call<ResponseNotification> call, Response<ResponseNotification> response) {
                if (response.isSuccessful())
                {
                    progressDialog.dismiss();
                    if (response.body().isStatus())
                    {
                        txt_noti_count.setText(response.body().getTotalRecords().toString());
                        progressDialog.dismiss();
                        ArrayList<NotificationModel> notificationActivities=response.body().getResult().getNotificationList();
                        notificationAdapter=new NotificationAdapter(notificationActivities,NotificationActivity.this);
                        recyclerView.setAdapter(notificationAdapter);
                    }
                    else {
                        txt_noti_count.setText("0");
                        progressDialog.dismiss();
                        Toast.makeText(NotificationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(NotificationActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseNotification> call, Throwable t) {
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
                    Toast.makeText(NotificationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}