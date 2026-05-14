package ae.okhaz.boss.view.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import ae.okhaz.admin.R;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ae.okhaz.boss.Adapter.Admin.SupplierListAdapter;
import ae.okhaz.boss.Model.SupplierModel;
import ae.okhaz.boss.Utils.DetectConnection;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.rests.Response.ResponseSupplierList;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SupplierListFragment extends Fragment {

    SessionManagement sessionManagement;
    HashMap user;
    String branchCode="";
    RecyclerView rv_supplier_list;
    SupplierListAdapter supplierListAdapter;
    ArrayList<SupplierModel>modelArrayList=new ArrayList<>();

    CardView sort_tv;
    RadioGroup shortRadioGroup;
    EditText edt_search;
    TextView no_data_txt;
    public int productLimitCount=0;
    RecyclerView.OnScrollListener onScrollListener;
    boolean isLoading = false;

    ProgressBar pb_supplier;

    //[{supplierID=S160, product_image=http://qtp.ae/QTPMobileApp/images/Product/7009709_OrganicColdPressedJuicePassionRed414ml_1.png, vatRate=null, varient_id=16287, increament=0, title=Organic Cold-Pressed Juice-Passion Red 414 ml, ItemId=7009709, product_name=Organic Cold-Pressed Juice-Passion Red 414 ml, price=25, qty=1, unit_value=PCS, stock=Stock, product_description=null, rewards=null}]
    //addCartLogin: {SupplierID=S160, CustID=235, Price=25, Quantity=1, unitID=16287, ItemId=7009709}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_supplier_list, container, false);
        onScrollListener = null;
       // ((MainDrawerBackActivity)getActivity()).viewSelector(getString(R.string.));
          ((MainDrawerBackActivity)getActivity()).viewSelector("Supplier List");
        MainDrawerBackActivity.ll_nav_title.setVisibility(View.VISIBLE);
        MainDrawerBackActivity.search_supplier.setVisibility(View.VISIBLE);

        MainDrawerBackActivity.img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));
        MainDrawerBackActivity.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((MainDrawerBackActivity)getActivity())!=null)
                    ((MainDrawerBackActivity)getActivity()).hideShowMenu();

            }
        });

         MainDrawerBackActivity.search_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainDrawerBackActivity.edt_search.setVisibility(View.VISIBLE);
            }
        });

        sessionManagement = SessionManagement.getInstance(getContext());
        user = sessionManagement.getUserDetails();

        branchCode=user.get(SessionManagement.KEY_BRANCH_ID).toString();

        rv_supplier_list=view.findViewById(R.id.rv_supplier_list);
        sort_tv=view.findViewById(R.id.sort_tv);
        edt_search=view.findViewById(R.id.edt_search);
        pb_supplier=view.findViewById(R.id.pb_supplier);
        no_data_txt=view.findViewById(R.id.no_data_txt);

        getSupplierList(branchCode,"","");

        MainDrawerBackActivity.edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        getSupplierList(branchCode,s.toString(),"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sort_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog bottomSheetDialog=new Dialog(getContext());
                bottomSheetDialog.setContentView(R.layout.item_layout_supplier_sort);

                shortRadioGroup = bottomSheetDialog.findViewById(R.id.rgShort);

                AppCompatRadioButton sort_az,sort_za;
                sort_az=bottomSheetDialog.findViewById(R.id.sort_az);
                sort_za=bottomSheetDialog.findViewById(R.id.sort_za);

                sort_az.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        getSupplierList(branchCode,"","az");
                    }
                });

                sort_za.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                        getSupplierList(branchCode,"","za");
                    }
                });

                int width = (int)(getResources().getDisplayMetrics().widthPixels*1);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*1);

                bottomSheetDialog.getWindow().setLayout(width,height);
                bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white_trans)));
                bottomSheetDialog.show();
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

    public void getSupplierList(String branchCode,String keyword,String sort){

        //pb_supplier.setVisibility(View.VISIBLE);
        //Tools.loading(getActivity()).show();

        Dialog progressDialog = new  Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(getContext()).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);
        progressDialog.show();
        rv_supplier_list.setVisibility(View.GONE);

        ServiceGenerator.getDelivery().getSuppliers(branchCode,"0",keyword,sort).enqueue(new Callback<ResponseSupplierList>() {
            @Override
            public void onResponse(Call<ResponseSupplierList> call, Response<ResponseSupplierList> response) {

               // pb_supplier.setVisibility(View.GONE);
               // Tools.loading(getActivity()).dismiss();
                progressDialog.dismiss();
                rv_supplier_list.setVisibility(View.VISIBLE);
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        modelArrayList.clear();
                        modelArrayList=response.body().getResult().getSupplierList();
                        supplierListAdapter=new SupplierListAdapter(getContext(),modelArrayList);

                        if (modelArrayList.size()==0){
                            no_data_txt.setVisibility(View.VISIBLE);
                            rv_supplier_list.setVisibility(View.GONE);
                        }
                        rv_supplier_list.setAdapter(supplierListAdapter);
                       // supplierListAdapter.notifyDataSetChanged();
                       initScrollListener(branchCode,keyword,sort);

                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseSupplierList> call, Throwable t) {
                //pb_supplier.setVisibility(View.GONE);
                //Tools.loading(getActivity()).dismiss();
                progressDialog.dismiss();
                rv_supplier_list.setVisibility(View.VISIBLE);
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

    private void initScrollListener(String branchCode,String keyword,String sort) {

        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager gridLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() == modelArrayList.size() - 1) {
                        //bottom of list!
                        if (DetectConnection.checkInternetConnection(getContext())) {
                            try {
                                Handler handler = new Handler();

                                final Runnable r = new Runnable() {
                                    public void run() {

                                        modelArrayList.add(null);
                                        supplierListAdapter.notifyItemInserted(modelArrayList.size() + 1);
                                        final int scrollPosition = modelArrayList.size();

                                        int currentSize = scrollPosition;
                                        productLimitCount = currentSize-1;
                                        loadMore(productLimitCount,branchCode,keyword,sort);
                                        isLoading = true;
                                    }
                                };
                                handler.post(r);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getContext(), "check internet connection", Toast.LENGTH_SHORT).show();

                        }

                    }
                }

            }
        };
        rv_supplier_list.setOnScrollListener(onScrollListener);

    }

    private void loadMore(final int scrollPosition, String branchCode,String keyword,String sort) {

        ServiceGenerator.getDelivery().getSuppliers(branchCode, String.valueOf(scrollPosition),keyword,sort).enqueue(new Callback<ResponseSupplierList>() {
            @Override
            public void onResponse(Call<ResponseSupplierList> call, Response<ResponseSupplierList> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        modelArrayList.remove(modelArrayList.size()-1);
                        supplierListAdapter.notifyItemRemoved(scrollPosition);
                        List<SupplierModel> tempList = response.body().getResult().getSupplierList();
                        modelArrayList.addAll(tempList);
                        supplierListAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                    else {
                        modelArrayList.remove(modelArrayList.size()-1);
                        supplierListAdapter.notifyItemRemoved(scrollPosition);
                        isLoading = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLoading = false;
                            }
                        }, 2000);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseSupplierList> call, Throwable t) {
                modelArrayList.remove(modelArrayList.size()-1);
                supplierListAdapter.notifyItemRemoved(scrollPosition);
                isLoading = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isLoading = false;
                    }
                }, 2000);

            }
        });
    }


}