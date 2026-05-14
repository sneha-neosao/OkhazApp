package ae.okhaz.boss.view.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import ae.okhaz.admin.R;

import ae.okhaz.boss.ImagePickerActivity;
import ae.okhaz.boss.Utils.Validations;
import ae.okhaz.boss.rests.Requests.RequestDeliveryBoy;
import ae.okhaz.boss.rests.Response.ResponseCommon;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDeliveryBoyActivity extends AppCompatActivity {

    ImageView img_back_edit_delivery_boy,iv_show_hide_password,iv_show_hide_confirm_password1;
    EditText edtDeliveryBoyPassword, edtDeliveryBoyConfirmPassword,edt_deliveryBoy_address,
    edt_deliveryBoy_fullName,edt_deliveryBoy_email,edt_deliveryBoy_contact,edt_deliveryBoy_vehicleNo,
    edt_deliveryBoy_gender,edt_deliveryBoy_userName;

    TextView tvStatus,tvVehicleType;
    LinearLayout llStatus,llVehicleType;
    RadioGroup statusRadioGroup,vehicleTypeRadioGroup,genderRadioGroup;
    View viewStatusBottomSheet,viewVehicleTypeBottomSheet,viewGenderBottomSheet;
    BottomSheetDialog dialogStatusBottomSheet,dialogVehicleTypeBottomSheet,dialogGenderBottomSheet;
    Button btn_submit;

    SessionManagement sessionManagement;
    HashMap user;
    private MultipartBody.Part imageToUpload;
    ImageView iv_image_delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_boy);

        sessionManagement = SessionManagement.getInstance(getApplicationContext());
        user = sessionManagement.getUserDetails();

        img_back_edit_delivery_boy = findViewById(R.id.img_back_add_delivery_boy);

        edtDeliveryBoyPassword=findViewById(R.id.edtPasswordAddDeliveryBoy);
        edtDeliveryBoyConfirmPassword=findViewById(R.id.edtConfirmPasswordAddDeliveryBoy);
        edt_deliveryBoy_fullName=findViewById(R.id.edt_deliveryBoy_fullName);
        edt_deliveryBoy_email=findViewById(R.id.edt_deliveryBoy_email);
        edt_deliveryBoy_contact=findViewById(R.id.edt_deliveryBoy_contact);
        edt_deliveryBoy_vehicleNo=findViewById(R.id.edt_deliveryBoy_vehicleNo);
        edt_deliveryBoy_address=findViewById(R.id.edt_deliveryBoy_address);
        edt_deliveryBoy_gender=findViewById(R.id.edt_deliveryBoy_gender);
        edt_deliveryBoy_userName=findViewById(R.id.edt_deliveryBoy_userName);
        iv_show_hide_confirm_password1=findViewById(R.id.iv_show_hide_confirm_password1);
        iv_show_hide_password=findViewById(R.id.iv_show_hide_password);

        btn_submit=findViewById(R.id.btn_submit);

        tvStatus=findViewById(R.id.tvStatusAddDeliveryBoy);
        tvVehicleType=findViewById(R.id.tvVehicleTypeAddDeliveryBoy);
        llStatus=findViewById(R.id.llStatusAddDeliveryBoy);
        llVehicleType=findViewById(R.id.llVehicleTypeAddDeliveryBoy);
        iv_image_delivery = findViewById(R.id.iv_image_delivery);

        /**Back  arrow click*/
        img_back_edit_delivery_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Status
        viewStatusBottomSheet = getLayoutInflater().inflate(R.layout.item_status_layout, null);
        statusRadioGroup = viewStatusBottomSheet.findViewById(R.id.rgStatus);
        dialogStatusBottomSheet = new BottomSheetDialog(AddDeliveryBoyActivity.this);

        //gender
        viewGenderBottomSheet = getLayoutInflater().inflate(R.layout.item_gender_layout, null);
        genderRadioGroup = viewGenderBottomSheet.findViewById(R.id.rgGender);
        dialogGenderBottomSheet = new BottomSheetDialog(AddDeliveryBoyActivity.this);

        //Vehicle type
        viewVehicleTypeBottomSheet = getLayoutInflater().inflate(R.layout.item_vehicle_type_layout, null);
        vehicleTypeRadioGroup = viewVehicleTypeBottomSheet.findViewById(R.id.rgVehicleType);
        dialogVehicleTypeBottomSheet = new BottomSheetDialog(AddDeliveryBoyActivity.this);

        /**Text view Status click listener*/
        llStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogStatusBottomSheet.setContentView(viewStatusBottomSheet);
                dialogStatusBottomSheet.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                dialogStatusBottomSheet.show();
            }
        });

        /**Text view gender click listener*/
        edt_deliveryBoy_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGenderBottomSheet.setContentView(viewGenderBottomSheet);
                dialogGenderBottomSheet.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                dialogGenderBottomSheet.show();
            }
        });

        /**Text view vehicle type click listener*/
        llVehicleType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogVehicleTypeBottomSheet.setContentView(viewVehicleTypeBottomSheet);
                dialogVehicleTypeBottomSheet.getWindow().setBackgroundDrawableResource(R.color.white_trans);
                dialogVehicleTypeBottomSheet.show();
            }
        });

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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation())
                {
                    addDeliveryBoy();
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
        Dexter.withActivity(AddDeliveryBoyActivity.this)
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

    public void ShowHidePass(View view){

        if(view.getId()==R.id.iv_show_hide_password){

            if(edtDeliveryBoyPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                iv_show_hide_password.setImageResource(R.drawable.ic_icon_feather_eye_off);

                //Show Password
                edtDeliveryBoyPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                //((ImageView)(view)).setImageResource(R.drawable.show_password);
                iv_show_hide_password.setImageResource(R.drawable.ic_icon_feather_eye);
                //Hide Password
                edtDeliveryBoyPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

    public void ShowHideConfirmPass(View view){

        if(view.getId()==R.id.iv_show_hide_confirm_password1){

            if(edtDeliveryBoyConfirmPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                iv_show_hide_confirm_password1.setImageResource(R.drawable.ic_icon_feather_eye_off);
                //Show Password
                edtDeliveryBoyConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                iv_show_hide_confirm_password1.setImageResource(R.drawable.ic_icon_feather_eye);
                //Hide Password
                edtDeliveryBoyConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
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
        else if(edtDeliveryBoyPassword.getText().toString().isEmpty()){
            edtDeliveryBoyPassword.setError("Please enter password");
        }
        else if(edtDeliveryBoyConfirmPassword.getText().toString().isEmpty()){
            edtDeliveryBoyConfirmPassword.setError("Please confirm password");
        }
        else if(!edtDeliveryBoyPassword.getText().toString().equals(edtDeliveryBoyConfirmPassword.getText().toString().trim()))
        {
            edtDeliveryBoyConfirmPassword.setError("Password not matching");
        }
        else
        {
            return true;
        }

        return false;
    }


    public void addDeliveryBoy(){

        RequestDeliveryBoy requestDeliveryBoy=new RequestDeliveryBoy();
        requestDeliveryBoy.setFirstName(edt_deliveryBoy_fullName.getText().toString());
        requestDeliveryBoy.setUserName(edt_deliveryBoy_userName.getText().toString());
        requestDeliveryBoy.setGender(edt_deliveryBoy_gender.getText().toString());
        requestDeliveryBoy.setPassWord(edtDeliveryBoyConfirmPassword.getText().toString());
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

        ServiceGenerator.getDelivery().addDeliveryBoy(requestDeliveryBoy).enqueue(new Callback<ResponseCommon>() {
            @Override
            public void onResponse(Call<ResponseCommon> call, Response<ResponseCommon> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        onBackPressed();
                        Toast.makeText(AddDeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AddDeliveryBoyActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(AddDeliveryBoyActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseCommon> call, Throwable t) {
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
                    Toast.makeText(AddDeliveryBoyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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