package ae.okhaz.boss.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import ae.okhaz.boss.Activitys.Supplier.SupplierActivity;

import com.ncorti.slidetoact.SlideToActView;
import ae.okhaz.admin.R;

import org.jetbrains.annotations.NotNull;

import ae.okhaz.boss.rests.Response.ResponseCheckStatus;
import ae.okhaz.boss.rests.Response.ResponseSupplier;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.GetStartedActivity;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    SessionManagement sessionManagement;
    HashMap user;
    public static SlideToActView btn_start_slide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sessionManagement = SessionManagement.getInstance(getApplicationContext());
        user = sessionManagement.getUserDetails();

        btn_start_slide=findViewById(R.id.btn_start_slide);

        btn_start_slide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NotNull SlideToActView slideToActView) {
                if (sessionManagement.isLoggedIn())
                {
                    if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Delivery Man")) {
                        getUserDetails(user.get(SessionManagement.KEY_ID).toString());
                    } else
                    {
                        if(user.get(SessionManagement.KEY_USER_TYPE).toString().equals("Supplier")){
                            getSupplierDetail(user.get(SessionManagement.KEY_ID).toString());
                        }
                        else {
                            // gotoSupplier();
                            gotoDrawerBack();
                        }
                    }


                }
                else {
                    Intent intent =new Intent(SplashActivity.this, TutorialActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
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

    private void gotomain() {
        Intent intent =new Intent(SplashActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void gotoSupplier()
    {
        Intent intent =new Intent(SplashActivity.this, SupplierActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void gotoDrawerBack()
    {
        Intent intent =new Intent(SplashActivity.this, MainDrawerBackActivity.class);
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

                        //gotoSupplier();
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
}