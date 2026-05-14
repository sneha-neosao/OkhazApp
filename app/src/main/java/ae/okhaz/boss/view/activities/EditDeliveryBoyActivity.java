package ae.okhaz.boss.view.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ae.okhaz.boss.ImagePickerActivity;
import ae.okhaz.boss.Model.DeliveryBoyModel;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import ae.okhaz.admin.R;
import com.squareup.picasso.Picasso;

import ae.okhaz.boss.Utils.Validations;
import ae.okhaz.boss.rests.Requests.RequestDeliveryBoy;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.Response.ResponseDeliveryMan;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditDeliveryBoyActivity extends AppCompatActivity {

    TextView tvStatus,tvVehicleType,txt_toolbar_title;

    ImageView img_back_edit_delivery_boy,iv_show_hide_password,iv_show_hide_confirm_password1;;
    EditText edt_deliveryBoy_pass,
            edt_deliveryBoy_confirmPass;

    RadioGroup statusRadioGroup,vehicleTypeRadioGroup,genderRadioGroup;
    View viewStatusBottomSheet,viewVehicleTypeBottomSheet,viewGenderBottomSheet;
    BottomSheetDialog dialogStatusBottomSheet,dialogVehicleTypeBottomSheet,dialogGenderBottomSheet;

    LinearLayout llStatus,llVehicleType;

    DeliveryBoyModel deliveryBoyModel;
    String str,staffID,id;
    EditText edtDeliveryBoyPassword1, edtDeliveryBoyConfirmPassword1,edt_deliveryBoy_address,
            edt_deliveryBoy_fullName,edt_deliveryBoy_email,edt_deliveryBoy_contact,edt_deliveryBoy_vehicleNo,
            edt_deliveryBoy_gender,edt_deliveryBoy_userName;

    Button btn_submit,btn_delete;

    SessionManagement sessionManagement;
    HashMap user;
    ImageView iv_image_delivery;

    Dialog progressDialog;
    private MultipartBody.Part imageToUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delivery_boy_1);

        sessionManagement = SessionManagement.getInstance(getApplicationContext());
        user = sessionManagement.getUserDetails();

        img_back_edit_delivery_boy = findViewById(R.id.img_back_add_delivery_boy);

        txt_toolbar_title=findViewById(R.id.txt_toolbar_title);
        txt_toolbar_title.setText("Update Delivery Boy");

        tvStatus=findViewById(R.id.tvStatusEditDeliveryBoy);
        tvVehicleType=findViewById(R.id.tvVehicleTypeEditDeliveryBoy);
        llStatus=findViewById(R.id.llStatusEditDeliveryBoy);
        llVehicleType=findViewById(R.id.llVehicleTypeEditDeliveryBoy);

        edtDeliveryBoyPassword1=findViewById(R.id.edtPasswordEditDeliveryBoy);
        edtDeliveryBoyConfirmPassword1=findViewById(R.id.edtConfirmPasswordEditDeliveryBoy);
        edt_deliveryBoy_fullName=findViewById(R.id.edt_deliveryBoy_fullName);
        edt_deliveryBoy_email=findViewById(R.id.edt_deliveryBoy_email);
        edt_deliveryBoy_contact=findViewById(R.id.edt_deliveryBoy_contact);
        edt_deliveryBoy_vehicleNo=findViewById(R.id.edt_deliveryBoy_vehicleNo);
        edt_deliveryBoy_address=findViewById(R.id.edt_deliveryBoy_address);
        edt_deliveryBoy_gender=findViewById(R.id.edt_deliveryBoy_gender);
        edt_deliveryBoy_userName=findViewById(R.id.edt_deliveryBoy_userName);
        iv_show_hide_confirm_password1=findViewById(R.id.iv_show_hide_confirm_password_edit);
        iv_show_hide_password=findViewById(R.id.iv_show_hide_password_edit);

        btn_submit=findViewById(R.id.btn_submit);
        btn_delete=findViewById(R.id.btn_delete);
        btn_delete.setVisibility(View.VISIBLE);

        iv_image_delivery = findViewById(R.id.iv_image_delivery);

        progressDialog = new  Dialog(this);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(this).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);

        if(getIntent().getStringExtra("staffCode")!=null){
            staffID = getIntent().getStringExtra("staffCode");
        }

        getDetails(staffID);


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_deliveryBoy_userName.getText().toString().isEmpty()){
                    SweetAlertDialog sweetAlertDialog=new SweetAlertDialog(EditDeliveryBoyActivity.this, SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitle("Are you sure?");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            deleteDeliveryBoy();
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    sweetAlertDialog.setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });

                    sweetAlertDialog.show();

                }
            }
        });

        btn_submit.setText("Update");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation())
                    updateDeliveryBoy();
            }
        });

        //Status
        viewStatusBottomSheet = getLayoutInflater().inflate(R.layout.item_status_layout, null);
        statusRadioGroup = viewStatusBottomSheet.findViewById(R.id.rgStatus);
        dialogStatusBottomSheet = new BottomSheetDialog(EditDeliveryBoyActivity.this);

        //gender
        viewGenderBottomSheet = getLayoutInflater().inflate(R.layout.item_gender_layout, null);
        genderRadioGroup = viewGenderBottomSheet.findViewById(R.id.rgGender);
        dialogGenderBottomSheet = new BottomSheetDialog(EditDeliveryBoyActivity.this);

        //Vehicle type
        viewVehicleTypeBottomSheet = getLayoutInflater().inflate(R.layout.item_vehicle_type_layout, null);
        vehicleTypeRadioGroup = viewVehicleTypeBottomSheet.findViewById(R.id.rgVehicleType);
        dialogVehicleTypeBottomSheet = new BottomSheetDialog(EditDeliveryBoyActivity.this);

        /**Back  arrow click*/
        img_back_edit_delivery_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**Status click listener*/
        llStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogStatusBottomSheet.setContentView(viewStatusBottomSheet);
                dialogStatusBottomSheet.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                dialogStatusBottomSheet.show();
            }
        });

        /**edt view gender click listener*/
        edt_deliveryBoy_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGenderBottomSheet.setContentView(viewGenderBottomSheet);
                dialogGenderBottomSheet.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                dialogGenderBottomSheet.show();
            }
        });

        /**vehicle type click listener*/
        llVehicleType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogVehicleTypeBottomSheet.setContentView(viewVehicleTypeBottomSheet);
                dialogVehicleTypeBottomSheet.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                dialogVehicleTypeBottomSheet.show();
            }
        });

        /**Set gender type*/
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton)genderRadioGroup.findViewById(i);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to radiobutton text"
                    edt_deliveryBoy_gender.setText(checkedRadioButton.getText());
                    dialogGenderBottomSheet.dismiss();
                }
            }
        });


        /*if(tvStatus.getParent() != null) {
            ((ViewGroup)tvStatus.getParent()).removeView(tvStatus); // <- fix
        }*/
        /**Set Status type*/
        statusRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton)statusRadioGroup.findViewById(i);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to radiobutton text"
                    tvStatus.setText(checkedRadioButton.getText());
                    dialogStatusBottomSheet.dismiss();
                }
            }
        });

        /**Set Vehicle type*/
        vehicleTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton)vehicleTypeRadioGroup.findViewById(i);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to radiobutton text"
                    tvVehicleType.setText(checkedRadioButton.getText());
                    dialogVehicleTypeBottomSheet.dismiss();
                }
            }
        });

        /*str=getIntent().getStringExtra("deliveryBoy");
        Log.e("deliveryBoy",str.toString());
        Type typeMyType = new TypeToken<DeliveryBoyModel>(){}.getType();
        deliveryBoyModel = new Gson().fromJson(str, typeMyType);
        Log.e("deliveryBoyModel",deliveryBoyModel.toString());
        setData(deliveryBoyModel);*/

        iv_show_hide_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtDeliveryBoyPassword1.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    // ((ImageView(view)).setImageResource(R.drawable.ic_icon_feather_eye);
                    iv_show_hide_password.setImageResource(R.drawable.ic_icon_feather_eye_off);
                    //Show Password
                    edtDeliveryBoyPassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    //((ImageView)(view)).setImageResource(R.drawable.show_password);
                    iv_show_hide_password.setImageResource(R.drawable.ic_icon_feather_eye);
                    //Hide Password
                    edtDeliveryBoyPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

        iv_show_hide_confirm_password1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtDeliveryBoyConfirmPassword1.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    iv_show_hide_confirm_password1.setImageResource(R.drawable.ic_icon_feather_eye_off);
                    //Show Password
                    edtDeliveryBoyConfirmPassword1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    //((ImageView)(view)).setImageResource(R.drawable.show_password);
                    iv_show_hide_confirm_password1.setImageResource(R.drawable.ic_icon_feather_eye);
                    //Hide Password
                    edtDeliveryBoyConfirmPassword1.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });

        iv_image_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //askPermission();
            }
        });
    }



    private void askPermission(){
        Dexter.withActivity(EditDeliveryBoyActivity.this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    public void setData(DeliveryBoyModel  deliveryBoyModel)
    {
        if(deliveryBoyModel.getImage()!= null){
            Glide.with(EditDeliveryBoyActivity.this).load(deliveryBoyModel.getImage()).into(iv_image_delivery);
        }

        if(deliveryBoyModel.getLoginstatus().equals("1"))
        tvStatus.setText("Active");
        else  tvStatus.setText("Inactive");

        if(deliveryBoyModel.getFirstName()!=null){
            edt_deliveryBoy_fullName.setText(deliveryBoyModel.getFirstName());
        }

        if(deliveryBoyModel.getUserName()!=null){
            edt_deliveryBoy_userName.setText(deliveryBoyModel.getUserName());
            edt_deliveryBoy_userName.setEnabled(false);
        }

        if(deliveryBoyModel.getEmailId()!=null){
            edt_deliveryBoy_email.setText(deliveryBoyModel.getEmailId());
        }

        if(deliveryBoyModel.getMobile()!=null){
            edt_deliveryBoy_contact.setText(deliveryBoyModel.getMobile());
        }

        if(deliveryBoyModel.getVehicleType()!=null){
            tvVehicleType.setText(deliveryBoyModel.getVehicleType());
        }

        if(deliveryBoyModel.getVehicleNo()!=null){
            edt_deliveryBoy_vehicleNo.setText(deliveryBoyModel.getVehicleNo());
        }

        if(deliveryBoyModel.getAddress2()!=null){
            edt_deliveryBoy_address.setText(deliveryBoyModel.getAddress2());
        }else if(deliveryBoyModel.getAddress()!=null){
            edt_deliveryBoy_address.setText(deliveryBoyModel.getAddress());
        }

        if(deliveryBoyModel.getGender()!=null){
            edt_deliveryBoy_gender.setText(deliveryBoyModel.getGender());
        }

        if(deliveryBoyModel.getPassWord()!=null){
            edtDeliveryBoyPassword1.setText(deliveryBoyModel.getPassWord());
        }

        if(deliveryBoyModel.getPassWord()!=null){
            edtDeliveryBoyConfirmPassword1.setText(deliveryBoyModel.getPassWord());
        }


    }

    public boolean checkValidation(){
        if(tvStatus.getText().toString().isEmpty()){
            tvStatus.setError("Please select user status");
        }
        else if(edt_deliveryBoy_fullName.getText().toString().isEmpty()){
            edt_deliveryBoy_fullName.setError("Please enter first Name");
        }
        else if(edt_deliveryBoy_userName.getText().toString().isEmpty()){
            edt_deliveryBoy_userName.setError("Please enter User Name");
        }
        else if(edt_deliveryBoy_contact.getText().toString().isEmpty()){
            edt_deliveryBoy_contact.setError("Please enter mobile number");
        }
        else if(!Validations.isValidEmail(edt_deliveryBoy_email.getText().toString())){
            edt_deliveryBoy_email.setError("Please enter valid email address");
        }
        else if(tvVehicleType.getText().toString().isEmpty()){
            tvVehicleType.setError("Please Select Vehicle Type");
        }
        else if(edt_deliveryBoy_vehicleNo.getText().toString().isEmpty()){
            edt_deliveryBoy_vehicleNo.setError("Please enter Vehicle Number");
        }
        else if(edt_deliveryBoy_address.getText().toString().isEmpty()){
            edt_deliveryBoy_address.setError("Please enter address");
        }
        else if(edt_deliveryBoy_gender.getText().toString().isEmpty()){
            edt_deliveryBoy_gender.setError("Please enter address");
        }
        else if(edtDeliveryBoyPassword1.getText().toString().isEmpty()){
            edtDeliveryBoyPassword1.setError("Please enter password");
        }
        else if(edtDeliveryBoyConfirmPassword1.getText().toString().isEmpty()){
            edtDeliveryBoyConfirmPassword1.setError("Please confirm password");
        }
        else if(!edtDeliveryBoyPassword1.getText().toString().equals(edtDeliveryBoyConfirmPassword1.getText().toString().trim()))
        {
            edtDeliveryBoyConfirmPassword1.setError("Password not matching");
        }
        else
        {
            return true;
        }

        return false;
    }

    public void deleteDeliveryBoy(){
        progressDialog.show();
        RequestDeliveryBoy requestDeliveryBoy=new RequestDeliveryBoy();
        if(deliveryBoyModel.getStaffId()!=null){
            requestDeliveryBoy.setStaffId(deliveryBoyModel.getStaffId());
        }
        requestDeliveryBoy.setFirstName(edt_deliveryBoy_fullName.getText().toString());
        requestDeliveryBoy.setUserName(edt_deliveryBoy_userName.getText().toString());
        requestDeliveryBoy.setGender(edt_deliveryBoy_gender.getText().toString());
        requestDeliveryBoy.setPassWord(edtDeliveryBoyConfirmPassword1.getText().toString());
        requestDeliveryBoy.setOwnerID(user.get(SessionManagement.KEY_ID).toString());
        requestDeliveryBoy.setEmailId(edt_deliveryBoy_email.getText().toString());
        requestDeliveryBoy.setVehicleType(tvVehicleType.getText().toString());
        requestDeliveryBoy.setMobile(edt_deliveryBoy_contact.getText().toString());
        requestDeliveryBoy.setVehicleNo(edt_deliveryBoy_vehicleNo.getText().toString());
        requestDeliveryBoy.setBranchid(user.get(SessionManagement.KEY_BRANCH_ID).toString());
        requestDeliveryBoy.setAddress(edt_deliveryBoy_address.getText().toString());

        if(tvStatus.getText().toString().equals("Active"))
        {
            requestDeliveryBoy.setLoginStatus(1);
        }
        else if(tvStatus.getText().toString().equals("Inactive")){
            requestDeliveryBoy.setLoginStatus(0);
        }

        ServiceGenerator.getDelivery().deleteDeliveryBoy(requestDeliveryBoy).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        finish();
                        Toast.makeText(EditDeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(EditDeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(EditDeliveryBoyActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    //updateBarcode(barcode,itemId,orderItemId);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    // updateBarcode(barcode,itemId,orderItemId);
                }
                else
                {
                    Toast.makeText(EditDeliveryBoyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void updateDeliveryBoy(){
        progressDialog.show();
        RequestDeliveryBoy requestDeliveryBoy=new RequestDeliveryBoy();
        if(deliveryBoyModel.getStaffId()!=null){
            requestDeliveryBoy.setStaffId(deliveryBoyModel.getStaffId());
        }
        requestDeliveryBoy.setFirstName(edt_deliveryBoy_fullName.getText().toString());
        requestDeliveryBoy.setUserName(edt_deliveryBoy_userName.getText().toString());
        requestDeliveryBoy.setGender(edt_deliveryBoy_gender.getText().toString());
        requestDeliveryBoy.setPassWord(edtDeliveryBoyConfirmPassword1.getText().toString());
        requestDeliveryBoy.setOwnerID(user.get(SessionManagement.KEY_ID).toString());
        requestDeliveryBoy.setEmailId(edt_deliveryBoy_email.getText().toString());
        requestDeliveryBoy.setVehicleType(tvVehicleType.getText().toString());
        requestDeliveryBoy.setMobile(edt_deliveryBoy_contact.getText().toString());
        requestDeliveryBoy.setVehicleNo(edt_deliveryBoy_vehicleNo.getText().toString());
        requestDeliveryBoy.setBranchid(user.get(SessionManagement.KEY_BRANCH_ID).toString());
        requestDeliveryBoy.setAddress(edt_deliveryBoy_address.getText().toString());

        if(tvStatus.getText().toString().equals("Active"))
        {
            requestDeliveryBoy.setLoginStatus(1);
        }
        else if(tvStatus.getText().toString().equals("Inactive")){
            requestDeliveryBoy.setLoginStatus(0);
        }

        ServiceGenerator.getDelivery().editDeliveryBoy(requestDeliveryBoy).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        //onBackPressed();
                        Toast.makeText(EditDeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getDetails(staffID);
                    }
                    else
                    {
                        Toast.makeText(EditDeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(EditDeliveryBoyActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException)
                {
                    // "Connection Timeout";
                    //updateBarcode(barcode,itemId,orderItemId);
                }
                else if (t instanceof IOException)
                {
                    // "Timeout";
                    // updateBarcode(barcode,itemId,orderItemId);
                }
                else
                {
                    Toast.makeText(EditDeliveryBoyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getDetails(String id){

        progressDialog.show();
        ServiceGenerator.getDelivery().getdeliverymanbyid(id).enqueue(new Callback<ResponseDeliveryMan>() {
            @Override
            public void onResponse(Call<ResponseDeliveryMan> call, Response<ResponseDeliveryMan> response) {
                if (response.isSuccessful())
                {
                    // progressDialog.dismiss();
                    if (!response.body().isStatus()) {
                        //  progressDialog.dismiss();
                        Toast.makeText(EditDeliveryBoyActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                         deliveryBoyModel=response.body().getResult();
                        setData(deliveryBoyModel);
                        Log.e("delivery",deliveryBoyModel.toString());

                    }

                }
                else
                {
                    Toast.makeText(EditDeliveryBoyActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseDeliveryMan> call, Throwable t) {
                progressDialog.dismiss();
                if (t instanceof SocketTimeoutException)
                {
                    getDetails(staffID);
                }
                else if (t instanceof IOException)
                {
                    getDetails(staffID);
                }
                else
                {

                    Toast.makeText(EditDeliveryBoyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, 101);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");

                RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), new File(uri.getPath()));
                imageToUpload = MultipartBody.Part.createFormData("files", "profilePhoto.png", mFile);
                //uploadImage();
            }
        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", this.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}