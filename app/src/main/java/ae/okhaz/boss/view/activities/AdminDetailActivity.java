package ae.okhaz.boss.view.activities;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import ae.okhaz.admin.R;
import ae.okhaz.boss.rests.Response.ResponseSupplier;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminDetailActivity extends AppCompatActivity {
    SessionManagement sessionManagement;
    HashMap user;

    DatePickerDialog.OnDateSetListener mDateListener;

    ImageView img_menu_adminDetail;

    TextView txt_fullName,txt_email,txt_contact,txt_dob,
    txt_address,txt_landmark,txt_place,txt_pincode,
            txt_country,txt_gender,txt_marital,txt_bloodgrp,txt_licence;

    EditText edt_full_name_admin,edt_email_admin,edt_contact_admin,edt_dob_admin,
            edt_address_admin,edt_landmark_admin,edt_place_admin,edt_pincode_admin,
            edt_country_admin, edt_gender_admin,edt_marital_status_admin,
            edt_blood_group_admin,edt_license_number_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail);

        sessionManagement = SessionManagement.getInstance(getApplicationContext());
        user = sessionManagement.getUserDetails();

        txt_fullName = findViewById(R.id.txt_fullName);
        txt_email = findViewById(R.id.txt_email);
        txt_contact = findViewById(R.id.txt_contact);
        txt_dob = findViewById(R.id.txt_dob);
        txt_address = findViewById(R.id.txt_address);
        txt_landmark = findViewById(R.id.txt_landmark);
        txt_place = findViewById(R.id.txt_place);
        txt_pincode = findViewById(R.id.txt_pincode);
        txt_country = findViewById(R.id.txt_country);
        txt_gender = findViewById(R.id.txt_gender);
        txt_marital = findViewById(R.id.txt_marital);
        txt_bloodgrp = findViewById(R.id.txt_bloodgrp);
        txt_licence = findViewById(R.id.txt_licence);

        edt_full_name_admin = findViewById(R.id.edt_full_name_admin);
        edt_email_admin = findViewById(R.id.edt_email_admin);
        edt_contact_admin = findViewById(R.id.edt_contact_admin);
        edt_dob_admin = findViewById(R.id.edt_dob_admin);
        edt_address_admin = findViewById(R.id.edt_address_admin);
        edt_landmark_admin = findViewById(R.id.edt_landmark_admin);
        edt_place_admin = findViewById(R.id.edt_place_admin);
        edt_pincode_admin = findViewById(R.id.edt_pincode_admin);
        edt_country_admin = findViewById(R.id.edt_country_admin);
        edt_gender_admin = findViewById(R.id.edt_gender_admin);
        edt_marital_status_admin = findViewById(R.id.edt_marital_status_admin);
        edt_blood_group_admin = findViewById(R.id.edt_blood_group_admin);
        edt_license_number_admin = findViewById(R.id.edt_license_number_admin);

        img_menu_adminDetail = findViewById(R.id.img_menu_adminDetail);

        img_menu_adminDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        edt_full_name_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    txt_fullName.setVisibility(View.VISIBLE);
                    edt_full_name_admin.setHint("");
                }
                else {
                    if(edt_full_name_admin.getText().toString().isEmpty()){
                        txt_fullName.setVisibility(View.GONE);
                        edt_full_name_admin.setHint("Full Name");
                    }
                    else
                        txt_fullName.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_email_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    txt_email.setVisibility(View.VISIBLE);
                    edt_email_admin.setHint("");
                }

                else {
                    if(edt_email_admin.getText().toString().isEmpty()){
                        txt_email.setVisibility(View.GONE);
                    edt_email_admin.setHint("Email");}
                    else
                        txt_email.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_contact_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    txt_contact.setVisibility(View.VISIBLE);
                    edt_contact_admin.setHint("");
                }
                else {
                    if(edt_contact_admin.getText().toString().isEmpty()) {
                        txt_contact.setVisibility(View.GONE);
                        edt_contact_admin.setHint("Contact");
                    }
                    else
                        txt_contact.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_dob_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog =  new DatePickerDialog(
                        AdminDetailActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                dialog.show();
            }
        });

        edt_dob_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    txt_dob.setVisibility(View.VISIBLE);
                    edt_dob_admin.setHint("");
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog =  new DatePickerDialog(
                            AdminDetailActivity.this,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateListener,
                            year,month,day
                    );
                    dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                    dialog.show();
                }
                else {
                    if(edt_dob_admin.getText().toString().isEmpty()){
                        txt_dob.setVisibility(View.GONE);
                        edt_dob_admin.setHint("DOB");
                    }
                    else
                        txt_dob.setVisibility(View.VISIBLE);
                }
            }
        });

        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker ,int year ,int month ,int day) {
                month = month + 1;

                Log.d( "onDateSet" , month + "/" + day + "/" + year );
                edt_dob_admin.setText( new StringBuilder().append( day ).append( "-" )
                        .append( month ).append( "-" ).append( year ) );
            }
        };

        edt_address_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    txt_address.setVisibility(View.VISIBLE);
                    edt_address_admin.setHint("");
                }
                else {
                    if(edt_address_admin.getText().toString().isEmpty()){
                        txt_address.setVisibility(View.GONE);
                        edt_address_admin.setHint("Address");
                    }
                    else
                        txt_address.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_landmark_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    txt_landmark.setVisibility(View.VISIBLE);
                    edt_landmark_admin.setHint("");
                }
                else {
                    if(edt_landmark_admin.getText().toString().isEmpty()) {
                        txt_landmark.setVisibility(View.GONE);
                        edt_landmark_admin.setHint("Landmark");
                    }
                    else
                        txt_landmark.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_place_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    txt_place.setVisibility(View.VISIBLE);
                    edt_place_admin.setHint("");
                }
                else {
                    if(edt_place_admin.getText().toString().isEmpty()) {
                        edt_place_admin.setHint("Place");
                        txt_place.setVisibility(View.GONE);
                    }
                    else
                        txt_place.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_pincode_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    txt_pincode.setVisibility(View.VISIBLE);
                    edt_pincode_admin.setHint("");
                }
                else {
                    if(edt_pincode_admin.getText().toString().isEmpty()){
                        txt_pincode.setVisibility(View.GONE);
                        edt_pincode_admin.setHint("Pincode");
                    }
                    else
                        txt_pincode.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_country_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    txt_country.setVisibility(View.VISIBLE);
                    edt_country_admin.setHint("");
                }
                else {
                    if(edt_country_admin.getText().toString().isEmpty()) {
                        txt_country.setVisibility(View.GONE);
                        edt_country_admin.setHint("Country");
                    }
                    else
                        txt_country.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_gender_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AdminDetailActivity.this,R.style.SheetDialog);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_gender);

                bottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.white_trans);

                bottomSheetDialog.show();
            }
        });

        edt_gender_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    txt_gender.setVisibility(View.VISIBLE);
                    edt_gender_admin.setHint("");
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AdminDetailActivity.this,R.style.SheetDialog);
                    bottomSheetDialog.setContentView(R.layout.layout_bottom_gender);
                    bottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                    bottomSheetDialog.show();
                }
                else {
                    if(edt_gender_admin.getText().toString().isEmpty()) {
                        txt_gender.setVisibility(View.GONE);
                        edt_gender_admin.setHint("Gender");
                    }
                    else
                        txt_gender.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_marital_status_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AdminDetailActivity.this,R.style.SheetDialog);
                bottomSheetDialog.setContentView(R.layout.layout_bottom_marital_status);
                bottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                bottomSheetDialog.show();
            }
        });

        edt_marital_status_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    txt_marital.setVisibility(View.VISIBLE);
                    edt_marital_status_admin.setHint("");
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AdminDetailActivity.this,R.style.SheetDialog);
                    bottomSheetDialog.setContentView(R.layout.layout_bottom_marital_status);
                    bottomSheetDialog.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                    bottomSheetDialog.show();
                }

                else {
                    if(edt_marital_status_admin.getText().toString().isEmpty()) {
                        txt_marital.setVisibility(View.GONE);
                        edt_marital_status_admin.setHint("Marital Status");
                    }
                    else
                        txt_marital.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_blood_group_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    txt_bloodgrp.setVisibility(View.VISIBLE);
                    edt_blood_group_admin.setHint("");
                }
                else {
                    if(edt_blood_group_admin.getText().toString().isEmpty()) {
                        txt_bloodgrp.setVisibility(View.GONE);
                        edt_blood_group_admin.setHint("Blood Group");
                    }
                    else
                        txt_bloodgrp.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_license_number_admin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    txt_licence.setVisibility(View.VISIBLE);
                    edt_license_number_admin.setHint("");
                }
                else {
                    if(edt_license_number_admin.getText().toString().isEmpty()){
                        txt_licence.setVisibility(View.GONE);
                        edt_license_number_admin.setHint("License Number");
                    }
                    else
                        txt_licence.setVisibility(View.VISIBLE);
                }
            }
        });



        // edt_full_name_admin.setOnF


        setData();

       // getSupplierDetail(user.get(SessionManagement.KEY_ID).toString());
    }

    public void setData(){
        edt_full_name_admin.setText(""+user.get(SessionManagement.KEY_USER_NAME));
        edt_email_admin.setText(""+user.get(SessionManagement.KEY_EMAIL));
        txt_fullName.setVisibility(View.VISIBLE);
        txt_email.setVisibility(View.VISIBLE);
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

                        edt_full_name_admin.setText(response.body().getResult().getSupplierOwnerName());

                        //gotoSupplier();
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