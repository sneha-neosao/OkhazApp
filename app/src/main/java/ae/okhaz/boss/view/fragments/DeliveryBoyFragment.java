package ae.okhaz.boss.view.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import ae.okhaz.boss.Adapter.Admin.DeliveryBoyAdapter;
import ae.okhaz.boss.Model.DeliveryBoyModel;
import ae.okhaz.admin.R;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.rests.Response.ResponseDeliveryBoyList;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.AddDeliveryBoyActivity;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeliveryBoyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeliveryBoyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveryBoyFragment.
     */
    // TODO: Rename and change types and number of parameters


    //RelativeLayout ll_delivery_boy;

    FloatingActionButton btn_addDeliveryBoy;
    RecyclerView recyclerView;
    //ProgressDialog progressDialog;

    SessionManagement sessionManagement;
    HashMap user;

    Dialog progressDialog;


    public static DeliveryBoyFragment newInstance(String param1, String param2) {
        DeliveryBoyFragment fragment = new DeliveryBoyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view= inflater.inflate(R.layout.fragment_delivery_boy, container, false);

        Tools.setSystemBarColor(getActivity(),R.color.colorprimary);

        MainDrawerBackActivity.ll_nav_title.setVisibility(View.VISIBLE);
        MainDrawerBackActivity.tv_toolbar_title.setText("Delivery Boy List");

        /*Set icon and text in place of setting in nav header*/
      //  MainDrawerBackActivity.tv_settings_drawer.setText(getString(R.string.delivery_boy));
       // MainDrawerBackActivity.iv_setting_drawer.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_feather_user_1));

        /*Selected item in navigation menu list*/
        ((MainDrawerBackActivity)getActivity()).viewSelector(getString(R.string.delivery_boy));

        //progressDialog=new ProgressDialog(getContext());

        progressDialog = new  Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(getContext()).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);

        btn_addDeliveryBoy=view.findViewById(R.id.btn_addDeliveryBoy);
        recyclerView=view.findViewById(R.id.rv_deliveryBoy);


        sessionManagement = SessionManagement.getInstance(getContext());
        user=  sessionManagement.getUserDetails();



        getDeliveryBoyList();

        btn_addDeliveryBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(getContext(), DeliveryBoyActivity.class)
                        .putExtra("fromAdd",1));*/

                startActivity(new Intent(getContext(), AddDeliveryBoyActivity.class)
                        .putExtra("fromAdd",1));

            }
        });

        /*ll_delivery_boy = view.findViewById(R.id.ll_delivery_boy);

        ll_delivery_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DeliveryBoyDetailsActivity.class));
            }
        });*/

        //Navigation click
        MainDrawerBackActivity.img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));
        MainDrawerBackActivity.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((MainDrawerBackActivity)getActivity())!=null)
                    ((MainDrawerBackActivity)getActivity()).hideShowMenu();

            }
        });

        //Back pressed
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if(((MainDrawerBackActivity)getActivity())!=null)
                        ((MainDrawerBackActivity)getActivity()).LoadLandingFragment();
                    return true;
                }
                return false;
            }
        });
        return view;
    }

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
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        progressDialog.dismiss();
                        ArrayList<DeliveryBoyModel> deliveryBoyModels=response.body().getResult().getDeliveryManList();
                        DeliveryBoyAdapter deliveryBoyAdapter=new DeliveryBoyAdapter(deliveryBoyModels,getContext());
                        recyclerView.setAdapter(deliveryBoyAdapter);
                    }
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "something wen't wrong , please try again..", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}