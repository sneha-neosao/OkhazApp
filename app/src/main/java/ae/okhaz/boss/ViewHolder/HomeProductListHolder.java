package ae.okhaz.boss.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import ae.okhaz.admin.R;

public class HomeProductListHolder extends RecyclerView.ViewHolder{

    public ImageView img_product;
    public TextView txt_product_active,txt_product_stock,txt_product_name,txt_product_unit,txt_product_price1,
            txt_product_price2;
    public SwitchMaterial switch_active,switch_stock;

    public HomeProductListHolder(@NonNull View itemView) {
        super(itemView);

        img_product=itemView.findViewById(R.id.img_product);
        txt_product_active=itemView.findViewById(R.id.txt_product_active);
        txt_product_stock=itemView.findViewById(R.id.txt_product_stock);
        txt_product_name=itemView.findViewById(R.id.txt_product_name);
        txt_product_unit=itemView.findViewById(R.id.txt_product_unit);
        txt_product_price1=itemView.findViewById(R.id.txt_product_price1);
        txt_product_price2=itemView.findViewById(R.id.txt_product_price2);
        switch_active=itemView.findViewById(R.id.switch_active);
        switch_stock=itemView.findViewById(R.id.switch_stock);

    }
}
