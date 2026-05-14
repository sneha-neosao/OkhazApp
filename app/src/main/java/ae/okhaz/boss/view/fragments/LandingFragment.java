package ae.okhaz.boss.view.fragments;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bolaware.viewstimerstory.Momentz;
import com.bolaware.viewstimerstory.MomentzCallback;
import com.bolaware.viewstimerstory.MomentzView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import ae.okhaz.boss.Fragments.Admin.OrderFragment;
import ae.okhaz.boss.Fragments.HomeFragment;
import ae.okhaz.admin.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import ae.okhaz.boss.Model.SliderModel;
import ae.okhaz.boss.Utils.Tools;
import ae.okhaz.boss.rests.Response.ResponseHomeSlider;
import ae.okhaz.boss.rests.ServiceGenerator;
import ae.okhaz.boss.sessionHandling.SessionManagement;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;
import ae.okhaz.boss.view.activities.ProductListActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ae.okhaz.boss.view.activities.MainDrawerBackActivity.setSystemBarColor;

public class LandingFragment extends Fragment implements MomentzCallback {

    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog dialog;
    public MaterialCardView cv_dashboard_landing,cv_delivery_boy_landing,
            cv_supplier_list_landing,cv_order_landing,cv_product_list_landing,cv_supplier_product_list_landing;
    LinearLayout linearLayout,toolbar_nav,ll_faq_bottom,ll_faq_main;
    ImageView imageView,search_iv,iv_toolbar_logo,img_menu;

    TextView tv_toolbar_title;

    CoordinatorLayout co_landing;

    NestedScrollView ns_landingFrag;

    //int menuClickFlag;

    SessionManagement sessionManagement;
    HashMap user;
    String role;
    ConstraintLayout container;
    BottomSheetBehavior bottomSheetBehavior;
    ConstraintLayout container1;
    private Momentz storyMoment;
    List<MomentzView> listOfViews = new ArrayList<>();
    ArrayList<String> imgUrls;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view= inflater.inflate(R.layout.fragment_landing, container, false);

        Tools.setSystemBarColor(getActivity(),R.color.green_1);

        /*Set icon and text in place of setting in nav header*/
      //  MainDrawerBackActivity.tv_settings_drawer.setText(getString(R.string.home));
       // MainDrawerBackActivity.iv_setting_drawer.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_home_16));

        /*Selected item in navigation menu list*/
        ((MainDrawerBackActivity)getActivity()).viewSelector(getString(R.string.home));


        sessionManagement = SessionManagement.getInstance(getContext());
        user=  sessionManagement.getUserDetails();
        imgUrls=new ArrayList<>();
        linearLayout=view.findViewById(R.id.ll_item_faq);
        ll_faq_bottom=view.findViewById(R.id.ll_faq_bottom);
        ll_faq_main=view.findViewById(R.id.ll_faq_main);
        ns_landingFrag=view.findViewById(R.id.ns_landingFrag);

        cv_dashboard_landing=view.findViewById(R.id.cv_dashboard_landing);
        cv_delivery_boy_landing=view.findViewById(R.id.cv_delivery_boy_landing);
        cv_order_landing=view.findViewById(R.id.cv_order_landing);
        cv_supplier_list_landing=view.findViewById(R.id.cv_supplier_list_landing);
        cv_product_list_landing=view.findViewById(R.id.cv_product_list_landing);
        cv_supplier_product_list_landing=view.findViewById(R.id.cv_supplier_product_list_landing);

        imageView=view.findViewById(R.id.img_menu);
        co_landing=view.findViewById(R.id.co_landing);

        tv_toolbar_title =getActivity().findViewById(R.id.tv_toolbar_title);
        iv_toolbar_logo =getActivity().findViewById(R.id.iv_toolbar_logo);
        img_menu =getActivity().findViewById(R.id.img_menu);
        search_iv =getActivity().findViewById(R.id.search_iv);
        toolbar_nav =getActivity().findViewById(R.id.toolbar_nav);


