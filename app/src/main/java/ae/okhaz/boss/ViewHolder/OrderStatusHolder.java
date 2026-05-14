package ae.okhaz.boss.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.admin.R;

public class OrderStatusHolder extends RecyclerView.ViewHolder
{
    public ImageView order_pending_img;
    public TextView order_no_tv,txt_count;
    public LinearLayout rv_order_next;

    public OrderStatusHolder(@NonNull View itemView) {
        super(itemView);
        order_pending_img=itemView.findViewById(R.id.order_pending_img);
        order_no_tv=itemView.findViewById(R.id.order_no_tv);
        rv_order_next=itemView.findViewById(R.id.rv_order_next);
        txt_count=itemView.findViewById(R.id.txt_count);
    }
}
