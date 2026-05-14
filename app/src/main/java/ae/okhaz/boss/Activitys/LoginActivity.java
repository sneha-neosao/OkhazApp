package ae.okhaz.boss.Activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.DetectConnection;
import ae.okhaz.boss.Utils.NetworkChangeReceiver;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.Utils.Validations;
import ae.okhaz.boss.rests.Response.ResponseCheckStatus;
import ae.okhaz.boss.rests.Response.ResponseLogin;
import ae.okhaz.boss.rests.Response.ResponseSupplier;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.ForgotPasswordActivity;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import ae.okhaz.boss.view.activities.SignUpActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReciverListner {

    ImageView login_btn,iv_back_login;
    NetworkChangeReceiver networkChangeReceive;
    AlertDialog no_internets;
    EditText email,password_edt;
    SessionManagement sessionManagement;
    HashMap users;
    //ProgressDialog progressDialog;
    TextView tv_forgot_password,btn_click_here_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_1);
        networkChangeReceive = new NetworkChangeReceiver();

        login_btn =  findViewById(R.id.login_btn);

        email =  findViewById(R.id.edt_email);
        password_edt =  findViewById(R.id.edt_pass);
        tv_forgot_password =  findViewById(R.id.tv_forgot_password);
        btn_click_here_login=findViewById(R.id.btn_click_here_login);

        iv_back_login=findViewById(R.id.iv_back_login);

        sessionManagement = SessionManagement.getInstance(LoginActivity.this);

        /*progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);*/


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(LoginActivity.this))
                {
                    checks();
                }
                else {
                    Toast.makeText(LoginActivity.this, "please check internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        btn_click_here_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        iv_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                finish();
            }
        });
    }

    public void checks()
    {
        if (!Validations.requireValidate(email))
        {
            email.setError("enter username");
            email.requestFocus();
        }
        else if (!Validations.requireValidate(password_edt))
        {
            password_edt.setError("enter password");
            password_edt.requestFocus();
        }
        else {

            String emailText = email.getText().toString();
            String passwordText = password_edt.getText().toString();

            Log.d("LOGIN_DATA", "Email: " + emailText);
            Log.d("LOGIN_DATA", "Password: " + passwordText);

            // or using System.out.println
            System.out.println("Email: " + emailText);
            System.out.println("Password: " + passwordText);

            doLogin(emailText, passwordText);
        }
    }

    private void doLogin(String username, String password) {
        //progressDialog.show();
        /*Tools.loading(this).show();
        Tools.loading(LoginActivity.this).dismiss();*/
        Dialog progressDialog = new  Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(this).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);
        progressDialog.show();
        ServiceGenerator.getDelivery().loginProcess(username, password).enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                progressDialog.dismiss();

                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        sessionManagement.createLoginSession(
                                response.body().getResult()[0].getUserName(),
                                response.body().getResult()[0].getOwnerID(),
                                response.body().getResult()[0].getId(),
                                response.body().getResult()[0].getUserType(),
                                response.body().getResult()[0].getStockView(),
                                response.body().getResult()[0].getStockSales(),
                                response.body().getResult()[0].getCostView(),
                                response.body().getResult()[0].getGraphView(),
                                response.body().getResult()[0].getUndercostsale(),
                                response.body().getResult()[0].getBranchid(),
                                response.body().getResult()[0].getEmpCode(),
                                response.body().getResult()[0].getLoginstatus(),
                                response.body().getResult()[0].getDeliveryMan(),
                                response.body().getResult()[0].getEmailId());

                        Log.e("userType",response.body().getResult()[0].getUserType().toString());

                        if(response.body().getResult()[0].getUserType().equals("Delivery Man")){
                            getUserDetails(response.body().getResult()[0].getId());
                        }else if(response.body().getResult()[0].getUserType().equals("Supplier")){
                            getSupplierDetail(response.body().getResult()[0].getId());
                        } else{
                            gotoDrawerBack();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                progressDialog.dismiss();
                //Tools.loading(LoginActivity.this).dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceive, intentFilter);
        setConnectionListners(this);
    }

    @Override
    public void OnNetworkChange(boolean isConnected) {

        if (isConnected)
        {
            if (no_internets != null)
            {
                if (no_internets.isShowing())
                {
                    no_internets.dismiss();
                }
            }
        }
        else
        {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
            LayoutInflater factory = LayoutInflater.from(LoginActivity.this);
            final View viewsones = factory.inflate(R.layout.item_no_internet, null);
            alertDialog.setCancelable(false);
            alertDialog.setView(viewsones);
            no_internets = alertDialog.create();
            no_internets.show();

            ImageView iv = viewsones.findViewById(R.id.no_internet_iv);
            Button retry_btn = viewsones.findViewById(R.id.retry_btn);


            retry_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (DetectConnection.checkInternetConnection(LoginActivity.this))
                    {
                        if (no_internets != null)
                        {
                            if (no_internets.isShowing())
                            {
                                no_internets.dismiss();
                            }
                        }
                    }
                }
            });

            Glide.with(getBaseContext())
                    .load(R.drawable.no_internet_connection)
                    .into(iv);
        }

    }


    public void setConnectionListners(NetworkChangeReceiver.ConnectivityReciverListner listners)
    {
        NetworkChangeReceiver.connectivityReciverListner = listners;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (networkChangeReceive!= null)
        {
            unregisterReceiver(networkChangeReceive);
        }
    }

    public void gotomain()
    {
        Intent intent =new Intent(LoginActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void gotoSupplier()
    {
        //Intent intent =new Intent(LoginActivity.this, SupplierActivity.class);
        Intent intent =new Intent(LoginActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void gotoDrawerBack()
    {
        Intent intent =new Intent(LoginActivity.this, MainDrawerBackActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void getSupplierDetail(String ID){
        ServiceGenerator.getDelivery().getSupplierDetails(ID).enqueue(new Callback<ResponseSupplier>() {
            @Override
            public void onResponse(Call<ResponseSupplier> call, Response<ResponseSupplier> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getStatus().equals("true"))
                    {
                        // Toast.makeText(SplashActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        sessionManagement.createSupplierSession(response.body().getResult().getSuppID(),
                                response.body().getResult().getIsPaymentDetailSection_On_OD_Available4Supplier(),
                                response.body().getResult().getCustomerDetailSection_On_OD_Available4Supplier(),
                                response.body().getResult().getOrderSummarySection_On_OD_Available4Supplier());

                        gotoDrawerBack();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseSupplier> call, Throwable t) {
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

    private void getUserDetails(String id) {
        ServiceGenerator.getDelivery().checkLoginStatus(id).enqueue(new Callback<ResponseCheckStatus>() {
            @Override
            public void onResponse(Call<ResponseCheckStatus> call, Response<ResponseCheckStatus> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {

                        if (response.body().getResult().isOnlineStatus())
                        {
                            sessionManagement.updateLoginStatus("1");
                        }
                        else {
                            sessionManagement.updateLoginStatus("0");
                        }

                        sessionManagement.updateAID(response.body().getResult().getAid());
                        /*if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Delivery Man")){
                            gotomain();
                        }
                        else{
                            gotoSupplier();
                        }*/
                        //gotomain();
                        gotoDrawerBack();
                    } else  {


                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseCheckStatus> call, Throwable t) {

            }
        });
    }

}