        role=user.get(SessionManagement.KEY_USER_TYPE).toString();
        if (role.equals("Delivery Man")){
            cv_dashboard_landing.setVisibility(View.VISIBLE);
            cv_delivery_boy_landing.setVisibility(View.GONE);
            cv_product_list_landing.setVisibility(View.GONE);
            cv_supplier_list_landing.setVisibility(View.GONE);
        }
        if (role.equals("Admin")){
            cv_product_list_landing.setVisibility(View.VISIBLE);
        }

        if (role.equals("Supplier")){
            cv_supplier_product_list_landing.setVisibility(View.VISIBLE);
            cv_product_list_landing.setVisibility(View.GONE);
            cv_supplier_list_landing.setVisibility(View.GONE);
        }

        cv_supplier_product_list_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProductListActivity.class)
                        .putExtra("suppID",user.get(SessionManagement.KEY_ID).toString()));
            }
        });

        MainDrawerBackActivity.ll_dashboard.setVisibility(View.GONE);
        MainDrawerBackActivity.ll_orders.setVisibility(View.GONE);
        MainDrawerBackActivity.ll_delivery_boy.setVisibility(View.GONE);
        MainDrawerBackActivity.ll_supplier_list.setVisibility(View.GONE);

        cv_product_list_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProductListActivity.class)
                        .putExtra("suppID",""));
            }
        });


        ll_faq_bottom.setOnTouchListener((view1, motionEvent) -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(),R.style.SheetDialog);
            bottomSheetDialog.setContentView(R.layout.bottom_layout_faq);
            bottomSheetDialog.show();

            if(bottomSheetDialog.isShowing()){
                ll_faq_bottom.setVisibility(View.GONE);
            }
            bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    ll_faq_bottom.setVisibility(View.VISIBLE);
                }
            });
            return false;
        });
       // sheetBehavior = BottomSheetBehavior.from(ll_faq_main);

        ns_landingFrag.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY>oldScrollY){
                    //your visibility code here
                    ll_faq_bottom.setVisibility(View.GONE);
                }
                else {
                    ll_faq_bottom.setVisibility(View.VISIBLE);
                }
            }
        });

       /* dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(R.layout.bottom_layout_faq);


        sheetBehavior=  dialog.getBehavior();*/

        cv_supplier_list_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDrawerMenu();
                if(sessionManagement.isLoggedIn()){
                    if(!MainDrawerBackActivity.isHide) {
                        MainDrawerBackActivity menuDrawerBack2 = (MainDrawerBackActivity) getContext();
                        menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
                    }
                    tv_toolbar_title.setVisibility(View.VISIBLE);
                    setToolbarAndLoadFragmentLanding("Supplier List",new SupplierListFragment());

                }else{
                    ((MainDrawerBackActivity)getActivity()).goTOLogin();
                }
            }
        });

        cv_dashboard_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionManagement.isLoggedIn()){

                    showDrawerMenu();

                    if(!MainDrawerBackActivity.isHide) {
                        MainDrawerBackActivity menuDrawerBack2 = (MainDrawerBackActivity) getContext();
                        menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
                    }
                    setToolbarAndLoadFragmentLanding("Dashboard",new DashboardFragment());

                }else{
                    ((MainDrawerBackActivity)getActivity()).goTOLogin();
                }
            }
        });

        cv_delivery_boy_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sessionManagement.isLoggedIn()){

                    showDrawerMenu();

                    if(!MainDrawerBackActivity.isHide) {
                        MainDrawerBackActivity menuDrawerBack2 = (MainDrawerBackActivity) getContext();
                        menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
                    }
                        setToolbarAndLoadFragmentLanding("Delivery boy", new DeliveryBoyFragment());

                }else{
                    ((MainDrawerBackActivity)getActivity()).goTOLogin();
                }
            }
        });

        cv_order_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionManagement.isLoggedIn()){

                    showDrawerMenu();

                    if(!MainDrawerBackActivity.isHide) {
                        MainDrawerBackActivity menuDrawerBack2 = (MainDrawerBackActivity) getContext();
                        menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
                    }
                        if (role.equals("Delivery Man")) {
                            loadOrder("Orders", new HomeFragment(), "Landing");
                        } else {
                            loadOrder("Orders", new OrderFragment(), role);
                        }

                }else{
                    ((MainDrawerBackActivity)getActivity()).goTOLogin();
                }


            }
        });

        /*linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });*/

       /* sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //  btnBottomSheet.setText("Close");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        sheetBehavior.setHideable(false);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });*/

        MainDrawerBackActivity.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((MainDrawerBackActivity)getActivity())!=null)
                    ((MainDrawerBackActivity)getActivity()).hideShowMenu();

            }
        });

        co_landing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("landing","Landing");
            }
        });

        container1 = view.findViewById(R.id.container);

        if(sessionManagement.isLoggedIn()) {
            getBannerData();
        }
        return view;
    }

    public void setToolbarAndLoadFragmentLanding(String title, Fragment fragment){
       // menuClickFlag = 1;

        MainDrawerBackActivity mainDrawerBackActivity = new MainDrawerBackActivity();
        mainDrawerBackActivity.setTotal(1);
        toolbar_nav.setBackgroundColor(getResources().getColor(R.color.colorprimary));

        iv_toolbar_logo.setVisibility(View.GONE);
        tv_toolbar_title.setVisibility(View.VISIBLE);
        tv_toolbar_title.setText(title);
        search_iv.setColorFilter(ContextCompat.getColor(this.getContext(), android.R.color.white),
                PorterDuff.Mode.MULTIPLY);
        img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.nav_supplier_fragment,fragment,fragment.getTag()).addToBackStack(null).commit();

    }

    public void loadOrder(String title, Fragment fragment, String role)
    {
        toolbar_nav.setBackgroundColor(getResources().getColor(R.color.colorprimary));

        iv_toolbar_logo.setVisibility(View.GONE);
        tv_toolbar_title.setVisibility(View.VISIBLE);
        tv_toolbar_title.setText(title);
        search_iv.setColorFilter(ContextCompat.getColor(this.getContext(), android.R.color.white),PorterDuff.Mode.MULTIPLY);
        img_menu.setImageDrawable(getResources().getDrawable(R.drawable.ic_icon_nav_menu_white));

        Bundle bundle=new Bundle();
        bundle.putInt("position",0);
        bundle.putString("roleFrom",role);
        fragment.setArguments(bundle);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.nav_supplier_fragment,fragment,fragment.getTag()).commit();

    }


    public void getBannerData(){
        String branchCode=user.get(SessionManagement.KEY_BRANCH_ID).toString();

        Dialog progressDialog = new  Dialog(getActivity());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(R.layout.custom_loading_dialog);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageView img_gif_load=progressDialog.findViewById(R.id.img_gif_load);
        Glide.with(getContext()).load(R.drawable.loading_custom_1).diskCacheStrategy(DiskCacheStrategy.RESOURCE) .into(img_gif_load);
        progressDialog.show();

        //Tools.loading(getActivity()).show();

        ServiceGenerator.getDelivery().getSlider(branchCode).enqueue(
                new Callback<ResponseHomeSlider>() {
                    @Override
                    public void onResponse(Call<ResponseHomeSlider> call, Response<ResponseHomeSlider> response) {

                        if (response.isSuccessful()) {
                            //Tools.loading(getActivity()).dismiss();
                            progressDialog.dismiss();
                            if (response.body().isStatus()) {
                                container1.setVisibility(View.VISIBLE);
                                ArrayList<SliderModel> posters=response.body().getResult();

                                for(int i=0;i<posters.size();i++)
                                {
                                    String type=posters.get(i).getMediaType();
                                    Log.e("MomentzView", "onResponse:   "+type );

                                    if(type.equals("video")){
                                        VideoView videoView = new VideoView(getContext());
                                        listOfViews.add(new MomentzView(videoView,5));
                                    }
                                    else if(type.equals("image")){
                                        ImageView imageView = new ImageView(getContext());
                                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                        listOfViews.add(new MomentzView(imageView,5));
                                    }

                                    imgUrls.add(posters.get(i).getBanner_image());
                                }
                                prepareStories();


                            } else {
                                Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT);
                               container1.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(getActivity(),response.message(),Toast.LENGTH_SHORT);
                            Log.e("response", response.message());
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseHomeSlider> call, Throwable t) {
                        //Tools.loading(getActivity()).dismiss();
                        progressDialog.dismiss();
                        container1.setVisibility(View.GONE);

                    }
                }
        );
    }


    private void prepareStories() {

         container = getActivity().findViewById(R.id.container);

        storyMoment =  new Momentz(getContext(), listOfViews, container, this, R.drawable.status_color_home_banner);

        storyMoment.start();
    }


    @Override
    public void done() {

    }

    @Override
    public void onNextCalled(@NotNull View view, @NotNull Momentz momentz, int i) {
        if(view instanceof VideoView)
        {
            momentz.pause(true);
            playVideo((VideoView) view,i, momentz);
        }
        else if(view instanceof ImageView)
        {
            momentz.pause(true);
            Picasso.get()
                    .load(imgUrls.get(i))
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into((ImageView) view, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            momentz.resume();
                            if(i == listOfViews.size()-1)
                            {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int i = 0; i <listOfViews.size(); i++) {
                                            momentz.prev();
                                        }
                                    }
                                },2000);
                                /*for (int i = 0; i <listOfViews.size(); i++) {
                                    momentz.prev();
                                }*/

                            }

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }
    }

    private void playVideo(VideoView videoView, int index, Momentz momentz) {
        String str = imgUrls.get(index);
        Uri uri = Uri.parse(str);

        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                if(what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START)
                {
                    momentz.editDurationAndResume(index, (videoView.getDuration()) / 1000);
                    return true;
                }
                return false;
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(index == listOfViews.size()-1)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i <listOfViews.size(); i++) {
                                momentz.prev();
                            }
                        }
                    },2000);

                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainDrawerBackActivity)getActivity()).viewSelector(getString(R.string.home));
        toolbar_nav.setBackgroundColor(getResources().getColor(R.color.green_1));
        setSystemBarColor(getActivity(),R.color.green_1);
        iv_toolbar_logo.setVisibility(View.VISIBLE);
        MainDrawerBackActivity.ll_nav_title.setVisibility(View.GONE);
        tv_toolbar_title.setVisibility(View.GONE);
        search_iv.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.black),
                PorterDuff.Mode.MULTIPLY);
        img_menu.setImageDrawable(getResources().getDrawable(R.drawable.okhaz_menu));
        MainDrawerBackActivity.edt_search.setVisibility(View.GONE);
        MainDrawerBackActivity.search_supplier.setVisibility(View.GONE);

        MainDrawerBackActivity.ll_dashboard.setVisibility(View.GONE);
        MainDrawerBackActivity.ll_orders.setVisibility(View.GONE);
        MainDrawerBackActivity.ll_delivery_boy.setVisibility(View.GONE);
        MainDrawerBackActivity.ll_supplier_list.setVisibility(View.GONE);

        if (role.equals("Delivery Man")) {
            MainDrawerBackActivity.fab_online.setVisibility(View.VISIBLE);
        }

       // getBannerData();

