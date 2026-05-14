package ae.okhaz.boss.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.admin.R;

/**
 * Created by Avinash on 27,November,2020
 */
public class OrderViewHolder extends RecyclerView.ViewHolder {
    public ImageView status_img;
    public LinearLayout ll_supplier_name,llDeviceName;
    public TextView order_no_tv,time_tv,customer_nm_tv,price_tv,order_status_tv,
            supplier_nm_tv,main_order_no_tv,order_transaction_type_tv,order_payment_status_tv,tvDeviceName;


    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        status_img = itemView.findViewById(R.id.status_img);
        order_no_tv = itemView.findViewById(R.id.order_no_tv);
        time_tv = itemView.findViewById(R.id.time_tv);
        customer_nm_tv = itemView.findViewById(R.id.customer_nm_tv);
        price_tv = itemView.findViewById(R.id.price_tv);
        order_status_tv = itemView.findViewById(R.id.order_status_tv);
        supplier_nm_tv = itemView.findViewById(R.id.supplier_nm_tv);
        main_order_no_tv = itemView.findViewById(R.id.main_order_no_tv);
        ll_supplier_name = itemView.findViewById(R.id.ll_supplier_name);
        order_payment_status_tv = itemView.findViewById(R.id.order_payment_status_tv);
        llDeviceName = itemView.findViewById(R.id.llDeviceName);
        tvDeviceName = itemView.findViewById(R.id.tvDeviceName);
        //order_transaction_type_tv = itemView.findViewById(R.id.order_transaction_type_tv);
    }
}
