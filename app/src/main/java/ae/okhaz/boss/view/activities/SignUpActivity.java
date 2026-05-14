package ae.okhaz.boss.view.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

import ae.okhaz.boss.Activitys.LoginActivity;
import ae.okhaz.boss.Model.BranchModel;
import ae.okhaz.boss.rests.Response.ResponseBranch;
import ae.okhaz.boss.rests.Response.ResponseOrderStatus;
import ae.okhaz.boss.rests.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import ae.okhaz.admin.R;
//import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    ImageView sign_in_img;
    ImageView iv_back_sign_up;
    TextView tv_click_here_sign_up;
    ArrayList<BranchModel> branchModels;
    ArrayList<String> branchModelsID=new ArrayList<>();
    ArrayList<String> branchModelsNAmes;
    EditText edt_branch;
    ListView listView;
    TextView txt_dialogName;
    ImageView img_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_in_img=findViewById(R.id.sign_in_img);
        edt_branch=findViewById(R.id.edt_branch);
        iv_back_sign_up=findViewById(R.id.iv_back_sign_up);
        tv_click_here_sign_up=findViewById(R.id.tv_click_here_sign_up);


        CountryCodePicker ccp;
        ccp = findViewById(R.id.ccp);
        ccp.setDefaultCountryUsingNameCode("AE");
        ccp.setFlagSize(31);
        ccp.setTextSize(38);

        branchModelsNAmes=getBranchList();

        edt_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(SignUpActivity.this);
                dialog.setContentView(R.layout.bottom_layout_branch_list);
                int width = (int)(getResources().getDisplayMetrics().widthPixels*1);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*1);

                dialog.getWindow().setLayout(width,height);
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white_trans)));
                dialog.show();
                // listView.removeAllViews();
               /* BottomSheetDialog dialog = new BottomSheetDialog(getContext(),R.style.SheetDialog);
                dialog.setContentView(R.layout.bottom_layout_delveryboy_list);
                dialog.getWindow().setBackgroundDrawableResource(R.color.white_trans);*/
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                listView=dialog.findViewById(R.id.listView);
                txt_dialogName=dialog.findViewById(R.id.txt_dialogName);
                img_close=dialog.findViewById(R.id.img_close);
                dialog.show();

                txt_dialogName.setText("Branch Name");

                ArrayAdapter adapter=new ArrayAdapter<>(SignUpActivity.this,R.layout.simple_list_item_single_choice,branchModelsNAmes);
                listView.setAdapter(adapter);

                img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String id=branchModels.get(i).getBranchid();
                        String name=branchModels.get(i).getBranchname();
                    }
                });
            }
        });

        iv_back_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sign_in_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,MainDrawerBackActivity.class));
            }
        });

        tv_click_here_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }


    public ArrayList<String> getBranchList(){
        ArrayList<String> arrayList=new ArrayList<>();
        ServiceGenerator.getDelivery().getBranches().enqueue(new Callback<ResponseBranch>() {
            @Override
            public void onResponse(Call<ResponseBranch> call, Response<ResponseBranch> response) {
                if (response.isSuccessful())
                {
                    if(response.body().isStatus()){
                        branchModels=response.body().getResult();
                        for (int i=0;i<branchModels.size();i++){
                            arrayList.add(branchModels.get(i).getBranchname());
                            branchModelsID.add(branchModels.get(i).getBranchid());
                        }
                    }
                }
                else
                {
                    // progressDialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBranch> call, Throwable t) {
                if (t instanceof SocketTimeoutException)
                {
                }
                else if (t instanceof IOException)
                {
                }
                else
                {
                    //progressDialog.dismiss();
                    //Toast.makeText(DeliveryBoyListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return arrayList;
    }

}