//        storyMoment.resume();
    }

    public void showDrawerMenu(){
        if (role.equals("Delivery Man")){
            MainDrawerBackActivity.ll_dashboard.setVisibility(View.GONE);
            MainDrawerBackActivity.ll_orders.setVisibility(View.VISIBLE);
            MainDrawerBackActivity.ll_delivery_boy.setVisibility(View.GONE);
            MainDrawerBackActivity.ll_supplier_list.setVisibility(View.GONE);
        }
        if (role.equals("Admin")){
            MainDrawerBackActivity.ll_dashboard.setVisibility(View.VISIBLE);
            MainDrawerBackActivity.ll_orders.setVisibility(View.VISIBLE);
            MainDrawerBackActivity.ll_delivery_boy.setVisibility(View.VISIBLE);
            MainDrawerBackActivity.ll_supplier_list.setVisibility(View.VISIBLE);
        }

        if (role.equals("Supplier")){
            MainDrawerBackActivity.ll_dashboard.setVisibility(View.VISIBLE);
            MainDrawerBackActivity.ll_orders.setVisibility(View.VISIBLE);
            MainDrawerBackActivity.ll_delivery_boy.setVisibility(View.VISIBLE);
            MainDrawerBackActivity.ll_supplier_list.setVisibility(View.GONE);
        }
    }
}