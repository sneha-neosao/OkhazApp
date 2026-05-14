package ae.okhaz.boss.Adapter.Admin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import ae.okhaz.boss.Fragments.HomeFragment;
import ae.okhaz.boss.Model.DeliveryBoyModel;
import ae.okhaz.admin.R;
import ae.okhaz.boss.ViewHolder.DeliveryBoyHolder;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.util.ArrayList;

public class DeliveryBoyAdapter extends RecyclerView.Adapter<DeliveryBoyHolder>
{
    ArrayList<DeliveryBoyModel> deliveryBoyModelArrayList;
    Context context;


    public DeliveryBoyAdapter(ArrayList<DeliveryBoyModel> deliveryBoyModelArrayList, Context context) {
        this.deliveryBoyModelArrayList = deliveryBoyModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public DeliveryBoyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_layout_delivery_boy_2, parent, false);
        return new DeliveryBoyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryBoyHolder holder, int position) {

        holder.txt_deliveryBoy_name.setText(deliveryBoyModelArrayList.get(position).getUserName());
        holder.txt_deli_orderCount.setText(deliveryBoyModelArrayList.get(position).getOrderCount()+" Orders");

        if(deliveryBoyModelArrayList.get(position).getOnlineStatus().equals("true")){
            holder.tv_delivery_boy_status.setText("Active");
            holder.iv_delivery_boy_active.setVisibility(View.VISIBLE);
            holder.iv_delivery_boy_inactive.setVisibility(View.GONE);
        }
        else {
            holder.tv_delivery_boy_status.setText("Inactive");
            holder.iv_delivery_boy_active.setVisibility(View.GONE);
            holder.iv_delivery_boy_inactive.setVisibility(View.VISIBLE);
            //Glide.with(context).load(R.drawable.round_shape_isonline).into(holder.delivery_boy_status_img);
        }
        holder.ll_delivery_boy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeliveryBoyModel deliveryBoyModel=deliveryBoyModelArrayList.get(position);
               // DeliveryBoyListActivity activity = (DeliveryBoyListActivity) view.getContext();
                MainDrawerBackActivity activity = (MainDrawerBackActivity) view.getContext();
                HomeFragment myFragment = new HomeFragment();
                Bundle bundle=new Bundle();
                //id
                String str = new Gson().toJson(deliveryBoyModel);
                Log.e("deliveryBoy",str);
                bundle.putString("deliveryBoy",str);
                bundle.putString("id",deliveryBoyModelArrayList.get(position).getId());
                bundle.putString("userName",deliveryBoyModelArrayList.get(position).getUserName());
                bundle.putString("password",deliveryBoyModelArrayList.get(position).getPassWord());
                bundle.putString("email",deliveryBoyModelArrayList.get(position).getEmailId());
                bundle.putString("lat",deliveryBoyModelArrayList.get(position).getLatitude());
                bundle.putString("lng",deliveryBoyModelArrayList.get(position).getLongitude());
                bundle.putString("roleFrom","DeliveryBoyAdapter");
                bundle.putInt("fromUpdate",1);
                myFragment.setArguments(bundle);
               // activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_deli_list, myFragment).addToBackStack(null).commit();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();

                /* context.startActivity(new Intent(context,SupplierActivity.class)
                        .putExtra("userName",deliveryBoyModelArrayList.get(position).getUserName())
                        .putExtra("password",deliveryBoyModelArrayList.get(position).getPassWord())
                        .putExtra("email",deliveryBoyModelArrayList.get(position).getEmailId())
                        .putExtra("fromUpdate",1));*/
            }
               /* HomeFragment newFragment = new HomeFragment();
                FragmentTransaction transaction =  context.getFragmentManager().beginTransaction();
                transaction.replace(R.id.viewFragments, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();*/


        });

       /* holder.btn_delivery_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context,DeliveryBoyActivity.class)
           .putExtra("userName",deliveryBoyModelArrayList.get(position).getUserName())
           .putExtra("password",deliveryBoyModelArrayList.get(position).getPassWord())
           .putExtra("email",deliveryBoyModelArrayList.get(position).getEmailId())
                .putExtra("fromUpdate",1));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return deliveryBoyModelArrayList.size();
    }
}
