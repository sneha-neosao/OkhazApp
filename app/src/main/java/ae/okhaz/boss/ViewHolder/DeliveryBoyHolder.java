package ae.okhaz.boss.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ae.okhaz.admin.R;

public class DeliveryBoyHolder extends RecyclerView.ViewHolder
{

    public TextView txt_deliveryBoy_name,txt_deli_orderCount,tv_delivery_boy_status;
    public ImageView delivery_boy_img,delivery_boy_status_img,iv_delivery_boy_active,iv_delivery_boy_inactive;
    public Button btn_delivery_update;
    public RelativeLayout rv_right_arrow,ll_delivery_boy;

    public DeliveryBoyHolder(@NonNull View itemView) {
        super(itemView);

        txt_deliveryBoy_name=itemView.findViewById(R.id.txt_deliveryBoy_name);
        txt_deli_orderCount=itemView.findViewById(R.id.txt_deli_orderCount);
        tv_delivery_boy_status=itemView.findViewById(R.id.tv_delivery_boy_status);
        iv_delivery_boy_active=itemView.findViewById(R.id.iv_delivery_boy_active);
        iv_delivery_boy_inactive=itemView.findViewById(R.id.iv_delivery_boy_inactive);
        delivery_boy_img=itemView.findViewById(R.id.delivery_boy_img);
        delivery_boy_status_img=itemView.findViewById(R.id.delivery_boy_status);
        btn_delivery_update=itemView.findViewById(R.id.btn_delivery_update);
        rv_right_arrow=itemView.findViewById(R.id.rv_right_arrow);
        ll_delivery_boy=itemView.findViewById(R.id.ll_delivery_boy);

    }
}
