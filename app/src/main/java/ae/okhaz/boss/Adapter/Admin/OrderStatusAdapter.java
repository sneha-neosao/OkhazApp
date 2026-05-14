package ae.okhaz.boss.Adapter.Admin;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.boss.Fragments.Admin.OrderFragment;
import ae.okhaz.boss.Model.OrderStatus;

import com.bumptech.glide.Glide;
import ae.okhaz.admin.R;
import ae.okhaz.boss.ViewHolder.OrderStatusHolder;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.util.ArrayList;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusHolder>{

    ArrayList<OrderStatus> orderArrayList;
    Context context;

    public OrderStatusAdapter(ArrayList<OrderStatus> orderArrayList, Context context) {
        this.orderArrayList = orderArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public OrderStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_order_status1, parent, false);
        return new OrderStatusHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusHolder holder, int position) {

        holder.order_no_tv.setText(orderArrayList.get(position).getOrderStatusName());
        holder.txt_count.setText(orderArrayList.get(position).getOrderCount());
        if(orderArrayList.get(position).getOrderStatusID().equals("-1"))
        {
            Glide.with(context).load(R.drawable.alll_order).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("0"))
        {
            Glide.with(context).load(R.drawable.test_order).into(holder.order_pending_img);
        }
       else if(orderArrayList.get(position).getOrderStatusID().equals("2"))
        {
            Glide.with(context).load(R.drawable.pending_cv).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("3"))
        {
            Glide.with(context).load(R.drawable.delivered).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("4"))
        {
            Glide.with(context).load(R.drawable.ready_for_pickup).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("5") )
        {
            Glide.with(context).load(R.drawable.processing).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("8"))
        {
            Glide.with(context).load(R.drawable.assigned_dm).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("11"))
        {
            Glide.with(context).load(R.drawable.processing).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("6"))
        {
            Glide.with(context).load(R.drawable.on_the_way_new).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("7"))
        {
            Glide.with(context).load(R.drawable.delivered_green).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("1"))
        {
            Glide.with(context).load(R.drawable.order_placed).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("21"))
        {
            Glide.with(context).load(R.drawable.partial_accpted).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("20"))
        {
            Glide.with(context).load(R.drawable.delivered).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("9"))
        {
            Glide.with(context).load(R.drawable.calceled).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("10"))
        {
            Glide.with(context).load(R.drawable.order_rejected).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("12"))
        {
            Glide.with(context).load(R.drawable.awaiting_payment).into(holder.order_pending_img);
        }
        else if(orderArrayList.get(position).getOrderStatusID().equals("22"))
        {
            Glide.with(context).load(R.drawable.order_failed).into(holder.order_pending_img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  if(orderArrayList.get(position).getOrderStatusID().equals("1"))
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",1);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();

                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("2"))
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",2);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("3"))
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",3);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();

                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("4"))
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",4);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();

                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("5") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",5);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();


                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("6") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",6);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("7") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",7);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("8"))
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",8);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("9") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",9);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("10") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",10);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("11"))
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",11);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("12") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",12);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("13") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",13);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("14") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",14);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("15") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",15);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("16") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",16);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("17") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",17);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("18") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",18);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("19") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",19);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("20") )
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",20);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else if(orderArrayList.get(position).getOrderStatusID().equals("21"))
                {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",21);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }
                else {
                    SupplierActivity activity = (SupplierActivity) view.getContext();
                    OrderFragment myFragment = new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("position",2);
                    myFragment.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
                }*/

                if(!MainDrawerBackActivity.isHide) {
                    MainDrawerBackActivity menuDrawerBack2 = (MainDrawerBackActivity)  view.getContext();
                    menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
                }

                int position1 = Integer.parseInt(orderArrayList.get(position).getOrderStatusID());
                openOrderFragment(position1,view);

            }
        });


    }

    public void openOrderFragment(int position,View view){
        MainDrawerBackActivity activity = (MainDrawerBackActivity) view.getContext();
        OrderFragment myFragment = new OrderFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("position",position);
        bundle.putString("fromFragment","Dashboard");
        myFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_supplier_fragment, myFragment).addToBackStack(null).commit();
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
}
