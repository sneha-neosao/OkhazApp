package ae.okhaz.boss.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import java.util.List;

import ae.okhaz.boss.Adapter.HomeProductListAdapter;
import ae.okhaz.boss.Model.ProductModel;
import ae.okhaz.boss.Model.SupplierModel;
import ae.okhaz.boss.Utils.DetectConnection;
import ae.okhaz.boss.rests.Response.ResponseProductList;
import ae.okhaz.boss.rests.Response.ResponseSupplierList;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    SessionManagement sessionManagement;
    HashMap user;
    String branchCode="",suppID="";
    RecyclerView rv_product_list;
    HomeProductListAdapter homeProductListAdapter;
    ArrayList<ProductModel> modelArrayList=new ArrayList<>();
    Toolbar toolbar_product;

    public int productLimitCount=0;
    RecyclerView.OnScrollListener onScrollListener;
    boolean isLoading = false;
    EditText edt_search;
    //ProgressDialog progressDialog;
    TextView no_data_txt;

    Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        try {
            //progressDialog = new ProgressDialog(this);
            progressDialog = new  Dialog(this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.custom_loading_dialog);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
            Glide.with(this).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);

            onScrollListener = null;

            sessionManagement = SessionManagement.getInstance(ProductListActivity.this);
            user = sessionManagement.getUserDetails();
            branchCode = user.get(SessionManagement.KEY_BRANCH_ID).toString();
            suppID = getIntent().getStringExtra("suppID");

            rv_product_list = findViewById(R.id.rv_product_list);
            toolbar_product = findViewById(R.id.toolbar_product);
            edt_search = findViewById(R.id.edt_search);
            no_data_txt=findViewById(R.id.no_data_txt);

            getProductList(suppID, "");

            edt_search.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getProductList(suppID, s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });

            toolbar_product.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    });
}
        catch (Exception e){
            Log.e("Products", "onCreate: "+e.toString());
        }
    }

    public void getProductList(String suppID,String keyword){

        progressDialog.show();

        ServiceGenerator.getDelivery().getProducts(branchCode,suppID,"0",keyword).enqueue(new Callback<ResponseProductList>() {
            @Override
            public void onResponse(Call<ResponseProductList> call, Response<ResponseProductList> response) {
               progressDialog.dismiss();
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        modelArrayList=response.body().getResult();
                        homeProductListAdapter=new HomeProductListAdapter(ProductListActivity.this,
                                modelArrayList);
                        rv_product_list.setLayoutManager(new GridLayoutManager(ProductListActivity.this,2));
                        rv_product_list.setAdapter(homeProductListAdapter);

                        if (modelArrayList.size()==0){
                            no_data_txt.setVisibility(View.VISIBLE);
                            rv_product_list.setVisibility(View.GONE);
                        }

                        initScrollListener(branchCode,suppID);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseProductList> call, Throwable t) {
                progressDialog.dismiss();
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

    private void initScrollListener(String branchCode,String suppID) {

        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() == modelArrayList.size() - 1) {
                        //bottom of list!
                        if (DetectConnection.checkInternetConnection(ProductListActivity.this)) {
                            try {
                                Handler handler = new Handler();

                                final Runnable r = new Runnable() {
                                    public void run() {

                                        modelArrayList.add(null);
                                        homeProductListAdapter.notifyItemInserted(modelArrayList.size() + 1);
                                        final int scrollPosition = modelArrayList.size();

                                        int currentSize = scrollPosition;
                                        productLimitCount = currentSize-1;
                                        loadMore(productLimitCount,branchCode,suppID);
                                        isLoading = true;
                                    }
                                };
                                handler.post(r);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ProductListActivity.this, "check internet connection", Toast.LENGTH_SHORT).show();

                        }

                    }
                }

            }
        };
        rv_product_list.setOnScrollListener(onScrollListener);

    }

    private void loadMore(final int scrollPosition, String branchCode,String suppID) {

        ServiceGenerator.getDelivery().getProducts(branchCode, suppID,String.valueOf(scrollPosition),"").enqueue(new Callback<ResponseProductList>() {
            @Override
            public void onResponse(Call<ResponseProductList> call, Response<ResponseProductList> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isStatus())
                    {
                        modelArrayList.remove(modelArrayList.size()-1);
                        homeProductListAdapter.notifyItemRemoved(scrollPosition);
                        List<ProductModel> tempList = response.body().getResult();
                        modelArrayList.addAll(tempList);
                        homeProductListAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                    else {
                        modelArrayList.remove(modelArrayList.size()-1);
                        homeProductListAdapter.notifyItemRemoved(scrollPosition);
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
            public void onFailure(Call<ResponseProductList> call, Throwable t) {
                modelArrayList.remove(modelArrayList.size()-1);
                homeProductListAdapter.notifyItemRemoved(scrollPosition);
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