package ae.okhaz.boss.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import ae.okhaz.boss.Fragments.Delivery.DM_OrderDetailsFragment;
import ae.okhaz.boss.Model.Order;
import ae.okhaz.admin.R;
import ae.okhaz.boss.ViewHolder.OrderViewHolder;
import ae.okhaz.boss.view.activities.MainDrawerBackActivity;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Order> orderArrayList;
    Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    int pos;

    public OrderAdapter(ArrayList<Order> orderArrayList, Context context, int pos) {
        this.orderArrayList = orderArrayList;
        this.context = context;
        this.pos = pos;
    }

    @Override
    public int getItemViewType(int position) {
        return orderArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(R.layout.item_order_layout_1, parent, false);
            return new OrderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder1, int position) {

            if (holder1 instanceof OrderViewHolder) {
                final OrderViewHolder holder = (OrderViewHolder) holder1;
                Order order = orderArrayList.get(position);

                if (order != null) {

                    if (orderArrayList.get(position).getOrderStatus() == 8) {
                        holder.status_img.setImageResource(R.drawable.assigned_dm);
                        //Glide.with(context).load(R.drawable.pending_cv).into(holder.status_img);
                        holder.order_status_tv.setText("   " + orderArrayList.get(position).getOrderStatus() + "   ");

                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 4) {
                        holder.status_img.setImageResource(R.drawable.ready_for_pickup);
                        // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                    }
                    else if (orderArrayList.get(position).getOrderStatus() == -1) {
                        holder.status_img.setImageResource(R.drawable.alll_order);
                        // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 2) {
                        holder.status_img.setImageResource(R.drawable.pending_cv);
                        // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 3) {
                        holder.status_img.setImageResource(R.drawable.delivered);
                        // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 5) {
                        holder.status_img.setImageResource(R.drawable.processing);
                        // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 1) {
                        holder.status_img.setImageResource(R.drawable.order_placed);
                        // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 0) {
                        holder.status_img.setImageResource(R.drawable.test_order);
                        // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 21) {
                        holder.status_img.setImageResource(R.drawable.partial_accpted);
                        // Glide.with(context).load(R.drawable.processing).into(holder.status_img);

                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 6) {
                        holder.status_img.setImageResource(R.drawable.on_the_way);
                        // Glide.with(context).load(R.drawable.ontheway_cv).into(holder.status_img);

                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 11) {
                        holder.status_img.setImageResource(R.drawable.processing);
                        // Glide.with(context).load(R.drawable.processing).into(holder.status_img);
                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 7) {
                        holder.status_img.setImageResource(R.drawable.delivered_green);
                        //Glide.with(context).load(R.drawable.delivered).into(holder.status_img);
                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 9) {
                        holder.status_img.setImageResource(R.drawable.calceled);
                        //Glide.with(context).load(R.drawable.calceled).into(holder.status_img);
                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 10) {
                        holder.status_img.setImageResource(R.drawable.order_rejected);
                        //Glide.with(context).load(R.drawable.calceled).into(holder.status_img);
                    }
                    else if (orderArrayList.get(position).getOrderStatus() == 20) {
                        holder.status_img.setImageResource(R.drawable.delivered);
                        //Glide.with(context).load(R.drawable.calceled).into(holder.status_img);
                    }
                    else if(orderArrayList.get(position).getOrderStatus() == 12)
                    {
                        holder.status_img.setImageResource(R.drawable.awaiting_payment);
                    }
                    else if(orderArrayList.get(position).getOrderStatus() == 22)
                    {
                        holder.status_img.setImageResource(R.drawable.awaiting_payment);
                    }


                    holder.main_order_no_tv.setText("(M.O #"+order.getPrimaryOrderNumber()+")");

                    if (orderArrayList.get(position).getSupplierName() != null && !orderArrayList.get(position).getSupplierName().isEmpty()) {
                        holder.ll_supplier_name.setVisibility(View.VISIBLE);
                        holder.supplier_nm_tv.setText(orderArrayList.get(position).getSupplierName());
                    }
                    else {
                        holder.ll_supplier_name.setVisibility(View.INVISIBLE);
                    }

                    if (orderArrayList.get(position).getOrderStatus1().equals("Pending")) {
                        holder.order_status_tv.setText("  " + orderArrayList.get(position).getOrderStatus1() + "  ");

                    } else {
                        holder.order_status_tv.setText("" + orderArrayList.get(position).getOrderStatus1() + "");

                    }

                    holder.order_no_tv.setText(""+orderArrayList.get(position).getOrderID());
                  //  holder.supplier_nm_tv.setText(orderArrayList.get(position).getN());


                    holder.price_tv.setText("AED " + orderArrayList.get(position).getGrandtotal() + "/ Qty : " + orderArrayList.get(position).getItemCount());

                    /*Set order time*/
                    holder.time_tv.setText(orderArrayList.get(position).getOrderDate() + " " + orderArrayList.get(position).getOrderTime());

                    /*Set customer name*/
                    holder.customer_nm_tv.setText(orderArrayList.get(position).getFirstName());

                    /*Set order payment status*//*
                    if(orderArrayList.get(position).getIsPaymentSuccessful()!= null){
                        if(orderArrayList.get(position).getIsPaymentSuccessful()){
                            holder.order_payment_status_tv.setText("PAID");
                        }else{
                            holder.order_payment_status_tv.setText("UNPAID");
                        }
                    }

                    *//*Set order transaction type*//*
                    if(orderArrayList.get(position).getOrderTransactionType()!= null){
                        holder.order_transaction_type_tv.setText(" - (" +orderArrayList.get(position).getOrderTransactionType()+")");
                    }*/

                    /*Set order payment status and order transaction type*/
                    String isPaid="";
                    if(order.getIsPaymentSuccessful()){
                        isPaid="PAID - ";
                    }else{
                        isPaid="UNPAID - ";
                    }
                    if(order.getOrderTransactionType()!=null) {

                        holder.order_payment_status_tv.setText(isPaid+order.getOrderTransactionType());
                    }

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                         /*   Bundle bundle = new Bundle();
                            bundle.putInt("orderID", orderArrayList.get(position).getOrderID());
                            bundle.putInt("pos", pos);
                            NavController navController = Navigation.findNavController(((MainActivity) context), R.id.nav_host_fragment);
                            navController.navigate(R.id.goto_order_details, bundle);*/
                            if(!MainDrawerBackActivity.isHide) {
                                MainDrawerBackActivity menuDrawerBack2 = (MainDrawerBackActivity)  view.getContext();
                                menuDrawerBack2.hideMenu(menuDrawerBack2.content_view, menuDrawerBack2.content_view1);
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("orderID", String.valueOf(orderArrayList.get(position).getOrderID()));
                            bundle.putInt("pos", pos);
                            bundle.putString("role", "Delivery Man");
                            bundle.putString("primaryId", orderArrayList.get(position).getPrimaryOrderNumber());
                            MainDrawerBackActivity activity = (MainDrawerBackActivity) view.getContext();
                            DM_OrderDetailsFragment myFragment = new DM_OrderDetailsFragment();
                            //AdminOrderDetailFragment myFragment = new AdminOrderDetailFragment();
                            myFragment.setArguments(bundle);
                            //activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_deli_list, myFragment).addToBackStack(null).commit();
                            activity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.nav_supplier_fragment, myFragment)
                                    .addToBackStack(null).commit();
                        }
                    });
                }
            }
            else if (holder1 instanceof LoadingViewHolder) {
                showLoadingView((LoadingViewHolder) holder1, position);
            }

    }

        private void showLoadingView(LoadingViewHolder holder, int position) {
        }


    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }






    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
